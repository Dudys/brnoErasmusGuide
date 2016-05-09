package pv239.fi.muni.cz.brnoerasmusguide.dataClasses;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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
    public DateTime startTime;

    public Event(JSONObject json) {
        Log.d("Event", "json: " + json.toString());
        try {
            id = json.getString("id");
            description = json.getString("description");
            name = json.getString("name");
            place = json.getJSONObject("place").getString("name");
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");
            startTime = formatter.parseDateTime(json.getString("start_time"));
            image = null;
        } catch(JSONException e) {
            Log.d("Event", "exception: " + e.getLocalizedMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;

        Event event = (Event) o;

        if (!id.equals(event.id)) return false;
        return name.equals(event.name);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }


}
