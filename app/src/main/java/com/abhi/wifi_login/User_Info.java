package com.abhi.wifi_login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User_Info {

    private String id;
    private String pwd;

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getpwd() {
        return pwd;
    }

    public void setpwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean load_Cred(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checkBoxValue = sharedPreferences.getBoolean("CheckBox_Value", false);
        id = sharedPreferences.getString("saved_id", "");
        pwd = sharedPreferences.getString("saved_pwd", "");
        return checkBoxValue;
    }

    public void save_cred(Context context)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("CheckBox_Value", true);
        editor.putString("saved_id", id);
        editor.putString("saved_pwd", pwd);
        editor.apply();
    }

}
