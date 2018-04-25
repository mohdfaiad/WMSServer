package com.wms.services.warehouse.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Supply {
    private int id;
    private Integer warehouseId;
    private Integer supplierId;
    private Integer materialId;
    private String defaultReceiptUnit;
    private BigDecimal defaultReceiptUnitAmount;
    private String defaultSubmissionUnit;
    private BigDecimal defaultSubmissionUnitAmount;
    private String defaultPrepareUnit;
    private BigDecimal defaultPrepareUnitAmount;
    private String defaultShipmentUnit;
    private BigDecimal defaultShipmentUnitAmount;
    private BigDecimal validPeriod;
    private String photoIndex;
    private String containerNo;
    private String factroy;
    private String workPosition;
    private String supplierType;
    private String type;
    private String size;
    private String groupPrincipal;
    private BigDecimal charge1;
    private BigDecimal charge2;
    private String singleBoxPhotoIndex;
    private String singleBoxPackagingBoxType;
    private BigDecimal singleBoxLength;
    private BigDecimal singleBoxWidth;
    private BigDecimal singleBoxHeight;
    private BigDecimal singleBoxSnp;
    private BigDecimal singleBoxRatedMinimumBoxCount;
    private BigDecimal singleBoxWeight;
    private BigDecimal singleBoxLayerCount;
    private BigDecimal singleBoxStorageCount;
    private BigDecimal singleBoxTheoreticalLayerCount;
    private BigDecimal singleBoxTheoreticalStorageHeight;
    private BigDecimal singleBoxThroreticalStorageCount;
    private String outerPackingPhotoIndex;
    private String outerPackingBoxType;
    private BigDecimal outerPackingLength;
    private BigDecimal outerPackingWidth;
    private BigDecimal outerPackingHeight;
    private String outerPackingSnp;
    private String outerPackingComment;
    private BigDecimal outerPackingRequiredLayers;
    private String shipmentInfoBoxType;
    private BigDecimal shipmentInfoBoxLength;
    private BigDecimal shipmentInfoBoxWidth;
    private BigDecimal shipmentInfoBoxHeight;
    private Integer isHistory;
    private Integer newestSupplyId;
    private int createPersonId;
    private Timestamp createTime;
    private Integer lastUpdatePersonId;
    private Timestamp lastUpdateTime;
    private int enabled;

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "WarehouseID", nullable = true)
    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Basic
    @Column(name = "SupplierID", nullable = true)
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    @Basic
    @Column(name = "MaterialID", nullable = true)
    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    @Basic
    @Column(name = "DefaultReceiptUnit", nullable = true, length = 64)
    public String getDefaultReceiptUnit() {
        return defaultReceiptUnit;
    }

    public void setDefaultReceiptUnit(String defaultReceiptUnit) {
        this.defaultReceiptUnit = defaultReceiptUnit;
    }

    @Basic
    @Column(name = "DefaultReceiptUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultReceiptUnitAmount() {
        return defaultReceiptUnitAmount;
    }

    public void setDefaultReceiptUnitAmount(BigDecimal defaultReceiptUnitAmount) {
        this.defaultReceiptUnitAmount = defaultReceiptUnitAmount;
    }

    @Basic
    @Column(name = "DefaultSubmissionUnit", nullable = true, length = 64)
    public String getDefaultSubmissionUnit() {
        return defaultSubmissionUnit;
    }

    public void setDefaultSubmissionUnit(String defaultSubmissionUnit) {
        this.defaultSubmissionUnit = defaultSubmissionUnit;
    }

    @Basic
    @Column(name = "DefaultSubmissionUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultSubmissionUnitAmount() {
        return defaultSubmissionUnitAmount;
    }

    public void setDefaultSubmissionUnitAmount(BigDecimal defaultSubmissionUnitAmount) {
        this.defaultSubmissionUnitAmount = defaultSubmissionUnitAmount;
    }

    @Basic
    @Column(name = "DefaultPrepareUnit", nullable = true, length = 64)
    public String getDefaultPrepareUnit() {
        return defaultPrepareUnit;
    }

    public void setDefaultPrepareUnit(String defaultPrepareUnit) {
        this.defaultPrepareUnit = defaultPrepareUnit;
    }

    @Basic
    @Column(name = "DefaultPrepareUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultPrepareUnitAmount() {
        return defaultPrepareUnitAmount;
    }

    public void setDefaultPrepareUnitAmount(BigDecimal defaultPrepareUnitAmount) {
        this.defaultPrepareUnitAmount = defaultPrepareUnitAmount;
    }

    @Basic
    @Column(name = "DefaultShipmentUnit", nullable = true, length = 64)
    public String getDefaultShipmentUnit() {
        return defaultShipmentUnit;
    }

    public void setDefaultShipmentUnit(String defaultShipmentUnit) {
        this.defaultShipmentUnit = defaultShipmentUnit;
    }

    @Basic
    @Column(name = "DefaultShipmentUnitAmount", nullable = true, precision = 3)
    public BigDecimal getDefaultShipmentUnitAmount() {
        return defaultShipmentUnitAmount;
    }

    public void setDefaultShipmentUnitAmount(BigDecimal defaultShipmentUnitAmount) {
        this.defaultShipmentUnitAmount = defaultShipmentUnitAmount;
    }

    @Basic
    @Column(name = "ValidPeriod", nullable = true, precision = 3)
    public BigDecimal getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(BigDecimal validPeriod) {
        this.validPeriod = validPeriod;
    }

    @Basic
    @Column(name = "PhotoIndex", nullable = true, length = 64)
    public String getPhotoIndex() {
        return photoIndex;
    }

    public void setPhotoIndex(String photoIndex) {
        this.photoIndex = photoIndex;
    }

    @Basic
    @Column(name = "ContainerNo", nullable = true, length = 64)
    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    @Basic
    @Column(name = "Factroy", nullable = true, length = 64)
    public String getFactroy() {
        return factroy;
    }

    public void setFactroy(String factroy) {
        this.factroy = factroy;
    }

    @Basic
    @Column(name = "WorkPosition", nullable = true, length = 64)
    public String getWorkPosition() {
        return workPosition;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    @Basic
    @Column(name = "SupplierType", nullable = true, length = 64)
    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @Basic
    @Column(name = "Type", nullable = true, length = 64)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "Size", nullable = true, length = 64)
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Basic
    @Column(name = "GroupPrincipal", nullable = true, length = 64)
    public String getGroupPrincipal() {
        return groupPrincipal;
    }

    public void setGroupPrincipal(String groupPrincipal) {
        this.groupPrincipal = groupPrincipal;
    }

    @Basic
    @Column(name = "Charge1", nullable = true, precision = 3)
    public BigDecimal getCharge1() {
        return charge1;
    }

    public void setCharge1(BigDecimal charge1) {
        this.charge1 = charge1;
    }

    @Basic
    @Column(name = "Charge2", nullable = true, precision = 3)
    public BigDecimal getCharge2() {
        return charge2;
    }

    public void setCharge2(BigDecimal charge2) {
        this.charge2 = charge2;
    }

    @Basic
    @Column(name = "SingleBoxPhotoIndex", nullable = true, length = 64)
    public String getSingleBoxPhotoIndex() {
        return singleBoxPhotoIndex;
    }

    public void setSingleBoxPhotoIndex(String singleBoxPhotoIndex) {
        this.singleBoxPhotoIndex = singleBoxPhotoIndex;
    }

    @Basic
    @Column(name = "SingleBoxPackagingBoxType", nullable = true, length = 64)
    public String getSingleBoxPackagingBoxType() {
        return singleBoxPackagingBoxType;
    }

    public void setSingleBoxPackagingBoxType(String singleBoxPackagingBoxType) {
        this.singleBoxPackagingBoxType = singleBoxPackagingBoxType;
    }

    @Basic
    @Column(name = "SingleBoxLength", nullable = true, precision = 3)
    public BigDecimal getSingleBoxLength() {
        return singleBoxLength;
    }

    public void setSingleBoxLength(BigDecimal singleBoxLength) {
        this.singleBoxLength = singleBoxLength;
    }

    @Basic
    @Column(name = "SingleBoxWidth", nullable = true, precision = 3)
    public BigDecimal getSingleBoxWidth() {
        return singleBoxWidth;
    }

    public void setSingleBoxWidth(BigDecimal singleBoxWidth) {
        this.singleBoxWidth = singleBoxWidth;
    }

    @Basic
    @Column(name = "SingleBoxHeight", nullable = true, precision = 3)
    public BigDecimal getSingleBoxHeight() {
        return singleBoxHeight;
    }

    public void setSingleBoxHeight(BigDecimal singleBoxHeight) {
        this.singleBoxHeight = singleBoxHeight;
    }

    @Basic
    @Column(name = "SingleBoxSNP", nullable = true, precision = 3)
    public BigDecimal getSingleBoxSnp() {
        return singleBoxSnp;
    }

    public void setSingleBoxSnp(BigDecimal singleBoxSnp) {
        this.singleBoxSnp = singleBoxSnp;
    }

    @Basic
    @Column(name = "SingleBoxRatedMinimumBoxCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxRatedMinimumBoxCount() {
        return singleBoxRatedMinimumBoxCount;
    }

    public void setSingleBoxRatedMinimumBoxCount(BigDecimal singleBoxRatedMinimumBoxCount) {
        this.singleBoxRatedMinimumBoxCount = singleBoxRatedMinimumBoxCount;
    }

    @Basic
    @Column(name = "SingleBoxWeight", nullable = true, precision = 3)
    public BigDecimal getSingleBoxWeight() {
        return singleBoxWeight;
    }

    public void setSingleBoxWeight(BigDecimal singleBoxWeight) {
        this.singleBoxWeight = singleBoxWeight;
    }

    @Basic
    @Column(name = "SingleBoxLayerCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxLayerCount() {
        return singleBoxLayerCount;
    }

    public void setSingleBoxLayerCount(BigDecimal singleBoxLayerCount) {
        this.singleBoxLayerCount = singleBoxLayerCount;
    }

    @Basic
    @Column(name = "SingleBoxStorageCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxStorageCount() {
        return singleBoxStorageCount;
    }

    public void setSingleBoxStorageCount(BigDecimal singleBoxStorageCount) {
        this.singleBoxStorageCount = singleBoxStorageCount;
    }

    @Basic
    @Column(name = "SingleBoxTheoreticalLayerCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxTheoreticalLayerCount() {
        return singleBoxTheoreticalLayerCount;
    }

    public void setSingleBoxTheoreticalLayerCount(BigDecimal singleBoxTheoreticalLayerCount) {
        this.singleBoxTheoreticalLayerCount = singleBoxTheoreticalLayerCount;
    }

    @Basic
    @Column(name = "SingleBoxTheoreticalStorageHeight", nullable = true, precision = 3)
    public BigDecimal getSingleBoxTheoreticalStorageHeight() {
        return singleBoxTheoreticalStorageHeight;
    }

    public void setSingleBoxTheoreticalStorageHeight(BigDecimal singleBoxTheoreticalStorageHeight) {
        this.singleBoxTheoreticalStorageHeight = singleBoxTheoreticalStorageHeight;
    }

    @Basic
    @Column(name = "SingleBoxThroreticalStorageCount", nullable = true, precision = 3)
    public BigDecimal getSingleBoxThroreticalStorageCount() {
        return singleBoxThroreticalStorageCount;
    }

    public void setSingleBoxThroreticalStorageCount(BigDecimal singleBoxThroreticalStorageCount) {
        this.singleBoxThroreticalStorageCount = singleBoxThroreticalStorageCount;
    }

    @Basic
    @Column(name = "OuterPackingPhotoIndex", nullable = true, length = 64)
    public String getOuterPackingPhotoIndex() {
        return outerPackingPhotoIndex;
    }

    public void setOuterPackingPhotoIndex(String outerPackingPhotoIndex) {
        this.outerPackingPhotoIndex = outerPackingPhotoIndex;
    }

    @Basic
    @Column(name = "OuterPackingBoxType", nullable = true, length = 64)
    public String getOuterPackingBoxType() {
        return outerPackingBoxType;
    }

    public void setOuterPackingBoxType(String outerPackingBoxType) {
        this.outerPackingBoxType = outerPackingBoxType;
    }

    @Basic
    @Column(name = "OuterPackingLength", nullable = true, precision = 3)
    public BigDecimal getOuterPackingLength() {
        return outerPackingLength;
    }

    public void setOuterPackingLength(BigDecimal outerPackingLength) {
        this.outerPackingLength = outerPackingLength;
    }

    @Basic
    @Column(name = "OuterPackingWidth", nullable = true, precision = 3)
    public BigDecimal getOuterPackingWidth() {
        return outerPackingWidth;
    }

    public void setOuterPackingWidth(BigDecimal outerPackingWidth) {
        this.outerPackingWidth = outerPackingWidth;
    }

    @Basic
    @Column(name = "OuterPackingHeight", nullable = true, precision = 3)
    public BigDecimal getOuterPackingHeight() {
        return outerPackingHeight;
    }

    public void setOuterPackingHeight(BigDecimal outerPackingHeight) {
        this.outerPackingHeight = outerPackingHeight;
    }

    @Basic
    @Column(name = "OuterPackingSNP", nullable = true, length = 64)
    public String getOuterPackingSnp() {
        return outerPackingSnp;
    }

    public void setOuterPackingSnp(String outerPackingSnp) {
        this.outerPackingSnp = outerPackingSnp;
    }

    @Basic
    @Column(name = "OuterPackingComment", nullable = true, length = 64)
    public String getOuterPackingComment() {
        return outerPackingComment;
    }

    public void setOuterPackingComment(String outerPackingComment) {
        this.outerPackingComment = outerPackingComment;
    }

    @Basic
    @Column(name = "OuterPackingRequiredLayers", nullable = true, precision = 3)
    public BigDecimal getOuterPackingRequiredLayers() {
        return outerPackingRequiredLayers;
    }

    public void setOuterPackingRequiredLayers(BigDecimal outerPackingRequiredLayers) {
        this.outerPackingRequiredLayers = outerPackingRequiredLayers;
    }

    @Basic
    @Column(name = "ShipmentInfoBoxType", nullable = true, length = 64)
    public String getShipmentInfoBoxType() {
        return shipmentInfoBoxType;
    }

    public void setShipmentInfoBoxType(String shipmentInfoBoxType) {
        this.shipmentInfoBoxType = shipmentInfoBoxType;
    }

    @Basic
    @Column(name = "ShipmentInfoBoxLength", nullable = true, precision = 3)
    public BigDecimal getShipmentInfoBoxLength() {
        return shipmentInfoBoxLength;
    }

    public void setShipmentInfoBoxLength(BigDecimal shipmentInfoBoxLength) {
        this.shipmentInfoBoxLength = shipmentInfoBoxLength;
    }

    @Basic
    @Column(name = "ShipmentInfoBoxWidth", nullable = true, precision = 3)
    public BigDecimal getShipmentInfoBoxWidth() {
        return shipmentInfoBoxWidth;
    }

    public void setShipmentInfoBoxWidth(BigDecimal shipmentInfoBoxWidth) {
        this.shipmentInfoBoxWidth = shipmentInfoBoxWidth;
    }

    @Basic
    @Column(name = "ShipmentInfoBoxHeight", nullable = true, precision = 3)
    public BigDecimal getShipmentInfoBoxHeight() {
        return shipmentInfoBoxHeight;
    }

    public void setShipmentInfoBoxHeight(BigDecimal shipmentInfoBoxHeight) {
        this.shipmentInfoBoxHeight = shipmentInfoBoxHeight;
    }

    @Basic
    @Column(name = "IsHistory", nullable = true)
    public Integer getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Integer isHistory) {
        this.isHistory = isHistory;
    }

    @Basic
    @Column(name = "NewestSupplyID", nullable = true)
    public Integer getNewestSupplyId() {
        return newestSupplyId;
    }

    public void setNewestSupplyId(Integer newestSupplyId) {
        this.newestSupplyId = newestSupplyId;
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
    @Column(name = "LastUpdatePersonID", nullable = true)
    public Integer getLastUpdatePersonId() {
        return lastUpdatePersonId;
    }

    public void setLastUpdatePersonId(Integer lastUpdatePersonId) {
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
    @Column(name = "Enabled", nullable = false)
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supply supply = (Supply) o;
        return id == supply.id &&
                createPersonId == supply.createPersonId &&
                enabled == supply.enabled &&
                Objects.equals(warehouseId, supply.warehouseId) &&
                Objects.equals(supplierId, supply.supplierId) &&
                Objects.equals(materialId, supply.materialId) &&
                Objects.equals(defaultReceiptUnit, supply.defaultReceiptUnit) &&
                Objects.equals(defaultReceiptUnitAmount, supply.defaultReceiptUnitAmount) &&
                Objects.equals(defaultSubmissionUnit, supply.defaultSubmissionUnit) &&
                Objects.equals(defaultSubmissionUnitAmount, supply.defaultSubmissionUnitAmount) &&
                Objects.equals(defaultPrepareUnit, supply.defaultPrepareUnit) &&
                Objects.equals(defaultPrepareUnitAmount, supply.defaultPrepareUnitAmount) &&
                Objects.equals(defaultShipmentUnit, supply.defaultShipmentUnit) &&
                Objects.equals(defaultShipmentUnitAmount, supply.defaultShipmentUnitAmount) &&
                Objects.equals(validPeriod, supply.validPeriod) &&
                Objects.equals(photoIndex, supply.photoIndex) &&
                Objects.equals(containerNo, supply.containerNo) &&
                Objects.equals(factroy, supply.factroy) &&
                Objects.equals(workPosition, supply.workPosition) &&
                Objects.equals(supplierType, supply.supplierType) &&
                Objects.equals(type, supply.type) &&
                Objects.equals(size, supply.size) &&
                Objects.equals(groupPrincipal, supply.groupPrincipal) &&
                Objects.equals(charge1, supply.charge1) &&
                Objects.equals(charge2, supply.charge2) &&
                Objects.equals(singleBoxPhotoIndex, supply.singleBoxPhotoIndex) &&
                Objects.equals(singleBoxPackagingBoxType, supply.singleBoxPackagingBoxType) &&
                Objects.equals(singleBoxLength, supply.singleBoxLength) &&
                Objects.equals(singleBoxWidth, supply.singleBoxWidth) &&
                Objects.equals(singleBoxHeight, supply.singleBoxHeight) &&
                Objects.equals(singleBoxSnp, supply.singleBoxSnp) &&
                Objects.equals(singleBoxRatedMinimumBoxCount, supply.singleBoxRatedMinimumBoxCount) &&
                Objects.equals(singleBoxWeight, supply.singleBoxWeight) &&
                Objects.equals(singleBoxLayerCount, supply.singleBoxLayerCount) &&
                Objects.equals(singleBoxStorageCount, supply.singleBoxStorageCount) &&
                Objects.equals(singleBoxTheoreticalLayerCount, supply.singleBoxTheoreticalLayerCount) &&
                Objects.equals(singleBoxTheoreticalStorageHeight, supply.singleBoxTheoreticalStorageHeight) &&
                Objects.equals(singleBoxThroreticalStorageCount, supply.singleBoxThroreticalStorageCount) &&
                Objects.equals(outerPackingPhotoIndex, supply.outerPackingPhotoIndex) &&
                Objects.equals(outerPackingBoxType, supply.outerPackingBoxType) &&
                Objects.equals(outerPackingLength, supply.outerPackingLength) &&
                Objects.equals(outerPackingWidth, supply.outerPackingWidth) &&
                Objects.equals(outerPackingHeight, supply.outerPackingHeight) &&
                Objects.equals(outerPackingSnp, supply.outerPackingSnp) &&
                Objects.equals(outerPackingComment, supply.outerPackingComment) &&
                Objects.equals(outerPackingRequiredLayers, supply.outerPackingRequiredLayers) &&
                Objects.equals(shipmentInfoBoxType, supply.shipmentInfoBoxType) &&
                Objects.equals(shipmentInfoBoxLength, supply.shipmentInfoBoxLength) &&
                Objects.equals(shipmentInfoBoxWidth, supply.shipmentInfoBoxWidth) &&
                Objects.equals(shipmentInfoBoxHeight, supply.shipmentInfoBoxHeight) &&
                Objects.equals(isHistory, supply.isHistory) &&
                Objects.equals(newestSupplyId, supply.newestSupplyId) &&
                Objects.equals(createTime, supply.createTime) &&
                Objects.equals(lastUpdatePersonId, supply.lastUpdatePersonId) &&
                Objects.equals(lastUpdateTime, supply.lastUpdateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, warehouseId, supplierId, materialId, defaultReceiptUnit, defaultReceiptUnitAmount, defaultSubmissionUnit, defaultSubmissionUnitAmount, defaultPrepareUnit, defaultPrepareUnitAmount, defaultShipmentUnit, defaultShipmentUnitAmount, validPeriod, photoIndex, containerNo, factroy, workPosition, supplierType, type, size, groupPrincipal, charge1, charge2, singleBoxPhotoIndex, singleBoxPackagingBoxType, singleBoxLength, singleBoxWidth, singleBoxHeight, singleBoxSnp, singleBoxRatedMinimumBoxCount, singleBoxWeight, singleBoxLayerCount, singleBoxStorageCount, singleBoxTheoreticalLayerCount, singleBoxTheoreticalStorageHeight, singleBoxThroreticalStorageCount, outerPackingPhotoIndex, outerPackingBoxType, outerPackingLength, outerPackingWidth, outerPackingHeight, outerPackingSnp, outerPackingComment, outerPackingRequiredLayers, shipmentInfoBoxType, shipmentInfoBoxLength, shipmentInfoBoxWidth, shipmentInfoBoxHeight, isHistory, newestSupplyId, createPersonId, createTime, lastUpdatePersonId, lastUpdateTime, enabled);
    }
}
