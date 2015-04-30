package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class UserLogin extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "UserLogin";
    private Context self;

    private void login(){
        //Grab the input
        EditText etUserName = (EditText)findViewById(R.id.login_user_name);
        EditText etPassword = (EditText)findViewById(R.id.login_password);
        String userName = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        UserRESTClient.login(userName, password, new Backend.BackendCallback() {
            @Override
            public void onRequestCompleted(Object result) {
                User user = (User)result;
                //Save their information to shared preferences (but lock it down)
                SharedPreferences userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
                user.commitPrefs(userPrefs);

                //Redirect them to main activity
                Intent intent = new Intent(self, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

            @Override
            public void onRequestFailed(String errorCode) {
                switch(errorCode){
                    case "400":
                        Toast.makeText(self, errorCode + " : Bad Request Made to the Server", Toast.LENGTH_LONG).show();
                        break;
                    case "404":
                        Toast.makeText(self, errorCode + " : No User Account Found", Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_user_login);
        self = this;

        //Wire up login button
        Button loginButton = (Button)findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        //Wire up send button on keyboard
        EditText et = (EditText)findViewById(R.id.login_password);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login();
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_login_sign_up, menu);
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
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.login_button: {
                login();
                break;
            }
        }
    }
}
