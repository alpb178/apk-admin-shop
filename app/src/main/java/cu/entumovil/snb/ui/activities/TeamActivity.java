package cu.entumovil.snb.ui.activities;

import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.models.Team;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.adapters.PlayerAdapter;

public class TeamActivity extends AppCompatActivity {

    private static final String TAG = SNBApp.APP_TAG + TeamActivity.class.getSimpleName();

    public static final String INTENT_TEAM_HOLDER = "INTENT_TEAM_HOLDER";

    private ImageView img_team_bg, img_team_logo;

    private ArrayList<Player> players;

    private RecyclerView playerRecyclerView;

    private PlayerAdapter playerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitleEnabled(false);

        Team team = (Team) getIntent().getExtras().get(INTENT_TEAM_HOLDER);
        setTitle(team.getName());

        img_team_logo = (ImageView) findViewById(R.id.img_team_logo);
        img_team_logo.setImageResource(TeamHelper.convertAcronymToDrawableResource(team.getName()));
        img_team_bg = (ImageView) findViewById(R.id.img_team_bg);
        img_team_bg.setColorFilter(ContextCompat.getColor(this, TeamHelper.convertAcronymToColorResource(team.getName())));

        initRecyclerView();
        retrieveData();
    }

    private void initRecyclerView() {
        playerRecyclerView = (RecyclerView) findViewById(R.id.player_list);
        playerAdapter = new PlayerAdapter(players, this);
        playerRecyclerView.setAdapter(playerAdapter);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        playerRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void retrieveData() {
        try {
            //TODO: Eliminar Fake fill cuando se haga la peticion real al servidor
            // Fake fill
//            Gson gson = new GsonBuilder()
//                    .setDateFormat("dd/mm/yyyy")
//                    .create();
//            players = gson.fromJson(getString(R.string.json_players),
//                    new TypeToken<List<Player>>() {}.getType());
            playerAdapter.clear();
            playerAdapter.animateTo(players);
            playerRecyclerView.scrollToPosition(0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
