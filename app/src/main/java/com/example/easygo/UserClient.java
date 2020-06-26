package com.example.easygo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.easygo.Models.UserModel;


public class UserClient extends Application {

    //to solve multidex problem
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private UserModel user = null;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

}
