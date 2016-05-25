package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;

/**
 * Created by Jakub Fiser on 4/14/2016.
 */
public class BuildingDetailActivity extends AppCompatActivity {

    public static final String BUILDING = "building";

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
    @Bind(R.id.days2_text_view)
    TextView days2;
    @Bind(R.id.hours2_text_view)
    TextView hours2;

    private Building b;

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
        setContentView(R.layout.activity_detail_building);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get a Building object if presented (should always be)
        Intent i = getIntent();
        b = i.getParcelableExtra(BUILDING);

        if(b.name.equals("")){
            getSupportActionBar().setTitle("Detail of building");
        } else {
            getSupportActionBar().setTitle(b.name);
        }
        loadImage(b.image);
        web.setText(b.web);
        address.setText(b.address);
        mhd.setText(b.mhdInfo);
        days.setText(b.openHours.get(0).weekdays);
        hours.setText(b.openHours.get(0).hours);
        if(b.openHours.size() == 2){
            days2.setText(b.openHours.get(1).weekdays);
            hours2.setText(b.openHours.get(1).hours);
        } else {
            days2.setVisibility(View.INVISIBLE);
            hours2.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * FAB action.
     */
    @OnClick(R.id.fab) protected void showMap() {
        Intent i = new Intent(BuildingDetailActivity.this, MapsActivity.class);
        i.putExtra(MapsActivity.ADDRESS, b.address);
        if(b.name.equals("")){
            i.putExtra(MapsActivity.NAME, b.address);
        } else {
            i.putExtra(MapsActivity.NAME, b.name);
        }
        startActivity(i);
        Log.d("BuildingDetail", "Should show map now.");
    }

    private void loadImage(String url){
        Picasso.with(BuildingDetailActivity.this)
                .load(url)
                .placeholder(R.mipmap.building_placeholder_lazyload)
                .error(R.mipmap.building_placeholder_error)
                .resize(480,270)
                .centerCrop()
                .into(thumbnail);
    }
}
