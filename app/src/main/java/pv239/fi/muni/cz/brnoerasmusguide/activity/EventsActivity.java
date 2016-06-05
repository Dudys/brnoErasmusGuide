package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Building;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Event;
import pv239.fi.muni.cz.brnoerasmusguide.services.ServiceApi;
import pv239.fi.muni.cz.brnoerasmusguide.services.StorageManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private static final String JSON_KEY_FOR_GROUPS = "groups_id_json_key";
    private static final String JSON_KEY_FOR_EVENTS = "events_json_key";
    private static final int FILTER_ALL = R.id.filter_all;
    private static final int FILTER_FUTURE = R.id.filter_future;
    private static final int FILTER_PAST = R.id.filter_past;

    private Context context = this;

    private List<String> erasmusGroupsId;
    private boolean showFilter = true;
    private int lastCheckedMenu = 0;
    private Menu actualMenu;

    @Bind(R.id.event_list) RecyclerView eventList;
    @Bind(R.id.facebook_prompt) LinearLayout fbPrompt;
    @Bind(R.id.login_button) LoginButton mLoginButton;

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            displayWelcomeMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.getItem(0).getSubMenu().getItem(0).setChecked(true);
        actualMenu = menu;
        return showFilter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        if(id == R.id.filter_all || id == R.id.filter_future || id == R.id.filter_past) {
            actualMenu.getItem(0).getSubMenu().getItem(lastCheckedMenu).setChecked(false);
            lastCheckedMenu = (id == R.id.filter_all ? 0 : (id == R.id.filter_future ? 1 : 2));
            item.setChecked(true);
            this.filterHasChanged(id);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ServiceApi.get().getGroups().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                TextView message = (TextView) fbPrompt.findViewById(R.id.fb_login_message);
                if (response.isSuccessful()) {
                    message.setVisibility(View.INVISIBLE);
                    fbPrompt.setVisibility(View.INVISIBLE);
                    mLoginButton.setVisibility(View.INVISIBLE);
                    erasmusGroupsId = response.body();
                } else {
                    message.setText("You have to be connected to internet to load any events!");
                    mLoginButton.setVisibility(View.INVISIBLE);
                }
                eventList.setAdapter(new EventAdapter());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                TextView message = (TextView) fbPrompt.findViewById(R.id.fb_login_message);
                message.setText("You have to be connected to internet to load any events!");
                mLoginButton.setVisibility(View.INVISIBLE);
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "user_events", "user_managed_groups"));
        mLoginButton.registerCallback(mCallbackManager, mCallback);

        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            }
        };

        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                displayWelcomeMessage(currentProfile);
            }
        };

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        eventList.setLayoutManager(llm);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void displayWelcomeMessage(Profile profile){
        if(profile != null) {
            Log.d("EventsActivity", "Welcome " + profile.getName());
        }
    }

    public void filterHasChanged(int option) {
        ((EventAdapter)eventList.getAdapter()).filterChanged(option);
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<Event> events = new ArrayList<>();
        private List<Event> displayedEvents = new ArrayList<>();

        public EventAdapter() {
            for (String groupId : erasmusGroupsId) {
                GraphRequest request = GraphRequest.newGraphPathRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + groupId + "/events",
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject j = response.getJSONObject();
                                Log.d("Facebook request", response.toString());
                                try {
                                    JSONArray jsonArray = j.getJSONArray("data");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            final Event e = new Event(jsonArray.getJSONObject(i));
                                            if (!events.contains(e) && e.startTime.isAfter(DateTime.now().minusMonths(1))) {
                                                events.add(e);
                                                Log.d("EventAdapter", "Event added");
                                            }
                                        } catch (JSONException e) {
                                            Log.d("Error", "when getting " + i + ". item from json!");
                                        }
                                    }
                                    displayedEvents.addAll(events);
                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d("EventAdapter", "Events ids added");
                            }
                        }
                );
                request.executeAsync();
            }

        }

        public void filterChanged(int options) {
            displayedEvents.clear();
            DateTime now = DateTime.now();
            for(Event e : events) {
                if(options == FILTER_ALL) {
                    displayedEvents.add(e);
                } else if(options == FILTER_FUTURE && e.startTime.isAfter(now.minusDays(1))) {
                    displayedEvents.add(e);
                } else if(options == FILTER_PAST && !e.startTime.isAfter(now.minusDays(1))) {
                    displayedEvents.add(e);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
            return new EventViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            Event e = displayedEvents.get(position);
            StringBuilder sb = new StringBuilder();
            sb.append(e.place);
            sb.append(" | ");
            sb.append(e.startTime.toString("dd.MM.yyyy' at 'HH:mm"));

            holder.title.setText(e.name);
            holder.place.setText(sb.toString());
            holder.eventId = e.id;
            holder.loadImage(e.id);
        }

        @Override
        public int getItemCount() {
            return displayedEvents.size();
        }

        protected class EventViewHolder extends RecyclerView.ViewHolder {

            protected String eventId;
            protected TextView title;
            protected ImageView image;
            protected TextView place;
            protected Button details;

            public EventViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.event_title);
                image = (ImageView) v.findViewById(R.id.event_image);
                place = (TextView) v.findViewById(R.id.event_place);
                details = (Button) v.findViewById(R.id.event_attend);

                details.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = "https://www.facebook.com/events/" + eventId;
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
            }

            public void loadImage(String eventId) {

                Bundle param = new Bundle();
                param.putString("fields", "cover");
                GraphRequest req = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + eventId,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject j = response.getJSONObject();
                                Log.d("Facebook request", response.toString());
                                String s = "";
                                try {
                                    s = j.getJSONObject("cover").getString("source");
                                } catch(JSONException ex) {
                                    Log.d("EventAdapter", "cover exception: " + ex.getLocalizedMessage());
                                }
                                if(!s.equals("")) {
                                    Picasso.with(getApplicationContext()).load(s).into(image);
                                    image.invalidate();
                                }
                            }
                        }
                );
                req.setParameters(param);
                req.executeAsync();
            }
        }
    }
}
