package com.example.hw9.myfirstapp.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hw9.myfirstapp.DataProvider;
import com.example.hw9.myfirstapp.Product;
import com.example.hw9.myfirstapp.R;
import com.example.hw9.myfirstapp.StockAdapter;
import com.example.hw9.myfirstapp.StockDetails;
import com.example.hw9.myfirstapp.StockLiterals;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class CurrentFragment extends Fragment {

	ProgressDialog progress;
	private List<Product> products = DataProvider.productList;
	private List<StockLiterals> stockLiteralses = new ArrayList<>();;
	private StockDetails stockDetails;
	private Product product;
	public AlertDialog dialogImage;
	public PhotoViewAttacher pViewAttacher;
	public Bitmap bit=null;
	public Menu menu;
	public PhotoViewAttacher photoViewAttacher;
	public SharedPreferences sharedPreferences = null;
	public View view;
	Handler handler = new Handler() {
		public void handleMessage(Message msg){
			if(msg.what == 0x200)
			{
				Log.d("a","handle get msg!");
				processListDataStock();


			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.current, container, false);
		sharedPreferences = getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
		stockDetails=(StockDetails) getArguments().getSerializable("SymbolObj");
		initStockLiterals(stockDetails);
		getStockDetailPicture(stockDetails.getSymbol());
		return view;
	}



	public void getStockDetailPicture(String symbol)
	{
		final String sybName = symbol;
		new Thread(){
			public void run(){
				String url = "http://chart.finance.yahoo.com/t?lang=en-US&s=" + sybName + "&height=400&width=700";
				try {
					InputStream inputStream = new URL(url).openStream();
					bit = BitmapFactory.decodeStream(inputStream);
					handler.sendEmptyMessage(0x200);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	public boolean buttonFavoriteClick(){//set favorite list
		boolean bool = false;// bool ==true means add some symbol
		if(stockDetails != null) {
			bool = true;
			String symbol = stockDetails.getSymbol();
			if (sharedPreferences == null) {
				//sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("symbol", symbol+"~");
				editor.commit();
			}
			else{
				//SharedPreferences.Editor editor = sharedPreferences.edit();
				String newPreferenceStr = sharedPreferences.getString("symbol","");
				String[] arraySymbol = newPreferenceStr.split("~");
				newPreferenceStr = "";
				for(int i=0;i<arraySymbol.length;i++){
					if(!symbol.equals(arraySymbol[i])&&!arraySymbol[i].equals("")){
						newPreferenceStr += arraySymbol[i] + "~";
					}
					else if(symbol.equals(arraySymbol[i])){
						bool = false;
					}
				}
				if(bool){//if bool is equal to ture, then add the symbol into list
					newPreferenceStr += symbol + "~";
				}
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("symbol",newPreferenceStr);
				editor.commit();
			}
		}
		return bool;

	}
	//initiate stockLiterals list

	public void initStockLiterals(StockDetails sd){

		Integer numC = 0;
		Integer numY = 0;
		Double changeP = (Math.round(sd.getChangePercent()*100.0))/100.0;
		if(changeP >0 )
		{
			numC = 1;
		}
		else
		{
			numC = -1;
		}

		Double ytdP = (Math.round(sd.getChangeYDP()*100.0))/100.0;
		if(ytdP > 0 ){
			numY = 1;
		}
		else
		{
			numY = -1;
		}

		if(stockLiteralses.size()>1)
		{
			return;
		}
		else{

			StockLiterals sl0 = new StockLiterals("NAME",sd.getName(),0);
			stockLiteralses.add(sl0);
			StockLiterals sl1 = new StockLiterals("SYMBOL",sd.getSymbol(),0);
			stockLiteralses.add(sl1);
			StockLiterals sl2 = new StockLiterals("LASTPRICE",sd.getLastPrice().toString(),0);
			stockLiteralses.add(sl2);
			StockLiterals sl3 = new StockLiterals("CHANGE",sd.ProcessChangeAndP(sd.getChange(),sd.getChangePercent()),numC);
			stockLiteralses.add(sl3);
			StockLiterals sl4 = new StockLiterals("TIMESTAMP",sd.getTime(),0);
			stockLiteralses.add(sl4);
			StockLiterals sl5 = new StockLiterals("MARKETCAP",sd.ProcessCapAndVolume(sd.getCap()).toString(),0);
			stockLiteralses.add(sl5);
			StockLiterals sl10 = new StockLiterals("VOLUME",sd.ProcessCapAndVolume(sd.getVolume()).toString(),0);
			stockLiteralses.add(sl10);
			StockLiterals sl6 = new StockLiterals("CHANGEYTD",sd.ProcessChangeAndP(sd.getChangeYD(),sd.getChangeYDP()),numY);
			stockLiteralses.add(sl6);
			StockLiterals sl7 = new StockLiterals("HIGH",sd.getHigh().toString(),0);
			stockLiteralses.add(sl7);
			StockLiterals sl8 = new StockLiterals("LOW",sd.getLow().toString(),0);
			stockLiteralses.add(sl8);
			StockLiterals sl9 = new StockLiterals("OPEN",sd.getOpen().toString(),0);
			stockLiteralses.add(sl9);
		}



	}

	public void analyzeStar(){
		MenuItem view = menu.findItem(R.id.action_settings);
		String symbol = stockDetails.getSymbol();
		String tem = sharedPreferences.getString("symbol","");
		String[] arrayStr = tem.split("~");
		for(int i=0;i<arrayStr.length;i++){
			if(symbol.equals(arrayStr[i]))
			{
				view.setIcon(R.drawable.star_fill);
			}
		}

	}




	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id == R.id.action_settings)
		{
			boolean bool = buttonFavoriteClick();
			if(bool)
			{
				item.setIcon(R.drawable.star_fill);
			}
			else
			{
				item.setIcon(R.drawable.star);
			}
			//Toast.makeText(SecondActivity.this,"set favorite",Toast.LENGTH_SHORT).show();
		}

		return super.onOptionsItemSelected(item);
	}

	public void processListDataStock()//process Stock Detials
	{
		// *******************************
//        set stock details in second activity
		StockAdapter stockAdapter = new StockAdapter(getActivity(),R.layout.stock_item,stockLiteralses);
		ListView stockListView = (ListView) view.findViewById(R.id.layout_stock_details);

		View fview = getActivity().getLayoutInflater().inflate(R.layout.detail_picture,null);
		View tview = getActivity().getLayoutInflater().inflate(R.layout.stock_detail_title,null);
		View dview = getActivity().getLayoutInflater().inflate(R.layout.dialog_image,null);
		//PhotoView photoView = (PhotoView) dview.findViewById(R.id.detailPictureForDia);
		ImageView fimg = (ImageView) fview.findViewById(R.id.detailPicture);
		fimg.setImageBitmap(bit);

		fimg.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//dialogImage
				ImageView newone = new ImageView(getContext());
				newone.setImageBitmap(bit);

				AlertDialog builder = new AlertDialog.Builder(getContext()).create();
				builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
				photoViewAttacher = new PhotoViewAttacher(newone);

				builder.setView(newone);
				builder.show();
				builder.getWindow().setLayout(700,400);
//
//				Dialog settingsDialog = new Dialog(getActivity());
//				settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//				settingsDialog.setContentView(getActivity().getLayoutInflater().inflate(R.layout.dialog_image
//						, null));
//				settingsDialog.show();


			}
		});
		//TextView ftitle = (TextView) tview.findViewById(R.id.details_title);


		stockListView.addFooterView(fview);
		stockListView.addHeaderView(tview);
		stockListView.setAdapter(stockAdapter);


//      end of stock details

//*********************************

	}



}
