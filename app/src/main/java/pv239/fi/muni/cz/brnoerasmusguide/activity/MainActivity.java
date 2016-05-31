package pv239.fi.muni.cz.brnoerasmusguide.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import pv239.fi.muni.cz.brnoerasmusguide.R;
import pv239.fi.muni.cz.brnoerasmusguide.fragment.AccommodationsFragment;
import pv239.fi.muni.cz.brnoerasmusguide.fragment.CanteensFragment;
import pv239.fi.muni.cz.brnoerasmusguide.fragment.EventsFragment;
import pv239.fi.muni.cz.brnoerasmusguide.fragment.FacultiesFragment;
import pv239.fi.muni.cz.brnoerasmusguide.fragment.WelcomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.main_container) FrameLayout mainContainer;

    private Fragment currentFragment;
    private boolean showFilter = false;
    private int lastCheckedMenu = 0;
    private Menu actualMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        currentFragment = new WelcomeFragment();
        ft.replace(mainContainer.getId(), currentFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
        if(id == R.id.filter_all || id == R.id.filter_future || id == R.id.filter_past) {
            actualMenu.getItem(0).getSubMenu().getItem(lastCheckedMenu).setChecked(false);
            lastCheckedMenu = (id == R.id.filter_all ? 0 : (id == R.id.filter_future ? 1 : 2));
            item.setChecked(true);
            ((EventsFragment)currentFragment).filterHasChanged(id);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        int title;
        showFilter = false;
        if (id == R.id.nav_accommodation) {
            currentFragment = new AccommodationsFragment();
            title = R.string.accommodation;
        } else if (id == R.id.nav_canteen) {
            currentFragment = new CanteensFragment();
            title = R.string.canteens;
        } else if (id == R.id.nav_faculty) {
            currentFragment = new FacultiesFragment();
            title = R.string.faculties;
        } else if (id == R.id.nav_event) {
            currentFragment = new EventsFragment();
            title = R.string.events;
            showFilter = true;
        }
        else {
            return false;
        }

        getSupportActionBar().setTitle(title);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(mainContainer.getId(), currentFragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        invalidateOptionsMenu();
        return true;
    }
}
