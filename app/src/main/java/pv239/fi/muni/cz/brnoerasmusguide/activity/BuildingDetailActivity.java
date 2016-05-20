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
    public static final String WEB = "web";

    @Bind(R.id.buildingDetail_thumbnail) ImageView thumbnail;
    @Bind(R.id.bottom_action_sheet_persistent) RecyclerView details;

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
        Building b = i.getParcelableExtra(BUILDING);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        details.setLayoutManager(llm);

        details.setAdapter(new BuildingDetailAdapter(b, i.getStringExtra(WEB)));
    }

    /**
     * FAB action.
     */
    @OnClick(R.id.fab) protected void showMap() {
        Intent i = new Intent(BuildingDetailActivity.this, MapsActivity.class);
        startActivity(i);
        Log.d("BuildingDetail", "Should show map now.");
    }

    /**
     *  Adapter for list of information.
     */
    public class BuildingDetailAdapter extends RecyclerView.Adapter<BuildingDetailAdapter.BuildingViewHolder> {

        private List<Integer> icons = Arrays.asList(android.R.drawable.ic_menu_view, android.R.drawable.ic_dialog_info);//, android.R.drawable.ic_menu_day);
        private List<String> strings = new ArrayList<>();

        public BuildingDetailAdapter(Building b, String web) {
            if(b.web == null) {
                strings.add(web);
            }
            strings.add(b.address);
        }

        @Override
        public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_list_item, parent, false);
            return new BuildingViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BuildingViewHolder holder, int position) {
            String item = strings.get(position);
            if(position == 0) {
                Integer image = icons.get(0);
                holder.image.setImageResource(image);
            } else {
                Integer image = icons.get(1);
                holder.image.setImageResource(image);
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
