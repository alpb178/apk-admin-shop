package cu.entumovil.snb.ui.activities.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import cu.entumovil.snb.core.managers.JewelManager;
import cu.entumovil.snb.core.models.jewel.Jewel;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.adapters.admin.UserSaleAdminAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesHistorical extends AppCompatActivity {


    public ArrayList<Jewel> jewelsSale;
    public ArrayList<Jewel> jewelsWait;
    int count_text_sale, count_text_wait = 0;
    private TextView text_wait, text_sale, text_profile_user_record;
    private SharedPreferences sharedPreferences;
    private RecyclerView jewelsRecyclerViewWait;
    private RecyclerView jewelsRecyclerViewSale;
    private UserSaleAdminAdapter jewelsAdapterWait;
    private UserSaleAdminAdapter jewelsAdapterSale;
    private SwipeRefreshLayout swipeRefresh;
    private Button button_profile_record_back;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_jewls);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        id = (Integer) bundle.get("client");


        swipeRefresh = findViewById(R.id.swipeRefreshAlexGoldList);
        text_wait = findViewById(R.id.text_profile_vendor_wait);
        text_sale = findViewById(R.id.text_profile_vendor_sale);
        text_profile_user_record = findViewById(R.id.text_profile_user_record);
        button_profile_record_back = findViewById(R.id.button_profile_record_back);

        text_profile_user_record.setVisibility(View.VISIBLE);
        button_profile_record_back.setVisibility(View.VISIBLE);

        button_profile_record_back.setOnClickListener(view -> onBackPressed());

        text_wait.setOnClickListener(view1 -> {
                    if (count_text_wait == 0) {
                        jewelsRecyclerViewWait.setVisibility(View.VISIBLE);
                        count_text_wait++;
                    } else {
                        jewelsRecyclerViewWait.setVisibility(View.GONE);
                        count_text_wait--;
                    }

                }
        );

        text_sale.setOnClickListener(view1 -> {
                    if (count_text_sale == 0) {
                        jewelsRecyclerViewSale.setVisibility(View.VISIBLE);
                        count_text_sale++;
                    } else {
                        jewelsRecyclerViewSale.setVisibility(View.GONE);
                        count_text_sale--;
                    }
                }
        );

        swipeRefresh.setColorSchemeResources(
                R.color.colorAlexGoldPlaceHolder,
                R.color.background_white,
                R.color.colorAlexGoldYellow
        );
        swipeRefresh.setRefreshing(true);
        initRecyclerView();
        retrieveData();

    }


    private void initRecyclerView() {
        jewelsRecyclerViewSale = findViewById(R.id.recycler_alex_gold_list_sale);
        jewelsAdapterSale = new UserSaleAdminAdapter(jewelsSale, swipeRefresh, getApplicationContext());
        jewelsRecyclerViewSale.setAdapter(jewelsAdapterSale);
        jewelsRecyclerViewSale.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        jewelsRecyclerViewSale.setItemAnimator(new DefaultItemAnimator());


        jewelsRecyclerViewWait = findViewById(R.id.recycler_alex_gold_list_wait);
        jewelsAdapterWait = new UserSaleAdminAdapter(jewelsWait, swipeRefresh, getApplicationContext());
        jewelsRecyclerViewWait.setAdapter(jewelsAdapterWait);
        jewelsRecyclerViewWait.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        jewelsRecyclerViewWait.setItemAnimator(new DefaultItemAnimator());

        swipeRefresh.setOnRefreshListener(() -> retrieveData());
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            JewelManager teamManager = new JewelManager();
            final Call<JsonArray> call = teamManager.findByJewelByUser(id);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                    if (null != response.body()) {

                        jewelsAdapterSale.clear();
                        jewelsAdapterWait.clear();
                        JsonArray jsonArray = response.body().getAsJsonArray();
                        jewelsSale = new ArrayList<>();
                        jewelsWait = new ArrayList<>();

                        for (int x = 0; x < jsonArray.size(); x++) {
                            JsonObject element = jsonArray.get(x).getAsJsonArray().get(0).getAsJsonObject();

                            Jewel jewel = Utils.RetrieveJewel(element);

                            if (jewel.getStatus().equalsIgnoreCase(getString(R.string.saled)))
                                jewelsSale.add(jewel);

                            if (jewel.getStatus().equalsIgnoreCase(getString(R.string.compromised)))
                                jewelsWait.add(jewel);

                        }
                        jewelsAdapterSale.animateTo(jewelsSale);
                        jewelsRecyclerViewSale.scrollToPosition(0);
                        text_sale.setText(getString(R.string.jewel_sale) + " (" + jewelsSale.size() + ")");

                        jewelsAdapterWait.animateTo(jewelsWait);
                        jewelsRecyclerViewWait.scrollToPosition(0);
                        text_wait.setText(getString(R.string.jewel_wait) + " (" + jewelsWait.size() + ")");
                        swipeRefresh.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            Log.w("Error", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
        }

    }
}
