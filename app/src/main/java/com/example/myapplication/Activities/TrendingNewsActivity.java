package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.myapplication.Adapters.AutoSuggestAdapter;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.NewsData.Entry;
import com.github.mikephil.charting.NewsData.LineNewsData;
import com.github.mikephil.charting.NewsData.LineNewsDataSet;
import com.github.mikephil.charting.interfaces.NewsDatasets.ILineNewsDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import Helpers.BottomNavigationViewHelper;

public class TrendingNewsActivity extends AppCompatActivity {

    @BindView(R.id.bottomNavigationView) BottomNavigationView bottomNavMenu; //Bottom Navigation View Menu
    EditText editText;
    String query;
    AutoSuggestAdapter autoSuggestAdapter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    Handler handler;
    View progressBar;
    TextView Progress_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        //setContentView(R.layout.app_content);

        ButterKnife.bind(this);
        drawChart("Coronavirus");
        progressBar = (View) findViewById(R.id.progress_bar);
        Progress_text=(TextView) findViewById(R.id.Progress_text);
        editText = (EditText) findViewById(R.id.trending_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println("Draw chart on editor action called");
                System.out.println("Draw chart on editor action is "+ actionId);
                //System.out.println("Draw chart on editor action type );
                boolean handled = false;
                if (actionId == 0 || actionId == 4) {
                    System.out.println("Draw chart  if condition text is "+ (String) editText.getText().toString());
                    String enterd_word=editText.getText().toString();
                    drawChart(enterd_word);
                    handled = true;
                }
                return handled;
            }
        });


        Toolbar toolbar=(Toolbar)findViewById(R.id.custom_home_toolbar);
        setSupportActionBar(toolbar);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView); //Disables the default transaction in the bottom navigation view
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);
        //Sets onClick listeners on the buttons on the bottom navigation view
        bottomNavMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        Intent homeIntent = new Intent(TrendingNewsActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                         break;


                    case R.id.headline:
                        Intent playListIntent = new Intent(TrendingNewsActivity.this, HeadlinesActivity.class);
                        startActivity(playListIntent);
                         break;

                    case R.id.trending:

                         break;


                    case R.id.favourites:
                        Intent favourtieIntent = new Intent(TrendingNewsActivity.this, FavouritesActivity.class);
                        startActivity(favourtieIntent);
                         break;

                }

                return false;
            }
        });
        //NEwsChart Code Starts Here


    }

    private void drawChart(String text) {
        System.out.println("Draw chart text is "+ text);
        query=text;
        text=text.replaceAll(" ","+");
        System.out.println("Draw chart text is "+ text);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://android-backend-274622.wn.r.appspot.com/api/trends/?word="+text;
        System.out.println("Draw chart url is "+ url);
        final List<Entry> entries_one= new ArrayList<>();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject responseobject = new JSONObject(response);
                            System.out.println("draw chart response is " + responseobject);
                            JSONArray timeline= responseobject.getJSONObject("default").getJSONArray("timelineNewsData");
                            System.out.println("draw chart timeline is " + timeline);
                            System.out.println("draw chart timeline length is " + timeline.length());
                            int[] values = new int[timeline.length()];
                            for(int i=0;i<timeline.length();i++){
                                JSONObject currentobject=timeline.getJSONObject(i);
                                System.out.println("draw chart currentobject " + currentobject);
                                int current_int= (int) currentobject.getJSONArray("value").get(0);
                                System.out.println("draw chart current_int " + current_int);
                                entries_one.add(new Entry(i,current_int));

                                values[i]=current_int;
                            }
                            System.out.println("values is "+ values);
                            LineNewsDataSet lineNewsDataSet=new LineNewsDataSet(entries_one,"Trending chart for "+query );
                            lineNewsDataSet.setColor(Color.parseColor("#b589d6"));
                            lineNewsDataSet.setHighLightColor(Color.parseColor("#b589d6"));
                            lineNewsDataSet.setCircleColor(Color.parseColor("#b589d6"));
                            lineNewsDataSet.setColor(Color.parseColor("#4b4680"));
                            List<ILineNewsDataSet> NewsDataSets=new ArrayList<>();
                            NewsDataSets.add(lineNewsDataSet);

                            LineNewsData NewsData=new LineNewsData(NewsDataSets);
                            LineChart chart=(LineChart) findViewById(R.id.chart);

                            Legend legend = chart.getLegend();
                            legend.setTextSize(24);
                            legend.setFormSize(24);
                           // legend.setTextColor(Color.parseColor("#b589d6"));
                            chart.getXAxis().setDrawGridLines(false);
                            chart.getAxisLeft().setDrawGridLines(false);
                            chart.getAxisRight().setDrawGridLines(false);
                            chart.getAxisLeft().setDrawZeroLine(false);
                            chart.getXAxis().setAxisLineWidth(0);
                            chart.getAxisLeft().setZeroLineWidth(0);
                            chart.getAxisLeft().setAxisLineWidth(0);
                            chart.getAxisRight().setAxisLineWidth(0);
                            chart.getAxisRight().setDrawZeroLine(false);
                            chart.getAxisLeft().setAxisLineColor(Color.parseColor("#FFFFFF"));
                            progressBar.setVisibility(View.GONE);
                            Progress_text.setVisibility(View.GONE);
                            chart.setVisibility(View.VISIBLE);

                            chart.setNewsData(NewsData);
                            chart.invalidate();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
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


                Intent opensearchpage= new Intent(TrendingNewsActivity.this, SearchPageActivity.class);

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
            }
        });
    }

}
