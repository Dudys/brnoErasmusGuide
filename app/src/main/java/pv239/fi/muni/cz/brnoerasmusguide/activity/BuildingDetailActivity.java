package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
    @Bind(R.id.bottom_action_sheet_persistent) ListView details;
    private List<String> strings = Arrays.asList();
    // Temporary icons, will be replaced.
    private List<Integer> icons = Arrays.asList(android.R.drawable.ic_menu_view, android.R.drawable.ic_menu_day, android.R.drawable.ic_dialog_info);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_building);
        ButterKnife.bind(this);

        // Get a Building object if presented (should always be)
        Intent i = getIntent();
        if(i.hasExtra(BUILDING)) {
            Building b = i.getParcelableExtra(BUILDING);
            strings = Arrays.asList(b.web, b.openingHours, b.mhdInfo);
        }

        details.setAdapter(new MyAdapter());
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
    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public String getItem(int position) {
            return strings.get(position);
        }

        public int getItemImage(int position) { return icons.get(position); }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(BuildingDetailActivity.this);
            convertView = inflater.inflate(R.layout.icon_list_item, parent, false);

            ImageView icon = (ImageView)convertView.findViewById(R.id.item_icon);
            icon.setImageResource(getItemImage(position));

            TextView text = (TextView)convertView.findViewById(R.id.item_text);
            text.setText(getItem(position));

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }
    }
}
