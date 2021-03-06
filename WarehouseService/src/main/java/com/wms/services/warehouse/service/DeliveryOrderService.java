package com.wms.services.warehouse.service;

import com.wms.services.warehouse.datastructures.*;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.model.*;
import com.wms.utilities.service.BaseService;

import java.util.List;

public interface DeliveryOrderService
 extends BaseService<DeliveryOrder,DeliveryOrderView>{

    int STATE_IN_LOADING = 0;
    int STATE_PARTIAL_LOADING = 1;
    int STATE_ALL_LOADING = 2;
    int STATE_IN_DELIVER = 3;
    int STATE_DELIVER_FINNISH = 4;

    int DELIVERY_TYPE_Unqualified=1;
    int DELIVERY_TYPE_Qualified=0;
    public void transferPakage(String accountBook, TransferArgs transferArgs);
    public List<TransferOrderItemView> transferAuto(String accountBook, TransferAuto TransferAuto);
    public List<TransferOrderItemView> transferAutoNew(String accountBook, TransferAuto TransferAuto);
    public List<TransferOrderItemView> putAwayAuto(String accountBook, TransferAuto TransferAuto);
    public void deliveryFinish(String accountBook,DeliveryFinish deliveryFinish);
    public void decreaseInAccounting(String accountBook,List<Integer> ids);
    public List<DeliveryOrderItemView> deliveryByPakage(String accountBook, DeliveryByPakage deliveryByPakage);
    List<DeliveryOrderAndItems> getPreviewData(String accountBook, List<Integer> deliveryOrderIDs) throws WMSServiceException;


}
