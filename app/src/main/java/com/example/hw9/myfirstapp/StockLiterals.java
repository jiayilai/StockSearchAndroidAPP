package com.example.hw9.myfirstapp;

/**
 * Created by yangyidong on 5/1/16.
 */
public class StockLiterals {
    String title;
    String value;
    Integer condition; // check for arrow

    public void setTitle(String value){
        this.title = value;
    }
    public String getTitle(){
        return this.title;
    }

    public void setValue(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }

    public void setCondition(Integer value){
        this.condition = value;
    }
    public Integer getCondition(){
        return this.condition;
    }

    public StockLiterals(String t,String v, Integer condition){
        this.title = t;
        this.value = v;
        this.condition = condition;
    }
    public void SetDataLiterals(String t,String v, Integer condition)
    {
        this.title = t;
        this.value = v;
        this.condition = condition;
    }
}
