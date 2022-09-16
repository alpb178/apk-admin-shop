package cu.entumovil.snb.ui.activities;

import static java.util.Objects.isNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Cache;
import cu.entumovil.snb.core.db.models.Favorite;
import cu.entumovil.snb.core.managers.GameManager;
import cu.entumovil.snb.core.models.Boxscore;
import cu.entumovil.snb.core.models.Game;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.models.Linescore;
import cu.entumovil.snb.core.models.Pitcher;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.models.Team;
import cu.entumovil.snb.core.utils.FavoriteType;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.adapters.PageAdapter;
import cu.entumovil.snb.ui.fragments.DetailsHomeclubFragment;
import cu.entumovil.snb.ui.fragments.DetailsVisitorFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        View.OnCreateContextMenuListener, View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + GameDetailsActivity.class.getSimpleName();

    public static final String INTENT_GAME_HOLDER = "INTENT_GAME_HOLDER";

    private Context context;

    private long selectedTeamId;

    private String selectedTeamName;

    private Game game;

    private Linescore linescore;

    private ArrayList<Boxscore> boxscores;

    private PageAdapter pagerAdapter;

    private ViewPager viewPager;

    private TabLayout tabs;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private AppBarLayout appbar;

    private DetailsVisitorFragment detailsVisitorFragment;

    private DetailsHomeclubFragment detailsHomeclubFragment;

    private TextView txt_details_game_visitor_score, txt_details_game_homeclub_score, txt_details_game_date, txt_details_game_stadium, txt_details_game_inning;

    private TextView txt_details_team_name_visitor, txt_details_team_name_homeclub;
    private ImageView img_details_team_name_visitor, img_details_team_name_homeclub;

    private TextView inning_1_visitor, inning_2_visitor, inning_3_visitor, inning_4_visitor;
    private TextView inning_5_visitor, inning_6_visitor, inning_7_visitor, inning_8_visitor;
    private TextView inning_9_visitor, inning_ei_visitor, hits_visitor, errors_visitor;

    private TextView inning_1_homeclub, inning_2_homeclub, inning_3_homeclub, inning_4_homeclub;
    private TextView inning_5_homeclub, inning_6_homeclub, inning_7_homeclub, inning_8_homeclub;
    private TextView inning_9_homeclub, inning_ei_homeclub, hits_homeclub, errors_homeclub;

    private Dao<Cache, Long> dao;

    private Gson gson;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);
        context = this;
        game = (Game) getIntent().getExtras().get(INTENT_GAME_HOLDER);
        toolbarSetting();

        initializeHeader();
        initializeVisitorFields();
        initializeHomeclubFileds();

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.mCollapsingToolbarLayout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(mCollapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(mCollapsingToolbarLayout)) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_white));
                    mCollapsingToolbarLayout.setTitle(
                            game.getVS().concat(" ").concat(String.valueOf(game.getCVS())).concat(" - ") +
                            game.getHC().concat(" ").concat(String.valueOf(game.getCHC()))
                    );
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black));
                    mCollapsingToolbarLayout.setTitle(null);
                }
            }
        });

        gson = new Gson();
        try {
            dao = SNBApp.DB_HELPER.getCachesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadLinescore();
        boxscores = new ArrayList<>();
        loadBoxscore();
    }

    private void initializeHeader() {
        txt_details_team_name_visitor = (TextView) findViewById(R.id.txt_details_team_name_visitor);
        txt_details_team_name_homeclub = (TextView) findViewById(R.id.txt_details_team_name_homeclub);
        txt_details_game_visitor_score = (TextView) findViewById(R.id.txt_details_game_visitor_score);
        txt_details_game_homeclub_score = (TextView) findViewById(R.id.txt_details_game_homeclub_score);
        txt_details_game_date = (TextView) findViewById(R.id.txt_details_game_date);
        txt_details_game_stadium = (TextView) findViewById(R.id.txt_details_game_stadium);
        txt_details_game_inning = (TextView) findViewById(R.id.txt_details_game_inning);

        txt_details_team_name_visitor.setText(game.getVS());
        txt_details_team_name_homeclub.setText(game.getHC());
        txt_details_game_visitor_score.setText(String.valueOf(game.getCVS()));
        txt_details_game_homeclub_score.setText(String.valueOf(game.getCHC()));
        txt_details_game_date.setText(
            isToday()
                ? Utils.convert24hrsTo12Hrs(game.getHora())
                : game.getFecha().replace('/','.').concat(" - ").concat(Utils.convert24hrsTo12Hrs(game.getHora()))
        );
        txt_details_game_stadium.setText(game.getEstadio());
    }

    private void initializeVisitorFields() {
        txt_details_team_name_visitor = (TextView) findViewById(R.id.txt_details_team_name_visitor);
        img_details_team_name_visitor = (ImageView) findViewById(R.id.img_details_team_name_visitor);
        inning_1_visitor = (TextView) findViewById(R.id.inning_1_visitor);
        inning_2_visitor = (TextView) findViewById(R.id.inning_2_visitor);
        inning_3_visitor = (TextView) findViewById(R.id.inning_3_visitor);
        inning_4_visitor = (TextView) findViewById(R.id.inning_4_visitor);
        inning_5_visitor = (TextView) findViewById(R.id.inning_5_visitor);
        inning_6_visitor = (TextView) findViewById(R.id.inning_6_visitor);
        inning_7_visitor = (TextView) findViewById(R.id.inning_7_visitor);
        inning_8_visitor = (TextView) findViewById(R.id.inning_8_visitor);
        inning_9_visitor = (TextView) findViewById(R.id.inning_9_visitor);
        inning_ei_visitor = (TextView) findViewById(R.id.inning_ei_visitor);
        hits_visitor = (TextView) findViewById(R.id.hits_visitor);
        errors_visitor = (TextView) findViewById(R.id.errors_visitor);

        img_details_team_name_visitor.setOnCreateContextMenuListener(this);
        img_details_team_name_visitor.setOnClickListener(this);
        img_details_team_name_visitor.setOnLongClickListener(this);
    }

    private void initializeHomeclubFileds() {
        txt_details_team_name_homeclub = (TextView) findViewById(R.id.txt_details_team_name_homeclub);
        img_details_team_name_homeclub = (ImageView) findViewById(R.id.img_details_team_name_homeclub);
        inning_1_homeclub = (TextView) findViewById(R.id.inning_1_homeclub);
        inning_2_homeclub = (TextView) findViewById(R.id.inning_2_homeclub);
        inning_3_homeclub = (TextView) findViewById(R.id.inning_3_homeclub);
        inning_4_homeclub = (TextView) findViewById(R.id.inning_4_homeclub);
        inning_5_homeclub = (TextView) findViewById(R.id.inning_5_homeclub);
        inning_6_homeclub = (TextView) findViewById(R.id.inning_6_homeclub);
        inning_7_homeclub = (TextView) findViewById(R.id.inning_7_homeclub);
        inning_8_homeclub = (TextView) findViewById(R.id.inning_8_homeclub);
        inning_9_homeclub = (TextView) findViewById(R.id.inning_9_homeclub);
        inning_ei_homeclub = (TextView) findViewById(R.id.inning_ei_homeclub);
        hits_homeclub = (TextView) findViewById(R.id.hits_homeclub);
        errors_homeclub = (TextView) findViewById(R.id.errors_homeclub);

        img_details_team_name_homeclub.setOnCreateContextMenuListener(this);
        img_details_team_name_homeclub.setOnClickListener(this);
        img_details_team_name_homeclub.setOnLongClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void toolbarSetting() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_black));
        getSupportActionBar().setElevation(0f);
        setTitle(null);

        detailsVisitorFragment = DetailsVisitorFragment.newInstance();
        detailsHomeclubFragment = DetailsHomeclubFragment.newInstance();
        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        pagerAdapter.addTab(detailsVisitorFragment);
        pagerAdapter.addTab(detailsHomeclubFragment);

        viewPager = (ViewPager) findViewById(R.id.detailsViewPager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabs = (TabLayout) findViewById(R.id.gameDetailsTabs);
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
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.add(Menu.NONE, R.id.contextual_menu_players, Menu.NONE, context.getString(R.string.menu_contextual_players));
        contextMenu.add(Menu.NONE, R.id.contextual_menu_favorite, Menu.NONE, context.getString(R.string.menu_contextual_favorite));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.contextual_menu_favorite) {
            String msg = "";
            try {
                Dao<Favorite, Long> dao = SNBApp.DB_HELPER.getFavoritesDao();
                Favorite fav = new Favorite(selectedTeamId, FavoriteType.TEAM.toString(), selectedTeamName);
                Map<String, Object> map = new HashMap<>();
                map.put("id", fav.getId());
                map.put("favoriteType", FavoriteType.TEAM.toString());
                List<Favorite> l = dao.queryForFieldValues(map);
                if (l.size() == 0) {
                    dao.create(fav);
                    msg = context.getString(R.string.action_add_favorite);
                } else {
                    msg = context.getString(R.string.action_duplicate_favorite);
                }
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage());
            }
            Toast t = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            t.show();
        } else if (item.getItemId() == R.id.contextual_menu_players) {
            Team team = new Team(selectedTeamId, selectedTeamName, new ArrayList<Player>());
            Intent i = new Intent().setClass(GameDetailsActivity.this, TeamActivity.class);
            i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
            startActivity(i);
            setResult(Activity.RESULT_OK);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onLongClick(View view) {
//        if (view.getId() == R.id.img_details_team_name_visitor) {
//            selectedTeamId = game.getIdPerdedor();
//            selectedTeamName = game.getPerdedor();
//        } else if (view.getId() == R.id.img_details_team_name_homeclub) {
//            selectedTeamId = game.getIdganador();
//            selectedTeamName = game.getGanador();
//        }
        return false;
    }

    @Override
    public void onClick(View view) {
//        Team team = new Team();
//        if (view.getId() == R.id.img_details_team_name_visitor) {
//            team.setId(game.getIdPerdedor());
//            team.setName(game.getPerdedor());
//        } else if (view.getId() == R.id.img_details_team_name_homeclub) {
//            team.setId(game.getIdganador());
//            team.setName(game.getGanador());
//        }
//        Intent i = new Intent().setClass(GameDetailsActivity.this, TeamActivity.class);
//        i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
//        startActivity(i);
//        setResult(Activity.RESULT_OK);
    }

    private void loadLinescore() {
        try {
            GameManager gameManager = new GameManager();
            final Call<ArrayList<Linescore>> call = gameManager.linescore(game.getIdJ());
            call.enqueue(new Callback<ArrayList<Linescore>>() {
                @Override
                public void onResponse(Call<ArrayList<Linescore>> call, Response<ArrayList<Linescore>> response) {
                    if (null != response.body()) {
                        linescore = response.body().get(0);
                        linescore.setGameId(game.getIdJ());
                        fillLinescoreDetails();
                        try {
                            popCache();
                            Map<String, Object> fieldValues = new HashMap<>();
                            fieldValues.put("guiType", "linescore");
                            fieldValues.put("objectId", game.getIdJ());
                            List q = dao.queryForFieldValues(fieldValues);
                            if (!q.isEmpty()) {
                                DeleteBuilder<Cache, ?> deleteBuilder = dao.deleteBuilder();
                                deleteBuilder.where().eq("id", ((Cache) q.get(0)).getId());
                                deleteBuilder.delete();
                            }
                            Cache cache = new Cache("linescore", gson.toJson(linescore));
                            cache.setObjectId(Long.valueOf(game.getIdJ()));
                            dao.create(cache);
                        } catch (SQLException e) {
                            Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Linescore>> call, Throwable throwable) {
                    try {
                        Map<String, Object> fieldValues = new HashMap<>();
                        fieldValues.put("guiType", "linescore");
                        fieldValues.put("objectId", game.getIdJ());
                        List q = dao.queryForFieldValues(fieldValues);
                        if (!q.isEmpty()) {
                            Type listType = new TypeToken<Linescore>(){}.getType();
                            linescore = gson.fromJson(((Cache) q.get(0)).getJson(), listType);
                            fillLinescoreDetails();
                        } else {
                            Toast.makeText(context, getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            try {
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put("guiType", "linescore");
                fieldValues.put("objectId", game.getIdJ());
                List q = dao.queryForFieldValues(fieldValues);
                if (!q.isEmpty()) {
                    Type listType = new TypeToken<Linescore>(){}.getType();
                    linescore = gson.fromJson(((Cache) q.get(0)).getJson(), listType);
                    fillLinescoreDetails();
                } else {
                    Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException ex) {
                Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.getMessage());
            }
            Log.e(TAG, e.getMessage());
        }
    }

    private void loadBoxscore() {
        try {
            GameManager gameManager = new GameManager();
            final Call<Boxscore> call = gameManager.boxscore(game.getIdJ());
            call.enqueue(new Callback<Boxscore>() {
                @Override
                public void onResponse(Call<Boxscore> call, Response<Boxscore> response) {
                    if (null != response.body()) {
                        boxscores.add(response.body());
                        boxscores.get(0).setGameId(game.getIdJ());
                        fillBoxscoreDetails();
                        try {
                            Map<String, Object> fieldValues = new HashMap<>();
                            fieldValues.put("guiType", "boxscore");
                            fieldValues.put("objectId", game.getIdJ());
                            List q = dao.queryForFieldValues(fieldValues);
                            if (!q.isEmpty()) {
                                DeleteBuilder<Cache, ?> deleteBuilder = dao.deleteBuilder();
                                deleteBuilder.where().eq("id", ((Cache) q.get(0)).getId());
                                deleteBuilder.delete();
                            }
                            Cache cache = new Cache("boxscore", gson.toJson(boxscores.get(0)));
                            cache.setObjectId(Long.valueOf(game.getIdJ()));
                            dao.create(cache);
                        } catch (SQLException e) {
                            Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(Call<Boxscore> call, Throwable throwable) {
                    try {
                        Map<String, Object> fieldValues = new HashMap<>();
                        fieldValues.put("guiType", "boxscore");
                        fieldValues.put("objectId", game.getIdJ());
                        List q = dao.queryForFieldValues(fieldValues);
                        if (!q.isEmpty()) {
                            Type listType = new TypeToken<Boxscore>(){}.getType();
                            boxscores.add((Boxscore) gson.fromJson(((Cache) q.get(0)).getJson(), listType));
                            fillBoxscoreDetails();
                        } else {
                            Toast.makeText(context, getString(R.string.message_timeout), Toast.LENGTH_SHORT).show();
                        }
                    } catch (SQLException e) {
                        Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }
                    Log.e(TAG, throwable.getMessage());
                }
            });
        } catch (Exception e) {
            try {
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put("guiType", "boxscore");
                fieldValues.put("objectId", game.getIdJ());
                List q = dao.queryForFieldValues(fieldValues);
                if (!q.isEmpty()) {
                    Type listType = new TypeToken<Boxscore>(){}.getType();
                    boxscores.add((Boxscore) gson.fromJson(((Cache) q.get(0)).getJson(), listType));
                    fillBoxscoreDetails();
                } else {
                    Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException ex) {
                Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.getMessage());
            }
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        return game.getFecha().equals(Utils.formatDate(calendar.getTime(), false));
    }

    protected void onGoBack(View v) {
        finish();
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

    private void fillLinescoreDetails() {
        // Datos del equipo visitador
        txt_details_team_name_visitor.setText(TeamHelper.fromAcronym(linescore.getVS()));
        img_details_team_name_visitor.setImageResource(TeamHelper.convertAcronymToDrawableResource(linescore.getVS()));
        hits_visitor.setText(linescore.getVSHit());
        errors_visitor.setText(linescore.getVSErr());
        inning_1_visitor.setText(linescore.getVS01());
        inning_2_visitor.setText(linescore.getVS02());
        inning_3_visitor.setText(linescore.getVS03());
        inning_4_visitor.setText(linescore.getVS04());
        inning_5_visitor.setText(linescore.getVS05());
        inning_6_visitor.setText(linescore.getVS06());
        inning_7_visitor.setText(linescore.getVS07());
        inning_8_visitor.setText(linescore.getVS08());
        inning_9_visitor.setText(linescore.getVS09());

        // Datos del equipo homeclub
        txt_details_team_name_homeclub.setText(TeamHelper.fromAcronym(linescore.getHC()));
        img_details_team_name_homeclub.setImageResource(TeamHelper.convertAcronymToDrawableResource(linescore.getHC()));
        hits_homeclub.setText(linescore.getHCHit());
        errors_homeclub.setText(linescore.getHCErr());
        inning_1_homeclub.setText(linescore.getHC01());
        inning_2_homeclub.setText(linescore.getHC02());
        inning_3_homeclub.setText(linescore.getHC03());
        inning_4_homeclub.setText(linescore.getHC04());
        inning_5_homeclub.setText(linescore.getHC05());
        inning_6_homeclub.setText(linescore.getHC06());
        inning_7_homeclub.setText(linescore.getHC07());
        inning_8_homeclub.setText(linescore.getHC08());
        inning_9_homeclub.setText(linescore.getHC09());

        try {
            if (linescore.getCarVS() > linescore.getCarHC())
                txt_details_game_visitor_score.setTextColor(getResources().getColor(R.color.md_grey_800));
            else if (linescore.getCarVS() < linescore.getCarHC())
                txt_details_game_homeclub_score.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS01()) > 0)
                inning_1_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS02()) > 0)
                inning_2_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS03()) > 0)
                inning_3_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS04()) > 0)
                inning_4_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS05()) > 0)
                inning_5_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS06()) > 0)
                inning_6_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS07()) > 0)
                inning_7_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS08()) > 0)
                inning_8_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getVS09()) > 0)
                inning_9_visitor.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC01()) > 0)
                inning_1_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC02()) > 0)
                inning_2_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC03()) > 0)
                inning_3_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC04()) > 0)
                inning_4_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC05()) > 0)
                inning_5_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC06()) > 0)
                inning_6_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC07()) > 0)
                inning_7_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC08()) > 0)
                inning_8_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
            if (Integer.parseInt(linescore.getHC09()) > 0)
                inning_9_homeclub.setTextColor(getResources().getColor(R.color.md_grey_800));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @SuppressLint("NewApi")
    private void fillBoxscoreDetails() {
        detailsVisitorFragment.gameDetailsAdapter.clear();
        detailsHomeclubFragment.gameDetailsAdapter.clear();
     // Acrualmente la Api cdo el juego se encuentra detenido o suspendido la info muestra.
            if (!isNull(boxscores.get(0).getBateoHC()) && !isNull(boxscores.get(0).getPitcheoVS()) &&
                    !isNull(boxscores.get(0).getBateoVS()) && !isNull(boxscores.get(0).getBateoHC()) &&
                    !isNull(boxscores.get(0).getPitcheoHC())) {
                for (Hitter h : boxscores.get(0).getBateoHC()) {
                    detailsVisitorFragment.gameDetailsAdapter.addItem(detailsVisitorFragment.gameDetailsAdapter.getItemCount(), h);
                }

                for (Pitcher p : boxscores.get(0).getPitcheoVS()) {
                    detailsVisitorFragment.gameDetailsAdapter.addItem(detailsVisitorFragment.gameDetailsAdapter.getItemCount(), p);
                }
                for (Hitter h : boxscores.get(0).getBateoVS()) {
                detailsVisitorFragment.gameDetailsAdapter.addItem(detailsVisitorFragment.gameDetailsAdapter.getItemCount(), h);
            }

                for (Pitcher p : boxscores.get(0).getPitcheoHC()) {
                detailsHomeclubFragment.gameDetailsAdapter.addItem(detailsHomeclubFragment.gameDetailsAdapter.getItemCount(), p);
            }
            }
            else {
                tabs.setVisibility(View.INVISIBLE);
                viewPager.setVisibility(View.INVISIBLE);
            }

            detailsVisitorFragment.visitorRecyclerView.scrollToPosition(0);
            detailsHomeclubFragment.homeclubRecyclerView.scrollToPosition(0);
    }

    private void popCache() {
        try {
            QueryBuilder<Cache, Long> queryBuilder = dao.queryBuilder();
            Where<Cache, Long> where = queryBuilder.where();
            long count = where.or(where.eq("guiType", "linescore"), where.eq("guiType", "boxscore")).countOf() / 2;
            if (count >= Integer.valueOf(getString(R.string.COUNT_ELEMENT_CACHE))) {
                DeleteBuilder<Cache, ?> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq("objectId", game.getIdJ());
                deleteBuilder.delete();
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
