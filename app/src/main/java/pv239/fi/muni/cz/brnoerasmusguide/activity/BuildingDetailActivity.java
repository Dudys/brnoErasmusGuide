package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Bind(R.id.buildingDetail_thumbnail) ImageView thumbnail;
    @Bind(R.id.bottom_action_sheet_persistent) RecyclerView details;

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

        // Get a Faculty object if presented (should always be)
        Intent i = getIntent();
        b = i.getParcelableExtra(BUILDING);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        details.setLayoutManager(llm);

        details.setAdapter(new BuildingDetailAdapter(b));
    }

    /**
     * FAB action.
     */
    @OnClick(R.id.fab) protected void showMap() {
        Intent i = new Intent(BuildingDetailActivity.this, MapsActivity.class);
        i.putExtra(MapsActivity.ADDRESS, b.address);
        i.putExtra(MapsActivity.NAME, b.name);
        startActivity(i);
        Log.d("BuildingDetail", "Should show map now.");
    }

    /**
     *  Adapter for list of information.
     */
    public class BuildingDetailAdapter extends RecyclerView.Adapter<BuildingDetailAdapter.BuildingViewHolder> {

        private List<Integer> icons = Arrays.asList(R.mipmap.ic_web_black_24dp, R.mipmap.ic_address_black_24dp,
                R.mipmap.ic_mhd_info_black_24dp, R.mipmap.ic_open_hours_black_24dp);
        private List<String> strings = new ArrayList<>();

        public BuildingDetailAdapter(Building b) {
            strings.add(b.web);
            strings.add(b.address);
            strings.add(b.mhdInfo);
            strings.add(b.openHours);
        }

        @Override
        public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_list_item, parent, false);
            return new BuildingViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BuildingViewHolder holder, int position) {
            String item = strings.get(position);
            switch (position){
                case 0:
                    holder.image.setImageResource(icons.get(0));
                    break;
                case 1:
                    holder.image.setImageResource(icons.get(1));
                    break;
                case 2:
                    holder.image.setImageResource(icons.get(2));
                    break;
                case 3:
                    holder.image.setImageResource(icons.get(3));
                    break;
            }
            holder.title.setText(item);
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }

        protected class BuildingViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView image;

            public BuildingViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.item_text);
                image = (ImageView) v.findViewById(R.id.item_icon);
            }
        }
    }
}
