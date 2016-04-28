package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;

import com.facebook.FacebookSdk;

import java.util.Arrays;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    @Bind(R.id.event_list) RecyclerView eventList;
    private List<String> events = Arrays.asList("One","Two","Three");
    private List<Integer> images = Arrays.asList(R.mipmap.building_base_image_thumbnail, R.mipmap.wrigley_building_chicago2, R.mipmap.building_base_image_thumbnail);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);

//        eventList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        eventList.setLayoutManager(llm);
        eventList.setAdapter(new EventAdapter(events, images));
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<String> titles;
        private List<Integer> images;

        public EventAdapter(List<String> titles, List<Integer> images) {
            this.titles = titles;
            this.images = images;
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
            return new EventViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            String item = titles.get(position);
            Integer image = images.get(position);
            holder.title.setText(item);
            holder.image.setImageResource(image);
        }

        @Override
        public int getItemCount() {
            return titles.size();
        }

        protected class EventViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView image;

            public EventViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.event_title);
                image = (ImageView) v.findViewById(R.id.event_image);
            }
        }
    }
}
