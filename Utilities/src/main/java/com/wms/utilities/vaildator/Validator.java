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

    public Validator min(Object min){
        MinValidatorCondition minValidatorCondition=new MinValidatorCondition();
        minValidatorCondition.setKey(key);
        minValidatorCondition.dateDeliver(min);
        conditions.add(minValidatorCondition);
        return  this;
    }

    public Validator greaterThan(Object greaterThan){
        GreaterThanValidatorCondition greaterThanValidatorCondition=new GreaterThanValidatorCondition();
        greaterThanValidatorCondition.setKey(key);
        greaterThanValidatorCondition.dateDeliver(greaterThan);
        conditions.add(greaterThanValidatorCondition);
        return  this;
    }

    public Validator max(Object max){
        MaxValidatorCondition maxValidatorCondition=new MaxValidatorCondition();
        maxValidatorCondition.setKey(key);
        maxValidatorCondition.dateDeliver(max);
        conditions.add(maxValidatorCondition);
        return this;
    }

    public Validator in(int[] object){
        InValidatorCondition inValidatorCondition=new InValidatorCondition();
        inValidatorCondition.setKey(key);
        inValidatorCondition.dateDeliver(object);
        conditions.add(inValidatorCondition);
        return this;
    }
    public Validator length(int length){
        LengthValidatorCondition lengthValidatorCondition=new LengthValidatorCondition();
        lengthValidatorCondition.setKey(key);
        lengthValidatorCondition.dateDeliver(length);
        conditions.add(lengthValidatorCondition);
        return this;
    }

    public Validator notnull(){
        NotnullValidatorCondition notnullValidatorCondition=new NotnullValidatorCondition();
        notnullValidatorCondition.setKey(key);
        conditions.add(notnullValidatorCondition);
        return this;
    }

    public  Validator notEmpty(){
        NotEmptyValidatorCondition notEmptyValidatorCondition=new NotEmptyValidatorCondition();
        notEmptyValidatorCondition.setKey(key);
        conditions.add(notEmptyValidatorCondition);
        return this;
    }
}

class ValidatorCondition {
    public void validate(Object values) { }
}

class MinValidatorCondition extends ValidatorCondition {
    private double min;
    private double actualValue;
    private String key;

    public void dateDeliver(Object min1) {
        try {
            min = Double.parseDouble(String.valueOf(min1));
        } catch (NumberFormatException e) {
        }
    }

    public void validate(Object value) {
        if (value == null) {
            throw new WMSServiceException("请填写"+key);
        }
        try {
            actualValue =  Double.parseDouble((String.valueOf(value)));
        } catch (NumberFormatException e) {
            throw new WMSServiceException(key+"检查的数据无法转换为数字类型");
        }
        if (actualValue < min) {
            throw new WMSServiceException(key+ "的值不能小于最低值"+min);
        }
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
class GreaterThanValidatorCondition extends ValidatorCondition {
    private double greaterThan;
    private double actualValue;
    private String key;

    public void dateDeliver(Object min1) {
        try {
            greaterThan = Double.parseDouble(String.valueOf(min1));
        } catch (NumberFormatException e) {
        }
    }

    public void validate(Object value) {
        if (value == null) {
            throw new WMSServiceException("请填写"+key);
        }
        try {
            actualValue =  Double.parseDouble((String.valueOf(value)));
        } catch (NumberFormatException e) {
            throw new WMSServiceException(key+"检查的数据无法转换为数字类型");
        }
        if (actualValue <= greaterThan) {
            throw new WMSServiceException(key+ "的值必须大于最低值"+greaterThan);
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
    private double max;
    private double actualValue;
    private String key;

    public void dateDeliver(Object max1) {
        try {
           max = Double.parseDouble(String.valueOf(max1));
       } catch (NumberFormatException e) {
        }

    }

    public void validate(Object value) {
        if (value == null) {
            throw new WMSServiceException("请填写"+key);
        }
        try {
            actualValue =  Double.parseDouble((String.valueOf(value)));

        } catch (NumberFormatException e) {
            throw new WMSServiceException(key+"检查的数据无法转换为数字类型");
        }
        if (actualValue > max) {
            throw new WMSServiceException(key+ "的值不能大于"+max);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}class InValidatorCondition extends ValidatorCondition {
    private double min;
    private double max;
    private double actualValue;
    private String key;

    public void dateDeliver(int[] object) {
        try {
            min = Double.parseDouble(String.valueOf(object[0]));
            max = Double.parseDouble(String.valueOf(object[1]));
        } catch (NumberFormatException e) {
        }}

    public void validate(Object value) {
        try {
            actualValue =  Double.parseDouble((String.valueOf(value)));

        } catch (NumberFormatException e) {
            throw new WMSServiceException(key+"检查的数据无法转换为数字类型");
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

}

class LengthValidatorCondition extends ValidatorCondition {
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

        } catch (Exception e) {
            throw new WMSServiceException(key+"检查的数据无法获取长度");
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
    // private int actualValue;
    private String key;
    private Object  actualValue;
    public void validate(Object value) {
         //  try {
         //   actualValue = String.valueOf(value).length();
         // } catch (Exception e) {
         //     throw new WMSServiceException("检查的数据无法获取长度");
         // }
        actualValue =value;
        if (actualValue == null) {
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
        if(value==null){
            throw new WMSServiceException(key+ "不能为空");
        }
        try {
            actualValue = String.valueOf(value);
        } catch (Exception e) {

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