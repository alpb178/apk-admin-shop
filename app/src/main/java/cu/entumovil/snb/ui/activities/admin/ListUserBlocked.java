package cu.entumovil.snb.ui.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.Users;
import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;
import cu.entumovil.snb.ui.adapters.UserAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUserBlocked extends AppCompatActivity {


    public ArrayList<User> teams;

    Button btn_register_back, fab_find_alex_gold_list, btn_create_user;
    private SwipeRefreshLayout swipeRefreshVendor;
    private LinearLayout layout_Not_Jewel;
    private RecyclerView qualificationRecyclerView;
    private UserAdapter qualificationAdapter;
    private JewelCatalog jewel;
    private TextView text_find_alex_gold_list, text_header;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        getSupportActionBar().hide();

        username = "all";

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            jewel = (JewelCatalog) bundle.get("jewel");
        }


        btn_register_back = findViewById(R.id.register_button_back);
        fab_find_alex_gold_list = findViewById(R.id.fab_find_alex_gold_list);
        text_find_alex_gold_list = findViewById(R.id.text_find_alex_gold_list);
        layout_Not_Jewel = findViewById(R.id.layout_Not_Jewel);
        text_header = findViewById(R.id.text_header);
        swipeRefreshVendor = findViewById(R.id.swipeRefreshAlexGoldListVendor);

        text_header.setText("Listado de Usuarios Bloqueados");


        swipeRefreshVendor.setColorSchemeResources(
                R.color.colorAlexGoldPlaceHolder,
                R.color.background_white,
                R.color.colorAlexGoldYellow
        );
        swipeRefreshVendor.setRefreshing(true);


        btn_register_back.setOnClickListener(view ->
                {
                    startActivity(new Intent().setClass(ListUserBlocked.this, Admin.class));
                    finish();
                }
        );


        fab_find_alex_gold_list.setOnClickListener(view -> {
            if (!text_find_alex_gold_list.getText().toString().equalsIgnoreCase(""))
                username = text_find_alex_gold_list.getText().toString();
            else
                username = "all";

            retrieveData();

        });

        initRecyclerView();

        retrieveData();

    }

    private void initRecyclerView() {
        qualificationRecyclerView = findViewById(R.id.recycler_alex_gold_list);
        qualificationAdapter = new UserAdapter(teams, jewel, getApplicationContext());
        qualificationRecyclerView.setAdapter(qualificationAdapter);
        qualificationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        qualificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshVendor.setOnRefreshListener(() -> retrieveData());
    }

    private void retrieveData() {
        try {
            swipeRefreshVendor.setRefreshing(true);
            disableField();
            layout_Not_Jewel.setVisibility(View.GONE);
            Users teamManager = new Users();
            final Call<JsonArray> call = teamManager.findUserByUserName(username);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if (null != response.body()) {
                        qualificationAdapter.clear();
                        JsonArray jewls = response.body().getAsJsonArray();
                        teams = new ArrayList<>();
                        for (int x = 0; x < jewls.size(); x++) {
                            JsonElement element = jewls.get(x);
                            int id = element.getAsJsonObject().get("id").getAsInt();
                            String username = element.getAsJsonObject().get("username").getAsString();
                            String email = element.getAsJsonObject().get("email").getAsString();
                            String rol = element.getAsJsonObject().get("rol").getAsString();
                            String vendor = element.getAsJsonObject().get("vendedor").getAsString();
                            String phone = element.getAsJsonObject().get("phone").getAsString();
                            String name = element.getAsJsonObject().get("name").getAsString();
                            String lastName = element.getAsJsonObject().get("last_name").getAsString();
                            int age = element.getAsJsonObject().get("age").getAsInt();
                            int count_jewel = element.getAsJsonObject().get("count_jewl").getAsInt();
                            String genre = element.getAsJsonObject().get("genre").getAsString();
                            Boolean blocked = element.getAsJsonObject().get("blocked").getAsBoolean();
                            User user = new User(id, username, email, phone, rol, name, lastName, age, genre, count_jewel, vendor, blocked);

                            if (blocked) teams.add(user);

                        }
                        qualificationRecyclerView.setVisibility(View.VISIBLE);
                        qualificationAdapter.animateTo(teams);
                        qualificationRecyclerView.scrollToPosition(0);
                        enableField();
                        swipeRefreshVendor.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefreshVendor.setRefreshing(false);
                    layout_Not_Jewel.setVisibility(View.VISIBLE);
                    enableField();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefreshVendor.setRefreshing(false);
            enableField();

        }

    }

    public void enableField() {
        fab_find_alex_gold_list.setEnabled(true);
        text_find_alex_gold_list.setEnabled(true);
        btn_register_back.setEnabled(true);
    }

    public void disableField() {
        btn_register_back.setEnabled(false);
        text_find_alex_gold_list.setEnabled(false);
        btn_register_back.setEnabled(false);
    }

}