package com.example.hw9.myfirstapp;

/**
 * Created by yangyidong on 5/2/16.
 */
public class AutoComplete {
    private String display;
    private String value;
    public String getDisplay()
    {
        return this.display;
    }
    public void setDisplay(String value)
    {
        this.display = value;
    }

    public String getValue()
    {
        return this.value;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public AutoComplete(String display,String value)
    {
        this.value = value;
        this.display = display;
    }
}
