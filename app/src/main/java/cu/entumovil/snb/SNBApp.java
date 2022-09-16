package cu.entumovil.snb;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import cu.entumovil.snb.core.db.SnbDBHelper;
import cu.entumovil.snb.core.db.models.Cache;
import cu.entumovil.snb.service.NetworkReceiver;

public class SNBApp extends Application {

    public static String APP_TAG = "SNBAPP_";

    private static final String TAG = APP_TAG + SNBApp.class.getSimpleName();

    public static SNBApp application;

    public static String HOST;

    public static String VERSION = "1.0.0";

    public static SnbDBHelper DB_HELPER;

    public static int DEFAULT_PAGE_LENGTH;

    public static Boolean mobileNetworkAvailable;

    public static String serviceStateMessage;

    private NetworkReceiver receiver;

    public static Bus BUS;

    public SNBApp() { }

    @Override
    public void onCreate() {
        super.onCreate();
        HOST = this.getString(R.string.HOST);
        DB_HELPER = OpenHelperManager.getHelper(this, SnbDBHelper.class);
        DEFAULT_PAGE_LENGTH = Integer.parseInt(this.getString(R.string.DEFAULT_PAGE_SIZE));
        try {
            PackageInfo pInfo = null;
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            VERSION = pInfo.versionName.concat("."+Integer.valueOf(pInfo.versionCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == application) {
            application = this;
            OnAppCrash();
        }
        BUS = new Bus(ThreadEnforcer.MAIN);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateAvailabilityListener listener = new PhoneStateAvailabilityListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_SERVICE_STATE);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        registerReceiver(receiver, filter);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo pi = packageManager.getPackageInfo("cu.entumovil.snb", PackageManager.GET_ACTIVITIES);
            ApplicationInfo ai = pi.applicationInfo;
            int uid = ai.uid;
            Log.d(TAG, String.valueOf(TrafficStats.getUidRxBytes(uid)).concat("B Read - ").concat(String.valueOf(TrafficStats.getUidTxBytes(uid))).concat("B Send"));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private Thread.UncaughtExceptionHandler defaultUEH;

    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            Log.e("SNB Fatal Error: ", ex.getMessage());
            onCreate();
        }
    };

    private void OnAppCrash() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }

    class PhoneStateAvailabilityListener extends PhoneStateListener {
        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            switch (serviceState.getState()){
                case ServiceState.STATE_EMERGENCY_ONLY:
                    mobileNetworkAvailable = false;
                    serviceStateMessage = getString(R.string.state_service_emergency);
                    break;
                case ServiceState.STATE_OUT_OF_SERVICE:
                    mobileNetworkAvailable = false;
                    serviceStateMessage = getString(R.string.state_service_out);
                    break;
                case ServiceState.STATE_POWER_OFF:
                    mobileNetworkAvailable = false;
                    serviceStateMessage = getString(R.string.state_service_off);
                    break;
                case ServiceState.STATE_IN_SERVICE:
                    mobileNetworkAvailable = true;
                    serviceStateMessage = getString(R.string.state_service_ok);
                    break;
            }
        }
    }

}
