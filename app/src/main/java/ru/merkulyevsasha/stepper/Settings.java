package ru.merkulyevsasha.stepper;

import android.content.Context;
import android.content.SharedPreferences;


public class Settings {

    public static String PREF_SETTINGS = "settings";
    public static String KEY_STEPS = "steps";

    private SharedPreferences mPref;

    public Settings(Context context){
        mPref = context.getSharedPreferences(PREF_SETTINGS, Context.MODE_PRIVATE);
    }

    public int getSteps(){
        return mPref.getInt(KEY_STEPS, 0);
    }

    public void saveSteps(int steps){
        mPref.edit().putInt(KEY_STEPS, steps).apply();
    }
}
