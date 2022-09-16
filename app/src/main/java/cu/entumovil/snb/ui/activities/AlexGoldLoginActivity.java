package cu.entumovil.snb.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class AlexGoldLoginActivity extends AppCompatActivity {

    private static final String TAG = SNBApp.APP_TAG + AlexGoldLoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alex_gold_activity_login);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(view -> {
            Intent intent = new Intent().setClass(AlexGoldLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }


}
