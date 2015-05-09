package com.example.alexfaber.sumanalarm.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexfaber on 4/20/15.
 */
public class Challenge {
    public boolean ended;
    public String _id, owner, userName, name, date;
    public ArrayList<Participant> participants;

    public Challenge(){
        _id = "";
        owner = "";
        userName = "";
        date = "";
        name = "";
        ended = false;
        participants = new ArrayList<Participant>();

    }

    public void addParticpant(Participant p){
        participants.add(p);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(("\n\t _id: " + _id));
        sb.append(("\n\t owner: " + owner));
        sb.append(("\n\t userName: " + userName));
        sb.append(("\n\t ended: " + ended));
        sb.append(("\n\t participants: "));
        for(Participant p : participants){
            sb.append("\n\t\t\t" + p.toString());
        }

        return sb.toString();
    }

}
