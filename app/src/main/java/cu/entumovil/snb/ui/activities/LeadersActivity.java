package cu.entumovil.snb.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.utils.SNBEvent;
import cu.entumovil.snb.ui.adapters.PageAdapter;
import cu.entumovil.snb.ui.fragments.LeadersFragment;

public class LeadersActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = SNBApp.APP_TAG + LeadersActivity.class.getSimpleName();

    final private static int NETWORK_CHECK_INTERVAL = 5000;

    private PageAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabs;

    private LeadersFragment leaderHittersFragment, leaderPicthersFragment;

    protected TextView txtConnectionStatus;

    protected boolean isCheckNetworkThreadActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSetting();
        txtConnectionStatus = findViewById(R.id.txtConnectionStatus);
    }

    private void toolbarSetting() {
        leaderHittersFragment = LeadersFragment.newInstance();
        leaderHittersFragment.setTitle(SNBApp.application.getResources().getString(R.string.label_tab_stats_hitting));
        leaderPicthersFragment = LeadersFragment.newInstance();
        leaderPicthersFragment.setTitle(SNBApp.application.getResources().getString(R.string.label_tab_stats_pitching));

        pagerAdapter = new PageAdapter(getSupportFragmentManager());
       //  pagerAdapter.addTab(leaderPicthersFragment);
        pagerAdapter.addTab(leaderHittersFragment);


        viewPager = (ViewPager) findViewById(R.id.leadersViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabs = (TabLayout) findViewById(R.id.leaderTabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.argb(150, 213, 213, 213), Color.WHITE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
