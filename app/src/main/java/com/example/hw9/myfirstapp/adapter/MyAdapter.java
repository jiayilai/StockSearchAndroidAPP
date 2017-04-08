package com.example.hw9.myfirstapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.hw9.myfirstapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Jiayi Lai on 2016-05-02.
 */
public class MyAdapter extends SimpleAdapter{
    JSONArray results = new JSONArray();
    public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to, JSONObject jsonObject) {
        super(context, data, resource, from, to);
        try {
            this.results = jsonObject.getJSONObject("d").getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.title);
        try {
            String urlTtile = "<a href =" + results.getJSONObject(position).getString("Url")+">"+textView.getText()+"</a>";
            textView.setText(Html.fromHtml(urlTtile));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setLinkTextColor(Color.BLACK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
