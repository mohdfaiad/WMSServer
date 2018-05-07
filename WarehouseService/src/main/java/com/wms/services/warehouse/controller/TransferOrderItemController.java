package com.wms.services.warehouse.controller;

import com.wms.utilities.model.TransferOrderItem;
import com.wms.utilities.model.TransferOrderItemView;

public interface TransferOrderItemController {
    void remove(String accountBook,String strIDs);
    void update(String accountBook,TransferOrderItem transferOrderItems[]);
    TransferOrderItemView[] find(String accountBook, String condStr);
}
