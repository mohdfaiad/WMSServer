package com.wms.services.settlement.service;

import com.wms.services.settlement.dao.SummaryNoteDAO;
import com.wms.services.settlement.dao.SummaryNoteItemDAO;
import com.wms.services.warehouse.service.SupplierServices;
import com.wms.services.warehouse.service.WarehouseService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@Transactional
public class SummaryNoteItemServiceImpl
implements SummaryNoteItemService{
    @Autowired
    SummaryNoteItemDAO summaryNoteItemDAO;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    SupplierServices supplierServices;

    @Override
    public int[] add(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException
    {
        this.validateEntities(accountBook,summaryNoteItems);
        return summaryNoteItemDAO.add(accountBook,summaryNoteItems);
    }

    @Override
    public void update(String accountBook, SummaryNoteItem[] summaryNoteItems) throws WMSServiceException
    {
        this.validateEntities(accountBook,summaryNoteItems);
        summaryNoteItemDAO.update(accountBook, summaryNoteItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        try {
            for (int id : ids) {
                if (summaryNoteItemDAO.find(accountBook, new Condition().addCondition("id", new Integer[]{id})).length == 0) {
                    throw new WMSServiceException(String.format("删除汇总单条目不存在，请重新查询！(%d)", id));
                }
            }

            summaryNoteItemDAO.remove(accountBook, ids);
        }
        catch (Throwable ex){
            throw new WMSServiceException("删除汇总单条目信息失败，如果汇总单条目信息已经被引用，需要先删除引用的内容，才能删除该汇总单条目");
        }
    }

    @Override
    public SummaryNoteItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.summaryNoteItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook,SummaryNoteItem[] summaryNoteItems) throws WMSServiceException{
        Stream.of(summaryNoteItems).forEach((summaryNoteItem -> {
            new Validator("使用面积").greaterThan(0).notEmpty().notnull().validate(summaryNoteItem.getArea());
            new Validator("放置天数").notEmpty().notnull().greaterThan(0).validate(summaryNoteItem.getDays());
            if(this.supplierServices.find(accountBook,
                    new Condition().addCondition("id",summaryNoteItem.getSupplierId())).length == 0){
                throw new WMSServiceException(String.format("供应商不存在，请重新提交！(%d)",summaryNoteItem.getSupplierId()));
            }
        }));
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.summaryNoteItemDAO.findCount(database,cond);
    }
}