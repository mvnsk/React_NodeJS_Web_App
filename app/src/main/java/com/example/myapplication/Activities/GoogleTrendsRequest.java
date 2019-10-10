package com.example.myapplication.Activities;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GoogleTrendsRequest extends StringRequest {

    public GoogleTrendsRequest(int method, String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("searchword","2dd1adaf26d7473eb25242fa4ebd8b80");
        // headers.put("key2","value2");
        return headers;
    }
}
