package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.content.Intent;
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
import com.example.alexfaber.sumanalarm.Models.Backend;
import com.example.alexfaber.sumanalarm.Models.Challenge;
import com.example.alexfaber.sumanalarm.Models.ChallengeRESTClient;
import com.example.alexfaber.sumanalarm.R;

import java.util.ArrayList;
import java.util.List;


public class Challenges extends ActionBarActivity implements View.OnClickListener{

    private ListView challengesListView;
    private ArrayAdapter<String> challengesArrayAdapter;
    private Context self;
    private final String TAG = "Challenges";


    private void updateChallengesListView(){
        ChallengeRESTClient.fetchAll("55358051ac10a93834970cc3", new Backend.BackendCallback(){
            @Override
            public void onRequestCompleted(Object result) {
                List<Challenge> challenges = (ArrayList)result;
                Log.v(TAG, challenges.toString());
                VolleyLog.v("updateChallengesListView: " + challenges.toString());
                String[] simpleChallenges = new String[challenges.size()];
                Challenge c;
                for(int i = 0; i < challenges.size(); i++){
                    c = challenges.get(i);
                    simpleChallenges[i] = c._id;
                }
                challengesArrayAdapter = new ArrayAdapter<>(self, android.R.layout.simple_list_item_1, simpleChallenges);
                challengesListView.setAdapter(challengesArrayAdapter);
                Log.v(TAG, simpleChallenges.toString());
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
        setContentView(R.layout.activity_challenges);
        self = this;

        //Wire up create challenge button
        Button confirmButton = (Button)findViewById(R.id.create_challenge);
        confirmButton.setOnClickListener(this);

        //Initialize list view
        this.challengesListView = (ListView)findViewById(R.id.challengesListView);
        this.challengesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Move to challenge activity
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
        switch (item.getItemId()) {
            case R.id.set_alarm_button:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.settings_button:
                intent = new Intent(this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.challenges_button:
                intent = new Intent(this, Challenges.class);
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
                Intent intent = new Intent(this, CreateChallenge.class);
                startActivity(intent);
            }
        }
    }
}
