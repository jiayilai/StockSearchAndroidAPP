package com.example.hw9.myfirstapp;

/**
 * Created by yangyidong on 5/2/16.
 *
 * Favorite List Items
 */
public class FavoriteItem {
    private String symbol;
    private String name;
    private Double change;
    private Double changeP;
    private Double lastPrice;
    private long cap;

    public String getSymbol(){
        return this.symbol;
    }
    public void setSymbol(String value){
        this.symbol = value;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String value){
        this.name = value;
    }
    public Double getChange(){
        return this.change;
    }
    public void setChange(Double value){
        this.change = value;
    }
    public Double getChangeP(){
        return this.changeP;
    }
    public void setChangeP(Double value){
        this.changeP = value;
    }
    public long getCap(){
        return this.cap;
    }
    public void setCap(long value){
        this.cap = value;
    }
    public Double getLastPrice(){
        return this.lastPrice;
    }
    public void setLastPrice(Double value){
        this.lastPrice = value;
    }

    public FavoriteItem(String symbol,String name,Double change,Double changeP,long cap,Double lastPrice){
        this.symbol = symbol;
        this.name = name;
        this.change = change;
        this.changeP = changeP;
        this.cap = cap;
        this.lastPrice = lastPrice;
    }

    public FavoriteItem(StockDetails stockDetails){ //input is stockdetails object
        this.symbol = stockDetails.getSymbol();
        this.name = stockDetails.getName();
        this.change = stockDetails.getChange();
        this.changeP = stockDetails.getChangePercent();
        this.cap = stockDetails.getCap();
        this.lastPrice = stockDetails.getLastPrice();
    }

}
