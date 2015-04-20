package com.example.alexfaber.sumanalarm.Models;

/**
 * Created by alexfaber on 4/20/15.
 */
public class Participant {
    public String _id;
    public String userName;
    public int score;
    public boolean accepted;

    public Participant(){
        _id = "";
        userName = "";
        score = 0;
        accepted = false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(("\n\t _id: " + _id));
        sb.append(("\n\t userName: " + userName));
        sb.append(("\n\t score: " + score));
        sb.append(("\n\t accepted: " + accepted));

        return sb.toString();
    }
}
