package com.wms.services.salary.service;

import com.wms.services.ledger.service.AccountTitleService;
import com.wms.services.ledger.service.PersonService;
import com.wms.services.ledger.service.TaxService;
import com.wms.services.salary.dao.PayNoteItemDAO;
import com.wms.services.salary.datestructures.*;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class PayNoteItemServiceImpl implements PayNoteItemService {
    @Autowired
    PayNoteItemDAO payNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    PersonService personService;
    @Autowired
    PayNoteService payNoteService;
    @Autowired
    TaxService taxService;
    @Autowired
    AccountTitleService accountTitleService;
    @Autowired
    PersonSalaryService personSalaryService;


    public int[] add(String accountBook, PayNoteItem[] payNoteItems) throws WMSServiceException
    {
        for(int i=0;i<payNoteItems.length;i++){
            payNoteItems[i].setState(PayNoteItemState.WAITING_FOR_CALCULATE_PAY);
            payNoteItems[i].setAfterTaxAmount(BigDecimal.ZERO);
            payNoteItems[i].setTaxAmount(BigDecimal.ZERO);
            payNoteItems[i].setPaidAmount(BigDecimal.ZERO);
        }
        //外键检测
        Stream.of(payNoteItems).forEach(
                (payNoteItem)->{
                    if(this.payNoteService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPayNoteId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)",payNoteItem.getPayNoteId()));
                    }

                    if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",payNoteItem.getPersonId()));
                    }
                }
        );
        //将人员薪资的总数找出来填到税前应付
        payNoteItems=this.getPersonAmount(accountBook,payNoteItems);
        return payNoteItemDAO.add(accountBook,payNoteItems);
    }

    @Transactional
    public void update(String accountBook, PayNoteItem[] payNoteItems) throws WMSServiceException{

        //外键检测
        Stream.of(payNoteItems).forEach(
                (payNoteItem)->{
                    if(this.payNoteService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPayNoteId()})).length == 0){
                        throw new WMSServiceException(String.format("薪资发放单不存在，请重新提交！(%d)",payNoteItem.getPayNoteId()));
                    }

                    if(this.personService.find(accountBook,
                            new Condition().addCondition("id",new Integer[]{ payNoteItem.getPersonId()})).length == 0){
                        throw new WMSServiceException(String.format("人员不存在，请重新提交！(%d)",payNoteItem.getPersonId()));
                    }
                }
        );
        this.getPersonAmount(accountBook,payNoteItems);
        payNoteItemDAO.update(accountBook,payNoteItems);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (payNoteItemDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除项目不存在，请重新查询！(%d)", id));
            }
        }
        try {
            payNoteItemDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除薪金发放单条目失败，如果薪金发放单条目已经被引用，需要先删除引用的内容，才能删除薪金发放单条目！");
        }
    }

    public PayNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException{
        return this.payNoteItemDAO.find(accountBook, cond);
    }

    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.payNoteItemDAO.findCount(database,cond);
    }

    //按条目或者整单计算税费 如果不提供条目id则默认按整单操作
    public void calculateTax(String accountBook, CalculateTax calculateTax){
        int payNoteId=calculateTax.getPayNoteId();
        BigDecimal[] taxAmount=null;
        java.util.List<Integer> payNoteItemId=calculateTax.getPayNoteItemId();
        PayNoteItem[] payNoteItems=null;
        PayNoteItemView[] payNoteItemViews=null;
        PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteId));
        int taxId=calculateTax.getTaxId();
        if(payNoteItemId!=null)
        {
            payNoteItemViews=payNoteItemDAO.find(accountBook,new Condition().addCondition("id",payNoteItemId.toArray(), ConditionItem.Relation.IN));
            if(payNoteItemViews.length!=payNoteItemId.size()){throw new WMSServiceException("查询薪资发放单条目出错,某些条目已经不存在！");}
        }
         else
             {
            payNoteItemViews=payNoteItemDAO.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
        }
        BigDecimal[] preTaxAmounts=new BigDecimal[payNoteItemViews.length];
        payNoteItems= this.getStateItem(payNoteItemViews,PayNoteItemState.WAITING_FOR_CALCULATE_PAY);

        //TODO 计算税费
        for(int i=0;i<payNoteItemViews.length;i++){
            //payNoteItemId.get(i)=payNoteItemViews[i].getId();
            preTaxAmounts[i]=payNoteItemViews[i].getPreTaxAmount();
        }
        for(int i=0;i<payNoteItems.length;i++){
            payNoteItems[i].setTaxAmount(new BigDecimal(10));
            payNoteItems[i].setAfterTaxAmount(payNoteItems[i].getPreTaxAmount().subtract(new BigDecimal(10)));
            payNoteItems[i].setState(PayNoteItemState.CALCULATED_PAY);
        }
        payNoteItemDAO.update(accountBook,payNoteItems);
    }
/*
    public void confirmItems(String accountBook,CalculateTax calculateTax){
        int payNoteId=calculateTax.getPayNoteId();
        int[] payNoteItemId=calculateTax.getPayNoteItemId();
        PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteId));
        PayNoteItemView[] payNoteItemViews=null;
        if(payNoteViews.length!=0){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
        //如果没提供条目id就按整单确认
        if(payNoteItemId.length!=0){
        payNoteItemViews=payNoteItemDAO.find(accountBook,new Condition().addCondition("id",payNoteItemId, ConditionItem.Relation.IN));}
        else{ payNoteItemViews=payNoteItemDAO.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));}
        //判断是否能确认
        for(int i=0;i<payNoteItemViews.length;i++){
            if(payNoteItemViews[i].getState()!=PayNoteItemState.CACULATED_WAITING_COMFIRMN){throw new WMSServiceException("操作的薪金发放单中条目未全部计算税费，无法确认！");
            }
        }
        //更新状态为确认
        List<PayNoteItem> payNoteItemList=new ArrayList<>();
        for(int i=0;i<payNoteItemViews.length;i++){
            PayNoteItem payNoteItem = ReflectHelper.createAndCopyFields(payNoteItemViews[i],PayNoteItem.class);
            payNoteItem.setState(PayNoteItemState.COMFIRMED);
            payNoteItemList.add(payNoteItem);
        }
        PayNoteItem[] payNoteItems=null;
        payNoteItems=(PayNoteItem[]) Array.newInstance(PayNoteItem.class,payNoteItemList.size());
        payNoteItemList.toArray(payNoteItems);
        payNoteItemDAO.update(accountBook,payNoteItems);
    }
*/

//将符合状态的条目提取出来
private PayNoteItem[] getStateItem(PayNoteItemView[] payNoteItemViews,int state){
    List<PayNoteItem> payNoteItemList=new ArrayList<>();
    for(int i=0;i<payNoteItemViews.length;i++){
        if(payNoteItemViews[i].getState()==state){
            PayNoteItem payNoteItem= ReflectHelper.createAndCopyFields(payNoteItemViews[i],PayNoteItem.class);
            payNoteItemList.add(payNoteItem);
        }
    }
    PayNoteItem[] payNoteItems=new PayNoteItem[payNoteItemList.size()];
    payNoteItemList.toArray(payNoteItems);
    return payNoteItems;
}


   //整单应付 同时将条目状态变为已付款
    public void realPayAll(String accountBook, PayNoteItemPay payNoteItemPays)
    {
        int payNoteId=payNoteItemPays.getPayNoteId();
        PayNoteItemView[] payNoteItemViews=null;
        payNoteItemViews=payNoteItemDAO.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
        PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteId));
        if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
        if(payNoteViews[0].getState()!=PayNoteState.CONFIRM_PAY){throw new WMSServiceException("薪资发放单不为已确认应付状态");}
        PayNoteItem[] payNoteItems= this.getStateItem(payNoteItemViews,PayNoteItemState.CALCULATED_PAY);
        //因为是全部完成，所以金额直接相等
        for(int i=0;i<payNoteItems.length;i++){
            payNoteItems[i].setPaidAmount(payNoteItems[i].getAfterTaxAmount());
            payNoteItems[i].setState(PayNoteItemState.PAYED);
        }
        payNoteItemDAO.update(accountBook,payNoteItems);
    }

    //按条目应付 同时将条目状态变为已付款
    public void realPayPartItems(String accountBook, PayNoteItemView[] payNoteItemViews)
    {
        if(payNoteItemViews.length==0){return;}
        int payNoteId=payNoteItemViews[0].getPayNoteId();
        PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteId));
        if(payNoteViews.length!=1){throw new WMSServiceException("查询薪资发放单出错,可能已经不存在！");}
        if(payNoteViews[0].getState()!=PayNoteState.CONFIRM_PAY){throw new WMSServiceException("薪资发放单不为已确认应付状态");}
        PayNoteItem[] payNoteItems= this.getStateItem(payNoteItemViews,PayNoteItemState.CALCULATED_PAY);
        for(int i=0;i<payNoteItemViews.length;i++){
            payNoteItems[i].setState(PayNoteItemState.PAYED);
        }
        payNoteItemDAO.update(accountBook,payNoteItems);
    }

    public void addAllItem(String accountBook,AddAllItem AddAllItem){
    int warehouseId= AddAllItem.getWarehouseId();
    int payNoteId= AddAllItem.getPayNoteId();
    PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteId));

    PayNoteItemView[] payNoteItemViews=payNoteItemDAO.find(accountBook,new Condition().addCondition("payNoteId",payNoteId));
    List<Integer> ids=new ArrayList<>();
    for(int i=0;i<payNoteItemViews.length;i++){ids.add(payNoteItemViews[i].getPersonId());}
        int[] idsArray=new int[ids.size()];
        /*
        for(int i=0;i<ids.size();i++){idsArray[i]=ids.get(i);}
 */
    if(payNoteViews.length!=1){throw new WMSServiceException("查询薪金发放单出错！");}
    int periodId=payNoteViews[0].getSalaryPeriodId();
    List<PayNoteItem> payNoteItemList=new ArrayList<>();
        PersonSalaryView[] personSalaryViews=personSalaryService.find(accountBook,new Condition().addCondition("salaryPeriodId",periodId).addCondition("warehouseId",warehouseId));
        Map<Integer, List<PersonSalaryView>> groupByPersonIdMap =
                Stream.of(personSalaryViews).collect(Collectors.groupingBy(PersonSalaryView::getPersonId));
        for (Map.Entry<Integer, List<PersonSalaryView>> entry : groupByPersonIdMap.entrySet()){
            if(ids.contains(entry.getKey())){continue;}
            PersonSalaryView[] personSalaryViewsEachGroup=new PersonSalaryView[entry.getValue().size()];
            entry.getValue().toArray(personSalaryViewsEachGroup);
            BigDecimal preTaxAmount=new BigDecimal(0);
            for(int i=0;i<personSalaryViewsEachGroup.length;i++){
                preTaxAmount=preTaxAmount.add(personSalaryViewsEachGroup[i].getAmount());
            }
            PayNoteItem payNoteItem=new PayNoteItem();
            payNoteItem.setPersonId(entry.getKey());
            payNoteItem.setPreTaxAmount(preTaxAmount);
            payNoteItem.setAfterTaxAmount(BigDecimal.ZERO);
            payNoteItem.setTaxAmount(BigDecimal.ZERO);
            payNoteItem.setPaidAmount(BigDecimal.ZERO);
            payNoteItem.setPayNoteId(payNoteId);
            payNoteItem.setState(PayNoteItemState.WAITING_FOR_CALCULATE_PAY);
            payNoteItem.setComment("自动生成薪资单条目");
            payNoteItemList.add(payNoteItem);
        }
        PayNoteItem[] payNoteItems=new PayNoteItem[payNoteItemList.size()];
        payNoteItemList.toArray(payNoteItems);
        //TODO
        payNoteItemDAO.add(accountBook,payNoteItems);
    }

//整单确认应付之前使用
    private PayNoteItem[] getPersonAmount(String accountBook,PayNoteItem[] payNoteItems){
        if(payNoteItems.length==0){
            return payNoteItems;}
        PayNoteView[] payNoteViews=payNoteService.find(accountBook,new Condition().addCondition("id",payNoteItems[0].getPayNoteId()));
        if(payNoteViews.length==0){throw new WMSServiceException("查找付款单出错，可能付款单已经不存在！"
        );}
        //如果整单还没确认应付可以更改工资
        if(payNoteViews[0].getState()!= PayNoteState.WAITING_FOR_CONFIRM){return payNoteItems;}
        //将人员薪资的总数找出来填到税前应付
        int  periodId=payNoteViews[0].getSalaryPeriodId();
        List<Integer> personIds=new ArrayList<>();
        for(int i=0;i<payNoteItems.length;i++){
            personIds.add(payNoteItems[i].getPersonId());
        }

        PersonSalaryView[] personSalaryViews= this.personSalaryService.find(accountBook,new Condition().addCondition("personId",personIds.toArray(), ConditionItem.Relation.IN).addCondition("salaryPeriodId",periodId));
        if(personSalaryViews.length==0){return payNoteItems;}

        Map<Integer, List<PersonSalaryView>> groupByPersonIdMap =
                Stream.of(personSalaryViews).collect(Collectors.groupingBy(PersonSalaryView::getPersonId));
        Iterator<Map.Entry<Integer,List<PersonSalaryView>>> entries = groupByPersonIdMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Integer, List<PersonSalaryView>> entry = entries.next();
            List<PersonSalaryView> personSalaryViews1=entry.getValue();
            PersonSalaryView[] personSalaryView=null;
            personSalaryView=(PersonSalaryView[]) Array.newInstance(PersonSalaryView.class,personSalaryViews1.size());
            personSalaryViews1.toArray(personSalaryView);
            BigDecimal amount=new BigDecimal(0);
            for(int i=0;i<personSalaryView.length;i++){
                amount=amount.add(personSalaryView[i].getAmount());
            }
            for(int i=0;i<payNoteItems.length;i++){
                if(payNoteItems[i].getPersonId()==entry.getKey())   {
                    payNoteItems[i].setPreTaxAmount(amount);
                    if(payNoteItems[i].getState()==PayNoteItemState.CALCULATED_PAY){
                    payNoteItems[i].setAfterTaxAmount(amount.subtract(payNoteItems[i].getTaxAmount()));}
                    break;
                }
            }
        }
        return payNoteItems;
    }
}
