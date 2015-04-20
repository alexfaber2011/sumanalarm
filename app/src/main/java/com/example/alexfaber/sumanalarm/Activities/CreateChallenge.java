package com.example.alexfaber.sumanalarm.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.R;
import com.example.alexfaber.sumanalarm.UsernameListViewHelper;

public class CreateChallenge extends ActionBarActivity implements View.OnClickListener{
    //Instance variables
    private UsernameListViewHelper usernameListViewHelper;
    private String TAG = "CreateChallenge";

    //Private methods
    private void respondToAddedUsername(){
        EditText et = (EditText)findViewById(R.id.add_username_text);

        //Check to see if they've actually supplied a username
        if(et.getText().toString().equals("")){
            Toast.makeText(this, "Must supply a username", Toast.LENGTH_LONG).show();
            return;
        }

        //Add username to array of usernames
        String username = et.getText().toString();
        if(!usernameListViewHelper.addUsername(username)){
            Toast.makeText(this, "Username (" + username + ") already added", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Successfully added " + username, Toast.LENGTH_LONG).show();

        //Clear textView
        et.setText("");

        //Show the updated list
        Log.v(TAG, usernameListViewHelper.getUsernames().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        this.usernameListViewHelper = new UsernameListViewHelper();

        //Wire up submit button
        Button confirmButton = (Button)findViewById(R.id.add_username_button);
        confirmButton.setOnClickListener(this);

        //Wire up send button on keyboard
        EditText et = (EditText)findViewById(R.id.add_username_text);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    respondToAddedUsername();
                    handled = true;
                }
                return handled;
            }
        });
        //
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
                respondToAddedUsername();
                break;
            }
        }
    }
}
