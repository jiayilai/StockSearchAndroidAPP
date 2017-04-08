package com.example.hw9.myfirstapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangyidong on 5/1/16.
 */


public class StockDetails implements Serializable{
    String status;
    String symbol;
    String name;
    Double lastPrice;
    Double change;
    Double changePercent;
    String time;
    Long cap;
    Long volume;
    Double changeYD;
    Double changeYDP;
    Double high;
    Double low;
    Double open;

    public String getStatus() {
        return status;
    }

    public void setStatus(String id) {
        this.status = id;
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

    public void setName(String id) {
        this.name = id;
    }
    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double id) {
        this.lastPrice = id;
    }
    public Double getChange() {
        return change;
    }

    public void setChange(Double id) {
        this.change = id;
    }
    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double id) {
        this.changePercent = id;
    }
    public String getTime() {
        return time;
    }

    public void setTime(String id) {
        this.time = id;
    }
    public Long getCap() {
        return cap;
    }

    public void setCap(Long id) {
        this.cap = id;
    }
    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long id) {
        this.volume = id;
    }
    public Double getChangeYD() { return changeYD; }

    public void setChangeYD(Double id) {
        this.changeYD = id;
    }
    public Double getChangeYDP() {
        return changeYDP;
    }

    public void setChangeYDP(Double id) {
        this.changeYDP = id;
    }
    public Double getHigh() {
        return high;
    }

    public void setHigh(Double id) {
        this.high = id;
    }
    public Double getLow() {
        return low;
    }

    public void setLow(Double id) {
        this.low = id;
    }
    public Double getOpen() {
        return open;
    }

    public void setOpen(Double id) {
        this.open = id;
    }

    public StockDetails(JSONObject jobj) throws JSONException {
        if(jobj != null)
            if(jobj.getString("Status").equals("SUCCESS")) {
                setStatus(jobj.getString("Status"));
                setSymbol(jobj.getString("Symbol"));
                setName(jobj.getString("Name"));
                setLastPrice(jobj.getDouble("LastPrice"));
                setCap(jobj.getLong("MarketCap"));
                setChange(jobj.getDouble("Change"));
                setVolume(jobj.getLong("Volume"));
                setChangePercent(jobj.getDouble("ChangePercent"));
                setChangeYD(jobj.getDouble("ChangeYTD"));
                setChangeYDP(jobj.getDouble("ChangePercentYTD"));
                setHigh(jobj.getDouble("High"));
                setLow(jobj.getDouble("Low"));
                setOpen(jobj.getDouble("Open"));
                setTime(MyActivity.strToDateLong(jobj.getString("Timestamp")));
            }
    }

    public static String ProcessCapAndVolume(Long value)//process Cap and volume's units  M  or B
    {
        String str = null;
        Double dou = 0.01;
        if(value < 100000)
        {
            str = value.toString();
        }
        else if(value < 100000000)
        {
            dou = (value / 1000000.00);
            dou = Math.round(dou*100.00)/100.00;
            str = dou.toString() +" Million";
        }
        else
        {
            dou = (value / 1000000000.00);
            dou = Math.round(dou*100.00)/100.00;
            str = dou.toString() +" Billion";
        }
        return str;
    }

    public String ProcessChangeAndP(Double change,Double changeP)//process change and changep
    {
        change = (Math.round(change*100.0))/100.0;
        changeP = (Math.round(changeP*100.0))/100.0;
        String str;
        if(changeP > 0 ){

            str = change.toString()+"(+"+changeP.toString()+"%)";
        }

        else
        {
            str = change.toString()+"("+changeP.toString()+"%)";
        }
        return str;
    }


}