package com.example.hw9.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yangyidong on 5/1/16.
 */
public class StockAdapter extends ArrayAdapter<StockLiterals> {

    protected static final String TAG = "StockAdapter";
    private List<StockLiterals> stockLiterals;

    public StockAdapter(Context context, int resource, List<StockLiterals> objects){
        super(context, resource, objects);
        this.stockLiterals = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.stock_item, parent, false);

        StockLiterals sl = this.stockLiterals.get(position);
        //set title and value
        TextView titleView = (TextView) convertView.findViewById(R.id.stock_title);
        titleView.setText(sl.getTitle());
        TextView stockContent = (TextView) convertView.findViewById(R.id.stock_content);
        stockContent.setText(sl.getValue());
        //set img
        if(sl.getCondition() != 0) {
            ImageView arrowImage = (ImageView) convertView.findViewById(R.id.trend_images);
            if (sl.getCondition() == 1) {
                arrowImage.setImageResource(R.drawable.up);
            } else if (sl.getCondition() == -1) {
                arrowImage.setImageResource(R.drawable.down);
            }
        }
        else
        {
            ImageView arrowImage = (ImageView) convertView.findViewById(R.id.trend_images);
            arrowImage.setVisibility(View.GONE);
        }

        return convertView;
    }
}
