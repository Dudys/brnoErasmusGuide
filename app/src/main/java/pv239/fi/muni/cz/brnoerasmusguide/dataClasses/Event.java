package pv239.fi.muni.cz.brnoerasmusguide.dataClasses;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jakubfiser on 08/05/16.
 */
public class Event {

    public String id;
    public String name;
    public String description;
    public String image;
    public String place;
    public String startTime;

    public Event(JSONObject json) {
        Log.d("Event", "json: " + json.toString());
        try {
            id = json.getString("id");
            description = json.getString("description");
            name = json.getString("name");
            place = json.getJSONObject("place").getString("name");
            startTime = json.getString("start_time");
            image = null;
        } catch(JSONException e) {
            Log.d("Event", "exception: " + e.getLocalizedMessage());
        }
    }
}
