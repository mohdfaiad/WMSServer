package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.SupplyDAO;
import com.wms.services.warehouse.model.Supply;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SupplyServiceImpl implements SupplyService {
    @Autowired
    SupplyDAO supplyDAO;
    @Transactional
    public int[] add(String accountBook, Supply[] supplies) throws WMSServiceException{
        try{
            return supplyDAO.add(accountBook,supplies);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void update(String accountBook, Supply[] supplies) throws WMSServiceException{
        try {
            supplyDAO.update(accountBook, supplies);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{

        /*
        for (int i=0;i<ids.length;i++)
        {

            Supply supplyRefference;
            ReceiptTicketItem[] receiptTicketItem;//收货单零件条目
            StockInfo[] stockInfo;//库存信息
            int SupplyID=ids[i];
            Condition condition = Condition.fromJson("{\"conditions\":[{\"key\":\"SupplyID\",\"values\":[\"" + SupplyID + "\"],\"relation\":\"EQUAL\"}], \"orders\":[{\"key\":\"name\",\"order\":\"ASC\"}]}");

        }
        */
        try {
            supplyDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }
    @Transactional
    public Supply[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.supplyDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
