package cu.entumovil.snb.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import cu.entumovil.snb.R;
import cu.entumovil.snb.ui.adapters.PageAdapter;
import cu.entumovil.snb.ui.fragments.ProfileDetailsFragment;
import cu.entumovil.snb.ui.fragments.ProfileJewelsFragment;

public class ActivityProfileUser extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private  Button btn_back;
    private TabLayout tabs;
    private PageAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        getSupportActionBar().hide();
        // Build elements Layout
        btn_back = findViewById(R.id.profile_button_back);
        // fill element
        btn_back.setOnClickListener(view -> {
            startActivity(new Intent(view.getContext(), ActivityListCatalog.class));
            finish();
        });

        toolbarSetting();
    }

    private void toolbarSetting() {
        ProfileJewelsFragment profileJewlsFragment = ProfileJewelsFragment.newInstance();
        ProfileDetailsFragment profileDetailsFragment = ProfileDetailsFragment.newInstance();
        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(profileJewlsFragment);
        pagerAdapter.addTab(profileDetailsFragment);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        tabs = findViewById(R.id.tabs);
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
}
