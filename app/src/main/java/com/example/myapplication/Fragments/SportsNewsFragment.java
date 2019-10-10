package com.example.myapplication.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Activities.BusinessNewsActivity;
import com.example.myapplication.NewsData.NewsData;
import com.example.myapplication.Adapters.MainNewsAdapter;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import Loader.NewsLoader;


public   class SportsNewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsData>>{
    public static boolean SPORTS_Fragment_Loaded=false;
    Gson gson = new Gson();


    private MainNewsAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mEmptyStateTextView;
    ListView newsListView;
    private static final int NEWS_LOADER_ID = 1;
    LoaderManager loaderManager;
    boolean isConnected;
    private View progressBar;
    TextView Progress_text;
    List<NewsData> newsFromNewsLoader;
    public SportsNewsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("World on create called ");
        View rootView = inflater.inflate(R.layout.news_list, container, false);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh_items);

        SPORTS_Fragment_Loaded=false;
        newsListView = (ListView) rootView.findViewById(R.id.list);

        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);
        progressBar = (View) rootView.findViewById(R.id.progress_bar);
        Progress_text=(TextView) rootView.findViewById(R.id.Progress_text);
        mAdapter = new MainNewsAdapter(getContext(), new ArrayList<NewsData>());
        newsListView.setAdapter(mAdapter);



        if (isConnected){

            loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            Progress_text.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // System.out.println("dialog position is " + position);
                // System.out.println("dialog view type is " + view.getClass().getSimpleName());
                NewsData NewsData = mAdapter.getItem(position);

                Intent openMainNews = new Intent(getContext(), BusinessNewsActivity.class);
                System.out.println("detail page web url before detail is "+ NewsData.getUrlOfStory());
                openMainNews.putExtra(BusinessNewsActivity.NEWS_INFO, NewsData);


                SharedPreferences pref = getActivity().getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                String json = gson.toJson(NewsData);
                editor.putString(BusinessNewsActivity.NEWS_INFO, json);
                editor.putInt(BusinessNewsActivity.DETAIL_ARTICLE_POSITION,position);

                editor.commit();
                startActivity(openMainNews);



            }
        });

        newsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                NewsData NewsData = mAdapter.getItem(position);
                String title=NewsData.getTitleOfStory();
                String image=NewsData.getImageOfStoryResource();
                String weburl=NewsData.getUrlOfStory();
                String bookmarked=NewsData.getIsStoryBookmarked();
                openDialog(title,image,weburl,bookmarked, NewsData,view);


                return true;
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to make your refresh action
                CallYourRefreshingMethod();
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


        return rootView;
    }

    private void CallYourRefreshingMethod() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://thisishw8.appspot.com/api/sports";
        final HashMap<String, Integer> map = new HashMap<>();
        int counter=0;
        for (NewsData var : newsFromNewsLoader)
        {
            String currentkey=var.getUrlOfStory();
            map.put(currentkey,counter);
            counter++;
            //System.out.println("swipe refresh is "+currentkey);
        }



        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        List<NewsData> fetchedlist= new ArrayList<>();;

                        try {
                            JSONObject baseJson = new JSONObject(response);
                            if (baseJson.has("response")) {
                                //Checking address Key Present or not

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
                                        NewsData temp = newsFromNewsLoader.get(index);
                                        fetchedlist.add(temp);
                                        System.out.println("swipe refresh  already exists");
                                        continue;

                                    }
                                    String bookmarked = "no";
                                    String nameOfSection = currentNews.getString("sectionName");

                                    String title = currentNews.getString("webTitle");
                                    String date = currentNews.getString("webPublicationDate");

                                    String id = currentNews.getString("id");
                                   /* JSONObject fields = currentNews.getJSONObject("fields");
                                    String thumbnail = fields.getString("thumbnail");

                                    String body = "";
                                    List<String> contributor = new ArrayList<>();
                                    NewsData news = new NewsData(title, thumbnail, url, date, nameOfSection, contributor, body, id, bookmarked);*/

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
                                        newsFromNewsLoader.add(0,news);
                                        newsFromNewsLoader.remove(newsFromNewsLoader.size() - 1);//.insert(news,counter);
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
                        mAdapter.clear();
                        mAdapter.addAll(newsFromNewsLoader);
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

    public void openDialog(final String title, final String imageUrl, final String weburl, final String bookmarked, final NewsData NewsData, final View view) {

        final Dialog dialog = new Dialog(getActivity());
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

                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

        bookmark_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                System.out.println("Figure out World Called");
                String toast_Add_msg='"'+ title+'"'+ " was added to Bookmarks";
                String toast_Remove_msg='"'+ title+'"'+ " was removed from Bookmarks";
                ImageButton card_bookmark_btn= (ImageButton) view.findViewById(R.id.bookmark_image);

                if(NewsData.getIsStoryBookmarked().equals("yes")){
                    bookmark_btn.setImageResource(R.drawable.bookmark_empty);
                    card_bookmark_btn.setImageResource(R.drawable.bookmark_empty);
                    NewsData.setIsStoryBookmarked("no");

                    Toast.makeText(getActivity(),toast_Remove_msg,Toast.LENGTH_SHORT).show();
                    SharedPreferences pref = getActivity().getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    if(pref.contains(weburl)){
                        editor.remove(weburl).commit();
                    }

                }
                else{
                    bookmark_btn.setImageResource(R.drawable.bookmark_fill);
                    card_bookmark_btn.setImageResource(R.drawable.bookmark_fill);
                    NewsData.setIsStoryBookmarked("yes");
                    Toast.makeText(getActivity(),toast_Add_msg,Toast.LENGTH_SHORT).show();

                    SharedPreferences pref = getActivity().getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    String json = gson.toJson(NewsData);
                    editor.putString(weburl, json);

                    editor.commit();
                }
            }
        });


        dialog.show();
    }



    @NonNull
    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle args) {
        System.out.println("World on create Loader called ");
        String builder="http://thisishw8.appspot.com/api/sports" ;
        return new NewsLoader(getActivity(), builder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsData>> loader, List<NewsData> NewsData) {
        int position;
        String detail_bookmarked;
        System.out.println("World on Load finished called ");
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i("tag", "This'll run 300 milliseconds later");
                    }
                },
                8000);



        progressBar.setVisibility(View.GONE);
        Progress_text.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake NewsData
        if(!SPORTS_Fragment_Loaded) {
            mAdapter.clear();
        }
        else{
            SharedPreferences pref = getActivity().getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
            if(pref.contains(BusinessNewsActivity.DETAIL_ARTICLE_POSITION)) {
                position = pref.getInt(BusinessNewsActivity.DETAIL_ARTICLE_POSITION,-1);


                System.out.println("world on else called");
                try{
                    NewsData tempnews = mAdapter.getItem(position);
                    if(pref.contains(BusinessNewsActivity.BOOKMARK_DETAIL_TAG)) {
                        detail_bookmarked = pref.getString(BusinessNewsActivity.BOOKMARK_DETAIL_TAG, null);

                        if (detail_bookmarked.equals("yes")) {
                            tempnews.setIsStoryBookmarked("yes");
                        } else {
                            tempnews.setIsStoryBookmarked("no");

                        }
                        if(NewsData.size()>position){
                            NewsData.set(position, tempnews);}
                    }}
                catch(Exception e){
                    System.out.println("world on else called error " + e);
                }

            }

            mAdapter.notifyNewsDataSetChanged();
        }

        if (NewsData != null && !NewsData.isEmpty()) {
            newsFromNewsLoader=NewsData;
            mAdapter.addAll(newsFromNewsLoader);
            SPORTS_Fragment_Loaded=true;


        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {

        if(!SPORTS_Fragment_Loaded) {
            mAdapter.clear();
        }
    }


}
