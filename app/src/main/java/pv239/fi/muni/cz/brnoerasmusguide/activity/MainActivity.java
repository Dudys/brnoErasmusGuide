package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pv239.fi.muni.cz.brnoerasmusguide.R;

public class MainActivity extends AppCompatActivity {

    private Button events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUI();

        events.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                Intent i = new Intent(MainActivity.this, EventsActivity.class);
                startActivity(i);
            }
        });
    }

    private void setUI(){
        events = (Button) findViewById(R.id.event_button);
    }
}
