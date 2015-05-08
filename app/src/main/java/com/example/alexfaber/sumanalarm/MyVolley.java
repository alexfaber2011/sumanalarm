package com.example.alexfaber.sumanalarm;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by alexfaber on 4/27/15.
 */
public class MyVolley {
    private static RequestQueue mRequestQueue;
    private static final String TAG = "MyVolley";

    static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        Log.v(TAG, "Init");
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static void logNetworkResponse(VolleyError e){
        try {
            VolleyLog.e("networkResponseData.data: " + new String(e.networkResponse.data));
            VolleyLog.e("networkResponseData.statusCode: " + e.networkResponse.statusCode);
        }catch(NullPointerException ee){
            Log.e(TAG, "logNetworkResponse error: Unable to read error");
        }

    }

    public static int getErrorStatusCode(VolleyError e){
        try{
            return e.networkResponse.statusCode;
        }catch(NullPointerException ee){
            Log.e(TAG, "getErrorStatusCode error: Unable to read error");
            return 500;
        }
    }
}
