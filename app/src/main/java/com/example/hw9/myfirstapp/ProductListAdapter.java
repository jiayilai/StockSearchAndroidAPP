package com.example.hw9.myfirstapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by yangyidong on 4/17/16.
 */
public class ProductListAdapter extends ArrayAdapter<Product> {

    private List<Product> products;

    public ProductListAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.products = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if( convertView == null )
        {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }

        Product product = products.get(position);

        TextView nameText = (TextView) convertView.findViewById(R.id.nameText);
        nameText.setText(product.getName());
        //get prices
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(product.getPrice());
        TextView priceText = (TextView) convertView.findViewById(R.id.priceText);
        priceText.setText(price);
        //get img dynamically
        ImageView imgV = (ImageView) convertView.findViewById(R.id.imageView);
        Bitmap bitmap = getBitmapFromAsset(product.getProdcutId());
        imgV.setImageBitmap(bitmap);


        return convertView;
    }
    //set img bitmap
    private Bitmap getBitmapFromAsset(String imgId){
        AssetManager assetManager = getContext().getAssets();//get asset class
        InputStream stream = null; //initialize

        try {
            stream = assetManager.open(imgId + ".png");  //open img stream
            return BitmapFactory.decodeStream(stream);  //return decode stream of img

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
