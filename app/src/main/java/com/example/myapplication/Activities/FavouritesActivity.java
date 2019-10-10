package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chootdev.recycleclick.RecycleClick;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.myapplication.Adapters.AutoSuggestAdapter;
import com.example.myapplication.Adapters.BookMarkAdapter;
import com.example.myapplication.Adapters.MainNewsAdapter;
import com.example.myapplication.NewsData.NewsData;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.myapplication.NewsData.NewsData;

import org.json.JSONArray;
import org.json.JSONObject;

import Helpers.BottomNavigationViewHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class FavouritesActivity extends AppCompatActivity {
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavMenu;//Bottom Navigation View Menu
    public RecyclerView recyclerView;
    TextView empty_view;
    public static boolean did_Search=false;

    ArrayList<NewsData> newsArrayList;
    private BookMarkAdapter mAdapter;
    Gson gson;
    public static boolean bookmark_removed=false;
    public static int clicked_position;
    Handler handler;
    Runnable runnable;
    RecyclerView.LayoutManager layoutManager;
    AutoSuggestAdapter autoSuggestAdapter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
                @Override
                protected void onCreate(@Nullable Bundle savedInstanceState) {

                   // setTheme(R.style.AppTheme);
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.test4);
                    //setContentView(R.layout.activity_favourites);
                    ButterKnife.bind(this);
                    NewsData NewsData;
                    recyclerView=findViewById(R.id.bookmark_recycler_view);
                    layoutManager=new GridLayoutManager(this,2);
                    recyclerView.setLayoutManager(layoutManager);
                    newsArrayList = new ArrayList<>();
                    mAdapter = new  BookMarkAdapter(getApplicationContext(), newsArrayList);
                    empty_view= (TextView)findViewById(R.id.empty_view);
                    did_Search=false;

                    Toolbar toolbar=(Toolbar)findViewById(R.id.custom_home_toolbar);
                    setSupportActionBar(toolbar);

                   // recyclerView.setAdapter(mAdapter);
                    SharedPreferences pref = this.getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                     gson = new Gson();
                    Map<String, ?> allEntries = pref.getAll();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        String key=entry.getKey();
                        if(key.startsWith("https")) {
                            try {
                                String json = pref.getString(key, "");
                                NewsData = gson.fromJson(json, NewsData.class);
                                newsArrayList.add(NewsData);
                                Log.d("saved values bookmark state is ", key + ": " + NewsData.getIsStoryBookmarked());
                            }
                            catch(Exception e){
                                Log.d("saved values error is", e.toString() );
                            }


                        }
                    else{
                            //Log.d("bookmaked values", key + ": " + NewsData.getTitleOfStory());
                            Log.d("saved values not article",entry.getKey() + ": " + entry.getValue().toString());
                    }
                    }
                   // mAdapter.addAll(newsArrayList);
                    if(newsArrayList.size()>0) {
                        mAdapter= new BookMarkAdapter(getApplicationContext(), newsArrayList);
                        recyclerView.setAdapter(mAdapter);
                      //  recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.));
                        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
                    }
                    else{

                        empty_view.setText("No Bookmarked Articles");
                    }
                    RecycleClick.addTo(recyclerView).setOnItemClickListener(new RecycleClick.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            // YOUR CODE
                            NewsData NewsData = newsArrayList.get(position);//mAdapter.getItem(position);

                            Intent openMainNews = new Intent(FavouritesActivity.this, BusinessNewsActivity.class);
                            System.out.println("detail page web url before detail is "+ NewsData.getUrlOfStory());
                            openMainNews.putExtra(BusinessNewsActivity.NEWS_INFO, NewsData);
                            openMainNews.putExtra("classname","FavouritesActivity");

                            SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            String json = gson.toJson(NewsData);
                            editor.putString(BusinessNewsActivity.NEWS_INFO, json);
                            editor.putInt(BusinessNewsActivity.DETAIL_ARTICLE_POSITION,position);
                            clicked_position=position;
                            editor.commit();
                            startActivity(openMainNews);
                        }
                    });
                    RecycleClick.addTo(recyclerView).setOnItemLongClickListener(new RecycleClick.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                            NewsData NewsData =newsArrayList.get(position);// mAdapter.getItem(position);
                            //TextView mytext = view.findViewById(R.id.titleOfStory_text_view);
                            String title=NewsData.getTitleOfStory();
                            String image=NewsData.getImageOfStoryResource();
                            String weburl=NewsData.getUrlOfStory();
                            String bookmarked=NewsData.getIsStoryBookmarked();
                            openDialog(title,image,weburl,bookmarked, NewsData,v,position);
                            return true;
                        }
                    });


                    BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
                    //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView); //Disables the default transaction in the bottom navigation view
                    Menu menu=bottomNavigationView.getMenu();
                    MenuItem menuItem=menu.getItem(3);
                    menuItem.setChecked(true);
                    //Sets onClick listeners on the buttons on the bottom navigation view
                    bottomNavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            switch (item.getItemId()){
                                case R.id.home:
                                    Intent homeIntent = new Intent(FavouritesActivity.this, MainActivity.class);
                                    startActivity(homeIntent);
                                    break;


                                case R.id.headline:
                                    Intent playListIntent = new Intent(FavouritesActivity.this, HeadlinesActivity.class);
                                    startActivity(playListIntent);
                                     break;



                                case R.id.trending:
                                    Intent trendingIntent = new Intent(FavouritesActivity.this, TrendingNewsActivity.class);
                                    startActivity(trendingIntent);
                                     break;
                                case R.id.favourites:

                                     break;
                            }

                            return false;
                        }
                    });

                }
    public void openDialog(final String title, final String imageUrl, final String weburl, final String bookmarked, final NewsData open_NewsData, final View view, final int position) {

        final Dialog dialog = new Dialog(FavouritesActivity.this);
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
                String toast_Add_msg=title+ " was added to Bookmarks";
                String toast_Remove_msg=title+ " was removed from Bookmarks";
                ImageButton card_bookmark_btn= (ImageButton) findViewById(R.id.bookmark_image);

                if(open_NewsData.getIsStoryBookmarked().equals("yes")){
                    System.out.println("listener called  if World Called");
                    bookmark_btn.setImageResource(R.drawable.bookmark_empty);
                   // card_bookmark_btn.setImageResource(R.drawable.bookmark_empty);
                    open_NewsData.setIsStoryBookmarked("no");
                    System.out.println("listener called  if after change " + open_NewsData.getIsStoryBookmarked());
                    Toast.makeText(FavouritesActivity.this,toast_Remove_msg,Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    if(pref.contains(weburl)){
                        editor.remove(weburl).commit();
                    }
                    dialog.dismiss();
                    newsArrayList.remove(position);
                    recyclerView.removeViewAt(position);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, newsArrayList.size());
                    System.out.println("arraylist size is"+ position);
                    System.out.println("arraylist adapter size is"+ mAdapter.getItemCount());
                    if(mAdapter.getItemCount()==0){



                       empty_view.setText("No Bookmarked Articles");
                   }

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
                    Toast.makeText(FavouritesActivity.this,toast_Add_msg,Toast.LENGTH_SHORT).show();

                    SharedPreferences pref = getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    String json;
                    json = gson.toJson(open_NewsData);
                    editor.putString(weburl, json);

                    editor.commit();
                }
            } catch (Exception e){
                Log.d("Favorites bookmark dialog " , e.toString());
                }
            }
        });


        dialog.show();
    }
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume error invoked ");
        try {
        if(bookmark_removed){
            newsArrayList.remove(clicked_position);
            recyclerView.removeViewAt(clicked_position);
            mAdapter.notifyItemRemoved(clicked_position);
            mAdapter.notifyItemRangeChanged(clicked_position, newsArrayList.size());
            bookmark_removed=false;
            if(mAdapter.getItemCount()==0){



                empty_view.setText("No Bookmarked Articles");
            }

        }
        }

        catch(Exception e){
            Log.d("lifecycle", "onResume error invoked " + e.toString());
        }

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){

                    try {
                        if(newsArrayList.size()==0){

                            empty_view.setText(R.string.no_bookmarked_articles);
                        }


                    }
                    catch(Exception e){
                        Log.d("dummy exception is ", e.toString());
                    }
            }
        },0,500);


// Stop a repeating task like this.
       try{
            if(did_Search){
               // mAdapter.notifyNewsDataSetChanged();
                Log.d("lifecycle", "inside did search");
                SharedPreferences pref = this.getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                gson = new Gson();
                newsArrayList.clear();
                Map<String, ?> allEntries = pref.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    String key=entry.getKey();
                    if(key.startsWith("https")) {
                        try {
                            String json = pref.getString(key, "");
                             NewsData NewsData = gson.fromJson(json, NewsData.class);
                            newsArrayList.add(NewsData);
                            Log.d("saved values bookmark state is ", key + ": " + NewsData.getIsStoryBookmarked());
                        }
                        catch(Exception e){
                            Log.d("saved values error is", e.toString() );
                        }


                    }
                    else{
                        //Log.d("bookmaked values", key + ": " + NewsData.getTitleOfStory());
                        Log.d("saved values not article",entry.getKey() + ": " + entry.getValue().toString());
                    }
                }
                // mAdapter.addAll(newsArrayList);
                if(newsArrayList.size()>0) {
                    Log.d("lifecycle", "inside did search size");
                   // mAdapter= new BookMarkAdapter(getApplicationContext(), newsArrayList);
                   // recyclerView.setAdapter(mAdapter);
                    //  recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.));
                    //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                   // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
                    mAdapter.notifyNewsDataSetChanged();
                }
                else{

                    empty_view.setText("No Bookmarked Articles");
                }



            }
        }
catch(Exception e){

}

                }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
      //  handler.removeCallbacks(runnable);
    }


   @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //getMenuInflater().inflate(R.menu.searchable,menu);
        // MenuItem searchitem=menu.findItem(R.id.search_icon);



        getMenuInflater().inflate(R.menu.dummy_search,menu);
        MenuItem searchitem=menu.findItem(R.id.action_search);
        SearchView searchView=(   SearchView)   searchitem.getActionView();

        //ImageView searchIcon=  (ImageView)  searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);

        final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);


        autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setThreshold(3);
        searchAutoComplete.setAdapter(autoSuggestAdapter);


        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);


                Intent opensearchpage= new Intent(FavouritesActivity.this, SearchPageActivity.class);

                opensearchpage.putExtra("query",queryString);
                did_Search=true;

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
            }
        });
    }


}
