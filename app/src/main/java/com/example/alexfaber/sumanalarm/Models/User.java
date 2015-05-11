package com.example.alexfaber.sumanalarm.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.alexfaber.sumanalarm.ApplicationController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexfaber on 4/20/15.
 */
public class User {
    public String _id;
    public String firstName;
    public String lastName;
    public String password;
    public String userName;
    public int snoozes;

    public User(){
        _id = "";
        firstName = "";
        lastName = "";
        userName = "";
        password = "";
        snoozes = 3;
    }

    public void commitPrefs(SharedPreferences userPrefs){
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putString("userName", userName);
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("password", password);
        editor.putInt("snoozes", snoozes);
        editor.putInt("remainingSnoozes", snoozes);
        editor.putString("_id", _id);
        editor.commit();
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(("\n\t _id: " + _id));
        sb.append(("\n\t firstName: " + firstName));
        sb.append(("\n\t firstName: " + lastName));
        sb.append(("\n\t userName: " + userName));
        sb.append(("\n\t password: " + password));
        sb.append(("\n\t snoozes: " + snoozes));

        return sb.toString();
    }

    public static void logout(SharedPreferences userPrefs){
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.remove("userName");
        editor.remove("firstName");
        editor.remove("lastName");
        editor.remove("password");
        editor.remove("snoozes");
        editor.remove("_id");
        editor.commit();
    }
}
