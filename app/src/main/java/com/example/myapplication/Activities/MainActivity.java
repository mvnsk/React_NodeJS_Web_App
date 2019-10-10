package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.*;
import java.io.*;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chootdev.recycleclick.RecycleClick;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.myapplication.Adapters.AutoSuggestAdapter;
import com.example.myapplication.Adapters.BookMarkAdapter;
import com.example.myapplication.Adapters.HomePageAdapter;
import com.example.myapplication.NewsData.NewsData;
import com.example.myapplication.Fragments.HomePageFragment;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;

import org.json.JSONArray;
import org.json.JSONObject;

import Helpers.BottomNavigationViewHelper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    //Bo
    LocationManager lm;
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String OPEN_WEATHER_URL = "https://api.openweathermap.org/NewsData/2.5/weather";
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomePageAdapter mAdapter;
    ArrayList<NewsData> newsArrayList;
    public static boolean Home_Fragment_Loaded=false;
    View progressBar;
    TextView Progress_text;
    Gson gson = new Gson();
    List<NewsData> fetchedlist;
    public static boolean bookmark_removed=false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static int clicked_position;
    @BindView(R.id.bottomNavigationView) BottomNavigationView bottomNavMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Launcher_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bookmark_removed=false;
        Home_Fragment_Loaded=false;


        progressBar = (View) findViewById(R.id.progress_bar);
        Progress_text=(TextView) findViewById(R.id.Progress_text);
        System.out.println("this is main");
        Toolbar toolbar=(Toolbar)findViewById(R.id.custom_home_toolbar);
        setSupportActionBar(toolbar);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
       // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
      try{
        requestLocationPermission();
        }
        catch(Exception e){
            System.out.println(e);
    }

        recyclerView=findViewById(R.id.home_recycler_view);
        layoutManager=new GridLayoutManager(this,1);
      //layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,
               // DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
       // recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(20));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,4));
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        //DividerItemDecoration itemDecor = new DividerItemDecoration(getContext, HORIZONTAL);
        //recyclerView.addItemDecoration(itemDecor);
        newsArrayList = new ArrayList<>();
        mAdapter = new HomePageAdapter(getApplicationContext(), newsArrayList);
     //   empty_view= (TextView)findViewById(R.id.empty_view);



      //  mAdapter= new HomePageAdapter(getApplicationContext(), newsArrayList);
       // recyclerView.setAdapter(mAdapter);

        RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // YOUR CODE
                NewsData NewsData = fetchedlist.get(position);//mAdapter.getItem(position);

                Intent openMainNews = new Intent(MainActivity.this, BusinessNewsActivity.class);
                System.out.println("detail page web url before detail is "+ NewsData.getUrlOfStory());
                openMainNews.putExtra(BusinessNewsActivity.NEWS_INFO, NewsData);
                openMainNews.putExtra("classname","FavouritesActivity");
                openMainNews.putExtra("classname","MainActivity");
                SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String json = gson.toJson(NewsData);
                editor.putString(BusinessNewsActivity.NEWS_INFO, json);
                editor.putInt(BusinessNewsActivity.DETAIL_ARTICLE_POSITION,position);
                System.out.println("yippy before click state is "+ NewsData.getIsStoryBookmarked());
                clicked_position=position;
                editor.commit();
                startActivity(openMainNews);
            }
        });


        RecycleClick.addTo(recyclerView).setOnItemLongClickListener(new RecycleClick.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                NewsData NewsData =fetchedlist.get(position);// mAdapter.getItem(position);
                //TextView mytext = view.findViewById(R.id.titleOfStory_text_view);
                String title=NewsData.getTitleOfStory();
                String image=NewsData.getImageOfStoryResource();
                String weburl=NewsData.getUrlOfStory();
                String bookmarked=NewsData.getIsStoryBookmarked();
                openDialog(title,image,weburl,bookmarked, NewsData,v,position);
                return true;
            }
        });



    CallYourRefreshingMethod();
      bottomNavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:

                       break;

                    case R.id.headline:
                        Intent playListIntent = new Intent(MainActivity.this, HeadlinesActivity.class);
                        startActivity(playListIntent);
                         break;
                    case R.id.trending:
                        Intent trendingIntent = new Intent(MainActivity.this, TrendingNewsActivity.class);
                        startActivity(trendingIntent);
                         break;
                    case R.id.favourites:
                        Intent favouriteIntent = new Intent(MainActivity.this, FavouritesActivity.class);
                        startActivity(favouriteIntent);
                         break;
                }

                return false;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                SwipeRefreshingMethod();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 1000);
            }
        });

    }

    private void SwipeRefreshingMethod() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://android-backend-274622.wn.r.appspot.com/api/home";

        final HashMap<String, Integer> map = new HashMap<>();
        int counter=0;
        for (NewsData var : fetchedlist)
        {
            String currentkey=var.getUrlOfStory();
            map.put(currentkey,counter);
            counter++;

        }




        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //fetchedlist= new ArrayList<>();;
                        List<NewsData> newlyfetched= new ArrayList<>();;
                        try {
                            JSONObject baseJson = new JSONObject(response);
                            if (baseJson.has("response")) {


                                baseJson=baseJson.getJSONObject("response");
                            }
                            JSONArray news_array = baseJson.getJSONArray("results");
                            System.out.println("swipe refresh news_array is " +news_array);
                            int counter=0;
                            for (int i = 0; i < news_array.length(); i++) {

                                try {
                                    System.out.println("swipe refresh iteration is " + i);
                                    JSONObject currentNews = news_array.getJSONObject(i);
                                    String url = currentNews.getString("webUrl");
                                     if (map.containsKey(url)) {
                                        int index = map.get(url);
                                        NewsData temp = fetchedlist.get(index);
                                        newlyfetched.add(temp);
                                        System.out.println("swipe refresh  already exists");
                                        continue;

                                    }
                                    String bookmarked = "no";
                                    String nameOfSection = currentNews.getString("sectionName");

                                    String title = currentNews.getString("webTitle");
                                    String date = currentNews.getString("webPublicationDate");

                                    String id = currentNews.getString("id");


                                    JSONArray elements = currentNews.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements");
                                    JSONArray bodyArray = currentNews.getJSONObject("blocks").getJSONArray("body");
                                    JSONObject bodyelem = bodyArray.getJSONObject(0);
                                    String body = bodyelem.getString("bodyTextSummary");
                                    System.out.println("testing elements is " + elements);
                                    JSONObject temp = elements.getJSONObject(0);
                                    System.out.println("testing temp is " + temp);
                                    JSONArray assets = temp.getJSONArray("assets");
                                    System.out.println("testing assets is " + assets);
                                    if (assets.length() > 0) {
                                        JSONObject t2 = assets.getJSONObject(0);
                                        System.out.println("testing t2 is " + t2);
                                        String thumbnail = t2.getString("file");
                                        System.out.println("testing thumbnail is" + thumbnail);
                                        List<String> contributor = new ArrayList<>();
                                        NewsData news = new NewsData(title, thumbnail, url, date, nameOfSection, contributor, body, id, bookmarked);
                                        //newsArrayList.add(news);
                                        fetchedlist.add(news);
                                        // newsFromNewsLoader.add(0,news);
                                        // newsFromNewsLoader.remove(newsFromNewsLoader.size() - 1);//.insert(news,counter);

                                        fetchedlist.add(0,news);
                                        fetchedlist.remove(fetchedlist.size() - 1);
                                        System.out.println("swipe refresh   adapter  updated at " + counter);
                                        counter++;
                                    }




                                    System.out.println("swipe refresh   adapter added");
                                }   catch(Exception e){
                                    System.out.println("swipe refresh error is " + e.toString());
                                    continue;

                                }
                            }

                        }catch(Exception e){
                            Log.d("world enws fragment","error reProgress_text articles");
                        }

                      /*  mAdapter= new HomePageAdapter(getApplicationContext(), (ArrayList<NewsData>) fetchedlist);
                        Progress_text.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(mAdapter);
                        Home_Fragment_Loaded=true;*/
                      mAdapter.notifyNewsDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    ///HERE STARTS SEARCHICON BING API CODE
   @SuppressLint("RestrictedApi")
   @Override
    public boolean onCreateOptionsMenu(Menu menu){
    //getMenuInflater().inflate(R.menu.searchable,menu);
   // MenuItem searchitem=menu.findItem(R.id.search_icon);



       getMenuInflater().inflate(R.menu.dummy_search,menu);
       MenuItem searchitem=menu.findItem(R.id.action_search);
       SearchView searchView=(   SearchView)   searchitem.getActionView();



    final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);


       autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);
       searchAutoComplete.setThreshold(3);
       searchAutoComplete.setAdapter(autoSuggestAdapter);


       searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
               String queryString=(String)adapterView.getItemAtPosition(itemIndex);
               searchAutoComplete.setText("" + queryString);


               Intent opensearchpage= new Intent(MainActivity.this, SearchPageActivity.class);

               opensearchpage.putExtra("query",queryString);
               startActivity(opensearchpage);
           }
       });
       searchAutoComplete.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int
                   count, int after) {
               System.out.println("sai baba before text called");
           }
           @Override
           public void onTextChanged(CharSequence s, int start, int before,
                                     int count) {
               handler.removeMessages(TRIGGER_AUTO_COMPLETE);
               handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                       AUTO_COMPLETE_DELAY);
           }
           @Override
           public void afterTextChanged(Editable s) {
           }
       });

       handler = new Handler(new Handler.Callback() {
           @Override
           public boolean handleMessage(Message msg) {
               if (msg.what == TRIGGER_AUTO_COMPLETE) {
                   if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                       makeApiCall(searchAutoComplete.getText().toString());
                   }
               }
               return false;
           }
       });


       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            System.out.println("sai baba query text submit called");
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            System.out.println("sai baba text change called");
            return false;
        }
    });
        return super.onCreateOptionsMenu(menu);
    }

    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)      {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {

                    JSONObject responseObject = new JSONObject(response);
                    System.out.println("sai baba responded object is " + responseObject);
                    JSONArray suggestionGroups = responseObject.getJSONArray("suggestionGroups");
                     JSONObject temp=   suggestionGroups.getJSONObject(0);
                    JSONArray searchSuggestions=temp.getJSONArray("searchSuggestions");

                    for (int i = 0; i < searchSuggestions.length(); i++) {
                        //restricting to  5 suggestions
                        if (i==5){break;}
                        JSONObject row = searchSuggestions.getJSONObject(i);
                        stringList.add(row.getString("displayText"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set NewsData here and notify
                autoSuggestAdapter.setNewsData(stringList);


                autoSuggestAdapter.notifyNewsDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("volley bing error is  "+ error);
            }
        });
}


//Weather Code Starts Here
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    Log.d("Main activity", " onRequestPermissionsResult");
    // Forward results to EasyPermissions
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
}

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() throws IOException {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            //Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.d("Main activity", " onRequestLocation ");
            double mylongitude = location.getLongitude();
            double mylatitude = location.getLatitude();
            System.out.println("latitude is" + mylatitude);
            System.out.println("longitude is" + mylongitude);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(mylatitude, mylongitude, 1);
            final String cityName = addresses.get(0).getLocality();
            final String stateName = addresses.get(0).getAdminArea();
            String countryName = addresses.get(0).getAddressLine(2);
            System.out.println("city name is " + cityName );
            System.out.println("state name is " + stateName);
            //VOLLEY REQUEST

            Uri.Builder builder = Uri.parse(OPEN_WEATHER_URL).buildUpon();
            builder.appendQueryParameter("q",cityName)
                    .appendQueryParameter("units", "metric")
                .appendQueryParameter("appid", "47d5d152f18479c50aff85f2ec29413c");
            String openurl=builder.build().toString();
            System.out.println("openweather url is " + openurl );
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, openurl, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            System.out.println("open Response"+ response.toString());
                            System.out.println();
                            JsonObject convertedObject = new Gson().fromJson(response.toString(), JsonObject.class);
                            System.out.println("converted object is"+  convertedObject.toString());
                            JsonObject main=convertedObject.get("main").getAsJsonObject();
                            JsonArray weather=convertedObject.get("weather").getAsJsonArray();
                            JsonObject weather_Zero= (JsonObject) weather.get(0);
                            String weather_Category=weather_Zero.get("main").getAsString().toLowerCase();

                            String category=weather_Zero.get("main").getAsString();
                            System.out.println("Weather category is"+ weather_Category);
                            Float temperature=Float.parseFloat(main.get("temp").getAsString());
                            int temp=Math.round(temperature);
                            String wet_temp=String.valueOf(temp)+ "\u00B0" +"C";
                            System.out.println("wet temp is "+wet_temp);
                            ImageView weather_image_view =(ImageView) findViewById(R.id.weather_image);
                            weather_image_view.setClipToOutline(true);
                            String image_source;
                            if(weather_Category.equals("clouds")){
                                weather_image_view.setImageResource(R.drawable.cloudy_weather);

                            }
                            else if(weather_Category.equals("clear")){
                                  weather_image_view.setImageResource(R.drawable.clear_weather);

                            }
                            else if(weather_Category.equals("snow")){
                                 weather_image_view.setImageResource(R.drawable.snowy_weather);

                            }
                            else if(weather_Category.equals("rain") || weather_Category.equals("drizzle") ){
                                 weather_image_view.setImageResource(R.drawable.rainy_weather);

                            }
                            else if(weather_Category.equals("thunderstorm")){
                                  weather_image_view.setImageResource(R.drawable.thunder_weather);

                            }
                            else{
                                weather_image_view.setImageResource(R.drawable.sunny_weather);
                            }
                            weather_image_view.setVisibility(View.VISIBLE);
                            TextView city_view=(TextView) findViewById(R.id.weather_city);
                            city_view.setText(cityName);

                           TextView state_view=(TextView) findViewById(R.id.weather_state);
                            state_view.setText(stateName);
                            TextView temp_view=(TextView) findViewById(R.id.weather_temp);
                            temp_view.setText(wet_temp);

                            TextView weather_cat_view= findViewById(R.id.weather_cat);
                           weather_cat_view.setText(category);




                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response","volley error");
                        }
                    }
            );

// add it to the RequestQueue


            //Instantiate the RequestQueue and add the request to the queue
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(getRequest);

        }

        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }


    }

    private void CallYourRefreshingMethod() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://android-backend-274622.wn.r.appspot.com/api/home";
        final HashMap<String, Integer> map = new HashMap<>();
        int counter=0;



        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        fetchedlist= new ArrayList<>();;

                        try {
                            JSONObject baseJson = new JSONObject(response);
                            if (baseJson.has("response")) {


                                baseJson=baseJson.getJSONObject("response");
                            }
                            JSONArray news_array = baseJson.getJSONArray("results");
                            System.out.println("swipe refresh news_array is " +news_array);
                            int counter=0;
                            for (int i = 0; i < news_array.length(); i++) {

                                try {
                                    System.out.println("swipe refresh iteration is " + i);
                                    JSONObject currentNews = news_array.getJSONObject(i);
                                    String url = currentNews.getString("webUrl");
                                /*    if (map.containsKey(url)) {
                                        int index = map.get(url);
                                        NewsData temp = newsFromNewsLoader.get(index);
                                        fetchedlist.add(temp);
                                        System.out.println("swipe refresh  already exists");
                                        continue;

                                    }*/
                                    String bookmarked = "no";
                                    String nameOfSection = currentNews.getString("sectionName");

                                    String title = currentNews.getString("webTitle");
                                    String date = currentNews.getString("webPublicationDate");

                                    String id = currentNews.getString("id");


                                    //JSONArray elements = currentNews.getJSONObject("blocks").getJSONObject("main").getJSONArray("elements");
                                    JSONObject fields = currentNews.getJSONObject("fields");
                                    String thumbnail=fields.getString("thumbnail");
                                    /*JSONArray bodyArray = currentNews.getJSONObject("blocks").getJSONArray("body");
                                    JSONObject bodyelem = bodyArray.getJSONObject(0);
                                    String body = bodyelem.getString("bodyTextSummary");
                                    System.out.println("testing elements is " + elements);
                                    JSONObject temp = elements.getJSONObject(0);
                                    System.out.println("testing temp is " + temp);
                                    JSONArray assets = temp.getJSONArray("assets");
                                    System.out.println("testing assets is " + assets);
                                    if (assets.length() > 0) {
                                        JSONObject t2 = assets.getJSONObject(0);
                                        System.out.println("testing t2 is " + t2);
                                        String thumbnail = t2.getString("file");
                                        System.out.println("testing thumbnail is" + thumbnail);
                                        List<String> contributor = new ArrayList<>();
                                        NewsData news = new NewsData(title, thumbnail, url, date, nameOfSection, contributor, body, id, bookmarked);
                                        //newsArrayList.add(news);
                                        fetchedlist.add(news);
                                       // newsFromNewsLoader.add(0,news);
                                       // newsFromNewsLoader.remove(newsFromNewsLoader.size() - 1);//.insert(news,counter);
                                        System.out.println("swipe refresh   adapter  updated at " + counter);
                                        counter++;
                                    }*/
                                    String body = "";
                                    List<String> contributor = new ArrayList<>();
                                    NewsData news = new NewsData(title, thumbnail, url, date, nameOfSection, contributor, body, id, bookmarked);
                                    fetchedlist.add(news);




                                    System.out.println("swipe refresh   adapter added");
                                }   catch(Exception e){
                                    System.out.println("swipe refresh error is " + e.toString());
                                    continue;

                                }
                            }

                        }catch(Exception e){
                            Log.d("world enws fragment","error reProgress_text articles");
                        }

                        mAdapter= new HomePageAdapter(getApplicationContext(), (ArrayList<NewsData>) fetchedlist);
                        Progress_text.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(mAdapter);
                        Home_Fragment_Loaded=true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void openDialog(final String title, final String imageUrl, final String weburl, final String bookmarked, final NewsData open_NewsData, final View view, final int position) {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_layout_v2);
        ImageView dialog_image=(ImageView) dialog.findViewById(R.id.dialog_image);

        Picasso.get().setLoggingEnabled(true);
        TextView dialogtitle=(TextView) dialog.findViewById(R.id.dialog_title);
        dialogtitle.setText(title);
        System.out.println("dialog image url is");
        System.out.print("dialog image url is " + imageUrl);
        Picasso.get()
                .load(imageUrl)
                .into(dialog_image);
        ImageButton twitter_btn=(ImageButton) dialog.findViewById(R.id.dialog_twitter);
        final ImageButton bookmark_btn=(ImageButton) dialog.findViewById(R.id.dialog_bookmark);
        if(bookmarked.equals("yes")){
            bookmark_btn.setImageResource(R.drawable.bookmark_fill);
        }
        else{
            bookmark_btn.setImageResource(R.drawable.bookmark_empty);
        }

        twitter_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String TWITTER_URL = "https://twitter.com/intent/tweet";
                Uri.Builder builder = Uri.parse(TWITTER_URL).buildUpon();
                String tweet="Check out this Link:" + "\n" + weburl;
                builder.appendQueryParameter("text", tweet)
                        .appendQueryParameter("hashtags", "CSCI571NewsSearch");
                String url=builder.toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                // Note the Chooser below. If no applications match,
                // Android displays a system message.So here there is no need for try-catch.

                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });
        //Bookmark Button Listener
        bookmark_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                try{
                    System.out.println("listener called World Called");
                    String toast_Add_msg= '"'+ title + '"'  +  " was added to Bookmarks";
                    String toast_Remove_msg='"'+ title+ '"'+ " was removed from Bookmarks";
                    ImageButton card_bookmark_btn= (ImageButton) findViewById(R.id.bookmark_image);

                    if(open_NewsData.getIsStoryBookmarked().equals("yes")){
                        System.out.println("listener called  if World Called");
                        bookmark_btn.setImageResource(R.drawable.bookmark_empty);
                        // card_bookmark_btn.setImageResource(R.drawable.bookmark_empty);
                        open_NewsData.setIsStoryBookmarked("no");
                        System.out.println("listener called  if after change " + open_NewsData.getIsStoryBookmarked());
                        Toast.makeText(MainActivity.this,toast_Remove_msg,Toast.LENGTH_SHORT).show();
                        SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        if(pref.contains(weburl)){
                            editor.remove(weburl).commit();
                        }
                        Log.d("home position is", String.valueOf(position));
                        Log.d("home fetchedlist is", String.valueOf(position));

                        //fetchedlist.remove(position);
                        fetchedlist.set(position,open_NewsData);
                        mAdapter.notifyItemChanged(position);
                        //newsArrayList.remove(position);
                        //recyclerView.removeViewAt(position);
                        //mAdapter.notifyItemRemoved(position);
                       // mAdapter.notifyItemRangeChanged(position, newsArrayList.size());
                        System.out.println("arraylist size is"+ position);
                        System.out.println("arraylist adapter size is"+ mAdapter.getItemCount());
                       /* if(mAdapter.getItemCount()==0){



                        }*/

                    /* if(newsArrayList.size()>0) {
                        //  mAdapter.removeItem(position);
                        //recyclerView.setAdapter( new BookMarkAdapter(getApplicationContext(), newsArrayList));
                    }
                    else{
                        TextView empty_view= findViewById(R.id.empty_view);

                        empty_view.setText("No Bookmarked Articles");
                    }*/

                    }
                    else{
                        System.out.println("listener called else  World Called");
                        bookmark_btn.setImageResource(R.drawable.bookmark_fill);
                        // card_bookmark_btn.setImageResource(R.drawable.bookmark_fill);
                        open_NewsData.setIsStoryBookmarked("yes");
                        Toast.makeText(MainActivity.this,toast_Add_msg,Toast.LENGTH_SHORT).show();

                        SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        String json;
                        json = gson.toJson(open_NewsData);
                        editor.putString(weburl, json);

                        editor.commit();
                        fetchedlist.set(position,open_NewsData);
                        mAdapter.notifyItemChanged(position);


                    }
                } catch (Exception e){
                    Log.d("Favo fetchedlist.set(position,open_NewsData);\n" +
                            "                        mAdapter.notifyItemChanged(position);rites bookmark dialog " , e.toString());
                }
            }
        });


        dialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume error invoked ");
        if(Home_Fragment_Loaded){

        try{
            NewsData tempnews = fetchedlist.get(clicked_position);
            SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            if(pref.contains(BusinessNewsActivity.BOOKMARK_DETAIL_TAG)) {
                String detail_bookmarked = pref.getString(BusinessNewsActivity.BOOKMARK_DETAIL_TAG, null);
                System.out.println("yippy state is "+ detail_bookmarked);
                if (detail_bookmarked.equals("yes")) {
                    tempnews.setIsStoryBookmarked("yes");
                } else {
                    tempnews.setIsStoryBookmarked("no");

                }
                if(fetchedlist.size()>clicked_position){
                    fetchedlist.set(clicked_position,tempnews);
                    mAdapter.notifyItemChanged(clicked_position);
                    //NewsData.set(position, tempnews);}
            }}}
        catch(Exception e){
            System.out.println("world on else called error " + e);
        }
        }



      /*  try {
            if(bookmark_removed){
                NewsData currentnews= fetchedlist.get(clicked_position);
                fetchedlist.set(clicked_position,p);
                mAdapter.notifyItemChanged(position);

                //recyclerView.removeViewAt(clicked_position);
                //mAdapter.notifyItemRemoved(clicked_position);
                //mAdapter.notifyItemRangeChanged(clicked_position, newsArrayList.size());
                mAdapter.notifyNewsDataSetChanged();
                bookmark_removed=false;
               /* if(mAdapter.getItemCount()==0){




                }

            }
        }

        catch(Exception e){
            Log.d("lifecycle", "onResume error invoked " + e.toString());
        }*/



// Stop a repeating task like this.



    }
}