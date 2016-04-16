package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.Bind;
import pv239.fi.muni.cz.brnoerasmusguide.R;

/**
 * Created by Jakub Fiser on 4/14/2016.
 */
public class BuildingDetailActivity extends AppCompatActivity {

    @Bind(R.id.buildingDetail_thumbnail)
    ImageView thumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_building);
    }
}
