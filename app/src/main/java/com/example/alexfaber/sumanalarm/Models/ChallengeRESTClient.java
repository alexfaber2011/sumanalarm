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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.MyVolley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexfaber on 6/6/6.
 * Faber sucks
 */
public class ChallengeRESTClient extends Application{
    private static final String TAG = "ChallengeRESTClient";
    private static final String SERVER_URL = "http://104.236.94.200:5555/";
//    private static final String SERVER_URL = "http://10.0.2.2:5000/";


    public static void create(String userId, ArrayList<String> userNames, String challengeName, Backend.BackendCallback callback){
        // Post params to be sent to the server
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("owner", userId);
            jsonParams.put("userNames", userNames);
            jsonParams.put("name", challengeName);
        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST, SERVER_URL + "challenges/",
                jsonParams,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback));
        queue.add(req);
    }

    /**
     * Returns an List of Challenge objects
     *
     * @param userId
     * @param callback
     */
    public static void fetchAll(String userId, Backend.BackendCallback callback){
        String getParams = String.format("?participantId=%1$s",userId);
        Log.v(TAG, "fetchAll called");
        RequestQueue queue = MyVolley.getRequestQueue();
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                SERVER_URL + "challenges/participant" + getParams,
                null,
                createMyReqArraySuccessListener(callback),
                createMyReqErrorListener(callback)
        );
        queue.add(req);
    }

    public static void updateAcceptance(String userId, String challengeId, boolean accept, Backend.BackendCallback callback){
        RequestQueue queue = MyVolley.getRequestQueue();
        Log.v("", SERVER_URL + "challenges/" + challengeId + "/accept/" + userId);
        // PUT params to be sent to the server
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("accept", accept);
        }catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.PUT,
                SERVER_URL + "challenges/" + challengeId + "/accept/" + userId,
                jsonParams,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)
        );
        queue.add(req);
    }

    private static Response.Listener<JSONObject> createMyReqSuccessListener(final Backend.BackendCallback callback){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v(TAG, "respone" + response.toString());
                VolleyLog.v(response.toString());
                callback.onRequestCompleted(response);
            }
        };
    }

    private static Response.Listener<JSONArray> createMyReqArraySuccessListener(final Backend.BackendCallback callback){
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                VolleyLog.v(response.toString());
                List<Challenge> challenges = new ArrayList<Challenge>();
                //iterate over each JSONObject
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = (JSONObject) response.get(i);
                        String JSONString = obj.toString();
                        Gson gson = new GsonBuilder().create();
                        challenges.add(gson.fromJson(JSONString, Challenge.class));
                    }catch(JSONException e){
                        e.printStackTrace();
                        Log.e(TAG, "Unable to extract JSON");
                    }

                }
                callback.onRequestCompleted(challenges);
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
