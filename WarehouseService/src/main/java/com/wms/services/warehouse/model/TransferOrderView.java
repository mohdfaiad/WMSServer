package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class TransferOrderView {
    private int id;
    private int warehouseId;
    private String no;
    private String description;
    private int state;
    private BigDecimal printTimes;
    private int createPersonId;
    private Timestamp createTime;
    private String lastUpdatePersonId;
    private Timestamp lastUpdateTime;
    private String warehouseName;
    private String createPersonName;
    private String lastUpdatePersonName;

    @Id
    @Basic
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID", nullable = false)
    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "No", nullable = false, length = 64)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    @Basic
    @Column(name = "Description", nullable = true, length = 64)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "State", nullable = false)
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Basic
    @Column(name = "PrintTimes", nullable = false, precision = 3)
    public BigDecimal getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(BigDecimal printTimes) {
        this.printTimes = printTimes;
    }

    @Basic
    @Column(name = "CreatePersonID", nullable = false)
    public int getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(int createPersonId) {
        this.createPersonId = createPersonId;
    }

    @Basic
    @Column(name = "CreateTime", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "LastUpdatePersonID", nullable = true, length = 64)
    public String getLastUpdatePersonId() {
        return lastUpdatePersonId;
    }

    public void setLastUpdatePersonId(String lastUpdatePersonId) {
        this.lastUpdatePersonId = lastUpdatePersonId;
    }

    @Basic
    @Column(name = "LastUpdateTime", nullable = true)
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "WarehouseName", nullable = true, length = 64)
    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Basic
    @Column(name = "CreatePersonName", nullable = true, length = 64)
    public String getCreatePersonName() {
        return createPersonName;
    }

    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    @Basic
    @Column(name = "LastUpdatePersonName", nullable = true, length = 64)
    public String getLastUpdatePersonName() {
        return lastUpdatePersonName;
    }

    public void setLastUpdatePersonName(String lastUpdatePersonName) {
        this.lastUpdatePersonName = lastUpdatePersonName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferOrderView that = (TransferOrderView) o;
        return id == that.id &&
                warehouseId == that.warehouseId &&
                state == that.state &&
                createPersonId == that.createPersonId &&
                Objects.equals(no, that.no) &&
                Objects.equals(description, that.description) &&
                Objects.equals(printTimes, that.printTimes) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(lastUpdatePersonId, that.lastUpdatePersonId) &&
                Objects.equals(lastUpdateTime, that.lastUpdateTime) &&
                Objects.equals(warehouseName, that.warehouseName) &&
                Objects.equals(createPersonName, that.createPersonName) &&
                Objects.equals(lastUpdatePersonName, that.lastUpdatePersonName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, no, description, state, printTimes, createPersonId, createTime, lastUpdatePersonId, lastUpdateTime, warehouseName, createPersonName, lastUpdatePersonName);
    }
}
