package cu.entumovil.snb.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import cu.entumovil.informationmodule.Util.ModuleUtil;
import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.utils.SNBEvent;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.adapters.PageAdapter;
import cu.entumovil.snb.ui.fragments.FavoritesFragment;
import cu.entumovil.snb.ui.fragments.GamesFragment;
import cu.entumovil.snb.ui.fragments.QualificationsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private static final String TAG = SNBApp.APP_TAG + MainActivity.class.getSimpleName();

    final private static int NETWORK_CHECK_INTERVAL = 5000;

    private TabLayout tabs;

    private PageAdapter pagerAdapter;

    private ViewPager viewPager;

    protected TextView txtConnectionStatus;

    protected boolean isCheckNetworkThreadActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarSetting();
        drawerSetting(toolbar);
        txtConnectionStatus = findViewById(R.id.txtConnectionStatus);
    }

    private void toolbarSetting() {
        QualificationsFragment qualificationsFragment = QualificationsFragment.newInstance();
        GamesFragment gamesFragment = GamesFragment.newInstance();
        FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(qualificationsFragment);
        pagerAdapter.addTab(gamesFragment);

//        pagerAdapter.addTab(favoritesFragment);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
//        tabs.getTabAt(1).select();
        tabs.setTabTextColors(Color.argb(150, 213, 213, 213), Color.WHITE);
    }

    private void drawerSetting(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final Dialog closeDialog = new Dialog(this);
            closeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            closeDialog.setContentView(R.layout.dialog_exit);

            TextView tittle = (TextView) closeDialog.findViewById(R.id.defaultDialogTitle);
            tittle.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));

            TextView btnOK = (TextView) closeDialog.findViewById(R.id.btn_ok);
            btnOK.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));

            TextView btnCancel = (TextView) closeDialog.findViewById(R.id.btn_cancel);
            btnCancel.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));

            closeDialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog.dismiss();
                }
            });
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog.dismiss();
                    MainActivity.super.onBackPressed();
                    finish();
                }
            });
            closeDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        Class activity = null;
        StatsType type = StatsType.NONE;

        if (id == R.id.nav_news) {
            activity = NewsActivity.class;
        } else if (id == R.id.nav_stats_single) {
            activity = StatsActivity.class;
            type = StatsType.SINGLE;
//        } else  if (id == R.id.nav_stats_collective) {
//            activity = StatsActivity.class;
//            type = StatsType.COLLECTIVE;
        } else if(id == R.id.nav_help) {
            ModuleUtil.initInformationActivity(MainActivity.this,
                    getResources().getString(R.string.app_name),
                    SNBApp.VERSION,
                    Utils.copyrightRange(SNBApp.application.getBaseContext()),
                    getResources().getString(R.string.app_description),
                    getResources().getString(R.string.app_description_end),
                    R.drawable.ic_launcher,-1,-1,-1,-1,R.color.colorPrimary,-1,-1,-1
            );
        } /*else if(id==R.id.nav_history) {
            activity = HistoryActivity.class;
        }*/ else if(id==R.id.nav_liders) {
            activity = LeadersActivity.class;
        }

        if (null != activity) {
            intent = new Intent().setClass(MainActivity.this, activity);
            if (!StatsType.NONE.equals(type)) intent.putExtra(StatsActivity.INTENT_STATSTYPE_HOLDER, type);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onOpenPremiumScreen(View v) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        Intent intent = new Intent().setClass(MainActivity.this, PremiumActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SNBApp.BUS.register(this);
        startCheckNetwork();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCheckNetwork();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCheckNetwork();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SNBApp.BUS.unregister(this);
        stopCheckNetwork();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            updateConnectionStatusUI(SNBEvent.CONNECTION_LOST);
            return false;
        } else {
            updateConnectionStatusUI(SNBEvent.CONNECTED);
        }
        return true;
    }

    protected void startCheckNetwork() {
        if (!isCheckNetworkThreadActive) {
            isCheckNetworkThreadActive = true;
            Thread checkNetworkThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isCheckNetworkThreadActive) {
                        isNetworkAvailable();
                        try {
                            Thread.sleep(NETWORK_CHECK_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            checkNetworkThread.start();
        }
    }

    protected void stopCheckNetwork() {
        isCheckNetworkThreadActive = false;
    }

    @Subscribe
    public void updateConnectionStatusUI(final SNBEvent snbEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (snbEvent) {
                    case CONNECTION_LOST:
                        txtConnectionStatus.setText(R.string.connection_close);
                        txtConnectionStatus.setVisibility(View.VISIBLE);
                        break;
                    case RECONNECTING:
                        txtConnectionStatus.setText(R.string.connection_reconnect);
                        txtConnectionStatus.setVisibility(View.VISIBLE);
                        break;
                    case CONNECTED:
                        stopCheckNetwork();
                        txtConnectionStatus.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

}
