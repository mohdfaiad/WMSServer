package com.wms.services.ledger.service;


import com.wms.services.ledger.dao.PersonDAO;
import com.wms.services.ledger.model.Person;
import com.wms.utilities.datastructures.Condition;
import com.wms.utilities.exceptions.dao.DatabaseNotFoundException;
import com.wms.utilities.exceptions.service.WMSServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    PersonDAO personDAO;

    @Transactional
    public int[] add(String accountBook, Person[] persons) throws WMSServiceException{
        try{
            return personDAO.add(accountBook,persons);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void update(String accountBook, Person[] persons) throws WMSServiceException{
        try {
            personDAO.update(accountBook, persons);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }

    @Transactional
    public void remove(String accountBook, int[] ids) throws WMSServiceException{
        try {
            personDAO.remove(accountBook, ids);
        } catch (DatabaseNotFoundException ex) {
            throw new WMSServiceException("Accountbook " + accountBook + " not found!");
        }
    }

    @Transactional
    public Person[] find(String accountBook, Condition cond) throws WMSServiceException{
        try {
            return this.personDAO.find(accountBook, cond);
        }catch (DatabaseNotFoundException ex){
            throw new WMSServiceException("Accountbook "+accountBook+" not found!");
        }
    }
}
