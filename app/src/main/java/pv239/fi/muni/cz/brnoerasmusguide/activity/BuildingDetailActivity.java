package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_building);
        ButterKnife.bind(this);

        // Get a Building object if presented (should always be)
        Intent i = getIntent();
        Building b = i.getParcelableExtra(BUILDING);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        details.setLayoutManager(llm);
        details.setAdapter(new BuildingDetailAdapter(b));
    }

    /**
     * FAB action.
     */
    @OnClick(R.id.fab) protected void showMap() {
        Log.d("BuildingDetail", "Should show map now.");
    }

    /**
     *  Adapter for list of information.
     *  TODO: Use reusable view.
     */
    public class BuildingDetailAdapter extends RecyclerView.Adapter<BuildingDetailAdapter.BuildingViewHolder> {

        private List<Integer> icons = Arrays.asList(android.R.drawable.ic_menu_view, android.R.drawable.ic_menu_day, android.R.drawable.ic_dialog_info);
        private List<String> strings;

        public BuildingDetailAdapter(Building b) {
            strings = Arrays.asList(b.web, b.openingHours, b.address);
        }

        @Override
        public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_list_item, parent, false);
            return new BuildingViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BuildingViewHolder holder, int position) {
            String item = strings.get(position);
            Integer image = icons.get(position);
            holder.title.setText(item);
            holder.image.setImageResource(image);
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
