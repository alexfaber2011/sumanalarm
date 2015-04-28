package com.example.alexfaber.sumanalarm.Models;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.MyVolley;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexfaber on 4/26/15.
 */
public class ChallengeRESTClient extends Application{
    private static final String TAG = "ChallengeRESTClient";
//    private static final String SERVER_URL = "http://104.236.94.200:5555/";
    private static final String SERVER_URL = "http://10.0.2.2:5000/";


    public static void create(String userId, ArrayList<String> userNames, Backend.BackendCallback callback){
        // Post params to be sent to the server
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("owner", userId);

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("owner", userId);
            jsonParams.put("userNames", userNames);
        }catch(JSONException e){
            e.printStackTrace();
        }

//        HashMap<String, String[]> arrayParams = new HashMap<String, String[]>();
//        arrayParams.put("userNames", userNames);

        Log.v(TAG, "CREATE 2 Called");
        RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST, SERVER_URL + "challenges/",
                jsonParams,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback));
        queue.add(req);
    }

    private static Response.Listener<JSONObject> createMyReqSuccessListener(final Backend.BackendCallback callback){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.v(response.toString());
                callback.onRequestCompleted(response);
            }
        };
    }

    private static Response.ErrorListener createMyReqErrorListener(final Backend.BackendCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyVolley.logNetworkResponse(error);
                callback.onRequestFailed(Integer.toString(MyVolley.getErrorStatusCode(error)));
            }
        };
    }

}
