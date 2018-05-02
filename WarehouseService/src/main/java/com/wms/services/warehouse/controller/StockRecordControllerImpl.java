package com.wms.services.warehouse.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.warehouse.service.StockRecordService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/stockRecord")

public class StockRecordControllerImpl implements StockRecordController {

    @Autowired
    StockRecordService stockRecordService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody StockRecord[] stockRecords) {
        return stockRecordService.add(accountBook, stockRecords);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody StockRecord[] stockRecords) {
        stockRecordService.update(accountBook, stockRecords);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        stockRecordService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public StockRecordView[] find(@PathVariable("accountBook") String accountBook,
                                  @PathVariable("strCond") String condStr) {
        return stockRecordService.find(accountBook, Condition.fromJson(condStr));
    }
}
