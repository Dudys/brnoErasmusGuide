package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pv239.fi.muni.cz.brnoerasmusguide.R;

import com.facebook.FacebookSdk;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_events);

    }

}
