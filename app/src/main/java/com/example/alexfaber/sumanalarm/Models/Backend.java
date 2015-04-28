package com.example.alexfaber.sumanalarm.Models;

/**
 * Created by alexfaber on 4/28/15.
 */
public class Backend {
    public interface BackendCallback {
        public void onRequestCompleted(Object result);
        public void onRequestFailed(String message);
    }
}
