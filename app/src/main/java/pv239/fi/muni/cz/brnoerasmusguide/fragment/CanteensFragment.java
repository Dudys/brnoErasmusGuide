package pv239.fi.muni.cz.brnoerasmusguide.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import pv239.fi.muni.cz.brnoerasmusguide.services.ServiceApiForBuldings;
import pv239.fi.muni.cz.brnoerasmusguide.services.StorageManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanteensFragment extends Fragment {

    @Bind(R.id.building_list) RecyclerView list;

    private static final String JSON_KEY = "canteen_json_key";
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    protected CanteenAdapter canteenAdapter;

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
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        list.setLayoutManager(mLayoutManager);

        ServiceApiForBuldings.get().getCanteens().enqueue(new Callback<List<Building>>() {
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {

                if(response.isSuccessful()) {
                    StorageManager.saveBuildings(JSON_KEY, response.body(), context);
                    canteenAdapter = new CanteenAdapter(response.body());
                } else {
                    List<Building> bl = StorageManager.loadBuildings(JSON_KEY, context);
                    canteenAdapter = new CanteenAdapter(bl);
                }
                list.setAdapter(canteenAdapter);
            }

            @Override
            public void onFailure(Call<List<Building>> call, Throwable throwable) {
                List<Building> bl = StorageManager.loadBuildings(JSON_KEY, context);
                canteenAdapter = new CanteenAdapter(bl);
                list.setAdapter(canteenAdapter);
            }
        });
    }

    public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder> {

        private List<Building> canteens;

        public CanteenAdapter(List<Building> canteens) {
            this.canteens = canteens;
        }

        @Override
        public CanteenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text_for_building, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = list.getChildAdapterPosition(view);
                    Building b = canteens.get(itemPosition);
                    showDetail(b);
                }
            });
            return new CanteenViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CanteenViewHolder holder, int position) {
            Building item = canteens.get(position);
            holder.title.setText(item.name);
        }

        @Override
        public int getItemCount() {
            return canteens.size();
        }

        protected class CanteenViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;

            public CanteenViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.title_of_building);
            }
        }
    }

    protected void showDetail(Building b) {
        Intent i = new Intent(context, BuildingDetailActivity.class);
        i.putExtra(BuildingDetailActivity.BUILDING, b);
        startActivity(i);
    }
}
