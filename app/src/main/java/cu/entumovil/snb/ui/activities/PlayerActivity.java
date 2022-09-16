package cu.entumovil.snb.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ImageView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.adapters.PlayerDetailsPageAdapter;
import cu.entumovil.snb.ui.fragments.PlayerStatsFragment;

public class PlayerActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener {

    private static final String TAG = SNBApp.APP_TAG + PlayerActivity.class.getSimpleName();

    public static final String INTENT_PLAYER_HOLDER = "INTENT_PLAYER_HOLDER";

    private PlayerDetailsPageAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabs;

    private ImageView img_player_picture, img_team_logo;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        player = (Player) getIntent().getExtras().get(INTENT_PLAYER_HOLDER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.MONOSPACE);

        setTitle(player.getNombre());
        toolbarSetting();

        Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.player_no_photo);
        CustomImage roundedImage = new CustomImage(photo, true);
        img_player_picture = (ImageView) findViewById(R.id.img_player_picture);
        img_player_picture.setImageDrawable(roundedImage);
        img_team_logo = (ImageView) findViewById(R.id.img_team_logo);
        img_team_logo.setImageResource(TeamHelper.convertAcronymToDrawableResource(player.getSigla()));
//        collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, TeamHelper.convertAcronymToColorResource(player.getSigla())));
//        tabs.setBackgroundColor(Color.WHITE);
//        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(this, TeamHelper.convertAcronymToColorResource(player.getSigla())));
    }

    private void toolbarSetting() {
//        PlayerDetailsFragment playerDetailsFragment = PlayerDetailsFragment.newInstance();
        PlayerStatsFragment playerStatsFragment = PlayerStatsFragment.newInstance(player);
        pagerAdapter = new PlayerDetailsPageAdapter(getSupportFragmentManager());
//        pagerAdapter.addTab(playerDetailsFragment);
        pagerAdapter.addTab(playerStatsFragment);

        viewPager = (ViewPager) findViewById(R.id.playerDetailsViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabs = (TabLayout) findViewById(R.id.playerDetailsTabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabTextColors(Color.argb(150, 213, 213, 213), Color.WHITE);
//        tabs.setTabTextColors(Color.rgb(213, 213, 213), Color.BLACK);
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
