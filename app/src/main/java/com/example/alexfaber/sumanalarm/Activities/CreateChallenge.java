package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.Models.Backend;
import com.example.alexfaber.sumanalarm.Models.ChallengeRESTClient;
import com.example.alexfaber.sumanalarm.R;
import com.example.alexfaber.sumanalarm.UserNameListViewHelper;

public class CreateChallenge extends ActionBarActivity implements View.OnClickListener{
    //Instance variables
    private UserNameListViewHelper userNameListViewHelper;
    private String TAG = "CreateChallenge";
    private ListView userNameListView;
    private ArrayAdapter<String> userNamesAdapter;
    private Context self;

    //Private methods

    private void updateListView(){
        this.userNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userNameListViewHelper.getUserNames());
        this.userNameListView.setAdapter(userNamesAdapter);
    }

    private void submitCreatedChallenge(){
        EditText et = (EditText)findViewById(R.id.add_username_text);

        //Check to see if they've actually supplied a username
        if(et.getText().toString().equals("")){
            Toast.makeText(this, "Must supply a username", Toast.LENGTH_LONG).show();
            return;
        }

        //Add username to array of usernames
        String username = et.getText().toString();
        if(!userNameListViewHelper.addUserName(username)){
            Toast.makeText(this, "Username (" + username + ") already added", Toast.LENGTH_LONG).show();
            return;
        }

        Log.v(TAG, et.getText().toString());

        //Clear textView
        et.setText("");

        //Show the updated list
        Log.v(TAG, userNameListViewHelper.getUserNames().toString());
        updateListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        this.userNameListViewHelper = new UserNameListViewHelper();
        self = this;

        //Wire up submit button
        Button confirmButton = (Button)findViewById(R.id.add_username_button);
        confirmButton.setOnClickListener(this);
        Button createChallengeButton = (Button)findViewById(R.id.create_challenge_button);
        createChallengeButton.setOnClickListener(this);

        //Wire up send button on keyboard
        EditText et = (EditText)findViewById(R.id.add_username_text);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                submitCreatedChallenge();
                return true;
            }
        });

        //Initialize listView
        this.userNameListView = (ListView)findViewById(R.id.userNameListView);
        this.userNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userNameListViewHelper.removeUserNameByIndex(position);
                updateListView();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_challenge, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_username_button: {
                submitCreatedChallenge();
                break;
            }
            case R.id.create_challenge_button: {
                ChallengeRESTClient.create("55358051ac10a93834970cc3", userNameListViewHelper.getUserNames(), new Backend.BackendCallback() {
                    @Override
                    public void onRequestCompleted(Object result) {
                        Toast.makeText(self, "Successfully Submitted to Server!", Toast.LENGTH_LONG).show();
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
        }
    }
}
