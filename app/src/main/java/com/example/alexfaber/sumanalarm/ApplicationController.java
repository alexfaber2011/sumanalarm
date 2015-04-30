package com.example.alexfaber.sumanalarm;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

/**
 * Created by alexfaber on 4/27/15.
 */
public class ApplicationController extends Application {
    public static final String TAG = "VolleyPatterns";
    public static final String USER_SHARED_PREFS = "USER_SHARED_PREFS";

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static ApplicationController sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize the singleton
        sInstance = this;
        init();


    }

    private void init(){
        MyVolley.init(this);
    }
}
