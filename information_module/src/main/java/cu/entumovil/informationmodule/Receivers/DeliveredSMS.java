package cu.entumovil.informationmodule.Receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cu.entumovil.informationmodule.R;

/**
 * Created by isidro.rodriguez on 5/17/2017.
 */

public class DeliveredSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                //Handle delivery success
//                preferences.edit().putBoolean("suscrito", true).commit();
                Toast.makeText(context, R.string.delivered_comment, Toast.LENGTH_SHORT).show();
                break;
            case Activity.RESULT_CANCELED:
                //Handle delivery failure
                break;
        }
        context.unregisterReceiver(this);
    }
}
