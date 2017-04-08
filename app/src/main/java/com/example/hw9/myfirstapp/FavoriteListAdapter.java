package com.example.hw9.myfirstapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by yangyidong on 5/2/16.
 */
public class FavoriteListAdapter extends ArrayAdapter<FavoriteItem> {

    private List<FavoriteItem> favoriteItems;

    public FavoriteListAdapter(Context context, int resource, List<FavoriteItem> objects) {
        super(context, resource, objects);
        this.favoriteItems = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
        {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.favorite_item, parent, false);
        }

        FavoriteItem item = favoriteItems.get(position);
        String signal = "";

        TextView symbolText = (TextView) convertView.findViewById(R.id.Fsymbol);
        symbolText.setText(item.getSymbol());
        TextView nameText = (TextView) convertView.findViewById(R.id.Fname);
        nameText.setText(item.getName());
        TextView priceText = (TextView) convertView.findViewById(R.id.Fprice);
        priceText.setText("$ "+ (Math.round(item.getLastPrice()*100.00)/100.00));
        TextView changeText = (TextView) convertView.findViewById(R.id.Fchange);
        if((Math.round(item.getChangeP()*100.00)/100.00)>0)
        {
            signal = "+";
            changeText.setBackgroundColor(Color.GREEN);
        }

        else if((Math.round(item.getChangeP()*100.00)/100.00)<0)
        {
            signal = "-";
            changeText.setBackgroundColor(Color.RED);
        }

        changeText.setText(signal + (Math.round(item.getChangeP()*100.00)/100.00)+ "%");
        TextView capText = (TextView) convertView.findViewById(R.id.Fcap);
        capText.setText("Market Cap : "+StockDetails.ProcessCapAndVolume(item.getCap()));
        //get prices
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        String price = formatter.format(item.getPrice());
//        TextView priceText = (TextView) convertView.findViewById(R.id.priceText);
//        priceText.setText(price);
        //get img dynamically
        //ImageView imgV = (ImageView) convertView.findViewById(R.id.imageView);
       // Bitmap bitmap = getBitmapFromAsset(product.getProdcutId());
       // imgV.setImageBitmap(bitmap);


        return convertView;
    }
}
