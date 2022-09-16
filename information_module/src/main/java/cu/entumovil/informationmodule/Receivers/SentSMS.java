package cu.entumovil.informationmodule.Receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by isidro.rodriguez on 5/17/2017.
 */

public class SentSMS extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                //Handle sent success
//                Toast.makeText(context, "Su comentario ha sido enviado", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                Toast.makeText(context, "No es posible enviar sus comentarios en este momento.", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                Toast.makeText(context, "Red móvil no disponible. Compruebe la disponibilidad en su dispositivo.", Toast.LENGTH_SHORT).show();
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                Toast.makeText(context, "No está activada la función para el envío de mensajes. Desactive el perfil Fuera de línea y vuelva a intentarlo.", Toast.LENGTH_SHORT).show();

                //Handle sent error
                break;
        }
        context.unregisterReceiver(this);
    }
}
