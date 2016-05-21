package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.Model.ParentWrapper;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Faculty;
import pv239.fi.muni.cz.brnoerasmusguide.services.ServiceApiForBuldings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuildingListActivity extends AppCompatActivity {

    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    private RecyclerView.LayoutManager mLayoutManager;

    protected Context context = this;

    @Bind(R.id.building_list) RecyclerView list;
    protected FacultyAdapter facultyAdapter;

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

        getSupportActionBar().setTitle("Faculties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        list.setLayoutManager(mLayoutManager);

        ServiceApiForBuldings.get().getFaculties().enqueue(new Callback<List<Faculty>>() {
            @Override
            public void onResponse(Call<List<Faculty>> call, Response<List<Faculty>> response) {
                facultyAdapter = new FacultyAdapter(BuildingListActivity.this, response.body());
                list.setAdapter(facultyAdapter);
            }

            @Override
            public void onFailure(Call<List<Faculty>> call, Throwable throwable) {

            }
        });
    }

    protected void showDetail(Building b, String web) {
        b.web = web;
        Intent i = new Intent(this, BuildingDetailActivity.class);
        i.putExtra(BuildingDetailActivity.BUILDING, b);
        startActivity(i);
    }

    /**
     *  Adapter for list of information.
     */
    // ExpandableRecyclerAdapter from BIG NERD RANCH
    public class FacultyAdapter extends ExpandableRecyclerAdapter<FacultyAdapter.FacultyViewHolder, FacultyAdapter.BuildingViewHolder> {
        private LayoutInflater mInflator;

        public FacultyAdapter(Context context, @NonNull List<Faculty> faculties) {
            super(faculties);
            this.mInflator = LayoutInflater.from(context);
        }

        @Override
        public FacultyViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
            View facultyView = mInflator.inflate(R.layout.faculty_header, parentViewGroup, false);
            return new FacultyViewHolder(facultyView);
        }

        @Override
        public BuildingViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
            View buildingView = mInflator.inflate(R.layout.buildings_of_faculty, childViewGroup, false);
            return new BuildingViewHolder(buildingView);
        }

        @Override
        public void onBindParentViewHolder(FacultyViewHolder facultyViewHolder, int position, ParentListItem parentListItem) {
            Faculty faculty = (Faculty) parentListItem;
            facultyViewHolder.bind(faculty);
        }

        @Override
        public void onBindChildViewHolder(BuildingViewHolder buildingViewHolder, int position, Object childListItem) {
            Building building = (Building) childListItem;
            buildingViewHolder.bind(building);
        }

        protected class FacultyViewHolder extends ParentViewHolder {

            private ImageView arrowExpandImageView;
            private TextView facultyName;
            private View topLine;

            public FacultyViewHolder(View itemView) {
                super(itemView);
                topLine = itemView.findViewById(R.id.top_line);
                facultyName = (TextView) itemView.findViewById(R.id.name_of_faculty);
                facultyName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded()) {
                            collapseView();
                            collapse();
                        } else {
                            expandView();
                            expand();
                        }
                    }
                });

                arrowExpandImageView = (ImageView) itemView.findViewById(R.id.expand_arrow_image);
                arrowExpandImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded()) {
                            collapseView();
                            collapse();
                        } else {
                            expandView();
                            expand();
                        }
                    }
                });
            }

            private void expand(){
                arrowExpandImageView.setImageResource(R.mipmap.ic_expand_less_black_24dp);
                topLine.setVisibility(View.VISIBLE);
                facultyName.setTextColor(Color.parseColor("#303F9F"));
            }

            private void collapse(){
                arrowExpandImageView.setImageResource(R.mipmap.ic_expand_more_black_24dp);
                topLine.setVisibility(View.INVISIBLE);
                facultyName.setTextColor(Color.parseColor("#000000"));
            }

            public void bind(Faculty faculty) {
                if(isExpanded()){
                    expand();
                } else {
                    collapse();
                }
                facultyName.setText(faculty.name);
            }

            @Override
            public boolean shouldItemViewClickToggleExpansion() {
                return false;
            }
        }

        protected class BuildingViewHolder extends ChildViewHolder {

            private TextView buildingName;
            private Building building;

            public BuildingViewHolder(View itemView) {
                super(itemView);
                buildingName = (TextView) itemView.findViewById(R.id.name_of_building);

                buildingName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemPosition = getAdapterPosition();
                        int i = itemPosition - 1;
                        while (i >= 0 && facultyAdapter.getItemViewType(i) == TYPE_CHILD) {
                            i--;
                        }
                        Faculty facultyOfBuilding = (Faculty) ((ParentWrapper) facultyAdapter.getListItem(i)).getParentListItem();
                        Building building = facultyOfBuilding.buildings.get(itemPosition - i - 1);
                        showDetail(building, facultyOfBuilding.web);
                    }
                });
            }

            public void bind(Building building) {
                this.building = building;
                if(building.name.equals("")){
                    buildingName.setText(building.address);
                } else {
                    buildingName.setText(building.name);
                }
            }
        }
    }
}
