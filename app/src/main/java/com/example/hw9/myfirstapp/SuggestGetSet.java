package com.example.hw9.myfirstapp;

/**
 * Created by yangyidong on 4/19/16.
 */
public class SuggestGetSet {

    String symbol,name;
    public SuggestGetSet(String id, String name){
        this.setSymbol(id);
        this.setName(name);
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String id) {
        this.symbol = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}