package com.brotherd.dmwappmvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dumingwei on 2017/9/3.
 */
public class User implements Parcelable {

    public long id;
    public String name;
    public String url;
    public String email;
    public String login;
    public String location;
    @SerializedName("avatar_url")
    public String avatarUrl;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        url = in.readString();
        email = in.readString();
        login = in.readString();
        location = in.readString();
        avatarUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public boolean hasEmail() {
        return email != null && !email.isEmpty();
    }

    public boolean hasLocation() {
        return location != null && !location.isEmpty();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(email);
        dest.writeString(login);
        dest.writeString(location);
        dest.writeString(avatarUrl);
    }
}
