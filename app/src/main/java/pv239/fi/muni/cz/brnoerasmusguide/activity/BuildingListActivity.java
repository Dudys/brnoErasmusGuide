package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;

public class BuildingListActivity extends AppCompatActivity {

    @Bind(R.id.building_list) RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);
        ButterKnife.bind(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);

        Building b1 = new Building("FI MUNI", "Bozetechova", "www.fi.muni.cz", "All the time", "Cannot tell");
        Building b2 = new Building("FF MUNI", "Grohova", "www.phil.muni.cz", "All the time", "Cannot tell");
        Building b3 = new Building("PrF MUNI", "Brno", "www.google.com", "Never", "Cannot tell");
        list.setAdapter(new BuildingsAdapter(Arrays.asList(b1,b2,b3)));
    }

    protected void showDetail(Building b) {
        Intent i = new Intent(this, BuildingDetailActivity.class);
        i.putExtra(BuildingDetailActivity.BUILDING, b);
        startActivity(i);
    }

    /**
     *  Adapter for list of information.
     */
    public class BuildingsAdapter extends RecyclerView.Adapter<BuildingsAdapter.BuildingViewHolder> {

        private List<Building> buildings;

        public BuildingsAdapter(List<Building> buildings) {
            this.buildings = buildings;
        }

        @Override
        public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = list.getChildAdapterPosition(view);
                    Building b = buildings.get(itemPosition);
                    showDetail(b);
                }
            });
            return new BuildingViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(BuildingViewHolder holder, int position) {
            Building item = buildings.get(position);
            holder.title.setText(item.name);
        }

        @Override
        public int getItemCount() {
            return buildings.size();
        }

        protected class BuildingViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;

            public BuildingViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(android.R.id.text1);
            }
        }
    }
}
