package com.example.hw9.myfirstapp;

/**
 * Created by yangyidong on 4/19/16.
 */
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParse {
    double current_latitude,current_longitude;
    StockDetails stockDetails;



    public JsonParse(){}
    public JsonParse(double current_latitude,double current_longitude){
        this.current_latitude=current_latitude;
        this.current_longitude=current_longitude;
    }
    public List<SuggestGetSet> getParseJsonWCF(String sName){
        List<SuggestGetSet> ListData = new ArrayList<SuggestGetSet>();
        URLConnection jc =null;
        InputStream is = null;
        BufferedReader reader = null;


        try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+temp);
            jc = js.openConnection();
            is = jc.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            JSONArray jsonResponse = new JSONArray(line);
            //JSONArray jsonArray = jsonResponse.getJSONArray("results");
            for(int i = 0; i < jsonResponse.length(); i++){
                JSONObject r = jsonResponse.getJSONObject(i);
                ListData.add(new SuggestGetSet(r.getString("Symbol"),r.getString("Name")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        } finally {
            if(reader != null)
            {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return ListData;

    }

    // get stock detail
    public StockDetails getParseJsonSD(String sName){

        final String url = "http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?symbol=" + sName.replace(" ", "%20");

                URLConnection jc =null;
                InputStream is = null;
                BufferedReader reader = null;
                try {

                    String rex = new String("SUCCESS");
                    URL js = new URL(url);
                    jc = js.openConnection();
                    is = jc.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    String jsonLine = reader.readLine();
                    MyActivity.tansJson = jsonLine;
                    JSONObject jsonResponse = new JSONObject(jsonLine);
                    if(jsonLine.contains("Status"))
                    {
                        if(jsonLine.contains("SUCCESS"))
                        {
                            stockDetails = new StockDetails(jsonResponse);
                        }
                        else
                        {
                            return null;
                        }
                    }
                    else if(jsonLine.contains("Message"))
                    {
                        return null;
                    }

                    else return null;

//            JSONArray jsonResponse = new JSONArray(line);
                    //JSONArray jsonArray = jsonResponse.getJSONArray("results");
//            for(int i = 0; i < jsonResponse.length(); i++){
//                JSONObject r = jsonResponse.getJSONObject(i);
//                ListData.add(new SuggestGetSet(r.getString("Symbol"),r.getString("Name")));
//            }
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                }


        return stockDetails;
    }

}