package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.NewsData.NewsData;
import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MainNewsAdapter extends ArrayAdapter<NewsData> {
    private static Context m_context;
    Gson gson = new Gson();
    public MainNewsAdapter(Context context, ArrayList<NewsData> pNewsData) {
        super(context, 0, pNewsData);
        m_context=context;
    }

  /*  SharedPreferences appSharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(m_context.getApplicationContext());
    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
    Gson gson = new Gson();*/
    /**
     *
     * @param position The position in the list of NewsData that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("World on adapter called");
        // Check if the existing view is being reused, otherwise inflate the view
        View newsListView = convertView;
        if (newsListView == null) {
            newsListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        // Get the NewsData object located at this position in the list
        final NewsData currentNews = getItem(position);

        TextView news_title_text_view = (TextView) newsListView.findViewById(R.id.titleOfStory_text_view);
        news_title_text_view.setText(currentNews.getTitleOfStory());


        ImageView news_image_view = (ImageView) newsListView.findViewById(R.id.imageOfStory_image_view);
        Picasso.get().load(currentNews.getImageOfStoryResource()).into(news_image_view);


        TextView sectionView = (TextView) newsListView.findViewById(R.id.section);
        String mystring = "|" + currentNews.getSectionOfStory();
        sectionView.setText(mystring);



        String originalDateTime = currentNews.getDateTimeOfStory();


        String time_woz =originalDateTime.substring(0, originalDateTime.length() - 1);
        Log.d("god","original time is" + time_woz.toString());
        LocalDateTime ldt=LocalDateTime.parse(time_woz);
        ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );


        ZonedDateTime LA_time = ldt.atZone( zoneId );
        Log.d("god", "la time is" + LA_time.toString());
        System.out.println("los angeles time is " + LA_time);
        LocalDateTime current_time = LocalDateTime.now();
        ZonedDateTime current_LA_time = current_time.atZone( zoneId );
        Log.d("god", "current la time is" + current_LA_time.toString());

        long diffInSeconds = Math.abs(java.time.Duration.between(LA_time,current_LA_time).getSeconds());
        Log.d("god", "seconds is" + diffInSeconds);
        long diffInMinutes =Math.abs( java.time.Duration.between(LA_time,current_LA_time).toMinutes());
        Log.d("god", "minutes is" + diffInMinutes);
        long diffInHours = Math.abs(java.time.Duration.between(LA_time,current_LA_time).toHours());
        Log.d("god", "hours is" + diffInHours);
        diffInMinutes=diffInMinutes%60;
        diffInSeconds=diffInSeconds%60;
        System.out.println("time difference is " + String.valueOf(diffInSeconds)+ " "+ String.valueOf(diffInMinutes) + " " + String.valueOf(diffInHours));
        TextView timeView = (TextView) newsListView.findViewById(R.id.time);
        String publish_time;
        if(diffInHours >24){
            int days=(int)diffInHours/24;
            publish_time=String.valueOf(days)+"d ago";
        }


       else if(diffInHours >0){
            publish_time=String.valueOf(diffInHours)+"h ago";
        }
        else if(diffInMinutes >0){
            publish_time= String.valueOf(diffInMinutes)+"m ago";
        }
        else{
            publish_time=String.valueOf(diffInSeconds)+"s ago";
        }


        timeView.setText(publish_time);
        final ImageButton btn=(ImageButton)newsListView.findViewById(R.id.bookmark_image);
        String bookmarked=currentNews.getIsStoryBookmarked();
        if(bookmarked=="yes"){
            btn.setImageResource(R.drawable.bookmark_fill);
            currentNews.setIsStoryBookmarked("yes");
        }
        else{
            btn.setImageResource(R.drawable.bookmark_empty);
            currentNews.setIsStoryBookmarked("no");
        }

        View.OnClickListener imgButtonHandler = new View.OnClickListener() {

            public void onClick(View v) {
                System.out.println("Figure out Main Called");

                String title=currentNews.getTitleOfStory();
                String weburl=currentNews.getUrlOfStory();
                String toast_Add_msg=title+ " was added to Bookmarks";
                String toast_Remove_msg=title+ " was removed from Bookmarks";
                SharedPreferences pref = m_context.getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                if(currentNews.getIsStoryBookmarked()=="no"){

                    btn.setImageResource(R.drawable.bookmark_fill);
                   currentNews.setIsStoryBookmarked("yes");
                    Toast.makeText(getContext(),toast_Add_msg,Toast.LENGTH_SHORT).show();
                 /*   String json = gson.toJson(currentNews);
                    prefsEditor.putString(weburl, json);
                    prefsEditor.commit();
                    SharedPreferences pref = m_context.getSharedPreferences("Bookmarked_articles", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();*/
                    String json = gson.toJson(currentNews);
                    editor.putString(weburl, json).commit();

                }
               else{
                    btn.setImageResource(R.drawable.bookmark_empty);

                    currentNews.setIsStoryBookmarked("no");
                    Toast.makeText(getContext(),toast_Remove_msg,Toast.LENGTH_SHORT).show();
                    if(pref.contains(weburl)){
                        editor.remove(weburl).commit();
                    }
               }

            }
        };
        btn.setOnClickListener(imgButtonHandler);
        return newsListView;

    }
}