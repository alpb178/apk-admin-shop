package cu.entumovil.snb.ui.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import cu.entumovil.snb.R;
import cu.entumovil.snb.ui.activities.ActivityListCatalog;
import cu.entumovil.snb.ui.activities.ActivityListUser;

public class Admin extends AppCompatActivity {

    private Button btn_back;

    private ImageView img_admin_user, img_admin_vendor, img_admin_jewel, img_admin_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().hide();

        // Build elements Layout
        btn_back = findViewById(R.id.button_admin_back);
        img_admin_user = findViewById(R.id.img_admin_user);
        img_admin_vendor = findViewById(R.id.img_admin_vendor);
        img_admin_jewel = findViewById(R.id.img_admin_jewel);
        img_admin_report = findViewById(R.id.img_admin_report);
        //Fill Components
        img_admin_user.setOnClickListener(view -> {
            startActivity(new Intent(view.getContext(), ActivityListUser.class));
        });

        img_admin_vendor.setOnClickListener(view -> {
            startActivity(new Intent(view.getContext(), ListVendor.class));
        });

        img_admin_jewel.setOnClickListener(view -> {
            startActivity(new Intent().setClass(getApplicationContext(), ActivityListCatalog.class));
        });

        img_admin_report.setOnClickListener(view -> {
            startActivity(new Intent().setClass(getApplicationContext(), ListUserBlocked.class));
        });

        btn_back.setOnClickListener(view -> {
            startActivity(new Intent().setClass(getApplicationContext(), ActivityListCatalog.class));
        });
    }

}
