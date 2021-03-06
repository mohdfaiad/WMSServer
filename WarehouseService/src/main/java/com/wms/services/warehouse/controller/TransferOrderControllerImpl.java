package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.datastructures.DeliveryByTransferOrder;
import com.wms.services.warehouse.datastructures.TransferOrderAndItems;
import com.wms.services.warehouse.datastructures.TransferFinishArgs;
import com.wms.services.warehouse.service.TransferOrderService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.DeliveryOrderItemView;
import com.wms.utilities.model.TransferOrder;
import com.wms.utilities.model.TransferOrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{accountBook}/transfer_order")
public class TransferOrderControllerImpl implements  TransferOrderController{
    @Autowired
    TransferOrderService transferOrderService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody TransferOrder[] transferOrders) {
        return transferOrderService.add(accountBook, transferOrders);
    }

    @Override
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        this.transferOrderService.remove(accountBook, ids);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody TransferOrder[] objs) {
        this.transferOrderService.update(accountBook, objs);
    }

    @Override
    @RequestMapping(value = "/{condStr}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public TransferOrderView[] find(@PathVariable("accountBook") String accountBook,
                                    @PathVariable("condStr") String condStr) {
        return this.transferOrderService.find(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value = "/transfer_finish", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void transferFinish(@PathVariable("accountBook") String accountBook,
                              @RequestBody TransferFinishArgs transferFinishArgs) {
        this.transferOrderService.transferFinish(accountBook, transferFinishArgs);
    }

    @Override
    @RequestMapping(value = "/transfer_some/{personId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void transferSome(@PathVariable("accountBook") String accountBook,
                               @RequestBody List<Integer> ids,
                             @PathVariable("personId") int personId) {
        this.transferOrderService.transferSome(accountBook, ids,personId);
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.transferOrderService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/preview/{strIDs}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<TransferOrderAndItems> getPreviewData(@PathVariable("accountBook") String accountBook,
                                                      @PathVariable("strIDs") String strIDs){
        Gson gson = new Gson();
        List<Integer> ids = gson.fromJson(strIDs, new TypeToken<List<Integer>>() {}.getType());
        return transferOrderService.getPreviewData(accountBook,ids);
    }

    @Override
    @RequestMapping(value = "/order_to_delivery",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<DeliveryOrderItemView> orderToDelivery(@PathVariable("accountBook") String accountBook,
                                                       @RequestBody DeliveryByTransferOrder deliveryByTransferOrder){
        return this.transferOrderService.orderToDelivery(accountBook,deliveryByTransferOrder);
    }
}
