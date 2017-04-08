package com.example.hw9.myfirstapp;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.example.hw9.myfirstapp.SlideCutListView.RemoveDirection;
import com.example.hw9.myfirstapp.SlideCutListView.RemoveListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw9.myfirstapp.StockDetails;
import com.example.hw9.myfirstapp.StockJson;
import com.example.hw9.myfirstapp.view.ResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Handler;

import java.util.Timer;
import java.util.logging.LogRecord;

public class MyActivity extends AppCompatActivity implements RemoveListener {

    private SlideCutListView slideCutListView ;
    private CoordinatorLayout coordinator;

    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler = new Handler();

    private ProgressDialog mProgressDialog;
    public ProgressBar rProgress;
    public AlertDialog dialog;
    public AlertDialog dialogInvalid;
    public AlertDialog dialogEmpty;
    public int dialogPosition = -1;
    public SharedPreferences sharedPreferences = null;
    public SharedPreferences sharedSymbol = null;
    private List<Product> products = DataProvider.productList;
    public List<FavoriteItem> favoriteItems = new ArrayList<>();
   // public List<StockDetails> stockDetailses;
    public static final String PRODUCT_ID = "PRODUCT_ID";
    private static final int REQUEST = 1;
    public static final String PRODUCT_STR = "FAVORITE";
    public StockDetails stockDetails;
    public String JsonAuto;
    public ActionBar actionBar;
    public static String tansJson;
    AlertDialog.Builder builder;
    AlertDialog.Builder invalide;
    AlertDialog.Builder empty;
    public ArrayList<String> ArrayJson = new ArrayList<>();
    public ArrayAdapter autoAdapter;
    public FavoriteListAdapter favoriteListAdapter;
    public AutoCompleteTextView acTextView;
    public Switch aSwitch;
    Handler handlerswitch;
    Runnable runnable;
    Runnable runnableAuto;
    public JsonParse jsonParse = new JsonParse();
    Handler handler = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0x200)
            {
                Log.d("a","handle get msg!");

                if(stockDetails==null)
                {
                    Log.d("aaaaa", "here!!!!!");
                    dialogInvalid = invalide.create();
                    dialogInvalid.show();
                }
                else
                {
                    Intent intent = new Intent(MyActivity.this, ResultActivity.class);
                    // StockDetails stockDetails = stockJson.getParseJsonWCF(symbol);
                    AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.symbol);
                    sharedSymbol = getSharedPreferences("sharedSymbol",Context.MODE_PRIVATE);
                    SharedPreferences.Editor symbolEditor = sharedSymbol.edit();
                    symbolEditor.putString("input",auto.getText().toString());
                    symbolEditor.commit();
                    intent.putExtra(PRODUCT_STR,tansJson);
                    intent.putExtra(PRODUCT_ID, stockDetails); //sent the prodcut id to second activities
                    //startActivityForResult(intent,REQUEST);  get feedback of favorite
                    startActivity(intent);
                }

            }
            else if(msg.what == 0x201)
            {
                processAutoView();
                mProgress.setVisibility(View.GONE);
            }
            else if(msg.what == 0x202)
            {
                processFavoriteList();
            }
            else if(msg.what == 0x203)
            {
                favoriteListAdapter.notifyDataSetChanged();


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
//        ActionBar bar = getSupportActionBar();
//        bar.setTitle("123");
//        bar.setDisplayShowTitleEnabled(true);


        // 给左上角图标的左边加上一个返回的图标
   //     bar.setDisplayHomeAsUpEnabled(true);
        //使左上角图标可点击
   //     bar.setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.stock);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        rProgress = (ProgressBar) findViewById(R.id.progressBar);
        rProgress.setVisibility(View.GONE);
//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setIndeterminate(false);
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress.setVisibility(View.GONE);
        // Start lengthy operation in a background thread
//        new Thread(new Runnable() {
//            public void run() {
//                while (mProgressStatus < 100) {
//                    mProgressStatus = doWork();
//
//                    // Update the progress bar
//                    mHandler.post(new Runnable() {
//                        public void run() {
//                            mProgress.setProgress(mProgressStatus);
//                        }
//                    });
//                }
//            }
//        }).start();





  // set autocomplete
        acTextView = (AutoCompleteTextView) findViewById(R.id.symbol);
        acTextView.setThreshold(3);
        acTextView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String symbol =((TextView) (view.findViewById(R.id.TextView))).getText().toString();
                symbol = symbol.substring(0,symbol.indexOf("\n"));
                acTextView.setText(symbol);
            }
        });
        //acTextView.setAdapter(new SuggestionAdapter(this,acTextView.getText().toString()));
        acTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=3)
                {
                    getAutoJson(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//dynamic build button in a linear layout by findViewById - favorites
//        LinearLayout layout = (LinearLayout) findViewById(R.id.favorites);
//
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//

        ///  generate favorite list
        sharedPreferences = getSharedPreferences("favorite",Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("symbol","").equals("")){//if there is favorite list, generate the list
            getFavoriteItems();
        }

//  add listener on switch  timer
        handlerswitch = new Handler();
        runnableAuto = new Runnable() {
            @Override
            public void run() {
                refreshFavoriteList();
                //Toast.makeText(MyActivity.this,"auto refreash",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this,5000);
            }
        };
        runnable = new Runnable() {
            @Override
            public void run() {
                rProgress.setVisibility(View.GONE);
                //Toast.makeText(MyActivity.this,"auto refreash",Toast.LENGTH_SHORT).show();

            }
        };

///  builder  create
        builder = new AlertDialog.Builder(MyActivity.this);
// Add the buttons

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FavoriteItem favoriteItem = favoriteListAdapter.getItem(dialogPosition);
                if(favoriteItem != null)
                {
                    removeFavorite(favoriteItem.getSymbol());
                    favoriteListAdapter.remove(favoriteItem);
                    favoriteListAdapter.notifyDataSetChanged();
                }


                // User clicked OK button
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // User cancelled the dialog
            }
        });

        invalide = new AlertDialog.Builder(MyActivity.this);

        invalide.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.symbol);
                        autoCompleteTextView.setText("");
                    }
                });
        invalide.setMessage("Invalid Symbol");

        empty = new AlertDialog.Builder(MyActivity.this);

        empty.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.symbol);
                autoCompleteTextView.setText("");
            }
        });
        empty.setMessage("Please enter a Stock Name/Symbol");


       // Toast.makeText(this, "on create "+sharedPreferences.getString("symbol",""), Toast.LENGTH_LONG).show();
      //************** end of Product List Adapter demo**************************
    }///******************************************  end of create function**************************************

    @Override
    protected void onResume() {
        super.onResume();
        sharedSymbol = getSharedPreferences("sharedSymbol",Context.MODE_PRIVATE);
        AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.symbol);
        auto.setText(sharedSymbol.getString("input",""));
       // Toast.makeText(this, "resume 2222: "+sharedSymbol.getString("input",""), Toast.LENGTH_LONG).show();
    }

    public void removeFavorite(String symbol){
        String newPreferenceStr = sharedPreferences.getString("symbol","");
        String[] arraySymbol = newPreferenceStr.split("~");
        newPreferenceStr = "";
        for(int i=0;i<arraySymbol.length;i++){
            if(!symbol.equals(arraySymbol[i])&&!arraySymbol[i].equals("")){
                newPreferenceStr += arraySymbol[i] + "~";
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("symbol",newPreferenceStr);
        editor.commit();
    }
    // function of refresh
    public void onclickRefreshBtn(View view){

        refreshFavoriteList();
       // Toast.makeText(MyActivity.this,"checked",Toast.LENGTH_SHORT).show();
    }
    public void refreshFavoriteList(){
        rProgress.setVisibility(View.VISIBLE);
        if(favoriteItems.size() != 0){
            for(int i=0;i<favoriteItems.size();i++)
            {
                FavoriteItem favoriteItem = favoriteItems.get(i);
                checkEachItem(favoriteItem);

            }
            handler.sendEmptyMessage(0x203);

        }


        handlerswitch.postDelayed(runnable,500);
    }
//check switch status
    public void checkSwitch(View view){
        aSwitch = (Switch) findViewById(R.id.switch1);

        if(aSwitch.isChecked()){

            handler.postDelayed(runnableAuto,5000);
        }
        else{

            handler.removeCallbacks(runnableAuto);
        }
    }
    //check each item's price and changp to update
    public void checkEachItem(final FavoriteItem item){
        new Thread(){
            public void run(){
                String symbol = item.getSymbol();
                FavoriteItem newItem = new FavoriteItem(jsonParse.getParseJsonSD(symbol));
                if(!(item.getLastPrice().equals(newItem.getLastPrice()))){
                    item.setLastPrice(newItem.getLastPrice());
                }
                if(!(item.getChangeP().equals(newItem.getChangeP()))){
                    item.setChangeP(newItem.getChangeP());
                }
            }
        }.start();
    }

    //create Favoritelist's items by JsonParse.getParseJsonSD(symbol) function;
    public void getFavoriteItems(){  //input is string of favorites' symbol seperate by "~"
        new Thread(){
            public void run(){
                if(favoriteItems.size() != 0)
                {
                    favoriteItems = new ArrayList<>();
                }
                sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();
                String favoriteStr = sharedPreferences.getString("symbol","");
                List<FavoriteItem> fItems = new ArrayList<FavoriteItem>();
                if(!favoriteStr.equals("")&&!favoriteStr.equals(null)){
                    String[] arrayFavorites = favoriteStr.split("~");
                    for(int i=0;i<arrayFavorites.length;i++){
                        if(!arrayFavorites[i].equals("")){//set a single favoriteItem into FavoriteList Data
                            FavoriteItem favoriteItem = new FavoriteItem(jsonParse.getParseJsonSD(arrayFavorites[i]));
                            fItems.add(favoriteItem);
                        }

                    }
                    favoriteItems = fItems;
                }
                handler.sendEmptyMessage(0x202);
            }
        }.start();


    }

    //generate adapter and list
    public void processFavoriteList(){
        if(favoriteListAdapter == null)
        {
            favoriteListAdapter = new FavoriteListAdapter(
                    this,R.layout.favorite_item,favoriteItems
            );
            slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
            slideCutListView.setRemoveListener(this);
            slideCutListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FavoriteItem item = favoriteItems.get(position);
                    getStocksDetailBySymbol(item.getSymbol());
                }
            });
            slideCutListView.setAdapter(favoriteListAdapter);


            slideCutListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    FavoriteItem favoriteItem = favoriteItems.get(position);
                    getStocksDetailBySymbol(favoriteItem.getSymbol());
                    //Toast.makeText(MyActivity.this, favoriteItems.get(position).toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            favoriteListAdapter.notifyDataSetChanged();
        }

    }


    //auto complete;
    public void getAutoJson(String sName)
    {
        mProgress.setVisibility(View.VISIBLE);
        final String str = sName.replace(" ", "%20");
        new Thread() {
            public void run() {
                URLConnection jc = null;
                InputStream is = null;
                BufferedReader reader = null;
                try {
                    String temp = str;
                    URL js = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input=" + temp);
                    jc = js.openConnection();
                    is = jc.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(is));
                    JsonAuto = reader.readLine();
                    handler.sendEmptyMessage(0x201);
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
            }
        }.start();
    }

    public void  processAutoView()
    {

        JSONArray jsonArray;
        ArrayJson.clear();
        try {
            jsonArray = new JSONArray(JsonAuto);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                ArrayJson.add(jsonObject.getString("Symbol")+"\n"+jsonObject.getString("Name")+
                        "("+jsonObject.getString("Exchange")+")");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        autoAdapter = new ArrayAdapter(this,R.layout.autocomplete,R.id.TextView,ArrayJson);
        ((AutoCompleteTextView)findViewById(R.id.symbol)).setAdapter(autoAdapter);
    }



//when you click Quote get EditText
    public void buttonQuoteClick(View view) throws JSONException {



        AutoCompleteTextView syb = (AutoCompleteTextView) findViewById(R.id.symbol);
        String symbol = syb.getText().toString();
        if(symbol==null||symbol.equals(""))
        {
            dialogEmpty = empty.create();
            dialogEmpty.show();
            return;
        }
        else
        getStocksDetailBySymbol(symbol);
//        Snackbar.make(coordinator,"askdkasdjhf",
//                Snackbar.LENGTH_LONG).setAction("Action",null).show();
        //pass the stockDetails to second layout without any check
        //Toast.makeText(MyActivity.this,symbol,Toast.LENGTH_SHORT).show();

    }

    //Wed May 4 15:47:56 UTC-04:00 2016

        public static String strToDateLong(String strDate) {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC-04:00' yyyy"); //
            SimpleDateFormat Rformatter = new SimpleDateFormat("dd MMM yyyy', 'HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date strtodate = formatter.parse(strDate, pos);
            strDate = Rformatter.format(strtodate);
            return strDate;
        }






    public void getStocksDetailBySymbol(String name)
    {
        final String symbol = name;
        new Thread() {
            public void run() {
                JsonParse stockJson = new JsonParse();
                stockDetails = stockJson.getParseJsonSD(symbol);
                Log.d("a","it's done!");
                handler.sendEmptyMessage(0x200);
            }
        }.start();
    }
// when click Clear to clear text
    public void buttonClearClick(View view) {
        EditText syb = (EditText) findViewById(R.id.symbol);
        syb.setText("");
        sharedSymbol = getSharedPreferences("sharedSymbol",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedSymbol.edit();
        editor.putString("input","");
        editor.commit();
    }
    //receive favorite symbol string

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//receive symbol by click favoriteBtn
        if(requestCode == REQUEST){//check where the data from
            if(resultCode == RESULT_OK){//check the transmit is ok

            }
        }
    }

    @Override
    public void removeItem(RemoveDirection direction, int position) {
        dialogPosition = position;
        builder.setMessage("Want to delete "+favoriteItems.get(position).getName()+" from favorites?");
        dialog = builder.create();
        dialog.show();

//            Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();

//        switch (direction) {
//            case RIGHT:
//                Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();
//                break;
//            case LEFT:
//                Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                break;
//        }
    }

}
