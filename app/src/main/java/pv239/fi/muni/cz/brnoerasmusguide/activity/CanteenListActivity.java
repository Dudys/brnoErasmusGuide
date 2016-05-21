package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Canteen;
import pv239.fi.muni.cz.brnoerasmusguide.services.ServiceApiForBuldings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanteenListActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.building_list) RecyclerView list;
    protected CanteenAdapter canteenAdapter;

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
        setContentView(R.layout.activity_building_list);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Canteens");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        list.setLayoutManager(mLayoutManager);

        ServiceApiForBuldings.get().getCanteens().enqueue(new Callback<List<Canteen>>() {
            @Override
            public void onResponse(Call<List<Canteen>> call, Response<List<Canteen>> response) {
                canteenAdapter = new CanteenAdapter(response.body());
                list.setAdapter(canteenAdapter);
            }

            @Override
            public void onFailure(Call<List<Canteen>> call, Throwable throwable) {

            }
        });
    }

    public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.CanteenViewHolder> {

        private List<Canteen> canteens;

        public CanteenAdapter(List<Canteen> canteens) {
            this.canteens = canteens;
        }

        @Override
        public CanteenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_text_for_building, parent, false);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = list.getChildAdapterPosition(view);
                    Canteen b = canteens.get(itemPosition);
                    showDetail(b);
                }
            });
            return new CanteenViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CanteenViewHolder holder, int position) {
            Canteen item = canteens.get(position);
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

    protected void showDetail(Canteen b) {
        Intent i = new Intent(this, CanteenDetailActivity.class);
        i.putExtra(CanteenDetailActivity.CANTEEN, b);
        startActivity(i);
    }
}
