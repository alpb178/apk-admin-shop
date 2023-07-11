package cu.entumovil.snb.ui.activities.admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.managers.ImageManager;
import cu.entumovil.snb.core.managers.JewelCatalogManager;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogCreate;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogPost;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalogPut;
import cu.entumovil.snb.core.utils.FileUtil;
import cu.entumovil.snb.ui.activities.ActivityListCatalog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateJewel extends AppCompatActivity {

    EditText create_model, create_weight, create_price, create_carats, create_large, create_count, create_description;
    ProgressBar swipeRefresh;
    JewelCatalog jewelEdit;
    ImageView imageView;
    int SELECT_PICTURE = 200;
    int imageId = 0;
    String imagePath;
    Uri selectedImageUri = null;
    private Button btn_back, btn_register, selectImage;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_jewel_catalogue);
        getSupportActionBar().hide();

       sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        btn_register = findViewById(R.id.btn_register);
        btn_back = findViewById(R.id.register_button_back);
        selectImage = findViewById(R.id.selectImage);
        create_model = findViewById(R.id.create_model);
        create_price = findViewById(R.id.create_price);
        create_carats = findViewById(R.id.create_carats);
        create_large = findViewById(R.id.create_large);
        create_count = findViewById(R.id.create_count);
        create_description = findViewById(R.id.create_description);
        create_weight = findViewById(R.id.create_weight);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        imageView = findViewById(R.id.previewImage);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            jewelEdit = (JewelCatalog) bundle.get("jewel");
            btn_register.setText(R.string.edit_jewel);
            create_model.setText(jewelEdit.getModel());
            create_description.setText(jewelEdit.getDescription());
            create_carats.setText(String.valueOf(jewelEdit.getCarats()));
            create_large.setText(String.valueOf(jewelEdit.getLarge()));
            create_price.setText(String.valueOf(jewelEdit.getPrice()));
            create_weight.setText(String.valueOf(jewelEdit.getWeight()));
            create_count.setText(String.valueOf(jewelEdit.getAvailability()));
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(jewelEdit.getImage_path())
                    .placeholder(R.drawable.splash)
                    .error(R.drawable.splash)
                    .into(imageView);
            imageId = jewelEdit.getImage();
            imagePath = jewelEdit.getImage_path();
        }


        selectImage.setOnClickListener(view -> imageChooser());

        btn_back.setOnClickListener(view -> {
            startActivity(new Intent().setClass(getApplicationContext(), ActivityListCatalog.class));
        });
        btn_register.setOnClickListener(view ->
                doLogin());
    }


    void doLogin() {


        if (create_model.getText().toString().equalsIgnoreCase("")) {
            create_model.setError(getString(R.string.required));
            return;
        }
        if (create_price.getText().toString().equalsIgnoreCase("")) {
            create_price.setError(getString(R.string.required));
            return;
        }
        if (create_carats.getText().toString().equalsIgnoreCase("")) {
            create_carats.setError(getString(R.string.required));
            return;
        }

        if (create_large.getText().toString().equalsIgnoreCase("")) {
            create_large.setError(getString(R.string.required));
            return;
        }

        if (create_count.getText().toString().equalsIgnoreCase("")) {
            create_count.setError(getString(R.string.required));
            return;
        }
        if (imageView.getVisibility() == View.GONE) {
            Toast.makeText(getApplicationContext(), "La Imagen es Obligatoria", Toast.LENGTH_SHORT).show();
            return;
        }

        uploadImage(selectedImageUri);


    }


    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void uploadImage(Uri fileUri) {
        disableField();

        if (fileUri != null) {
            try {

                disableField();
                File file = FileUtil.from(getApplicationContext(), fileUri);
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(fileUri)),
                                file
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                ImageManager teamManager = new ImageManager();
                Call<JsonArray> call = teamManager.uploadImage(body);
                call.enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        if (response.code() != 200) {
                            swipeRefresh.setVisibility(View.GONE);
                            enableField();
                            Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                        } else {

                            JsonArray image = response.body().getAsJsonArray();
                            for (int x = 0; x < image.size(); x++) {
                                JsonElement element = image.get(x);
                                imageId = element.getAsJsonObject().get("id").getAsInt();
                                imagePath = element.getAsJsonObject().get("formats").getAsJsonObject().get("large").getAsJsonObject().get("url").getAsString();
                            }
                            if (getIntent().getExtras() != null)
                                updateJewelCatalog();
                            else
                                createJewelCatalog();

                        }
                        swipeRefresh.setVisibility(View.GONE);
                        enableField();
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable throwable) {
                        swipeRefresh.setVisibility(View.GONE);
                        enableField();
                        Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (Exception e) {
                swipeRefresh.setVisibility(View.GONE);
                enableField();
                Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
            }

        } else if (getIntent().getExtras() != null)
            updateJewelCatalog();
        else {
            createJewelCatalog();

        }

    }

    private void createJewelCatalog() {
        try {
            JewelCatalogManager teamManager = new JewelCatalogManager();
            String model = create_model.getText().toString();
            String description = create_description.getText().toString();
            float carats = Float.valueOf(create_carats.getText().toString());
            float large = Float.valueOf(create_large.getText().toString());
            float price = Float.valueOf(create_price.getText().toString());
            float weight = Float.valueOf(create_weight.getText().toString());
            int count = Integer.valueOf(create_count.getText().toString());

            JewelCatalogCreate jewel = new JewelCatalogCreate(model, imageId, imagePath, weight, carats, price, large, description, count, sharedPreferences.getInt("id", 0));
            JewelCatalogPost jewelPost = new JewelCatalogPost(jewel);


            final Call<JsonObject> call = teamManager.createJewelCatalog(jewelPost);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.code() != 200) {
                        enableField();
                        Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent().setClass(getApplicationContext(), ActivityListCatalog.class);
                        startActivity(intent);

                    }
                    enableField();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable throwable) {
                    enableField();
                    Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            enableField();
            Log.w("Error", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
        }


    }

    private void updateJewelCatalog() {
        try {

            JewelCatalogManager teamManager = new JewelCatalogManager();
            String model = create_model.getText().toString();
            String description = create_description.getText().toString();
            float carats = Float.valueOf(create_carats.getText().toString());
            float large = Float.valueOf(create_large.getText().toString());
            float price = Float.valueOf(create_price.getText().toString());
            float weight = Float.valueOf(create_weight.getText().toString());
            int count = Integer.valueOf(create_count.getText().toString());

            if (getIntent().getExtras() != null) {
                jewelEdit.setModel(model);
                jewelEdit.setDescription(description);
                jewelEdit.setCarats(carats);
                jewelEdit.setPrice(large);
                jewelEdit.setWeight(weight);
                jewelEdit.setPrice(price);
                jewelEdit.setAvailability(count);
                jewelEdit.setImage(imageId);
                jewelEdit.setImage_path(imagePath);
                jewelEdit.setOwner(sharedPreferences.getInt("id", 0));

                final Call<JsonObject> call = teamManager.updateJewelCatalog(jewelEdit.getId(), new JewelCatalogPut(jewelEdit));
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() != 200) {
                            enableField();
                            Toast.makeText(getApplicationContext(), "Error en los Datos", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent().setClass(getApplicationContext(), ActivityListCatalog.class);
                            startActivity(intent);

                        }
                        enableField();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable throwable) {
                        enableField();
                        Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        } catch (Exception e) {
            enableField();
            Log.w("Error", e.toString());
            Toast.makeText(getApplicationContext(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
        }
    }


    public void enableField() {
        swipeRefresh.setVisibility(View.GONE);
        btn_register.setVisibility(View.VISIBLE);
        btn_register.setEnabled(true);
        btn_back.setEnabled(true);
        create_model.setEnabled(true);
        create_price.setEnabled(true);
        create_carats.setEnabled(true);
        create_large.setEnabled(true);
        create_count.setEnabled(true);
        create_description.setEnabled(true);


    }

    public void disableField() {
        swipeRefresh.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);
        btn_back.setEnabled(false);
        create_model.setEnabled(false);
        create_price.setEnabled(false);
        create_carats.setEnabled(false);
        create_large.setEnabled(false);
        create_count.setEnabled(false);
        create_description.setEnabled(false);

    }
}


