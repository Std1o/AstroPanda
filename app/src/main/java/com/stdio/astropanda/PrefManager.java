package com.stdio.astropanda;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    private static final String PREF_NAME = "AstroPanda";

    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public String getName() {
        return pref.getString(KEY_NAME, "");
    }

    public void setName(String value) {
        editor.putString(KEY_NAME, value);
        editor.apply();
    }

    public String getAge() {
        return pref.getString(KEY_AGE, "");
    }

    public void setAge(String value) {
        editor.putString(KEY_AGE, value);
        editor.apply();
    }
}
