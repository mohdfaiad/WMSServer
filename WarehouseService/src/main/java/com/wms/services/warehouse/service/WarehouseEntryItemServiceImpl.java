package com.wms.services.warehouse.service;

import com.wms.services.warehouse.dao.WarehouseEntryItemDAO;
import com.wms.services.warehouse.datastructures.TransferStock;
import com.wms.utilities.IDChecker;
import com.wms.utilities.ReflectHelper;
import com.wms.utilities.datastructures.ConditionItem;
import com.wms.utilities.model.*;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.service.WMSServiceException;
import com.wms.utilities.vaildator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.stream.Stream;

@Service
@Transactional
public class WarehouseEntryItemServiceImpl implements WarehouseEntryItemService {
    @Autowired
    WarehouseEntryItemDAO warehouseEntryItemDAO;
    @Autowired
    WarehouseEntryService warehouseEntryService;
    @Autowired
    StockRecordService stockRecordService;
    @Autowired
    SupplyService supplyService;
    @Autowired
    StorageLocationService storageLocationService;
    @Autowired
    PersonService personService;
    @Autowired
    IDChecker idChecker;

    @Override
    public int[] add(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        if (warehouseEntryItems.length == 0) return new int[]{};
        WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook, warehouseEntryItems);
        //验证字段
        this.validateEntities(accountBook, warehouseEntryItems);
        //增加库存
        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            TransferStock transferStock = new TransferStock();
            transferStock.setAmount(warehouseEntryItem.getRealAmount());
            transferStock.setUnit(warehouseEntryItem.getUnit());
            transferStock.setUnitAmount(warehouseEntryItem.getUnitAmount());
            transferStock.setInventoryDate(warehouseEntryItem.getInventoryDate());
            transferStock.setRelatedOrderNo(warehouseEntryView.getNo());
            transferStock.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
            transferStock.setSupplyId(warehouseEntryItem.getSupplyId());
            this.stockRecordService.addAmount(accountBook, transferStock);
        });
        //添加到数据库中
        int[] ids = this.warehouseEntryItemDAO.add(accountBook, warehouseEntryItems);
        return ids;
    }

    @Override
    public void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems) throws WMSServiceException {
        this.update(accountBook,warehouseEntryItems,false);
    }

    @Override
    public void update(String accountBook, WarehouseEntryItem[] warehouseEntryItems,boolean allowUpdateInspectionAmount) throws WMSServiceException {
        WarehouseEntryView warehouseEntryView = this.getWarehouseEntryView(accountBook, warehouseEntryItems);
        this.validateEntities(accountBook, warehouseEntryItems);
        Stream.of(warehouseEntryItems).forEach(warehouseEntryItem -> {
            WarehouseEntryItemView[] foundOriItems = this.warehouseEntryItemDAO.find(accountBook, new Condition().addCondition("id", warehouseEntryItem.getId()));
            if (foundOriItems.length == 0)
                throw new WMSServiceException(String.format("入库单条目不存在，请重新提交！", warehouseEntryItem.getId()));
            WarehouseEntryItemView oriItemView = foundOriItems[0];
            BigDecimal deltaRealAmount = warehouseEntryItem.getRealAmount().subtract(oriItemView.getRealAmount());
            //不用管拒收数量 BigDecimal deltaRefuseAmount = warehouseEntryItem.getRefuseAmount().subtract(oriItemView.getRefuseAmount());
            //送检数量不能改
            BigDecimal deltaInspectionAmount = warehouseEntryItem.getInspectionAmount().subtract(oriItemView.getInspectionAmount());
            if (!allowUpdateInspectionAmount && deltaInspectionAmount.compareTo(BigDecimal.ZERO) != 0) {
                throw new WMSServiceException("不允许修改入库单条目送检数量！");
            }
            //修改实收数量，单位或单位数量，或收货库区，更新库存
            if (deltaRealAmount.compareTo(BigDecimal.ZERO)!=0
                    || oriItemView.getUnit().compareTo(warehouseEntryItem.getUnit()) != 0
                    || oriItemView.getUnitAmount().compareTo(warehouseEntryItem.getUnitAmount()) != 0
                    || oriItemView.getStorageLocationId() != warehouseEntryItem.getStorageLocationId()) {
                //如果已经送检，禁止修改入库数量
                if(warehouseEntryItem.getState() != WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE){
                    throw new WMSServiceException(String.format("不允许修改已送检/入库的入库单条目(%d)",warehouseEntryItem.getId()));
                }
                //冲抵原库存
                TransferStock transferStockAgainst = new TransferStock();
                transferStockAgainst.setAmount(oriItemView.getRealAmount().negate());
                transferStockAgainst.setUnit(oriItemView.getUnit());
                transferStockAgainst.setUnitAmount(oriItemView.getUnitAmount());
                transferStockAgainst.setInventoryDate(oriItemView.getInventoryDate());
                transferStockAgainst.setRelatedOrderNo(warehouseEntryView.getNo());
                transferStockAgainst.setSourceStorageLocationId(oriItemView.getStorageLocationId());
                transferStockAgainst.setSupplyId(oriItemView.getSupplyId());
                this.stockRecordService.addAmount(accountBook, transferStockAgainst);

                //增加新库存
                TransferStock transferStockNew = new TransferStock();
                transferStockNew.setAmount(warehouseEntryItem.getRealAmount());
                transferStockNew.setUnit(warehouseEntryItem.getUnit());
                transferStockNew.setUnitAmount(warehouseEntryItem.getUnitAmount());
                transferStockNew.setInventoryDate(warehouseEntryItem.getInventoryDate());
                transferStockNew.setRelatedOrderNo(warehouseEntryView.getNo());
                transferStockNew.setSourceStorageLocationId(warehouseEntryItem.getStorageLocationId());
                transferStockNew.setSupplyId(warehouseEntryItem.getSupplyId());
                this.stockRecordService.addAmount(accountBook, transferStockNew);
            }
        });
        this.warehouseEntryItemDAO.update(accountBook, warehouseEntryItems);
    }

    @Override
    public void remove(String accountBook, int[] ids) throws WMSServiceException {
        WarehouseEntryView warehouseEntryView = null;
        for(int id : ids) {
            WarehouseEntryItemView[] foundItemViews = this.warehouseEntryItemDAO.find(accountBook,new Condition().addCondition("id",id));
            if(foundItemViews.length == 0){
                throw new WMSServiceException(String.format("无法找到入库单条目，请重新提交(%d)"));
            }
            WarehouseEntryItemView oriItemView = foundItemViews[0];
            if(warehouseEntryView == null) {
                final WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", oriItemView.getWarehouseEntryId()));
                if (warehouseEntryViews.length == 0)
                    throw new WMSServiceException(String.format("入库单(%d)不存在，请重新提交！", oriItemView.getWarehouseEntryId()));
                warehouseEntryView = warehouseEntryViews[0];
            }
            if(oriItemView.getState() != WarehouseEntryItemService.WAIT_FOR_PUT_IN_STORAGE){
                throw new WMSServiceException(String.format("不允许删除已送检/已入库的入库单条目(%d)",oriItemView.getId()));
            }
            //冲抵原库存
            TransferStock transferStockAgainst = new TransferStock();
            transferStockAgainst.setAmount(oriItemView.getRealAmount().negate());
            transferStockAgainst.setUnit(oriItemView.getUnit());
            transferStockAgainst.setUnitAmount(oriItemView.getUnitAmount());
            transferStockAgainst.setInventoryDate(oriItemView.getInventoryDate());
            transferStockAgainst.setRelatedOrderNo(warehouseEntryView.getNo());
            transferStockAgainst.setSourceStorageLocationId(oriItemView.getStorageLocationId());
            transferStockAgainst.setSupplyId(oriItemView.getSupplyId());
            this.stockRecordService.addAmount(accountBook, transferStockAgainst);
        }
        try {
            this.warehouseEntryItemDAO.remove(accountBook, ids);
        } catch (Throwable ex) {
            throw new WMSServiceException("删除失败，如果条目已经被引用，需要先删除引用项目");
        }
    }

    @Override
    public WarehouseEntryItemView[] find(String accountBook, Condition cond) throws WMSServiceException {
        return this.warehouseEntryItemDAO.find(accountBook, cond);
    }

    private void validateEntities(String accountBook, WarehouseEntryItem[] warehouseEntryItems) {
        //数据验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    new Validator("订单数量").min(0).validate(warehouseEntryItem.getExpectedAmount());
                    new Validator("实收数量").min(0).max(warehouseEntryItem.getExpectedAmount()).validate(warehouseEntryItem.getRealAmount());
                    new Validator("单位数量").min(0).validate(warehouseEntryItem.getUnitAmount());
                    new Validator("已分配送检数量").min(0).max(warehouseEntryItem.getRealAmount()).validate(warehouseEntryItem.getInspectionAmount());
                    new Validator("拒收数量").min(0).max(warehouseEntryItem.getExpectedAmount()).validate(warehouseEntryItem.getRefuseAmount());
                    new Validator("拒收单位").notEmpty().validate(warehouseEntryItem.getRefuseUnit());
                    new Validator("拒收单位数量").min(0).validate(warehouseEntryItem.getRefuseUnitAmount());
                }
        );

        //外键验证
        Stream.of(warehouseEntryItems).forEach(
                (warehouseEntryItem) -> {
                    if (this.warehouseEntryService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getWarehouseEntryId())).length == 0) {
                        throw new WMSServiceException(String.format("入库单不存在，请重新提交！(%d)", warehouseEntryItem.getWarehouseEntryId()));
                    } else if (supplyService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getSupplyId())).length == 0) {
                        throw new WMSServiceException(String.format("供货信息不存在，请重新提交！(%d)", warehouseEntryItem.getSupplyId()));
                    } else if (storageLocationService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getStorageLocationId())).length == 0) {
                        throw new WMSServiceException(String.format("库位不存在，请重新提交！(%d)", warehouseEntryItem.getStorageLocationId()));
                    } else if (warehouseEntryItem.getPersonId() != null && personService.find(accountBook,
                            new Condition().addCondition("id", warehouseEntryItem.getPersonId())).length == 0) {
                        throw new WMSServiceException(String.format("作业人员不存在，请重新提交！(%d)", warehouseEntryItem.getPersonId()));
                    }
                }
        );
    }

    private WarehouseEntryView getWarehouseEntryView(String accountBook, WarehouseEntryItem[] warehouseEntryItems) {
        //验证入库单号必须全部相同
        Stream.of(warehouseEntryItems).reduce((last, cur) -> {
            if (last.getWarehouseEntryId() != cur.getWarehouseEntryId())
                throw new WMSServiceException("入库单条目所属的入库单必须相同！");
            return cur;
        });
        //获取入库单
        int warehouseEntryID = warehouseEntryItems[0].getWarehouseEntryId();
        final WarehouseEntryView[] warehouseEntryViews = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", warehouseEntryID));
        if (warehouseEntryViews.length == 0)
            throw new WMSServiceException(String.format("入库单(%d)不存在，请重新提交！", warehouseEntryID));
        WarehouseEntryView warehouseEntryView = warehouseEntryViews[0];
        return warehouseEntryView;
    }

    @Override
    public long findCount(String database,Condition cond) throws WMSServiceException{
        return this.warehouseEntryItemDAO.findCount(database,cond);
    }

    @Override
    public void receive(String accountBook,int[] ids) {
        WarehouseEntryItemView[] itemViews = this.find(accountBook, new Condition().addCondition("id", ReflectHelper.intArrayToIntegerArray(ids), ConditionItem.Relation.IN));
        Stream.of(itemViews).forEach((warehouseEntryItemView -> {
            if(warehouseEntryItemView.getState() == WarehouseEntryItemService.UNQUALIFIED ||
                    warehouseEntryItemView.getState() == WarehouseEntryItemService.QUALIFIED){
                throw new WMSServiceException(warehouseEntryItemView.getWarehouseEntryNo()+"已经入库，请不要重复入库！");
            }
            this.idChecker.check(StorageLocationService.class, accountBook, warehouseEntryItemView.getQualifiedStorageLocationId(), "合格品库位");
            WarehouseEntryView warehouseEntryView = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", warehouseEntryItemView.getWarehouseEntryId()))[0];
            TransferStock transferStock = new TransferStock();
            transferStock.setSourceStorageLocationId(warehouseEntryItemView.getStorageLocationId());
            transferStock.setNewStorageLocationId(warehouseEntryItemView.getQualifiedStorageLocationId());
            transferStock.setAmount(warehouseEntryItemView.getRealAmount());
            transferStock.setSupplyId(warehouseEntryItemView.getSupplyId());
            transferStock.setUnit(warehouseEntryItemView.getUnit());
            transferStock.setUnitAmount(warehouseEntryItemView.getUnitAmount());
            transferStock.setRelatedOrderNo(warehouseEntryView.getNo() + "(正品移库)");
            this.stockRecordService.RealTransformStock(accountBook,transferStock);

            warehouseEntryItemView.setState(WarehouseEntryItemService.QUALIFIED);
            WarehouseEntryItem warehouseEntryItem = ReflectHelper.createAndCopyFields(warehouseEntryItemView,WarehouseEntryItem.class);
            this.update(accountBook,new WarehouseEntryItem[]{warehouseEntryItem});
        }));
    }

    @Override
    public void reject(String accountBook,int[] ids) {
        WarehouseEntryItemView[] itemViews = this.find(accountBook, new Condition().addCondition("id", ReflectHelper.intArrayToIntegerArray(ids), ConditionItem.Relation.IN));
        Stream.of(itemViews).forEach((warehouseEntryItemView -> {
            if(warehouseEntryItemView.getState() == WarehouseEntryItemService.UNQUALIFIED ||
                    warehouseEntryItemView.getState() == WarehouseEntryItemService.QUALIFIED){
                throw new WMSServiceException(warehouseEntryItemView.getWarehouseEntryNo()+"已经入库，请不要重复入库！");
            }
            this.idChecker.check(StorageLocationService.class, accountBook, warehouseEntryItemView.getUnqualifiedStorageLocationId(), "不良品库位");
            WarehouseEntryView warehouseEntryView = this.warehouseEntryService.find(accountBook, new Condition().addCondition("id", warehouseEntryItemView.getWarehouseEntryId()))[0];
            TransferStock transferStock = new TransferStock();
            transferStock.setSourceStorageLocationId(warehouseEntryItemView.getStorageLocationId());
            transferStock.setNewStorageLocationId(warehouseEntryItemView.getUnqualifiedStorageLocationId());
            transferStock.setAmount(warehouseEntryItemView.getRealAmount());
            transferStock.setSupplyId(warehouseEntryItemView.getSupplyId());
            transferStock.setUnit(warehouseEntryItemView.getUnit());
            transferStock.setUnitAmount(warehouseEntryItemView.getUnitAmount());
            transferStock.setRelatedOrderNo(warehouseEntryView.getNo() + "(不良品移库)");
            this.stockRecordService.RealTransformStock(accountBook,transferStock);

            warehouseEntryItemView.setState(WarehouseEntryItemService.UNQUALIFIED);
            WarehouseEntryItem warehouseEntryItem = ReflectHelper.createAndCopyFields(warehouseEntryItemView, WarehouseEntryItem.class);
            this.update(accountBook,new WarehouseEntryItem[]{warehouseEntryItem});
        }));
    }
}
