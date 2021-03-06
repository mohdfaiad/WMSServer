package com.wms.services.salary.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wms.services.salary.datestructures.AccountSynchronize;
import com.wms.services.salary.service.PayNoteService;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.model.AccountTitleView;
import com.wms.utilities.model.PayNote;
import com.wms.utilities.model.PayNoteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{accountBook}/pay_note")
public class PayNoteControllerImpl implements PayNoteController {
    @Autowired
    PayNoteService payNoteService;

    @RequestMapping(value="/",method = RequestMethod.POST)
    public int[] add(@PathVariable("accountBook") String accountBook,
                     @RequestBody PayNote[] payNotes){
        return payNoteService.add(accountBook,payNotes);
    }

    @RequestMapping(value = "/",method = RequestMethod.PUT)
    @ResponseBody
    public void update(@PathVariable("accountBook") String accountBook,
                       @RequestBody PayNote[] payNotes) {
        payNoteService.update(accountBook,payNotes);
    }

    @RequestMapping(value = "/{strIDs}",method = RequestMethod.DELETE)
    @ResponseBody
    public void remove(@PathVariable("accountBook") String accountBook,
                       @PathVariable("strIDs") String strIDs) {
        Gson gson = new Gson();
        int ids[] = gson.fromJson(strIDs,new TypeToken<int[]>(){}.getType());
        payNoteService.remove(accountBook,ids);
    }

    @RequestMapping(value = "/{condStr}",method = RequestMethod.GET)
    public PayNoteView[] find(@PathVariable("accountBook") String accountBook,
                              @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        PayNoteView[] payNoteViews =payNoteService.find(accountBook, cond);
        return payNoteViews;
    }

    @RequestMapping(value = "/{condStr}/find_son",method = RequestMethod.GET)
    public AccountTitleView[] findSonAccountTitleForAssociation(@PathVariable("accountBook") String accountBook,
                                   @PathVariable("condStr") String condStr) {
        Condition cond = Condition.fromJson(condStr);
        AccountTitleView[] accountTitleViews =payNoteService.findSonTitleForAssociation(accountBook, cond);
        return accountTitleViews;
    }

    @Override
    @RequestMapping(value="/count/{condStr}",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public long findCount(@PathVariable("accountBook") String accountBook,
                          @PathVariable("condStr") String condStr){
        return this.payNoteService.findCount(accountBook, Condition.fromJson(condStr));
    }

    @Override
    @RequestMapping(value="/confirm_to_account_title",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void confirmToAccountTitle(@PathVariable("accountBook") String accountBook,
                                      @RequestBody AccountSynchronize accountSynchronize ){
         this.payNoteService.confirmToAccountTitle(accountBook, accountSynchronize);
    }

    @Override
    @RequestMapping(value="/real_pay_to_account_title",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void realPayToAccountTitle(@PathVariable("accountBook") String accountBook,
                                      @RequestBody AccountSynchronize accountSynchronize){
        this.payNoteService.realPayToAccountTitle(accountBook,accountSynchronize);
    }
}
