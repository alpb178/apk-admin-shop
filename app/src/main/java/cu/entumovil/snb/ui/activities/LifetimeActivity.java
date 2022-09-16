package cu.entumovil.snb.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.ui.adapters.PageAdapter;
import cu.entumovil.snb.ui.fragments.LifeHittersFragment;
import cu.entumovil.snb.ui.fragments.LifePitchersFragment;

public class LifetimeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = SNBApp.APP_TAG + LifetimeActivity.class.getSimpleName();

    private PageAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabs;

    public LifeHittersFragment lifeHittersFragment;

    private LifePitchersFragment lifePitchersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifetime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSetting();
    }

    private void toolbarSetting() {
        lifeHittersFragment = LifeHittersFragment.newInstance();
        lifePitchersFragment = LifePitchersFragment.newInstance();

        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(lifeHittersFragment);
        pagerAdapter.addTab(lifePitchersFragment);

        viewPager = (ViewPager) findViewById(R.id.lifeViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabs = (TabLayout) findViewById(R.id.lifeTabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.rgb(255, 204, 128), Color.WHITE);
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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
