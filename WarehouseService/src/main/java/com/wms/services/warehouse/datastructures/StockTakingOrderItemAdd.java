package com.wms.services.warehouse.datastructures;

import org.omg.CORBA.INTERNAL;

import java.sql.Timestamp;

public class StockTakingOrderItemAdd {
    public int getStockTakingOrderId() {
        return stockTakingOrderId;
    }

    public int getSupplyId() {
        return supplyId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setStockTakingOrderId(int stockTakingOrderId) {
        this.stockTakingOrderId = stockTakingOrderId;
    }

    public void setSupplyId(int supplyId) {
        this.supplyId = supplyId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }
    int stockTakingOrderId;

    int supplyId;

    Integer personId;

    Timestamp checkTime=new Timestamp(System.currentTimeMillis());

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    int mode=0;

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    int warehouseId;
}
