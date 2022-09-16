package cu.entumovil.snb.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Subscribe;
import androidx.appcompat.app.AppCompatActivity;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.utils.SNBEvent;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.adapters.PageAdapter;
import cu.entumovil.snb.ui.fragments.StatsFieldersFragment;
import cu.entumovil.snb.ui.fragments.StatsHittersFragment;
import cu.entumovil.snb.ui.fragments.StatsPitchersFragment;

public class  StatsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = SNBApp.APP_TAG + StatsActivity.class.getSimpleName();

    public static final String INTENT_STATSTYPE_HOLDER = "INTENT_STATSTYPE_HOLDER";

    final private static int NETWORK_CHECK_INTERVAL = 5000;

    private PageAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabs;

    private StatsType statsType;

    private LinearLayout layoutRowSelectedTeam;

    public StatsHittersFragment statsHittersFragment;

    private StatsPitchersFragment statsPitchersFragment;

    private StatsFieldersFragment statsFieldersFragment;

    private boolean showAllMenuFilter;

    private Menu menu;

    private String title;

    protected TextView txtConnectionStatus;

    protected boolean isCheckNetworkThreadActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        statsType = (StatsType) getIntent().getExtras().get(INTENT_STATSTYPE_HOLDER);
        switch (statsType) {
            case SINGLE:
                title = getString(R.string.title_activity_stats_single);
                break;
            case COLLECTIVE:
                title = getString(R.string.title_activity_stats_collective);
                break;
            case SPECIALIZED:
                title = getString(R.string.title_activity_stats_specialized);
                setTheme(R.style.AppThemePremium);
                break;
        }

        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSetting();
        txtConnectionStatus = findViewById(R.id.txtConnectionStatus);
    }

    private void toolbarSetting() {
        statsHittersFragment = StatsHittersFragment.newInstance(statsType);
        statsPitchersFragment = StatsPitchersFragment.newInstance(statsType);
        statsFieldersFragment = StatsFieldersFragment.newInstance(statsType);

        if (StatsType.COLLECTIVE.equals(statsType)) {
            layoutRowSelectedTeam = findViewById(R.id.layoutRowSelectedTeam);
        }

        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(statsHittersFragment);
        pagerAdapter.addTab(statsPitchersFragment);
        if (!StatsType.SPECIALIZED.equals(statsType)) pagerAdapter.addTab(statsFieldersFragment);

        viewPager = findViewById(R.id.statsViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabs = findViewById(R.id.statsTabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.argb(150, 213, 213, 213), Color.WHITE);

        switch (statsType) {
            case SPECIALIZED:
                tabs.setBackgroundColor(getResources().getColor(R.color.md_orange_deep_400));
                tabs.setTabTextColors(Color.rgb(255, 204, 128), Color.WHITE);
                tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.md_orange_deep_800));
                break;
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.team_stats_menu, menu);
        if (StatsType.COLLECTIVE.equals(statsType)) {
            MenuItem item = menu.findItem(R.id.clean_team_selection);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clean_team_selection) {
            layoutRowSelectedTeam.removeAllViews();
            layoutRowSelectedTeam.setVisibility(View.GONE);
            statsHittersFragment.setTeamId(0);
            statsHittersFragment.reloadFragment(true);
            item.setVisible(false);
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadSelectedTeam(Hitter h) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.row_stats_hitters, layoutRowSelectedTeam, true);
        ImageView imgPhoto = (ImageView) v.findViewById(R.id.img_stats_player_photo);
        ImageView imgArrow = (ImageView) v.findViewById(R.id.img_arrow_right);
        TextView player_name = (TextView) v.findViewById(R.id.lbl_stats_player_name);
        TextView player_team = (TextView) v.findViewById(R.id.lbl_stats_player_team);
        TextView player_average = (TextView) v.findViewById(R.id.lbl_stats_player_average);
        TextView player_hits = (TextView) v.findViewById(R.id.lbl_stats_player_hits);
        TextView player_runs = (TextView) v.findViewById(R.id.lbl_stats_player_runs);
        TextView player_runsimpulsed = (TextView) v.findViewById(R.id.lbl_stats_player_runsimpulsed);
        TextView player_homeruns = (TextView) v.findViewById(R.id.lbl_stats_player_homeruns);
        TextView player_obp = (TextView) v.findViewById(R.id.lbl_stats_player_obp);
        TextView player_ops = (TextView) v.findViewById(R.id.lbl_stats_player_ops);
        TextView player_ballkk = (TextView) v.findViewById(R.id.lbl_stats_player_ballkk);

        imgPhoto.setImageResource(TeamHelper.convertAcronymToDrawableResource(h.getSigla()));
        player_name.setText(h.getNombre());
        player_team.setVisibility(View.INVISIBLE);
        player_average.setText(h.getAVE());
        player_hits.setText(String.valueOf(h.getH()));
        player_runs.setText(String.valueOf(h.getCA()));
        player_runsimpulsed.setText(String.valueOf(h.getCI()));
        player_homeruns.setText(String.valueOf(h.getHR()));
        player_obp.setText(h.getOBP());
        player_ops.setText(h.getOPS());
        player_ballkk.setText(String.valueOf(h.getBB()).concat("/").concat(String.valueOf(h.getKK())));

        MenuItem item = menu.findItem(R.id.clean_team_selection);
        item.setVisible(true);
        layoutRowSelectedTeam.setBackgroundColor(ContextCompat.getColor(this, R.color.md_grey_200));
        layoutRowSelectedTeam.setVisibility(View.VISIBLE);
        statsHittersFragment.setTeamId(21);
        statsHittersFragment.reloadFragment(false);
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
