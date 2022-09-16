package cu.entumovil.informationmodule.UI.Activities;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cu.entumovil.informationmodule.R;
import cu.entumovil.informationmodule.Receivers.DeliveredSMS;
import cu.entumovil.informationmodule.Receivers.SentSMS;
import cu.entumovil.informationmodule.Util.ModuleUtil;


public class FeedBackActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final int REQUEST_SMS = 1489;
    public Spinner themes;
    private Context context;
    private String feedBackText = "";
    public static Boolean movilNetworkAvailable;

    private static final int FEED_SMS_MAX_SIZE = 101 - 4 - 1 - 6 - 0;

    public EditText feedBackEditText;
    public EditText feedbackEmail;
    private static final String FREE_COMMENT_TAG = "FEED 1016";
    private MenuItem sendItem;
    private static final String ACTION_SENT = "cu.entumovil.snb.SENT";
    private static final String ACTION_DELIVERED = "cu.entumovil.snb.DELIVERED";
    private String toastMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feed_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(getString(R.string.enviar_comentario));
        initialize();
    }

    public void initialize() {
        context = this;
        themes = (Spinner) findViewById(R.id.cbxFeedbackTheme);
        ArrayAdapter<String> themesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.themes));
        themesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themes.setAdapter(themesAdapter);

        feedBackEditText = (EditText) findViewById(R.id.txtFeedbackBody);
        InputFilter filters[] = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(FEED_SMS_MAX_SIZE);
        feedBackEditText.setFilters(filters);
        feedbackEmail = (EditText) findViewById(R.id.edtFeedbackEmail);

        feedBackEditText.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));
        feedbackEmail.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));

        TextView lblFeedbackHeaderTitle = (TextView) findViewById(R.id.lblFeedbackHeaderTitle);
        TextView lblFeedbackRegardless = (TextView) findViewById(R.id.lblFeedbackRegardless);

        lblFeedbackHeaderTitle.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));
        lblFeedbackRegardless.setTypeface(ModuleUtil.getTypefaceRoboto(this, ModuleUtil.ROBOTO_LIGHT));

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateAvaiabilityListener listener = new PhoneStateAvaiabilityListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_SERVICE_STATE);
    }

    class PhoneStateAvaiabilityListener extends PhoneStateListener {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
//            Log.e("mensaje", "entra aqui en el cambio de estado del servicio.");
            switch (serviceState.getState()) {
                case ServiceState.STATE_EMERGENCY_ONLY:
                    movilNetworkAvailable = false;
                    toastMessage = getString(R.string.emergy_calls_only);
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    movilNetworkAvailable = false;
                    toastMessage = getString(R.string.out_of_service);
                    break;
                case ServiceState.STATE_POWER_OFF:
                    movilNetworkAvailable = false;
                    toastMessage = getString(R.string.plane_mode);
                    break;
                case ServiceState.STATE_IN_SERVICE:
                    movilNetworkAvailable = true;
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_back, menu);
        sendItem = menu.findItem(R.id.menu_send_feedback);
        if (ModuleUtil.ic_send_commentLocal != -1) {
            sendItem.setIcon(ModuleUtil.ic_send_commentLocal);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_send_feedback) {
            Boolean sendComment = true;
            feedBackText = "";
            if (!((TextView) themes.getSelectedView()).getText().toString().equals(getString(R.string.seleccin_un_tema))) {
                feedBackText += ((TextView) themes.getSelectedView()).getText().toString() + "\n";
            }
            if (!TextUtils.isEmpty(feedbackEmail.getText())) {
                feedBackText += feedbackEmail.getText().toString() + "\n";
            }
            if (!TextUtils.isEmpty(feedBackEditText.getText())) {
                feedBackText += feedBackEditText.getText().toString() + "\n";
                sendComment = true;
            } else {
                feedBackEditText.setError(getString(R.string.comment_cant_be_empty));
                sendComment = false;
            }
            if (!movilNetworkAvailable) {
                sendComment = false;
                Toast.makeText(FeedBackActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }

            if (sendComment == true)
                if (isSMSPermissionGranted())
                    sendComment();
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendComment() {
        PendingIntent sIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_SENT), 0);
        PendingIntent dIntent = PendingIntent.getBroadcast(context, 0,
                new Intent(ACTION_DELIVERED), 0);
        //Send the message
        context.registerReceiver(new SentSMS(), new IntentFilter(ACTION_SENT));
        context.registerReceiver(new DeliveredSMS(), new IntentFilter(ACTION_DELIVERED));
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(
                getString(R.string.incoming_free_number), null,
                getString(R.string.free_commnet_tag) + " " + feedBackText,
                sIntent, dIntent);
        themes.setSelection(0);
        feedbackEmail.setText("");
        feedBackEditText.setText("");
    }

    public boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(FeedBackActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendComment();
                } else {
                    Toast.makeText(FeedBackActivity.this, R.string.app_no_funcio_ok_sin_permiso_de_envio_de_sms, Toast.LENGTH_LONG).show();
                }
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
