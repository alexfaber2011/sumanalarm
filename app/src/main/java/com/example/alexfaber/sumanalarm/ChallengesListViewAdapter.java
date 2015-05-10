package com.example.alexfaber.sumanalarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexfaber.sumanalarm.Models.Challenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexfaber on 5/10/15.
 */
public class ChallengesListViewAdapter extends ArrayAdapter<Challenge> {
    private ArrayList<Challenge> challenges;
    private Challenge tempChallenge;

    public ChallengesListViewAdapter(Context context, ArrayList<Challenge> challenges){
        super(context, R.layout.list_view_challenge, R.id.challenge_name_text, challenges);
        this.challenges = challenges;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = super.getView(position, convertView, parent);
        ImageView icon = (ImageView)row.findViewById(R.id.challenge_status_image);
        TextView challengeNameTV = (TextView)row.findViewById(R.id.challenge_name_text);
        TextView ownerNameTv = (TextView)row.findViewById(R.id.challenge_owner_text);
        tempChallenge = challenges.get(position);
        challengeNameTV.setText(tempChallenge.name);
        ownerNameTv.setText(tempChallenge.owner);
        return(row);
    }

//    @Override
//    public int getCount(){
//        return 1;
//    }
}
