package com.zyt.kineticlock.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;


    public SharedPreferencesHelper(Context context,String name){
        this.mContext=context;
        sharedPreferences=context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void putValue(String key,Object value){
        if(value instanceof String )
        {
            editor.putString(key,String.valueOf(value));
        }
        else if(value instanceof Integer)
        {
            editor.putInt(key,(Integer)value);
        }
        else if(value instanceof Float)
        {
            editor.putFloat(key,(Float)value);
        }
        else if(value instanceof Long)
        {
            editor.putLong(key,(Long)value);
        }
        else if(value instanceof Boolean)
        {
            editor.putBoolean(key,(Boolean)value);
        }
        editor.apply();
    }

    public Object getValue(String key,Object defaultValue){
        if(defaultValue instanceof String)
        {
            return sharedPreferences.getString(key,String.valueOf(defaultValue));
        }
         else if(defaultValue instanceof Integer)
        {
            return sharedPreferences.getInt(key,(Integer)defaultValue);
        }
        else if(defaultValue instanceof Float)
        {
            return sharedPreferences.getFloat(key,(Float) defaultValue);
        }
        else if(defaultValue instanceof Long)
        {
            return sharedPreferences.getLong(key,(Long) defaultValue);
        }
        else if(defaultValue instanceof Boolean)
        {
            return sharedPreferences.getBoolean(key,(Boolean) defaultValue);
        }
        else
        {
            return defaultValue;
        }
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }

    public void remove(String key){
        editor.remove(key);
        editor.apply();
    }

}
