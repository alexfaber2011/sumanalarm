package com.example.alexfaber.sumanalarm;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexfaber.sumanalarm.Models.Challenge;
import com.example.alexfaber.sumanalarm.Models.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexfaber on 5/10/15.
 */
public class ChallengesListViewAdapter extends ArrayAdapter<Challenge> {
    private ArrayList<Challenge> challenges;
    private Challenge tempChallenge;
    private String userId;
    private TextView challengeNameTV, ownerNameTV, dateTV;

    private boolean userHasAcceptedChallenge(ArrayList<Participant> participants){
        boolean hasAccepted = false;
        for(Participant p : participants){
            if(p._id.equals(userId)){
                hasAccepted = p.accepted;
            }
        }
        return hasAccepted;
    }

    public ChallengesListViewAdapter(Context context, String userId, ArrayList<Challenge> challenges){
        super(context, R.layout.list_view_challenge, R.id.challenge_name_text, challenges);
        this.challenges = challenges;
        this.userId = userId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = super.getView(position, convertView, parent);
        ImageView icon = (ImageView)row.findViewById(R.id.challenge_status_image);
        challengeNameTV = (TextView)row.findViewById(R.id.challenge_name_text);
        ownerNameTV = (TextView)row.findViewById(R.id.challenge_owner_text);
        dateTV = (TextView)row.findViewById(R.id.challenge_date_text);
        tempChallenge = challenges.get(position);
        if(tempChallenge.ended){
            icon.setImageResource(R.drawable.ic_action_stop);
        }else if(userHasAcceptedChallenge(tempChallenge.participants) && !tempChallenge.ended){
            icon.setImageResource(R.drawable.ic_action_accept);
        }else{
            icon.setImageResource(R.drawable.ic_action_deny);
        }
        challengeNameTV.setText(tempChallenge.name);
        ownerNameTV.setText(tempChallenge.userName);
        dateTV.setText(tempChallenge.date.substring(0,10));
        return(row);
    }
}
