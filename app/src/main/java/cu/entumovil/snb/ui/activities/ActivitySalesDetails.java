package cu.entumovil.snb.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.JewelCatalogManager;
import cu.entumovil.snb.core.managers.JewelManager;
import cu.entumovil.snb.core.models.Data;
import cu.entumovil.snb.core.models.JewelSalePost;
import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.models.jewel.Jewel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySalesDetails extends AppCompatActivity {

    ImageView image_jewel;
    Context view;
    private TextView model, price, profile_username, profile_date_compromised, profile_date_saled, label_profile_date_saled, label_profile_date_compromised,
            profile_email, profile_phone, profile_rol, details_less, details_count_pcs, btn_sale_cancel, details_plus, details_text_less;
    private Button buy_button_back, btn_buy;
    private User client;
    private Jewel jewel;
    private int count = 0;
    private int countJewelCatalog = 0;
    private SharedPreferences sharedPreferences;
    private ProgressBar swipeRefresh;
    private int idUserLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sale_details);
        Bundle bundle = getIntent().getExtras();
        view = getApplicationContext();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        jewel = (Jewel) bundle.get("jewel");
        idUserLogged = sharedPreferences.getInt("id", 0);

        ///-----------------------------------------------
        swipeRefresh = findViewById(R.id.swipeRefresh);
        model = findViewById(R.id.details_model);
        profile_username = findViewById(R.id.profile_username);
        profile_phone = findViewById(R.id.profile_phone);
        details_count_pcs = findViewById(R.id.details_count_pcs);
        profile_date_compromised = findViewById(R.id.profile_date_compromised);
        profile_date_saled = findViewById(R.id.profile_date_saled);
        label_profile_date_saled = findViewById(R.id.label_profile_date_saled);
        buy_button_back = findViewById(R.id.buy_button_back);
        btn_buy = findViewById(R.id.btn_buy);
        btn_sale_cancel = findViewById(R.id.btn_sale_cancel);
        image_jewel = findViewById(R.id.image_jewel);
        details_plus = findViewById(R.id.details_plus);
        details_less = findViewById(R.id.details_less);
        details_text_less = findViewById(R.id.details_text_less);
        label_profile_date_compromised = findViewById(R.id.label_profile_date_compromised);

        ///------------------------------------------------------
        model.setText(" Modelo: "+jewel.getJewl_catalogue().getModel() + "\n Código: " +jewel.getJewl_catalogue().getCode());

        profile_username.setText(jewel.getUsers_permissions_client().getUsername());
        profile_phone.setText(jewel.getUsers_permissions_client().getPhone());
        details_count_pcs.setText(String.valueOf(jewel.getCount()));
        profile_date_compromised.setText(jewel.getCreated_date());
        profile_date_saled.setText(jewel.getUpdated_date());

        Glide.with(getApplicationContext())
                .load(jewel.getJewl_catalogue().getImage_path())
                .placeholder(R.drawable.splash)
                .error(R.drawable.splash)
                .into(image_jewel);


        if (jewel.getStatus() != null) {
            if (jewel.getStatus().equalsIgnoreCase(getString(R.string.saled))) {
                label_profile_date_saled.setText(getString(R.string.date_buy));
                btn_buy.setVisibility(View.GONE);
                btn_sale_cancel.setVisibility(View.GONE);
                details_plus.setVisibility(View.GONE);
                details_less.setVisibility(View.GONE);
                details_text_less.setText("Cantidad Comprada");
            }
        } else {
            label_profile_date_saled.setVisibility(View.GONE);
            label_profile_date_compromised.setVisibility(View.GONE);
            btn_sale_cancel.setVisibility(View.GONE);

        }


        buy_button_back.setOnClickListener(view -> {
            startActivity(new Intent(view.getContext(), ActivityProfileUser.class));
            finish();
        });

        btn_buy.setOnClickListener(view -> {
            if (jewel.getStatus() != null) {
                updateJewel();
            } else {
                createJewel();
            }


        });

        btn_sale_cancel.setOnClickListener(view -> {

            updateJewelCatalogue();
        });


        details_plus.setOnClickListener(view -> {
            if (count < jewel.getJewl_catalogue().getAvailability()) {
                count++;
                details_count_pcs.setText(String.valueOf(count));
            } else {
                details_plus.setVisibility(View.INVISIBLE);
                details_less.setVisibility(View.VISIBLE);
            }

        });

        details_less.setOnClickListener(view -> {
            if (count >= jewel.getJewl_catalogue().getAvailability()) {
                count--;
                details_count_pcs.setText(String.valueOf(count));
            } else {
                details_plus.setVisibility(View.VISIBLE);
                details_less.setVisibility(View.INVISIBLE);

            }

        });


    }

    private void createJewel() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            JewelManager teamManager = new JewelManager();
            JewelSalePost jewelSalePost = new JewelSalePost(count, getString(R.string.compromised), jewel.getUsers_permissions_client().getId(), idUserLogged, jewel.getJewl_catalogue().getId());
            final Call<JsonObject> call = teamManager.createJewel(new Data(jewelSalePost));
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                    if (response.code() != 200) {
                        swipeRefresh.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), getString(R.string.message_data_error), Toast.LENGTH_SHORT).show();
                    } else {
                        updateJewelCatalogue();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Log.w(view.getString(R.string.message_unexpected_error), throwable.toString());
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.w(view.getString(R.string.message_unexpected_error), e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
        }

    }

    private void updateJewel() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            JewelManager teamManager = new JewelManager();
            Data.DataUpdateJewelSales dataUpdate = new Data.DataUpdateJewelSales(new Data.DataUpdateJewelSale("vendida"));
            final Call<JsonObject> call = teamManager.updateJewel(jewel.getId(), dataUpdate);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() != 200) {
                        swipeRefresh.setVisibility(View.GONE);
                        Toast.makeText(view, view.getString(R.string.message_data_error), Toast.LENGTH_SHORT).show();
                    } else {
                        swipeRefresh.setVisibility(View.GONE);
                        Intent intent = new Intent(view, ActivityProfileUser.class);
                        view.startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Log.w(view.getString(R.string.message_unexpected_error), throwable.toString());
                    Toast.makeText(view, view.getString(R.string.message_data_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.w(view.getString(R.string.message_unexpected_error), e.toString());
            Toast.makeText(view, view.getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
        }

    }

    private void updateJewelCatalogue() {
        try {
            swipeRefresh.setVisibility(View.VISIBLE);
            JewelCatalogManager teamManager = new JewelCatalogManager();
            if (jewel.getStatus() == null) {
                countJewelCatalog = jewel.getJewl_catalogue().getAvailability() - count;
            } else {
                countJewelCatalog = count + jewel.getJewl_catalogue().getAvailability();
            }
            Data.DataUpdate dataUpdate = new Data.DataUpdate(new Data.DataUpdateJewel(countJewelCatalog));
            final Call<JsonObject> call = teamManager.updateJewelCatalogue(jewel.getJewl_catalogue().getId(), dataUpdate);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() != 200) {
                        swipeRefresh.setVisibility(View.GONE);
                        Toast.makeText(view, view.getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    } else {
                        if (jewel.getStatus() != null) {
                            deleteJewel();
                        }
                        swipeRefresh.setVisibility(View.GONE);
                        Intent intent = new Intent(view, ActivityProfileUser.class);
                        //  intent.putExtra("userLogged", userLogged);
                        startActivity(intent);
                        finish();

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Toast.makeText(view, view.getString(R.string.message_data_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.w(view.getString(R.string.message_unexpected_error), e.toString());
            Toast.makeText(getApplicationContext(), view.getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
        }

    }

    private void deleteJewel() {
        try {
            JewelManager teamManager = new JewelManager();
            final Call<JsonObject> call = teamManager.deleteJewel(jewel.getId());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() != 200) {
                        swipeRefresh.setVisibility(View.GONE);
                        Toast.makeText(view, view.getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    } else {
                        swipeRefresh.setVisibility(View.GONE);
                        Intent intent = new Intent(view, ActivityProfileUser.class);
                        //  intent.putExtra("userLogged", userLogged);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    Toast.makeText(view, view.getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            Log.w(view.getString(R.string.message_unexpected_error), e.toString());
            Toast.makeText(view, view.getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            swipeRefresh.setVisibility(View.GONE);
        }

    }


}
