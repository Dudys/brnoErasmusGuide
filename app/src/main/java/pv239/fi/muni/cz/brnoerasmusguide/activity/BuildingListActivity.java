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
import pv239.fi.muni.cz.brnoerasmusguide.services.ServiceApiForFaculties;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);
        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        list.setLayoutManager(mLayoutManager);

        ServiceApiForFaculties.get().getFaculties().enqueue(new Callback<List<Faculty>>() {
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
        Intent i = new Intent(this, BuildingDetailActivity.class);
        i.putExtra(BuildingDetailActivity.BUILDING, b);
        i.putExtra(BuildingDetailActivity.WEB, web);
        startActivity(i);
    }

    /**
     *  Adapter for list of information.
     */
//    public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {
//
//        private List<Integer> icons = Arrays.asList(R.mipmap.ic_expand_less_black_24dp, R.mipmap.ic_expand_more_black_24dp);
//        private List<Faculty> faculties;
//
//        public FacultyAdapter(List<Faculty> faculties) {
//            this.faculties = faculties;
//        }
//
//        @Override
//        public FacultyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_header, parent, false);
//            return new FacultyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(FacultyViewHolder holder, int position) {
//            Faculty item = faculties.get(position);
//            holder.title.setText(item.name);
//            holder.image.setImageResource(icons.get(1));
//        }
//
//        @Override
//        public int getItemCount() {
//            return faculties.size();
//        }
//
//        protected class FacultyViewHolder extends RecyclerView.ViewHolder
//                implements View.OnClickListener {
//
//            private int originalHeight = 0;
//            private boolean isViewExpanded = false;
//
//            protected TextView title;
//            protected ImageView image;
//            protected RecyclerView recyclerView;
//            protected View topLine;
//            protected View bottomLine;
//
//            public FacultyViewHolder(View v) {
//                super(v);
//                v.setOnClickListener(this);
//
//                title = (TextView) v.findViewById(R.id.name_of_faculty);
//                image = (ImageView) v.findViewById(R.id.expand_arrow_image);
//                recyclerView = (RecyclerView) v.findViewById(R.id.buildings_list);
//                topLine = v.findViewById(R.id.top_line);
//                bottomLine = v.findViewById(R.id.bottom_line);
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//
//                if (!isViewExpanded) {
//                    recyclerView.setVisibility(View.GONE);
//                    recyclerView.setEnabled(false);
//                    topLine.setVisibility(View.GONE);
//                    topLine.setEnabled(false);
//                    bottomLine.setVisibility(View.GONE);
//                    bottomLine.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void onClick(final View view) {
//                if (originalHeight == 0) {
//                    originalHeight = view.getHeight();
//                }
//
//                // Declare a ValueAnimator object
//                ValueAnimator valueAnimator;
//                if (!isViewExpanded) {
//                    // set views for expand
//                    recyclerView.setVisibility(View.VISIBLE);
//                    recyclerView.setEnabled(true);
//                    image.setImageResource(icons.get(0));
//                    topLine.setVisibility(View.VISIBLE);
//                    topLine.setEnabled(true);
//                    bottomLine.setVisibility(View.VISIBLE);
//                    bottomLine.setEnabled(true);
//                    isViewExpanded = true;
//
//                    int facultyPosition = list.getChildAdapterPosition(view);
//                    List<Faculty.Building> buildingsOfFaculty = faculties.get(facultyPosition).buildings;
//                    recyclerView.setAdapter(new BuildingAdapter(buildingsOfFaculty));
//
//                    valueAnimator = ValueAnimator.ofInt(originalHeight, 400);//originalHeight + originalHeight * buildingsOfFaculty.size());
//                } else {
//                    isViewExpanded = false;
//                    int facultyPosition = list.getChildAdapterPosition(view);
//                    valueAnimator = ValueAnimator.ofInt(400, originalHeight);//originalHeight + originalHeight * faculties.get(facultyPosition).buildings.size(), originalHeight);
//
//                    Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out
//
//                    a.setDuration(200);
//                    // Set a listener to the animation and configure onAnimationEnd
//                    a.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            image.setImageResource(icons.get(1));
//                            recyclerView.setVisibility(View.INVISIBLE);
//                            recyclerView.setEnabled(false);
//                            topLine.setVisibility(View.INVISIBLE);
//                            topLine.setEnabled(false);
//                            bottomLine.setVisibility(View.INVISIBLE);
//                            bottomLine.setEnabled(false);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//
//                    // Set the animation on the custom view
//                    recyclerView.startAnimation(a);
//                }
//                valueAnimator.setDuration(200);
//                valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        Integer value = (Integer) animation.getAnimatedValue();
//                        view.getLayoutParams().height = value.intValue();
//                        view.requestLayout();
//                    }
//                });
//                valueAnimator.start();
//            }
//
//            public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingsViewHolder> {
//
//                private List<Faculty.Building> buildings;
//
//                public BuildingAdapter(List<Faculty.Building> buildings) {
//                    this.buildings = buildings;
//                }
//
//                @Override
//                public BuildingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.buildings_of_faculty, parent, false);
//                    itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            int itemPosition = recyclerView.getChildAdapterPosition(view);
//                            Faculty.Building b = buildings.get(itemPosition);
//                            showDetail(b);
//                        }
//                    });
//                    return new BuildingsViewHolder(itemView);
//                }
//
//                @Override
//                public void onBindViewHolder(BuildingsViewHolder holder, int position) {
//                    Faculty.Building item = buildings.get(position);
//                    if (item.name.equals("")) {
//                        holder.title.setText(item.address);
//                    } else {
//                        holder.title.setText(item.name);
//                    }
//                }
//
//                @Override
//                public int getItemCount() {
//                    return buildings.size();
//                }
//
//                protected class BuildingsViewHolder extends RecyclerView.ViewHolder {
//                    protected TextView title;
//
//                    public BuildingsViewHolder(View v) {
//                        super(v);
//
//                        title = (TextView) v.findViewById(R.id.name_of_building);
//                    }
//                }
//            }
//        }


//    POMOCI KLASIK ADATERU
//    public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {
//
//        private List<Integer> icons = Arrays.asList(R.mipmap.ic_expand_less_black_24dp, R.mipmap.ic_expand_more_black_24dp);
//
//        private List<Faculty> faculties;
//
//        public FacultyAdapter(List<Faculty> faculties) {
//            this.faculties = faculties;
//        }
//
//        @Override
//        public FacultyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.faculty_header, parent, false);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int itemPosition = list.getChildAdapterPosition(view);
//                    Faculty b = faculties.get(itemPosition);
//                    showDetail(b);
//                }
//            });
//            return new FacultyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(FacultyViewHolder holder, int position) {
//            Faculty item = faculties.get(position);
//            holder.title.setText(item.name);
//            holder.image.setImageResource(icons.get(1));
//        }
//
//        @Override
//        public int getItemCount() {
//            return faculties.size();
//        }
//
//        protected class FacultyViewHolder extends RecyclerView.ViewHolder {
//            protected TextView title;
//            protected ImageView image;
//
//            public FacultyViewHolder(View v) {
//                super(v);
//
//                title = (TextView) v.findViewById(R.id.name_of_faculty);
//                image = (ImageView) v.findViewById(R.id.expand_arrow_image);
//            }
//        }


//        POMOCI BIG NERD RANCH
    public class FacultyAdapter extends ExpandableRecyclerAdapter<FacultyAdapter.FacultyViewHolder, FacultyAdapter.BuildingViewHolder> {
        private LayoutInflater mInflator;
//        private List<Faculty> faculties;

        public FacultyAdapter(Context context, @NonNull List<Faculty> faculties) {
            super(faculties);
            this.mInflator = LayoutInflater.from(context);
//            this.faculties = faculties;
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
                        int i = itemPosition-1;
                        while(i >= 0 && facultyAdapter.getItemViewType(i) == TYPE_CHILD){
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
