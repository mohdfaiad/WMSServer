package com.wms.services.settlement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.settlement.service.SummaryDetailsService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.SummaryDetails;
import com.wms.utilities.model.SummaryDetailsView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/summary_details")
public class SummaryDetailsControllerImpl implements SummaryDetailsController{
    @Autowired
    SummaryDetailsService summaryDetailsService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody SummaryDetails[] summaryDetails) {
        return summaryDetailsService.add(accountBook, summaryDetails);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody SummaryDetails[] summaryDetails) {
        summaryDetailsService.update(accountBook, summaryDetails);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strIDs}", method = RequestMethod.DELETE)
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs, new TypeToken<int[]>() {
        }.getType());
        summaryDetailsService.remove(accountBook, ids);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{strCond}", method = RequestMethod.GET)
    public SummaryDetailsView[] find(@PathVariable("accountBook") String accountBook,
                                     @PathVariable("strCond") String condStr) {
        return summaryDetailsService.find(accountBook, Condition.fromJson(condStr));
    }


    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.summaryDetailsService.findCount(accountBook, Condition.fromJson(condStr));
    }
}
