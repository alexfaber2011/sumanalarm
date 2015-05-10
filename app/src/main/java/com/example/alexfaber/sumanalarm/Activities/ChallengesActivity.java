package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyLog;
import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.ChallengesListViewAdapter;
import com.example.alexfaber.sumanalarm.Models.Backend;
import com.example.alexfaber.sumanalarm.Models.Challenge;
import com.example.alexfaber.sumanalarm.Models.ChallengeRESTClient;
import com.example.alexfaber.sumanalarm.Models.Participant;
import com.example.alexfaber.sumanalarm.Models.User;
import com.example.alexfaber.sumanalarm.R;

import java.util.ArrayList;
import java.util.List;


public class ChallengesActivity extends ActionBarActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private ListView challengesListView;
    private ArrayAdapter<Challenge> challengesArrayAdapter;
    private Context self;
    private final String TAG = "ChallengesActivity";
    private SharedPreferences userPrefs;
    private String userId;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<Challenge> challenges;
    private Challenge c;


    private void updateChallengesListView(){
        //Grab shared preferences; make sure they're logged in before fetching
        userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        userId = userPrefs.getString("_id", null);
        if(userId == null){
            Toast.makeText(self, "MUST BE LOGGED IN", Toast.LENGTH_LONG).show();
            return;
        }

        ChallengeRESTClient.fetchAll(userId, new Backend.BackendCallback(){
            @Override
            public void onRequestCompleted(Object result) {
                challenges = (ArrayList)result;
                String[] simpleChallenges = new String[challenges.size()];
                for(int i = 0; i < challenges.size(); i++){
                    c = challenges.get(i);
                    simpleChallenges[i] = c.name;
                }
//                challengesArrayAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, simpleChallenges);
                challengesArrayAdapter = new ChallengesListViewAdapter(self, challenges);
                challengesListView.setAdapter(challengesArrayAdapter);
                Log.v(TAG, simpleChallenges.toString());
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onRequestFailed(String errorCode) {
                swipeLayout.setRefreshing(false);
                switch(errorCode){
                    case "400":
                        Toast.makeText(self, errorCode + " : Bad Request Made to the Server", Toast.LENGTH_LONG).show();
                        break;
                    case "404":
                        Toast.makeText(self, errorCode + " : No Challenges Found", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_challenges);
        self = this;

        //Wire up swipe layout
        swipeLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        //Wire up create challenge button
        Button confirmButton = (Button)findViewById(R.id.create_challenge);
        confirmButton.setOnClickListener(this);

        //Initialize list view
        this.challengesListView = (ListView)findViewById(R.id.challengesListView);
        this.challengesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Move to challenge activity
                Intent intent = new Intent(self, ChallengeActivity.class);
                Challenge clickedChallenge = challenges.get(position);
                intent.putParcelableArrayListExtra("participants", clickedChallenge.participants);
                intent.putExtra("_id",      clickedChallenge._id);
                intent.putExtra("name",     clickedChallenge.name);
                intent.putExtra("owner",    clickedChallenge.owner);
                intent.putExtra("userName", clickedChallenge.userName);
                intent.putExtra("date",     clickedChallenge.date);
                intent.putExtra("ended",    clickedChallenge.ended);
                startActivity(intent);
            }
        });

        //Grab any challenges that are available
        updateChallengesListView();
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
                SharedPreferences userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
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
            case R.id.create_challenge: {
                Intent intent = new Intent(this, CreateChallengeActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRefresh(){
        super.onResume();
        swipeLayout.setRefreshing(true);
        updateChallengesListView();
    }

    @Override
    public void onStart(){
        super.onStart();
        swipeLayout.setRefreshing(true);
        updateChallengesListView();
    }
}
