package cu.entumovil.snb.core.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cu.entumovil.snb.R;

public class Utils {

    public static String convert24hrsTo12Hrs(String hour) {
        String[] s = hour.split(":");
        Calendar today = Calendar.getInstance();
        Calendar newHour = Calendar.getInstance();
        newHour.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Integer.parseInt(s[0]), Integer.parseInt(s[1]));

        int h = (newHour.get(Calendar.HOUR) == 0) ? 12 : newHour.get(Calendar.HOUR);
        String m = (String.valueOf(newHour.get(Calendar.MINUTE)).length() == 1)
                ? "0"+newHour.get(Calendar.MINUTE)
                : String.valueOf(newHour.get(Calendar.MINUTE));
        String am_pm = (newHour.get(Calendar.AM_PM) == 0) ? "AM" : "PM";
        return h + ":" + m + " " + am_pm;
    }

    public static String formatDate(Date date, boolean en) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = en ? new SimpleDateFormat("MM/dd/yy") : new SimpleDateFormat("dd/MM/yyyy");
        if (null == date) {
            return format.format(calendar.getTime());
        } else {
            return format.format(date);
        }
    }

    public static String historyDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM");
        return format.format(date);
    }

    public static String gameStatusWrapper(String status) {
        String result = "";
        switch (status) {
            case "Demor":
                result = "Demorado";
                break;
            case "Deten":
                result = "Detenido";
                break;
            case "Susp":
                result = "Suspendido";
                break;
            case "Sell":
                result = "Sellado";
                break;
            case "Progr":
                result = "Programado";
                break;
            case "Term":
                result = "Terminado";
                break;
            default:
                result = "Entrada: " + status;
        }
        return result;
    }

    public static String copyrightRange(Context context) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String strRange = context.getString(R.string.app_copyright_start_year);
        if ((currentYear - Integer.parseInt(context.getString(R.string.app_copyright_start_year))) >= 1) {
            strRange = context.getString(R.string.app_copyright_start_year) + "-" + String.valueOf(currentYear);
        }
        return context.getString(R.string.app_copyright_symbol) + strRange;
    }
}
