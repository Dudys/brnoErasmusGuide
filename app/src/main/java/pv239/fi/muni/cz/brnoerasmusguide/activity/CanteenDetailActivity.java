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
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Canteen;

/**
 * Created by Jan Duda on 5/20/2016.
 */
public class CanteenDetailActivity extends AppCompatActivity {

    public static final String CANTEEN = "canteen";

    @Bind(R.id.buildingDetail_thumbnail)
    ImageView thumbnail;
    @Bind(R.id.bottom_action_sheet_persistent)
    RecyclerView details;

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

        // Get a Canteen object if presented (should always be)
        Intent i = getIntent();
        Canteen c = i.getParcelableExtra(CANTEEN);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        details.setLayoutManager(llm);

        details.setAdapter(new CanteenDetailAdapter(c));
    }

    /**
     * FAB action.
     */
    @OnClick(R.id.fab) protected void showMap() {
        Intent i = new Intent(CanteenDetailActivity.this, MapsActivity.class);
        startActivity(i);
        Log.d("CanteenDetail", "Should show map now.");
    }

    /**
     *  Adapter for list of information.
     */
    public class CanteenDetailAdapter extends RecyclerView.Adapter<CanteenDetailAdapter.CanteenViewHolder> {

        private List<Integer> icons = Arrays.asList(R.mipmap.ic_web_black_24dp, R.mipmap.ic_address_black_24dp,
                R.mipmap.ic_mhd_info_black_24dp, R.mipmap.ic_open_hours_black_24dp);

        private List<String> strings = new ArrayList<>();

        public CanteenDetailAdapter(Canteen c) {
            strings.add(c.web);
            strings.add(c.address);
            strings.add(c.mhdInfo);
            strings.add(c.openHours.get(0).hours);
        }

        @Override
        public CanteenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_list_item, parent, false);
            return new CanteenViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CanteenViewHolder holder, int position) {
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

        protected class CanteenViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView image;

            public CanteenViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.item_text);
                image = (ImageView) v.findViewById(R.id.item_icon);
            }
        }
    }
}
