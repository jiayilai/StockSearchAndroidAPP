package com.example.hw9.myfirstapp;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by yangyidong on 5/1/16.
 */



public class StockJson {
    double current_latitude,current_longitude;

    public StockJson(){}
    public StockJson(double current_latitude,double current_longitude){
        this.current_latitude=current_latitude;
        this.current_longitude=current_longitude;
    }
    public String getParseJsonWCF(String sName){
        StockDetails ListData;
        String line = "";
        HttpURLConnection jc = null;
        InputStream is = null;
        BufferedReader reader = null;




        try {
            String temp = sName.replace(" ", "%20");
            URL js = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+temp);
            jc = (HttpURLConnection) js.openConnection();
            is = jc.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            line = jsonResponse.getString("Status");

//            ListData = new StockDetails(jsonResponse);

            //JSONArray jsonArray = jsonResponse.getJSONArray("results");
            // for(int i = 0; i < jsonResponse.length(); i++){
//                JSONObject r = jsonResponse.getJSONObject(i);
//               ListData.add(new SuggestGetSet(r.getString("Symbol"),r.getString("Name")));
//        }
        } catch (Exception e1) {

            e1.printStackTrace();
            line = "no json obj";

        }finally {
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


            return line;

    }

}