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
import cu.entumovil.snb.ui.fragments.RecordsFragment;

public class RecordActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = SNBApp.APP_TAG + RecordActivity.class.getSimpleName();

    private PageAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabs;

    private RecordsFragment serieRecordFragment, gameRecordFragment, specialRecordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSetting();
    }

    private void toolbarSetting() {
        serieRecordFragment = RecordsFragment.newInstance();
        serieRecordFragment.setTitle(SNBApp.application.getResources().getString(R.string.label_tab_record_serie));
        gameRecordFragment = RecordsFragment.newInstance();
        gameRecordFragment.setTitle(SNBApp.application.getResources().getString(R.string.label_tab_record_game));
        specialRecordFragment = RecordsFragment.newInstance();
        specialRecordFragment.setTitle(SNBApp.application.getResources().getString(R.string.label_tab_record_special));

        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(serieRecordFragment);
        pagerAdapter.addTab(gameRecordFragment);
        pagerAdapter.addTab(specialRecordFragment);

        viewPager = (ViewPager) findViewById(R.id.recordsViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabs = (TabLayout) findViewById(R.id.recordTabs);
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
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
