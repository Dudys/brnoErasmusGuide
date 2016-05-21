package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Canteen;

/**
 * Created by Jan Duda on 5/20/2016.
 */
public class CanteenDetailActivity extends AppCompatActivity {

    public static final String CANTEEN = "canteen";

    @Bind(R.id.buildingDetail_thumbnail)
    ImageView thumbnail;
    @Bind(R.id.web_text_view)
    TextView web;
    @Bind(R.id.address_text_view)
    TextView address;
    @Bind(R.id.mhd_text_view)
    TextView mhd;
    @Bind(R.id.days_text_view)
    TextView days;
    @Bind(R.id.hours_text_view)
    TextView hours;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_canteen);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get a Canteen object if presented (should always be)
        Intent i = getIntent();
        Canteen c = i.getParcelableExtra(CANTEEN);

        getSupportActionBar().setTitle(c.name);
        web.setText(c.web);
        address.setText(c.address);
        mhd.setText(c.mhdInfo);
        days.setText(c.openHours.get(0).weekdays);
        hours.setText(c.openHours.get(0).hours);
        if(c.openHours.size() == 2){
            ((TextView) findViewById(R.id.days2_text_view)).setText(c.openHours.get(1).weekdays);
            ((TextView) findViewById(R.id.hours2_text_view)).setText(c.openHours.get(1).hours);
        }
    }

    /**
     * FAB action.
     */
    @OnClick(R.id.fab) protected void showMap() {
        Intent i = new Intent(CanteenDetailActivity.this, MapsActivity.class);
        startActivity(i);
        Log.d("CanteenDetail", "Should show map now.");
    }
}
