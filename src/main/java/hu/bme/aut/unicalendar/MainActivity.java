package hu.bme.aut.unicalendar;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import hu.bme.aut.unicalendar.adapter.SimpleFragmentPagerAdapter;
import hu.bme.aut.unicalendar.adapter.SubjectAdapter;
import hu.bme.aut.unicalendar.data.CalendarDatabase;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SimpleFragmentPagerAdapter fragmentPagerAdapter;
    private CalendarDatabase database;
    private boolean notificationOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatabase();

        initTabbedViewPager();
        initFab();

        notificationOn = MyService.running;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        menu.findItem(R.id.menu_clear_all).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleClearAllClick();
                return true;
            }
        });
        menu.findItem(R.id.menu_select_all).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleSelectAllClick();
                return true;
            }
        });
        menu.findItem(R.id.menu_select_date).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleSelectDateClick();
                return true;
            }
        });
        menu.findItem(R.id.menu_notification_on).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleNotificationOnClick();
                return false;
            }
        }).setVisible(!MyService.running);
        menu.findItem(R.id.menu_notification_off).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                handleNotificationOffClick();
                return false;
            }
        }).setVisible(MyService.running);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_notification_on).setVisible(!notificationOn);
        menu.findItem(R.id.menu_notification_off).setVisible(notificationOn);
        switch (viewPager.getCurrentItem()) {
            case 0:
                menu.findItem(R.id.menu_select_date).setVisible(true);
                break;
            default:
                menu.findItem(R.id.menu_select_date).setVisible(false);
                break;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void initTabbedViewPager() {
        viewPager = findViewById(R.id.viewpager);
        fragmentPagerAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });
    }

    public interface ActionListener {
        public void handleFabClick();
        public void handleClearAllClick();
        public void handleSelectAllClick();
    }

    private void handleFabClick() {
        ((ActionListener)fragmentPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).handleFabClick();
    }

    private void handleClearAllClick() {
        ((ActionListener)fragmentPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).handleClearAllClick();
    }

    private void handleSelectAllClick() {
        ((ActionListener)fragmentPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).handleSelectAllClick();
    }

    private void handleSelectDateClick() {
        fragmentPagerAdapter.handleSelectDateClick(viewPager.getCurrentItem());
    }

    private void handleNotificationOnClick() {
        notificationOn = true;
        startService(new Intent(this, MyService.class));
        invalidateOptionsMenu();
    }

    private void handleNotificationOffClick() {
        notificationOn = false;
        stopService(new Intent(this, MyService.class));
        invalidateOptionsMenu();
    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                handleFabClick();
            }
        });
    }

    private void initDatabase() {
        database = Room.databaseBuilder(
                getApplicationContext(),
                CalendarDatabase.class,
                "calendar").build();
    }

    public CalendarDatabase getDatabase() {
        return database;
    }

    public void updatePages() {
        fragmentPagerAdapter.updatePages();
    }

    public void updateVisibility() {
        fragmentPagerAdapter.updateVisibility();
    }
}
