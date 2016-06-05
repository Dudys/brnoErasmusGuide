package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.dataClasses.Event;

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

public class EventsActivity extends Fragment {

    private static final String[] ERASMUS_GROUP_ID = {"1652673088347828", "11480938751"};
    private static final int FILTER_ALL = R.id.filter_all;
    private static final int FILTER_FUTURE = R.id.filter_future;
    private static final int FILTER_PAST = R.id.filter_past;

    @Bind(R.id.event_list) RecyclerView eventList;
    @Bind(R.id.facebook_prompt) LinearLayout fbPrompt;
    @Bind(R.id.login_button) LoginButton mLoginButton;

    private Context appContext;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.appContext = context;
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        appContext = null;
    }

    private Context getApplicationContext() {
        return appContext;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        // AppEventsLogger.activateApp(this);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
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
            fbPrompt.setVisibility(View.INVISIBLE);
            eventList.setAdapter(new EventAdapter());
//            TextView message = (TextView) fbPrompt.findViewById(R.id.fb_login_message);
//            if (eventList.getChildCount() == 0){
//                message.setText("Not found any events from Erasmus Facebook group and ISC MU Brno page!");
//                mLoginButton.setVisibility(View.INVISIBLE);
//            } else {
//                message.setVisibility(View.INVISIBLE);
//
//            }
        }
    }

    public void filterHasChanged(int option) {
        ((EventAdapter)eventList.getAdapter()).filterChanged(option);
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<Event> events = new ArrayList<>();
        private List<Event> displayedEvents = new ArrayList<>();

        public EventAdapter() {
            for (String groupId : ERASMUS_GROUP_ID) {
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
                                            if (!events.contains(e) && e.startTime.isAfter(DateTime.now().minusMonths(2))) {
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
            holder.loadImage(e.id);
        }

        @Override
        public int getItemCount() {
            return displayedEvents.size();
        }

        protected class EventViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView image;
            protected TextView place;

            public EventViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.event_title);
                image = (ImageView) v.findViewById(R.id.event_image);
                place = (TextView) v.findViewById(R.id.event_place);
                v.findViewById(R.id.event_friends_attending).setVisibility(View.GONE);
                v.findViewById(R.id.event_attend).setVisibility(View.GONE);
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
