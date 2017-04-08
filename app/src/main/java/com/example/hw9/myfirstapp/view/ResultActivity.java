package com.example.hw9.myfirstapp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hw9.myfirstapp.DataProvider;
import com.example.hw9.myfirstapp.MyActivity;
import com.example.hw9.myfirstapp.Product;
import com.example.hw9.myfirstapp.StockDetails;
import com.example.hw9.myfirstapp.StockLiterals;
import com.example.hw9.myfirstapp.adapter.MyFragmentPagerAdapter;
import com.example.hw9.myfirstapp.fragment.CurrentFragment;
import com.example.hw9.myfirstapp.fragment.HistoricalFragment;
import com.example.hw9.myfirstapp.fragment.NewsFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.example.hw9.myfirstapp.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ResultActivity extends AppCompatActivity {
    private ViewPager pager;
    private List<String> titleList;
    private List<Fragment> fragList;
    private TabLayout tabLayout;
    private ShareDialog fb;
    private CallbackManager callbackManager;
    private JSONObject quoteResult;
    private String symbol;
    private String name;
    private Double lastPrice;
    public StockDetails stockDetails;
    ProgressDialog progress;
    public Bitmap bit=null;
    public Menu menu;
    Handler handlerswitch;
    Runnable runnable;
    public SharedPreferences sharedPreferences = null;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        progress = ProgressDialog.show(this, "Stock Details",
                "Loading", false);
        handlerswitch = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                progress.dismiss();

            }
        };
        handlerswitch.postDelayed(runnable,1000);
        ActionBar bar = getSupportActionBar();
        //设置标题
        bar.setDisplayShowTitleEnabled(true);


        // 给左上角图标的左边加上一个返回的图标
        bar.setDisplayHomeAsUpEnabled(true);
        //使左上角图标可点击
        bar.setDisplayShowHomeEnabled(true);

        //receive intent from mainactivity
        sharedPreferences = getSharedPreferences("favorite",Context.MODE_PRIVATE);
        stockDetails = (StockDetails) getIntent().getSerializableExtra(MyActivity.PRODUCT_ID);


        Intent intent = getIntent();
        String quoteJson = intent.getStringExtra(MyActivity.PRODUCT_STR);
        symbol = stockDetails.getSymbol();
        bar.setTitle(stockDetails.getName());
        //log intent messages
        Log.i("qupteReceive", quoteJson);
        Log.i("symbolReceive", symbol);
        //parse received message
        try {
            quoteResult = new JSONObject(quoteJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            name = quoteResult.getString("Name");
            lastPrice = Math.round(stockDetails.getLastPrice()*100.00)/100.00;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // titlelist
        titleList = new ArrayList<String>();
        titleList.add("CURRENT");
        titleList.add("HISTORICAL");
        titleList.add("NEWS");

        //viewpager fragment
        Fragment currentFragment = new CurrentFragment();
        Fragment historicalFragment = new HistoricalFragment();
        Fragment newsFragment = new NewsFragment();
        fragList = new ArrayList<Fragment>();
        fragList.add(currentFragment);
        fragList.add(historicalFragment);
        fragList.add(newsFragment);
        //send message to fragments
        Bundle bundle = new Bundle();
        bundle.putString("Symbol", symbol);
        bundle.putSerializable("SymbolObj",stockDetails);
        currentFragment.setArguments(bundle);
        historicalFragment.setArguments(bundle);
        newsFragment.setArguments(bundle);
        //set adapter
        pager = (ViewPager) findViewById(R.id.pager);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragList, titleList);
        pager.setAdapter(adapter);
        //create tabitems automatically and tie tablayout with viewpager
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //facebook share
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        fb = new ShareDialog(this);
        fb.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                System.out.println("FB Share success");
                Context context = getApplicationContext();
                CharSequence text = "You share this post.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            public void onCancel() {
                System.out.println("FB Share cancel");
                Context context = getApplicationContext();
                CharSequence text = "Facebook share canceled.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("FB Share error");
                Context context = getApplicationContext();
                CharSequence text = "Facebook share error.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
       //
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings: {
                Log.i("menuitem", "star item is clicked");
                String symbol = (stockDetails != null)?(stockDetails.getName()):("success");
                boolean bool = buttonFavoriteClick();
                if(bool)
                {
                    Toast.makeText(this,"Bookmarked "+symbol+"!!",Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.star_fill);
                }
                else
                {
                    Toast.makeText(this,"Unfollowed "+symbol+"!!",Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.star);
                }

                return true;
            }
            case R.id.facebook: {
                Log.i("menuitem", "facebook item is clicked");
                Context context = getApplicationContext();
                CharSequence text = null;
                text = "sharing " + name + " !!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    String title = null;
                    String description = null;
                    String url = null;

                    title = "Current Stock Price of " + name + ", " + lastPrice;
                    description = "Stock Information of " + name;
                    url = "http://chart.finance.yahoo.com/t?s=" + symbol + "&lang=en-US&width=500&height=500";

                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("http://dev.markitondemand.com/MODApis/"))
                            .setContentTitle(title)
                            .setContentDescription(description)
                            .setImageUrl(Uri.parse(url))
                            .build();
                    fb.show(content);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean buttonFavoriteClick(){//set favorite list
        boolean bool = false;// bool ==true means add some symbol
        if(stockDetails != null) {
            bool = true;
            String symbol = stockDetails.getSymbol();
            if (sharedPreferences == null) {
                sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("symbol", symbol+"~");
                editor.commit();
            }
            else{
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                String newPreferenceStr = sharedPreferences.getString("symbol","");
                if(newPreferenceStr == "")
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("symbol", symbol+"~");
                    editor.commit();
                    return true;
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my, menu);
        this.menu = menu;
        analyzeStar();
        return true;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
