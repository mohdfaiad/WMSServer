package com.wms.utilities.vaildator;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import com.thoughtworks.xstream.mapper.Mapper;
import com.wms.utilities.exceptions.service.WMSServiceException;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class Validator {
private List<ValidatorCondition> conditions=new ArrayList<ValidatorCondition>();
private
String key;

    public  Validator(String key1)
    {
        key=key1;
    }

    public void validate(Object object){
        for(ValidatorCondition condition:conditions){
            condition.validate(object);
        }
    }

    public Validator min(int min){
        Validator validator=new Validator(key);
        MinValidatorCondition minValidatorCondition=new MinValidatorCondition();
        minValidatorCondition.setKey(key);
        minValidatorCondition.dateDeliver(min);
        conditions.add(minValidatorCondition);
        return  validator;
    }

    public Validator max(int max){
        Validator validator=new Validator(key);
        MaxValidatorCondition maxValidatorCondition=new MaxValidatorCondition();
        maxValidatorCondition.setKey(key);
        maxValidatorCondition.dateDeliver(max);
        conditions.add(maxValidatorCondition);
        return validator;
    }

    public Validator in(int[] object){
        Validator validator=new Validator(key);
        InValidatorCondition inValidatorCondition=new InValidatorCondition();
        inValidatorCondition.setKey(key);
        inValidatorCondition.dateDeliver(object);
        conditions.add(inValidatorCondition);
        return validator;
    }
    public Validator length(int length){
        Validator validator=new Validator(key);
        LengthValidatorCondition lengthValidatorCondition=new LengthValidatorCondition();
        lengthValidatorCondition.setKey(key);
        lengthValidatorCondition.dateDeliver(length);
        conditions.add(lengthValidatorCondition);
        return validator;
    }

    public Validator notnull(){
        Validator validator=new Validator(key);
        NotnullValidatorCondition notnullValidatorCondition=new NotnullValidatorCondition();
        notnullValidatorCondition.setKey(key);
        conditions.add(notnullValidatorCondition);
        return validator;
    }

    public  Validator notEmpty(){
        Validator validator=new Validator(key);
        NotEmptyValidatorCondition notEmptyValidatorCondition=new NotEmptyValidatorCondition();
        notEmptyValidatorCondition.setKey(key);
        conditions.add(notEmptyValidatorCondition);
        return validator;
    }
}

class ValidatorCondition {
    public void validate(Object values) { }
}

class MinValidatorCondition extends ValidatorCondition {
    private int min;
    private int actualValue;
    private String key;

    public void dateDeliver(int min1) {
       // try {
        //    min = Integer.parseInt(String.valueOf(object));
       // } catch (NumberFormatException e) {
        min=min1;
    }

    public void validate(Object value) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));

        } catch (NumberFormatException e) {
            throw new WMSServiceException("检查的数据无法转换为数字类型");
        }
        if (actualValue < min) {
            throw new WMSServiceException(key+ "的值小于最低值");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class MaxValidatorCondition extends ValidatorCondition {
    private int max;
    private int actualValue;
    private String key;

    public void dateDeliver(int max1) {
        //try {
        //    max = Integer.parseInt(String.valueOf(object));
       // } catch (NumberFormatException e) {
        //}
        max=max1;
    }

    public void validate(Object value) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));

        } catch (NumberFormatException e) {
            throw new WMSServiceException("检查的数据无法转换为数字类型");
        }
        if (actualValue > max) {
            throw new WMSServiceException(key+ "的值大于最大值");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}class InValidatorCondition extends ValidatorCondition {
    private int min;
    private int max;
    private int actualValue;
    private String key;

    public void dateDeliver(int[] object) {
        try {
            min = Integer.parseInt(String.valueOf(object[0]));
            max = Integer.parseInt(String.valueOf(object[1]));
        } catch (NumberFormatException e) {
        }}

    public void validate(Object value) {
        try {
            actualValue = Integer.parseInt(String.valueOf(value));

        } catch (NumberFormatException e) {
            throw new WMSServiceException("检查的数据无法转换为数字类型");
        }
        if (actualValue < min||actualValue>max) {
            throw new WMSServiceException(key+ "的值不在指定的区间范围内");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}class LengthValidatorCondition extends ValidatorCondition {
    private int length;
    private int actualValue;
    private String key;

    public void dateDeliver(Object object) {
        try {
            length = Integer.parseInt(String.valueOf(object));
        } catch (NumberFormatException e) {
        }}

    public void validate(Object value) {
        try {
            actualValue = String.valueOf(value).length();

        } catch (NumberFormatException e) {
            throw new WMSServiceException("检查的数据无法转换为数字类型");
        }
        if (actualValue > length) {
            throw new WMSServiceException(key+ "字符串长度超过上限！");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class NotnullValidatorCondition extends ValidatorCondition {
    private int actualValue;
    private String key;

    public void validate(Object value) {
        try {
            actualValue = String.valueOf(value).length();

        } catch (NumberFormatException e) {
            throw new WMSServiceException("检查的数据无法转换为数字类型");
        }
        if (actualValue == 0) {
            throw new WMSServiceException(key+ "的值不能为空");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class NotEmptyValidatorCondition extends ValidatorCondition {
    private String actualValue;
    private String key;


    public void validate(Object value) {
        try {
            actualValue = String.valueOf(value);

        } catch (NumberFormatException e) {
            throw new WMSServiceException("检查的数据无法转换为数字类型");
        }
        if (actualValue == "") {
            throw new WMSServiceException(key+ "的值不能为空");
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}