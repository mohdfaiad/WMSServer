package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.TransferRecordDAO;
import com.wms.utilities.IDChecker;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.TransferRecord;
import com.wms.utilities.model.TransferRecordView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.stream.Stream;
@Service
@Transactional
public class TransferRecordServiceImpl implements TransferRecordService {

    @Autowired
    TransferRecordDAO transferRecordDAO;
    @Autowired
    IDChecker idChecker;
    @Autowired
    SessionFactory sessionFactory;

    @Override

    public int[] add(String accountBook, TransferRecord[] transferRecords) throws WMSServiceException {
        Stream.of(transferRecords).forEach(transferRecord -> {
            this.idChecker.check(WarehouseService.class,accountBook,transferRecord.getWarehouseId(),"仓库");
            this.idChecker.check(SupplyService.class,accountBook,transferRecord.getSupplyId(),"供货");
        });
        for(int i=0;i<transferRecords.length;i++){
            transferRecords[i].setTime(new Timestamp(System.currentTimeMillis()));
        }
        return this.transferRecordDAO.add(accountBook,transferRecords);

    }

    @Override
    public TransferRecordView[] find(String accountBook, Condition cond) throws WMSServiceException{
            return this.transferRecordDAO.find(accountBook, cond);
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.transferRecordDAO.findCount(database,cond);
    }

}
