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
    private static final String TAG = "ConnectionManager";
    private static final String SERVER_URL = "http://manflowyoga.herokuapp.com/";

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

}
