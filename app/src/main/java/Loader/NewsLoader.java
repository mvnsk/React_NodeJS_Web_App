package Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.myapplication.NewsData.NewsData;

import java.util.List;

import Utils.HomeUtils;
import Utils.QueryUtils;

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {
    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();
    private static boolean SEARCH_REQUEST=false;
    /** Query URL */
    private String mUrl;
    private boolean is_home_page;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url to load NewsData from
     */
    public NewsLoader(Context context, String url){
        super(context);
        mUrl = url;
        is_home_page=false;
    }
    public NewsLoader(Context context, String url, Boolean home_page){
        super(context);
        mUrl = url;
        is_home_page=home_page;
    }


    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading() called ...");
        System.out.println("onStartLoading() called");
        forceLoad();
    }

    @Override
    public List<NewsData> loadInBackground() {
        Log.i(LOG_TAG, "TEST: onloadInBackground() called ...");

        if (mUrl == null) {
            return null;
        }
        System.out.println("murl in newsloader is "+mUrl);
        // Perform the network request, parse the response, and extract a list of earthquakes.
        if(is_home_page) {
            List<NewsData> NewsData = HomeUtils.fetchNewsData(mUrl);
            return NewsData;
        }
        else{
            List<NewsData> NewsData = QueryUtils.fetchNewsData(mUrl);
            return NewsData;
        }
    }
}
