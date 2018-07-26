package com.wms.services.warehouse.service;

import com.wms.services.ledger.service.AccountPeriodService;
import com.wms.services.warehouse.dao.WarehouseDAO;
import com.wms.services.warehouse.datastructures.WarehouseEntryAndItems;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sun.java2d.pipe.AAShapePipe;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class WarehouseServiceImpl implements WarehouseService{
    @Autowired
    WarehouseDAO warehouseDAO;
    @Autowired
    AccountPeriodService accountPeriodService;

    @Transactional
    public int[] add(String accountBook, Warehouse[] warehouses) throws WMSServiceException
    {
        //自动增加会计期间
        List<AccountPeriod> accountPeriodList=new ArrayList<>();
       for(int i=0;i<warehouses.length;i++){
           Validator validator=new Validator("仓库名");
           validator.notnull().notEmpty().validate(warehouses[i].getName());

           if(warehouses[i].getEnabled()!=0&&warehouses[i].getEnabled()!=1){
               throw new WMSServiceException("是否启用只能为0和1！");
           }
       }

        for(int i=0;i<warehouses.length;i++){
            for(int j=i+1;j<warehouses.length;j++){
                String name=warehouses[i].getName();
                if(name.equals(warehouses[j].getName())){throw new WMSServiceException("仓库 名称"+name+"在添加的列表中重复!");}
            }
        }
        //重复
        Stream.of(warehouses).forEach((warehouse)->{
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{warehouse.getName()});
            if(warehouseDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("仓库名："+warehouse.getName()+"已经存在!");
            }
        });
        int[] ids=warehouseDAO.add(accountBook,warehouses);
        List<Integer> idsList=new ArrayList<>();
        for(int i=0;i<ids.length;i++){idsList.add(ids[i]);}
        Warehouse[] warehouses1=this.warehouseDAO.findTable(accountBook,new Condition().addCondition("id",idsList.toArray(), ConditionItem.Relation.IN));
        for(int i=0;i<warehouses1.length;i++){
            AccountPeriod accountPeriod=new AccountPeriod();
            accountPeriod.setName(warehouses1[i].getName()+"仓库默认会计期间");
            accountPeriod.setStartTime(new Timestamp(System.currentTimeMillis()));
            accountPeriod.setEnded(AccountPeriodService.ended_ture);
            accountPeriod.setWarehouseId(warehouses1[i].getId());
            accountPeriodList.add(accountPeriod);
        }
        AccountPeriod[] accountPeriods=new AccountPeriod[accountPeriodList.size()];
        accountPeriodList.toArray(accountPeriods);
        this.accountPeriodService.add(accountBook,accountPeriods);
         return ids;
    }

    @Transactional
    public void update(String accountBook, Warehouse[] warehouses) throws WMSServiceException{
        for(int i=0;i<warehouses.length;i++){
            Validator validator=new Validator("仓库名");
            validator.notnull().notEmpty().validate(warehouses[i].getName());
            if(warehouses[i].getEnabled()!=0&&warehouses[i].getEnabled()!=1){
                throw new WMSServiceException("是否启用只能为0和1！");
            }
        }
        for(int i=0;i<warehouses.length;i++){
            for(int j=i+1;j<warehouses.length;j++){
                String name=warehouses[i].getName();
                if(name.equals(warehouses[j].getName())){throw new WMSServiceException("仓库 名称"+name+"在添加的列表中重复!");}
            }
        }
        Stream.of(warehouses).forEach(
                (supplier)->{
                    if(this.warehouseDAO.find(accountBook,
                            new Condition().addCondition("id",supplier.getId())).length == 0){
                        throw new WMSServiceException(String.format("要修改的项目不存在请确认后修改(%d)",supplier.getId()));
                    }
                }
        );
        for(int i=0;i<warehouses.length;i++){
            Condition cond = new Condition();
            cond.addCondition("name",new String[]{warehouses[i].getName()});
            cond.addCondition("id",new Integer[]{warehouses[i].getId()}, ConditionItem.Relation.NOT_EQUAL);
            if(warehouseDAO.find(accountBook,cond).length > 0){
                throw new WMSServiceException("仓库名称重复："+warehouses[i].getName());
            }
        }
            warehouseDAO.update(accountBook, warehouses);
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        for (int id : ids) {
            if (warehouseDAO.find(accountBook, new Condition().addCondition("id", id)).length == 0) {
                throw new WMSServiceException(String.format("删除仓库不存在，请重新查询！(%d)", id));
            }
        }
        try {
            warehouseDAO.remove(accountBook, ids);
        } catch (Exception e) {
            throw new WMSServiceException("删除供仓库失败，如果仓库已经被引用，需要先删除引用的内容，才能删除该仓库！");
        }
    }

    @Transactional
    public WarehouseView[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.warehouseDAO.find(accountBook, cond);
    }

    @Transactional
    public Warehouse[] findTable(String accountBook, Condition cond) throws WMSServiceException{
        return this.warehouseDAO.findTable(accountBook, cond);
    }

    @Override
    @Transactional
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.warehouseDAO.findCount(database,cond);
    }
}
