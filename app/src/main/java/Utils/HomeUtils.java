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

public class HomeUtils {

    private HomeUtils() {

    }

    /** Creates fetch news NewsData which passes in the url and
     * Creates an http request, extracts NewsData from the json response and parses them
     */
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
            System.out.println(" testing json response is" + jsonResponse.toString());
        } catch (IOException e) {

            System.out.println("Error closing input stream " + e);
        }
        List<NewsData> news = extractFromJson(jsonResponse);

        // Return the {@link List<NewsData>}
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

            System.out.println("Error with creating URL " + e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
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
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setReadTimeout(10000);
            //urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            // If the request was successful (response code == 200),
            // then read input stream and parse the connection.
            // if (urlConnection.getResponseCode() == 200) {
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
            // } else {
            //    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            // }
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    /**
     * Return an {@link List<NewsData>} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
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

                baseJson=baseJson.getJSONObject("response");
            }
            JSONArray news_array = baseJson.getJSONArray("results");
            System.out.println("testing json response is" + newsJson);
            System.out.println("testing array is" + news_array.toString() );
            for (int i = 0; i < news_array.length(); i++) {
                JSONObject currentNews = news_array.getJSONObject(i);
                String bookmarked="no";
                String nameOfSection = currentNews.getString("sectionName");

                String title = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");
                String id=currentNews.getString("id");
                JSONObject fields=currentNews.getJSONObject("fields");
                String thumbnail=fields.getString("thumbnail");

                String body="";
                List<String> contributor = new ArrayList<>();
                NewsData news = new NewsData(title, thumbnail, url, date, nameOfSection, contributor, body, id ,bookmarked);
                newsArrayList.add(news);
                }

            }

         catch (JSONException e) {


        }

        return newsArrayList;
    }
}
