package Utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.NewsData.NewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private QueryUtils() {

    }


    public static List<NewsData> fetchNewsData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            System.out.println("Error closing input stream " + e);
        }
        List<NewsData> news = extractFromJson(jsonResponse);

        // Return the {@link List<NewsData>}
        return news;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            System.out.println("Error with creating URL " + e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(streamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsData> extractFromJson(String newsJson) {

        List<NewsData> newsArrayList = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJson)) {
            return null;
        }

        try {
            JSONObject baseJson = new JSONObject(newsJson);
            if (baseJson.has("response")) {
                //Checking address Key Present or not
               System.out.println("basejson inside if condition");
                baseJson=baseJson.getJSONObject("response");

                 // Present Key
            }

            //JSONArray news_array = baseJson.getJSONObject("response").getJSONArray("results");
            JSONArray news_array = baseJson.getJSONArray("results");

            for (int i = 0; i < news_array.length(); i++) {

                try{
                JSONObject currentNews = news_array.getJSONObject(i);
                String bookmarked = "no";
                // Extract out the name of section, title of the story, the date and url values
                String nameOfSection = currentNews.getString("sectionName");
                System.out.println("testing section  is " + nameOfSection);
                String title = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");
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
                    newsArrayList.add(news);
                    if(newsArrayList.size()>=10){
                        break;
                    }
                }
            }
                catch(Exception e){
                    continue;
                }
            }

        } catch (JSONException e) {


        }

        return newsArrayList;
    }
}