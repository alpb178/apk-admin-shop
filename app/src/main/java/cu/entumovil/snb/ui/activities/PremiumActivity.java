package cu.entumovil.snb.ui.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.ui.adapters.AccountAdapter;
import cu.entumovil.snb.ui.adapters.AccountModel;

public class PremiumActivity extends AppCompatActivity {

    private static final String TAG = SNBApp.APP_TAG + PremiumActivity.class.getSimpleName();

    private Context mContext;

    private View mView;

    private LinearLayout layoutSubscribe, stats_layout, layoutLifetime, layoutRecord;

    private TextView lblUnsubscribePremium;

    private ImageView img_info_stats;

    private AlertDialog accountDialog, suscribeDialog;

    private Button suscribeBtn;

    ArrayList<AccountModel> mSelectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutSubscribe = (LinearLayout) findViewById(R.id.layoutSubscribe);
        stats_layout = (LinearLayout) findViewById(R.id.stats_layout);
        img_info_stats = (ImageView) findViewById(R.id.img_info_stats);
        layoutLifetime = (LinearLayout) findViewById(R.id.layoutLifetime);
        layoutRecord = (LinearLayout) findViewById(R.id.layoutRecord);
        lblUnsubscribePremium = (TextView) findViewById(R.id.lblUnsubscribePremium);
        mView = layoutRecord.getRootView();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.GET_ACCOUNTS,
                            Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCOUNT_MANAGER},
                    123);
        }
        suscribeBtn = (Button) findViewById(R.id.bottomlinearLayout);
        requestSuscription();
    }

    private void requestSuscription() {
        if (!getPreferences(0).getBoolean("registered", false)) {
            if (getPreferences(0).getString("mail", "").isEmpty())
                selectAccount();
            if (checkIfUserIsRegistered()) {
                suscribeBtn.setVisibility(View.INVISIBLE);
                getPreferences(MODE_PRIVATE).edit().putBoolean("registered", true).commit();
            } else {
                suscribeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getPreferences(0).getString("mail", "").isEmpty()) {
                            selectAccount();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PremiumActivity.this);
                            builder.setTitle("Suscribirme");
                            builder.setMessage("¿Confirma suscribirse a zona premium?");
                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (SNBApp.mobileNetworkAvailable) {
                                        sendSMS();
                                        getPreferences(MODE_PRIVATE).edit().putBoolean("registered", true).commit();
                                        setLayoutActions();
                                        dialogInterface.dismiss();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(
                                                suscribeBtn, SNBApp.serviceStateMessage,
                                                Snackbar.LENGTH_LONG).setAction("Reenviar", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                requestSuscription();
                                            }
                                        });
                                        snackbar.show();
                                    }
                                }
                            });
                            suscribeDialog = builder.create();
                            suscribeDialog.show();
                        }
                    }
                });
            }
            stats_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  Toast.makeText(PremiumActivity.this,"Debes estar suscrito para acceder",
                          Toast.LENGTH_LONG).show();
                }
            });
            layoutLifetime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PremiumActivity.this,"Debes estar suscrito para acceder",
                            Toast.LENGTH_LONG).show();
                }
            });
            layoutRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PremiumActivity.this,"Debes estar suscrito para acceder",
                            Toast.LENGTH_LONG).show();
                }
            });
        } else {
            setLayoutActions();
        }
    }

    private void selectAccount() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccounts();
        mSelectedItems = new ArrayList<>();

        AuthenticatorDescription[] descriptions = manager.getAuthenticatorTypes();
        PackageManager pm = getPackageManager();
        int pos = 0;
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                mSelectedItems.add(new AccountModel(account.name,
                        pm.getDrawable(descriptions[pos].packageName, descriptions[pos].iconId, null)));

            }
            pos++;
        }

        if (mSelectedItems.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AccountAdapter arrayAdapter = new AccountAdapter(mSelectedItems, this);
            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getPreferences(MODE_PRIVATE).edit().
                            putString("mail", mSelectedItems.get(which).getMail()).
                            commit();
                    PremiumActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PremiumActivity.this, "Cuenta seleccionada, puede proceder a suscribirse", Toast.LENGTH_LONG).show();
                        }
                    });
                    accountDialog.dismiss();
                }
            });
            builder.setTitle("Seleccione una cuenta para suscribirse");
            accountDialog = builder.create();
            accountDialog.show();
        } else {
            final Dialog d = new Dialog(this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_info);
            Button b = (Button) d.findViewById(R.id.btnOk);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            d.show();
        }
    }

    private boolean checkIfUserIsRegistered() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED)
            requestSuscription();
    }

    private void setLayoutActions() {
        layoutSubscribe.setVisibility(View.GONE);
        lblUnsubscribePremium.setVisibility(View.GONE);
        img_info_stats.setVisibility(View.INVISIBLE);
        stats_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(PremiumActivity.this, StatsActivity.class);
                intent.putExtra(StatsActivity.INTENT_STATSTYPE_HOLDER, StatsType.SPECIALIZED);
                startActivity(intent);
            }
        });
        layoutLifetime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(PremiumActivity.this, LifetimeActivity.class);
                startActivity(intent);
            }
        });
        layoutRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent().setClass(PremiumActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendSMS() {
        final PendingIntent sentPI = PendingIntent.getBroadcast(getApplicationContext(), 100, new Intent("cu.entumovil.snb.REGISTRATION_SENT"), PendingIntent.FLAG_ONE_SHOT);
        SmsManager smsManager = SmsManager.getDefault();
        String sendTo = SNBApp.application.getResources().getString(R.string.SUBCRIPTION_SHORT_NUMBER);
        String keyword = SNBApp.application.getResources().getString(R.string.SUBCRIPTION_KEYWORD);
        smsManager.sendTextMessage(sendTo, null, keyword +" "+ getPreferences(0).getString("mail", ""), sentPI, null);
    }

}
