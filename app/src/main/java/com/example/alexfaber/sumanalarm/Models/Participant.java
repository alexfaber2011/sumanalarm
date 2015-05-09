package com.example.alexfaber.sumanalarm.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alexfaber on 4/20/15.
 */
public class Participant implements Parcelable {
    public String _id;
    public String userName;
    public double score;
    public boolean accepted;
    //TODO add a alarm time field

    public Participant(){
        _id = "";
        userName = "";
        score = 0.0;
        accepted = false;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

//        sb.append(("\n\t _id: " + _id));
        sb.append(("\n\t userName: " + userName));
        sb.append(("\n\t score: " + score));
        sb.append(("\n\t accepted: " + accepted));

        return sb.toString();
    }

    protected Participant(Parcel in) {
        _id = in.readString();
        userName = in.readString();
        score = in.readDouble();
        accepted = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(userName);
        dest.writeDouble(score);
        dest.writeByte((byte) (accepted ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };
}