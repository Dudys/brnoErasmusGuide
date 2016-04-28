package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.event_button)
    Button events;

//    @Bind(R.id.testButton) Button detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        events.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                startActivity(i);
            }
        });
    }

    @OnClick(R.id.testButton) protected void showDetail(View v) {
        Intent i = new Intent(MainActivity.this, BuildingDetailActivity.class);
        String name = "FI MUNI";
        String address = "Bozetechova 48, Brno, 602 00";
        String web = "www.google.com";
        String openingHours = "Mon: 00:00 - 24:00\nTue: 00:00 - 24:00\nWed: 00:00 - 24:00\nThu: 00:00 - 24:00\nFri: 00:00 - 24:00\n";
        String mhdInfo = "Unknown";
        Building detail = new Building(name, address, web, openingHours, mhdInfo);
        i.putExtra(BuildingDetailActivity.BUILDING, detail);
        startActivity(i);
    }
}
