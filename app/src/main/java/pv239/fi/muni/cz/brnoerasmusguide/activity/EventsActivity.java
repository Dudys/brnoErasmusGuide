package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    @Bind(R.id.event_list) RecyclerView eventList;
    @Bind(R.id.facebook_prompt) LinearLayout fbPrompt;

    private CallbackManager mCallbackManager;
    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private LoginButton mLoginButton;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_events);
        ButterKnife.bind(this);

        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();

        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "user_events"));
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
            Log.d("EventsActivity","Welcome " + profile.getName());

            fbPrompt.setVisibility(View.INVISIBLE);
            List<String> events = Arrays.asList("181538512240958","284623215209931","837627703032668");
            eventList.setAdapter(new EventAdapter(events));
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

        private List<Event> events = new ArrayList<>();

        public EventAdapter(List<String> eventList) {

            for(String event : eventList) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + event,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                JSONObject j = response.getJSONObject();
                                final Event e = new Event(j);
                                events.add(e);
                                Log.d("EventAdapter", "Event added");
                                notifyDataSetChanged();
                            }
                        }
                ).executeAsync();
            }
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
            return new EventViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int position) {
            Event e = events.get(position);
            holder.title.setText(e.name);
            holder.place.setText(e.place);
            holder.startTime.setText(e.startTime);
            holder.loadImage(e.id);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        protected class EventViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView image;
            protected TextView place;
            protected TextView startTime;

            public EventViewHolder(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.event_title);
                image = (ImageView) v.findViewById(R.id.event_image);
                place = (TextView) v.findViewById(R.id.event_place);
                startTime = (TextView) v.findViewById(R.id.event_time);
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
                                Picasso.with(getApplicationContext()).load(s).into(image);
                            }
                        }
                );
                req.setParameters(param);
                req.executeAsync();
            }
        }
    }
}
