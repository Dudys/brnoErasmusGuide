package pv239.fi.muni.cz.brnoerasmusguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.activity.BuildingDetailActivity;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;
import pv239.fi.muni.cz.brnoerasmusguide.services.ServiceApi;
import pv239.fi.muni.cz.brnoerasmusguide.services.StorageManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationsFragment extends Fragment {

    @Bind(R.id.building_list) RecyclerView list;

    private static final String JSON_KEY = "dormitories_json_key";
    private BuildingsAdapter adapter;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faculty_list, container, false);
        ButterKnife.bind(this, v);
        list.addItemDecoration(new DividerItemDecorator(context));
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        loadJSON();
    }

    protected void showDetail(Building b) {
        Intent i = new Intent(context, BuildingDetailActivity.class);
        i.putExtra(BuildingDetailActivity.BUILDING, b);
        startActivity(i);
    }

    private void loadJSON() {
        Call<List<Building>> call = ServiceApi.get().getDormitories();
        call.enqueue(new Callback<List<Building>>() {
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {

                if(response.isSuccessful()) {
                    StorageManager.saveBuildings(JSON_KEY, response.body(), context);
                    adapter = new BuildingsAdapter(response.body());
                } else {
                    List<Building> bl = StorageManager.loadBuildings(JSON_KEY, context);
                    adapter = new BuildingsAdapter(bl);
                }
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Building>> call, Throwable t) {

                List<Building> bl = StorageManager.loadBuildings(JSON_KEY, context);
                adapter = new BuildingsAdapter(bl);
                list.setAdapter(adapter);
            }
        });
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
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text_for_building, parent, false);
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
                title = (TextView) v.findViewById(R.id.title_of_building);
            }
        }
    }
}
