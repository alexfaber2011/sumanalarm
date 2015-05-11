package com.example.alexfaber.sumanalarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexfaber.sumanalarm.Models.Participant;

import java.util.ArrayList;

/**
 * Created by alexfaber on 5/10/15.
 */
public class ParticipantListViewAdapter extends ArrayAdapter<Participant> {
    private ArrayList<Participant> participants;
    private Participant tempParticipant;
    private TextView participantUserNameTV, scoreTV;

    public ParticipantListViewAdapter(Context context, ArrayList<Participant> participants){
        super(context, R.layout.list_view_participant, R.id.participant_name_text, participants);
        this.participants = participants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = super.getView(position, convertView, parent);
        ImageView icon = (ImageView)row.findViewById(R.id.participant_status_image);
        participantUserNameTV = (TextView)row.findViewById(R.id.participant_name_text);
        scoreTV = (TextView)row.findViewById(R.id.particpant_score_text);
        tempParticipant = participants.get(position);
        if(tempParticipant.accepted){
            icon.setImageResource(R.drawable.ic_action_accept);
        }else {
            icon.setImageResource(R.drawable.ic_action_deny);
        }
        participantUserNameTV.setText(tempParticipant.userName);
        scoreTV.setText("Score: " + String.valueOf(Math.ceil(tempParticipant.score)));
        return(row);
    }
}
