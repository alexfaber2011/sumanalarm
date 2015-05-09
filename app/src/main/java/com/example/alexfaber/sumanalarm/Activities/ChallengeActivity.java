package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.Models.Backend;
import com.example.alexfaber.sumanalarm.Models.Challenge;
import com.example.alexfaber.sumanalarm.Models.ChallengeRESTClient;
import com.example.alexfaber.sumanalarm.Models.Participant;
import com.example.alexfaber.sumanalarm.Models.User;
import com.example.alexfaber.sumanalarm.R;

import java.util.ArrayList;

public class ChallengeActivity extends ActionBarActivity implements View.OnClickListener{
    private String name, _id, owner, userName, date, loggedInUserId;
    private ArrayList<Participant> participants;
    private Participant[] participantArray;
    private TextView nameTV, userNameTv, dateTV, endedTv;
    private ListView participantLV;
    private ArrayAdapter participantArrayAdapter;
    private Context self;
    private Button acceptDenyEndButton;
    private SharedPreferences userPrefs;
    private boolean userOwnsChallenge, ended;
    private Challenge updatedChallenge;

    /**
     * May not be necessary, but was having sending the array list to the adapter via `toString()`
     *
     * @param participants
     * @return
     */
    private Participant[] buildParticipantArray(ArrayList<Participant> participants){
        Participant[] pArray = new Participant[participants.size()];
        for(int i = 0; i < participants.size(); i++){
            pArray[i] = participants.get(i);
        }
        return pArray;
    }

    private boolean userHasAcceptedChallenge(ArrayList<Participant> participants){
        boolean hasAccepted = false;
        for(Participant p : participants){
            if(p._id.equals(loggedInUserId)){
                hasAccepted = p.accepted;
            }
        }
        return hasAccepted;
    }

    private void toggleAcceptOrDenyButton(){
        //Check to see if the user is one of the participants, and allow display accept or deny
        acceptDenyEndButton = (Button) findViewById(R.id.challenge_accept_deny_end_button);
        if(userHasAcceptedChallenge(participants)){
            acceptDenyEndButton.setText("Deny");
        }else{
            acceptDenyEndButton.setText("Accept");
        }
    }

    private void acceptOrDeny(){
        ChallengeRESTClient.updateAcceptance(loggedInUserId, _id, !userHasAcceptedChallenge(participants), new Backend.BackendCallback() {
            @Override
            public void onRequestCompleted(Object result) {
                updatedChallenge = (Challenge)result;
                //Update the listView
                participantLV = (ListView) findViewById(R.id.challenge_participants);
                participants = updatedChallenge.participants;
                participantArrayAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, buildParticipantArray(participants));
                participantLV.setAdapter(participantArrayAdapter);
                //Toggle the accept or deny button
                toggleAcceptOrDenyButton();
            }

            @Override
            public void onRequestFailed(String errorCode) {
                switch(errorCode){
                    case "400":
                        Toast.makeText(self, errorCode + " : Bad Request Made to the Server", Toast.LENGTH_LONG).show();
                        break;
                    case "404":
                        Toast.makeText(self, errorCode + " : No Challenges or userId Found", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(self, errorCode + " Error", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    private void endChallenge(){
        ChallengeRESTClient.endChallenge(loggedInUserId, _id, new Backend.BackendCallback(){
            @Override
            public void onRequestCompleted(Object result) {
                //Toggle the accept or deny button
                updatedChallenge = (Challenge)result;
                endedTv.setText((updatedChallenge.ended) ? "Ended" : "Active");
                if(updatedChallenge.ended){
                    acceptDenyEndButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRequestFailed(String errorCode) {
                switch(errorCode){
                    //TODO Handle the servers error code when user tries to end a challenge that's not theirs.  (shouldn't happen, but it would be nice)
                    case "400":
                        Toast.makeText(self, errorCode + " : Bad Request Made to the Server", Toast.LENGTH_LONG).show();
                        break;
                    case "404":
                        Toast.makeText(self, errorCode + " : No Challenges or userId Found", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_challenge);

        self = this;

        //Grab some userPrefs
        userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        loggedInUserId = userPrefs.getString("_id", null);
        if (loggedInUserId == null) {
            Intent loginIntent = new Intent(this, UserLoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(loginIntent);
            return;
        }

        userOwnsChallenge = false;

        //Populate some stuff from the intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        _id = intent.getStringExtra("_id");
        owner = intent.getStringExtra("owner");
        userName = intent.getStringExtra("userName");
        date = intent.getStringExtra("date");
        participants = intent.getParcelableArrayListExtra("participants");
        ended = intent.getBooleanExtra("ended", false);

        //update text views
        nameTV = (TextView) findViewById(R.id.challenge_name);
        userNameTv = (TextView) findViewById(R.id.challenge_user_name);
        dateTV = (TextView) findViewById(R.id.challenge_date);
        nameTV.setText(name);
        userNameTv.setText("Created By: " + userName);
        dateTV.setText(date.substring(0, 10));
        endedTv = (TextView) findViewById(R.id.challenge_ended);
        endedTv.setText((ended) ? "Ended" : "Active");


        //Update the listView
        participantLV = (ListView) findViewById(R.id.challenge_participants);
        participantArrayAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, buildParticipantArray(participants));
        participantLV.setAdapter(participantArrayAdapter);

        //Wire up accept/deny/delete button
        acceptDenyEndButton = (Button) findViewById(R.id.challenge_accept_deny_end_button);
        acceptDenyEndButton.setOnClickListener(this);

        //Check to see if the user owns this challenge
        if(userPrefs.getString("_id", null).equals(owner)){
            //make the button an end button
            if(ended){
                acceptDenyEndButton.setVisibility(View.GONE);
            }else{
                acceptDenyEndButton = (Button) findViewById(R.id.challenge_accept_deny_end_button);
                acceptDenyEndButton.setText("END");
                userOwnsChallenge = true;
            }
        }else {
            //Set accept or deny appropriately
            toggleAcceptOrDenyButton();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            case R.id.set_alarm_button:
                intent = new Intent(this, MainActivity.class);
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
            case R.id.logout_button:
                User.logout(userPrefs);
                intent = new Intent(this, UserLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.challenge_accept_deny_end_button: {
                if(userOwnsChallenge){
                    endChallenge();
                }else {
                    acceptOrDeny();
                }
            }
        }
    }
}
