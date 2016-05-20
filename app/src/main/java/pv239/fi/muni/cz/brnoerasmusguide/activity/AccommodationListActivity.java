package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class AccommodationListActivity extends AppCompatActivity {

    @Bind(R.id.building_list) RecyclerView list;
    private BuildingsAdapter adapter;
    private static final String ACCOMMODATION_URL = "https://is.muni.cz/www/396035/63281828/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);
        ButterKnife.bind(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(llm);
        loadJSON();
    }

    protected void showDetail(Building b) {
        Intent i = new Intent(this, BuildingDetailActivity.class);
        i.putExtra(BuildingDetailActivity.BUILDING, b);
        startActivity(i);
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ACCOMMODATION_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerService request = retrofit.create(ServerService.class);
        Call<List<Building>> call = request.getBuildings();
        call.enqueue(new Callback<List<Building>>() {
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {

                List<Building> buildingsResponse = response.body();
                adapter = new BuildingsAdapter(buildingsResponse);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Building>> call, Throwable t) {
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

    public interface ServerService {
        @GET("dorms.json")
        Call<List<Building>> getBuildings();
    }
}
