package com.example.myapplication.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.NewsData.NewsData;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedBusinessNewsFragment###3newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedBusinessNewsFragment extends Fragment {
    public static final String NEWS_INFO = "com.example.myapplication.NewsData.NewsData";
    private NewsData NewsData;

    public DetailedBusinessNewsFragment(){
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detailed_business_news, container, false);
       // Toolbar myToolbar = (Toolbar) rootView.findViewById(R.id.detail_toolbar);
       // setSupportActionBar(myToolbar);

        //Receives the NewsData that was passed through the intent
        readDisplayedDetailedNews();

        // Updates title of news story
        TextView news_title_text_view = (TextView) rootView.findViewById(R.id.detail_title);

        // Updates image of news story
        ImageView news_image_view = (ImageView) rootView.findViewById(R.id.detail_image);

        // Updates section of news story
        TextView sectionView = (TextView) rootView.findViewById(R.id.detail_section);

        // Updates author of news story
       // TextView authorView = (TextView) rootView.findViewById(R.id.mauthorName);

        // Updates date of news story
       // TextView dateView = (TextView) rootView.findViewById(R.id.date);
        TextView timeView = (TextView) rootView.findViewById(R.id.time);
        TextView articleview=(TextView) rootView.findViewById(R.id.detail_link);
        // Updates body of news story
        TextView body_of_news_story = (TextView) rootView.findViewById(R.id.detail_body);
        //body_of_news_story.setMovementMethod(new ScrollingMovementMethod());
        //articleview.setMovementMethod(new ScrollingMovementMethod());

        displayDetailedNews(news_title_text_view, news_image_view, sectionView,articleview , body_of_news_story);

        return rootView;
    }

    private void displayDetailedNews(TextView news_title_text_view, ImageView news_image_view, TextView sectionView, TextView linkview, TextView body_of_news_story) {
        // Gets title of news
        news_title_text_view.setText(NewsData.getTitleOfStory());

        // Gets image of news
        Picasso.get().load(NewsData.getImageOfStoryResource()).into(news_image_view);

        // Gets section of news
        String mystring = NewsData.getSectionOfStory();
        sectionView.setText(mystring);
        body_of_news_story.setText(NewsData.getBodyOfStory());
        System.out.println("detail page body is "+ NewsData.getBodyOfStory());
        System.out.println("detail page image url is " + NewsData.getImageOfStoryResource());
        System.out.println("detail page web url is "+ NewsData.getUrlOfStory());

        // Gets author of news
     /*   List<String> author = NewsData.getReporterName();
        if(!author.isEmpty()){
            StringBuilder output = new StringBuilder();
            for(int i = 0; i<author.size();i++){
                String all_author_names = author.get(i);
                output.append(all_author_names);
                output.append(" & ");
            }
            output.deleteCharAt(output.length()-2);

            authorView.setText(output);
        }
        else{
            authorView.setVisibility(View.GONE);
        }*/

        // Gets body of news

        linkview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriOfNews = Uri.parse(NewsData.getUrlOfStory());

                Intent intent = new Intent(Intent.ACTION_VIEW, uriOfNews);

                startActivity(intent);
            }
        });
    }


    private void readDisplayedDetailedNews() {
       /* Intent intent = getActivity().getIntent();
        NewsData = intent.getParcelableExtra(NEWS_INFO);
        System.out.println("detail page web url within last method is is "+ NewsData.getUrlOfStory());*/
        SharedPreferences pref = getActivity().getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
        Gson gson = new Gson();
        String json = pref.getString(NEWS_INFO, "");
        NewsData= gson.fromJson(json, NewsData.class);
        System.out.println("detail page web url within last method is is "+ NewsData.getUrlOfStory());
    }
}
