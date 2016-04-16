package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pv239.fi.muni.cz.brnoerasmusguide.R;

public class MainActivity extends AppCompatActivity {

    private Button events;

    @Bind(R.id.testButton) Button detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUI();

        events.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                startActivity(i);
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(v);
            }
        });
    }

    protected void showDetail(View v) {
        Intent i = new Intent(MainActivity.this, BuildingDetailActivity.class);
        startActivity(i);
    }

    private void setUI(){
        events = (Button) findViewById(R.id.event_button);
    }
}
