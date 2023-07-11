package cu.entumovil.snb.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.JewelCatalogManager;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.activities.admin.Admin;
import cu.entumovil.snb.ui.activities.admin.CreateJewel;
import cu.entumovil.snb.ui.adapters.ListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListCatalog extends AppCompatActivity {

    public ArrayList<JewelCatalog> teams;
    EditText text_find_alex_gold_list;
    TextView text_welcome;
    Button fab_profile_alex_gold_list, fab_find_alex_gold_list, fab_admin_alex_gold_list, btn_create_user;
    private Context context;
    private LinearLayout layout_Not_Jewel;
    private RecyclerView qualificationRecyclerView;
    private ListAdapter qualificationAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private String model = "all";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_catalog);
        getSupportActionBar().hide();
        context = this.getApplicationContext();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);


        // Build elements Layout
        swipeRefresh = findViewById(R.id.swipeRefreshAlexGoldList);
        fab_profile_alex_gold_list = findViewById(R.id.fab_profile_alex_gold_list);
        fab_find_alex_gold_list = findViewById(R.id.fab_find_alex_gold_list);
        fab_admin_alex_gold_list = findViewById(R.id.fab_admin_alex_gold_list);
        layout_Not_Jewel = findViewById(R.id.layout_Not_Jewel);
        text_find_alex_gold_list = findViewById(R.id.text_find_alex_gold_list);
        text_welcome = findViewById(R.id.text_welcome);
        btn_create_user = findViewById(R.id.btn_create_user);

        //Fill Components
        text_welcome.setText("Bienvenido " + sharedPreferences.getString("username", ""));

        swipeRefresh.setColorSchemeResources(
                R.color.colorAlexGoldPlaceHolder,
                R.color.background_white,
                R.color.colorAlexGoldYellow
        );
        swipeRefresh.setRefreshing(true);
        fab_profile_alex_gold_list.setOnClickListener(view -> {
            startActivity(new Intent(this, ActivityProfileUser.class));
            finish();
        });

        fab_admin_alex_gold_list.setOnClickListener(view -> {
            startActivity(new Intent(this, Admin.class));
        });

        btn_create_user.setOnClickListener(view -> {
            startActivity(new Intent(this, CreateJewel.class));
        });

        fab_find_alex_gold_list.setOnClickListener(view -> {
            if (!text_find_alex_gold_list.getText().toString().equalsIgnoreCase(""))
                model = text_find_alex_gold_list.getText().toString();
            else
                model = "all";
            retrieveData();
        });

        initRecyclerView();
        retrieveData();

    }

    private void initRecyclerView() {
        qualificationRecyclerView = findViewById(R.id.recycler_alex_gold_list);
        qualificationAdapter = new ListAdapter(teams, getApplicationContext());
        qualificationRecyclerView.setAdapter(qualificationAdapter);
        qualificationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        qualificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh.setOnRefreshListener(() -> retrieveData());
    }

    private void retrieveData() {
        try {

            disabled();
            JewelCatalogManager teamManager = new JewelCatalogManager();
            final Call<JsonArray> call = teamManager.findJewelCatalogueByModel(model);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (null != response.body()) {
                        qualificationAdapter.clear();
                        JsonArray jewel = response.body();
                        teams = new ArrayList<>();
                        for (int x = 0; x < jewel.size(); x++) {
                            JsonObject element = jewel.get(x).getAsJsonObject();

                            JewelCatalog jewelCatalog = Utils.RetrieveJewelCatalog(element);

                            teams.add(jewelCatalog);
                        }
                        qualificationRecyclerView.setVisibility(View.VISIBLE);
                        qualificationAdapter.animateTo(teams);
                        qualificationRecyclerView.scrollToPosition(0);
                    } else {
                        layout_Not_Jewel.setVisibility(View.VISIBLE);
                    }
                    enabled();
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    Log.w("Error",throwable.toString());
                    layout_Not_Jewel.setVisibility(View.VISIBLE);
                    enabled();
                }
            });
        } catch (Exception e) {
            layout_Not_Jewel.setVisibility(View.VISIBLE);
            enabled();
            Log.w("Error",e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

        }
    }

    public void disabled() {
        swipeRefresh.setRefreshing(true);
        layout_Not_Jewel.setVisibility(View.GONE);
        fab_find_alex_gold_list.setEnabled(false);
        text_find_alex_gold_list.setEnabled(false);
        fab_profile_alex_gold_list.setEnabled(false);
    }

    public void enabled() {
        fab_find_alex_gold_list.setEnabled(true);
        text_find_alex_gold_list.setEnabled(true);
        fab_profile_alex_gold_list.setEnabled(true);
        swipeRefresh.setRefreshing(false);
    }
}



