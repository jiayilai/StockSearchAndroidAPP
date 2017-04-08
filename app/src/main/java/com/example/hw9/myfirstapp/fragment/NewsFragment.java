package com.example.hw9.myfirstapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.hw9.myfirstapp.adapter.MyAdapter;
import com.example.hw9.myfirstapp.utility.StockRestClient;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.hw9.myfirstapp.R;
import cz.msebera.android.httpclient.Header;

public class NewsFragment extends Fragment {
    Gson gson = new Gson();
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fragment receives message from activity
        String text=getArguments().getString("Symbol");
        Log.i("newsFragmentReceive", text);
        View view = inflater.inflate(R.layout.news, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        //bing search
        RequestParams params = new RequestParams();
        //the input paramater
        params.put("query", text);
        StockRestClient.get("", params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        String bingResponse = gson.toJson(response);
                        Log.i("newsBingResponse", bingResponse);
                        List dateSource = getData(response);
                        //  设置SimpleAdapter监听器
                        SimpleAdapter myAdapter = new MyAdapter(getActivity(),
                                dateSource, R.layout.news_item,
                                new String[]{"Title", "Description", "Source", "Date"}, new int[]{R.id.title,
                                R.id.description, R.id.source, R.id.date}, response);
                                listView.setAdapter(myAdapter);
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                Uri uri = Uri.parse("http://google.com");
//                                Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                                startActivity(it);
//                            }
//                        });
                    }
                }
        );
        return view;
    }

    private List<Map<String, String>> getData(JSONObject jsonObj) {
        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();

        JSONArray results = null;
        try {
            results = jsonObj.getJSONObject("d").getJSONArray("results");
            for (int i = 0; i < Math.min(results.length(), 4); i++) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject result = results.getJSONObject(i);
                map.put("Title", result.getString("Title"));
                map.put("Description", result.getString("Description"));
                map.put("Source", "Publisher : " + result.getString("Source"));
                map.put("Date", "Date : " + strToDateLong(result.getString("Date")));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat Rformatter = new SimpleDateFormat("dd MMM yyyy', 'HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        strDate = Rformatter.format(strtodate);
        return strDate;
    }
}
