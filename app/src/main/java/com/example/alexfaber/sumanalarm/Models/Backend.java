package com.example.alexfaber.sumanalarm.Models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.callumtaylor.asynchttp.AsyncHttpClient;
import net.callumtaylor.asynchttp.response.JsonResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Backend {
    private static final String TAG = "Backend";
    private static final String SERVER_URL = "http://104.236.94.200:5555/";

    public interface BackendCallback {
        public void onRequestCompleted(Object result);
        public void onRequestFailed(String message);
    }

    private static String handleFailure(JsonElement response) {
        String errorMessage = "unknown server error";

        if (response == null)
            return errorMessage;

        JsonObject result = response.getAsJsonObject();

        //Server will return all error messages (except in the case of a crash) as a single level JSON
        //with one key called "message". This is a convention for this server.
        try {
            errorMessage = result.get("message").toString();
        }
        catch (Exception e) {
            Log.d(TAG, "Unable to parse server error message");
        }

        return errorMessage;
    }

    public static void createChallenge(String userId, String[] userNames, final BackendCallback callback){
        AsyncHttpClient client = new AsyncHttpClient(SERVER_URL);
        StringEntity jsonParams = null;

        try{
            JSONObject json = new JSONObject();
            json.put("owner", userId);
            json.put("userNames", userNames);
            jsonParams = new StringEntity(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }

        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "application/json"));

        client.post("challenges/", jsonParams, headers, new JsonResponseHandler() {
            @Override
            public void onSuccess() {
                JsonObject result = getContent().getAsJsonObject();

                Log.v(TAG, result.toString());

                //Start creating some challenge shit.
                Gson gson = new GsonBuilder().create();
                Challenge challenge = gson.fromJson(result, Challenge.class);

                Log.v(TAG, challenge.toString());
                callback.onRequestCompleted(challenge);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error){
                Log.v(TAG, "statusCode: " + statusCode);
                Log.v(TAG, "headers: " + headers.toString());
                Log.v(TAG, "statusCode: " + responseBody.toString());
                Log.v(TAG, "statusCode: " + error.toString());
            }
        });
    }

}
