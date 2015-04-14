package com.example.alexfaber.sumanalarm;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by alexfaber on 4/14/15.
 */
public class UsernameListViewHelper {
    private ArrayList<String> usernames;
    private String TAG = "UsernameListViewHelper";

    public UsernameListViewHelper(){
        //Constructor, nothing to do here at the moment.
        this.usernames = new ArrayList<String>();
    }

    /*
    * Will add the name, unless it exists already
    * return: true if added, false if the username already exists.
    * */
    public boolean addUsername(String username){
        if(!this.usernames.contains(username)){
            this.usernames.add(username);
            Log.v(this.TAG, "Usernames: " + this.usernames.toString());
            return true;
        }
        Log.v(this.TAG, "Failed to add username: " + username);
        return false;
    }

    public boolean removeUsername(String username){
        int index = this.usernames.indexOf(username);

        if(index != -1){
            this.usernames.remove(index);
            return true;
        }
        return false;
    }

    public ArrayList<String> getUsernames(){
        return this.usernames;
    }
}
