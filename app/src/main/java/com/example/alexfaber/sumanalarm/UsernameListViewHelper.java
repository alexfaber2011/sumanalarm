package com.example.alexfaber.sumanalarm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alexfaber on 4/14/15.
 */
public class UsernameListViewHelper {
    private ArrayList<String> userNames;
    private String TAG = "UserNameListViewHelper";

    public UsernameListViewHelper(){
        //Constructor, nothing to do here at the moment.
        this.userNames = new ArrayList<String>();
    }

    /*
    * Will add the name, unless it exists already
    * return: true if added, false if the username already exists.
    * */
    public boolean addUserName(String username){
        if(!this.userNames.contains(username)){
            this.userNames.add(username);
            Log.v(this.TAG, "Usernames: " + this.userNames.toString());
            return true;
        }
        Log.v(this.TAG, "Failed to add username: " + username);
        return false;
    }

    public boolean removeUserName(String username){
        int index = this.userNames.indexOf(username);

        if(index != -1){
            this.userNames.remove(index);
            return true;
        }
        return false;
    }

    public void removeUserNameByIndex(int index){
        this.userNames.remove(index);
    }

    public ArrayList<String> getUserNames(){
//        String[] stockArr = new String[this.userNames.size()];
//        return this.userNames.toArray(stockArr);
        return userNames;
    }

    public String buildUserNamesString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(String userName : this.userNames){
            sb.append("\"" + userName + "\",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }
}
