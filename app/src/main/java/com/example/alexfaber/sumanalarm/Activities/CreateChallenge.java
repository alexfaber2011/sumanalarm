package com.example.alexfaber.sumanalarm.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.R;
import com.example.alexfaber.sumanalarm.UsernameListViewHelper;

public class CreateChallenge extends ActionBarActivity implements View.OnClickListener{

    private UsernameListViewHelper usernameListViewHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_challenge);
        this.usernameListViewHelper = new UsernameListViewHelper();

        Button confirmButton = (Button)findViewById(R.id.add_username_button);
        confirmButton.setOnClickListener(this);
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
                TextView tv = (TextView)findViewById(R.id.add_username_text);

                //Check to see if they've actually supplied a username
                if(tv.getText().toString().equals("")){
                    Toast.makeText(this, "Must supply a username", Toast.LENGTH_LONG).show();
                    return;
                }

                //Add username to array of usernames
                String username = tv.getText().toString();
                if(!usernameListViewHelper.addUsername(username)){
                    Toast.makeText(this, "Username (" + username + ") already added", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(this, "Successfully added " + username, Toast.LENGTH_LONG).show();

                //Clear textView
                tv.setText("");

                //Show the updated list

                }
            }
        }
    }
}
