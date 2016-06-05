package pv239.fi.muni.cz.brnoerasmusguide.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Faculty;

/**
 * Created by jakubfiser on 26/05/16.
 */
public class StorageManager {

    public static void saveFaculties(String key, List<Faculty> list, Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        sp.edit().putString(key, gson.toJson(list)).apply();
    }

    public static List<Faculty> loadFaculties(String key, Context context) {
        Gson gson = new Gson();
        String fac = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).getString(key, "default");
        if(!"default".equals(fac)) {
            return gson.fromJson(fac, new TypeToken<List<Faculty>>() {}.getType());
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveBuildings(String key, List<Building> list, Context context) {
        Gson gson = new Gson();
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        sp.edit().putString(key, gson.toJson(list)).apply();
    }

    public static List<Building> loadBuildings(String key, Context context) {
        Gson gson = new Gson();
        String fac = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).getString(key, "default");
        if(!"default".equals(fac)) {
            return gson.fromJson(fac, new TypeToken<List<Building>>() {}.getType());
        } else {
            return new ArrayList<>();
        }
    }
}
