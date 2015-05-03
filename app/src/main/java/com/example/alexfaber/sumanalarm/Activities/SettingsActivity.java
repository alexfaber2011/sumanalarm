package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.Models.Backend;
import com.example.alexfaber.sumanalarm.Models.User;
import com.example.alexfaber.sumanalarm.Models.UserRESTClient;
import com.example.alexfaber.sumanalarm.R;


public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {

    private Context self;
    private SharedPreferences userPrefs;
    private String userName, userId;
    private int maxSnoozes;
    private EditText userNameET, maxSnoozesEt;

    public void saveSettings(){
        //Check to see if they've actually supplied a username
        String updatedUserName = userNameET.getText().toString();
        String updatedMaxSnoozes = maxSnoozesEt.getText().toString();
        if(updatedUserName.equals("") || updatedMaxSnoozes.equals("")){
            Toast.makeText(this, "Must supply a username and snoozes", Toast.LENGTH_LONG).show();
            return;
        }
        UserRESTClient.updateSettings(userId, updatedUserName, updatedMaxSnoozes, new Backend.BackendCallback() {
            @Override
            public void onRequestCompleted(Object result) {
                User user = (User)result;
                user.commitPrefs(userPrefs);
                Toast.makeText(self, "Successfully updated User Account", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestFailed(String errorCode) {
                switch(errorCode){
                    case "400":
                        Toast.makeText(self, errorCode + " : Bad Request Made to the Server", Toast.LENGTH_LONG).show();
                        break;
                    case "404":
                        Toast.makeText(self, errorCode + " : No User Accounts Found", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(self, errorCode + " Error", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        self = this;

        //Wire up submit button
        Button confirmButton = (Button)findViewById(R.id.save_settings);
        confirmButton.setOnClickListener(this);

        //Get userName and make sure they're logged in at the same time
        userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        userName = userPrefs.getString("userName", null);
        maxSnoozes = userPrefs.getInt("snoozes", -1);
        userId = userPrefs.getString("_id", null);
        if(userName == null || maxSnoozes == -1 || userId == null){
            Intent intent = new Intent(this, UserLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return;
        }

        //Update the editText box with number of snoozes and id
        userNameET = (EditText)findViewById(R.id.settings_user_name);
        maxSnoozesEt = (EditText)findViewById(R.id.settings_max_snoozes);
        userNameET.setText(userName, TextView.BufferType.EDITABLE);
        maxSnoozesEt.setText(Integer.toString(maxSnoozes), TextView.BufferType.EDITABLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.set_alarm_button:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.settings_button:
                intent = new Intent(this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.create_challenge_action_button:
                intent = new Intent(this, CreateChallengeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.challenges_button:
                intent = new Intent(this, ChallengesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_settings: {
                saveSettings();
                break;
            }
        }
    }
}
