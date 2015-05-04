package com.example.alexfaber.sumanalarm.Models;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.alexfaber.sumanalarm.MyVolley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexfaber on 4/26/15.
 */
public class UserRESTClient extends Application{
    private static final String TAG = "UserRESTClient";
    private static final String SERVER_URL = "http://104.236.94.200:5555/";
//    private static final String SERVER_URL = "http://10.0.2.2:5000/";


    public static void login(String userName, String password, Backend.BackendCallback callback){
        String getParams = String.format("?userName=%1$s&password=%2$s", userName, password);
        Log.v(TAG, "login called");
        RequestQueue queue = MyVolley.getRequestQueue();
        JsonArrayRequest req = new JsonArrayRequest(
                Request.Method.GET,
                SERVER_URL + "users/" + getParams,
                null,
                createMyReqArraySuccessListener(callback),
                createMyReqErrorListener(callback)
        );
        queue.add(req);
    }

    public static void signup(String firstName, String lastName, String userName, String password, Backend.BackendCallback callback){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("firstName", firstName);
            jsonParams.put("lastName", lastName);
            jsonParams.put("userName", userName);
            jsonParams.put("password", password);
        }catch(JSONException e){
            e.printStackTrace();
        }

        Log.v(TAG, "signup called");
        RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST, SERVER_URL + "users/",
                jsonParams,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback));
        queue.add(req);
    }

    public static void fetchSettings(String userId, Backend.BackendCallback callback){
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("userName", userId);
        } catch(JSONException e){
            e.printStackTrace();
        }

        String getParams = String.format("?_id=%1$s", userId);
        RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, SERVER_URL + "users/" + getParams,
                jsonParams,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)
        );
        queue.add(req);
    }

    public static void updateSettings(String userId, String userName, String snoozes, Backend.BackendCallback callback){
        JSONObject jsonParams = new JSONObject();
        try{
            jsonParams.put("userName", userName);
            jsonParams.put("snoozes", snoozes);
        }catch(JSONException e){
            e.printStackTrace();
        }

        RequestQueue queue = MyVolley.getRequestQueue();
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.PUT, SERVER_URL + "users/" + userId,
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
                VolleyLog.v(response.toString());
                String JSONString = response.toString();
                Gson gson = new GsonBuilder().create();
                User u = gson.fromJson(JSONString, User.class);
                callback.onRequestCompleted(u);
            }
        };
    }

    private static Response.Listener<JSONArray> createMyReqArraySuccessListener(final Backend.BackendCallback callback){
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                VolleyLog.v(response.toString());
                User u = null;
                //Should only have one element (silly, but that's the way the backend is setup atm)
                try {
                    JSONObject obj = (JSONObject) response.get(0);
                    String JSONString = obj.toString();
                    Gson gson = new GsonBuilder().create();
                    u = (gson.fromJson(JSONString, User.class));
                }catch(JSONException e){
                    e.printStackTrace();
                    Log.e(TAG, "Unable to extract JSON");
                }
                callback.onRequestCompleted(u);
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
