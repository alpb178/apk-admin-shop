package cu.entumovil.snb.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import cu.entumovil.snb.ui.adapters.UserSaleAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileJewelsFragment extends Fragment {

    public ArrayList<Jewel> jewelsSale;
    public ArrayList<Jewel> jewelsWait;
    TextView text_wait, text_sale;
    int count_text_sale, count_text_wait = 0;
    SharedPreferences sharedPreferences;
    private Context context;
    private View rootView;
    private RecyclerView jewelsRecyclerViewWait;
    private RecyclerView jewelsRecyclerViewSale;
    private UserSaleAdapter jewelsAdapterWait;
    private UserSaleAdapter jewelsAdapterSale;
    private SwipeRefreshLayout swipeRefresh;

    public ProfileJewelsFragment() {

    }

    public static ProfileJewelsFragment newInstance(
    ) {
        ProfileJewelsFragment fragment = new ProfileJewelsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_jewls, container, false);
        rootView = view;
        context = view.getContext();
        setHasOptionsMenu(true);
        sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        // Build elements Layout
        swipeRefresh = rootView.findViewById(R.id.swipeRefreshAlexGoldList);
        text_sale = rootView.findViewById(R.id.text_profile_vendor_sale);
        text_wait = rootView.findViewById(R.id.text_profile_vendor_wait);
        // fill element
        swipeRefresh.setColorSchemeResources(
                R.color.colorAlexGoldPlaceHolder,
                R.color.background_white,
                R.color.colorAlexGoldYellow
        );

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

        swipeRefresh.setRefreshing(true);
        initRecyclerView();
        retrieveData();

        return view;
    }

    @Override
    public String toString() {
        return "Historial Ventas";
    }

    private void initRecyclerView() {
        jewelsRecyclerViewSale = rootView.findViewById(R.id.recycler_alex_gold_list_sale);
        jewelsAdapterSale = new UserSaleAdapter(jewelsSale, swipeRefresh,getContext());
        jewelsRecyclerViewSale.setAdapter(jewelsAdapterSale);
        jewelsRecyclerViewSale.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        jewelsRecyclerViewSale.setItemAnimator(new DefaultItemAnimator());


        jewelsRecyclerViewWait = rootView.findViewById(R.id.recycler_alex_gold_list_wait);
        jewelsAdapterWait = new UserSaleAdapter(jewelsWait, swipeRefresh,getContext());
        jewelsRecyclerViewWait.setAdapter(jewelsAdapterWait);
        jewelsRecyclerViewWait.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        jewelsRecyclerViewWait.setItemAnimator(new DefaultItemAnimator());

        swipeRefresh.setOnRefreshListener(() -> retrieveData());
    }

    private void retrieveData() {
        try {
            swipeRefresh.setRefreshing(true);
            JewelManager teamManager = new JewelManager();
            final Call<JsonArray> call = teamManager.findJewelByVendor(sharedPreferences.getInt("id", -1));
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
                    Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {

            Toast.makeText(context, getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setRefreshing(false);
        }

    }

}
