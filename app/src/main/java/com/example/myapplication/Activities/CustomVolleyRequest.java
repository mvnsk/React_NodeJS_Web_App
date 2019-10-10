package com.example.myapplication.Activities;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomVolleyRequest extends StringRequest {

        public CustomVolleyRequest(int method, String url,
                                   Response.Listener<String> listener,
                                   Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<>();
            headers.put("Ocp-Apim-Subscription-Key","9eff42cab26d4b45b90635390b3e4a7b");
           // headers.put("key2","value2");
            return headers;
        }
    }//2dd1adaf26d7473eb25242fa4ebd8b80

