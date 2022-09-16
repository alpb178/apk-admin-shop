package cu.entumovil.informationmodule.Util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;

import cu.entumovil.informationmodule.UI.Activities.InformationActivity;

/**
 * Created by user on 1/31/2017.
 */

public class ModuleUtil {
    public static final String ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
    public static final String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
    public static final String ROBOTO_BLACK="fonts/Roboto-Black.ttf";
    public static final String ROBOTO_BLACK_ITALIC="fonts/Roboto-BlackItalic.ttf";
    public static final String ROBOTO_BOLD_ITALIC="fonts/Roboto-BoldItalic.ttf";
    public static final String ROBOTO_ITALIC="fonts/Roboto-Italic.ttf";
    public static final String ROBOTO_LIGHT_ITALIC = "fonts/Roboto-LightItalic.ttf";
    public static final String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final String ROBOTO_MEDIUM_ITALIC = "fonts/Roboto-MediumItalic.ttf";
    public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String ROBOTO_THIN = "fonts/Roboto-Thin.ttf";
    public static final String ROBOTO_THIN_ITALIC = "fonts/Roboto-ThinItalic.ttf";
    public static final String ROBOTO_CONDENSED_BOLD = "fonts/RobotoCondensed-Bold.ttf";
    public static final String ROBOTO_CONDENSED_BOLD_ITALIC = "fonts/RobotoCondensed-BoldItalic.ttf";
    public static final String ROBOTO_CONDENSED_ITALIC = "fonts/RobotoCondensed-Italic.ttf";
    public static final String ROBOTO_CONDENSED_LIGHT = "fonts/RobotoCondensed-Light.ttf";
    public static final String ROBOTO_CONDENSED_LIGHT_ITALIC = "fonts/RobotoCondensed-LightItalic.ttf";
    public static final String ROBOTO_CONDENSED_REGULAR = "fonts/RobotoCondensed-Regular.ttf";
    public static final String ROBOTO_SLAB_BOLD = "fonts/RobotoSlab-Bold.ttf";
    public static final String ROBOTO_SLAB_LIGHT = "fonts/RobotoSlab-Light.ttf";
    public static final String ROBOTO_SLAB_REGULAR = "fonts/RobotoSlab-Regular.ttf";
    public static final String ROBOTO_SLAB_THIN = "fonts/RobotoSlab-Thin.ttf";

    public static final String INBOX="inbox";
    public static final String SENT="sent";
    public static final String DRAFT="draft";


    //texts
    public static String appNameLocal;
    public static String vertionLocal;
    public static String copyRightTextCreationYearLocal;
    public static String appDescriptionTextStartLocal;
    public static String appDescriptionTextEndLocal;

    //icons
    public static int logo=-1;
    public static int ic_desoftLocal=-1;
    public static int ic_entumovilLocal=-1;
    public static int ic_etecsaLocal=-1;
    public static int ic_send_commentLocal=-1;

    //colors
    public static int appNameColorLocal=-1;
    public static int descriptionWordTextColorLocal=-1;
    public static int descriptionTextColorLocal=-1;
    public static int vertionAndCopyRightColorLocal=-1;

    public static Object UpdateActivity = null;



    /**
     * @param context
     * @param robotoTypefaceType Select from ModuleUtil class. Example ModuleUtil.ROBOTO_LIGHT
     * @return
     */
    public static Typeface getTypefaceRoboto(Context context,String robotoTypefaceType){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), robotoTypefaceType);
        return typeface;
    }

    /**
     *
     * @param activity Activity from is calling
     * @param appName Name of the application
     * @param vertion Vertion of this application
     * @param copyRightTextCreationYear Year of creation of the application
     * @param appDescriptionTextStart Brief description of the application.
     * @param appDescriptionTextEnd Information of contact. Example: \r\n \r\n Para mayor información, búsquenos a través de nuestros contactos y marque la diferencia con nuestros servicios.
                                    \r\n \r\n Visítenos en http://www.entumovil.cu
                                    \r\n \r\n Escriba a: atencion@entumovil.cu
                                    \r\n \r\n Teléfono: 7 8323501
     * @param appIcon Icon of the application to show in AboutActivity
     * @param ic_desoft Desoft icon. If ic_desoft is not defined, the app show a default icon.
     * @param ic_entumovil entuMovil icon. If ic_entumovil is not defined, the app show a default icon.
     * @param ic_etecsa Etectsa icon. If ic_etectsa is not defined, the app show a default icon.
     * @param ic_send_comment If ic_send_comment is not defined, the app show a default white icon
     * @param appNameColor The color to show the app name.
     * @param descriptionWordTextColor The color to show the description word.
     * @param descriptionTextColor The color of the text to show as application description.
     * @param vertionAndCopyRightColor The color of vertion and copyright text.
     */
    public static void initInformationActivity(Activity activity,
                                               String appName, String vertion,
                                               String copyRightTextCreationYear,
                                               String appDescriptionTextStart,String appDescriptionTextEnd,
                                               int appIcon,int ic_desoft, int ic_entumovil,
                                               int ic_etecsa,int ic_send_comment,
                                               int appNameColor, int descriptionWordTextColor,
                                               int descriptionTextColor, int vertionAndCopyRightColor){

        Intent intent = new Intent(activity, InformationActivity.class);
        logo = appIcon;
        appNameLocal = appName;
        vertionLocal=vertion;
        copyRightTextCreationYearLocal = copyRightTextCreationYear;
        appDescriptionTextStartLocal = appDescriptionTextStart;
        appDescriptionTextEndLocal = appDescriptionTextEnd;
        ic_desoftLocal = ic_desoft;
        ic_entumovilLocal = ic_entumovil;
        ic_etecsaLocal = ic_etecsa;
        appNameColorLocal = appNameColor;
        descriptionWordTextColorLocal = descriptionWordTextColor;
        descriptionTextColorLocal = descriptionTextColor;
        ic_send_commentLocal = ic_send_comment;
        vertionAndCopyRightColorLocal = vertionAndCopyRightColor;
        activity.startActivity(intent);
    }

    public static void initInformationActivity(Activity activity){
        Intent intent = new Intent(activity, InformationActivity.class);
        activity.startActivity(intent);
    }

    public static void deleteSmsFromInbox(Context context, String body, String number, String folder){
        Uri uriSms = Uri.parse("content://sms/"+folder);
        String[] reqCols = new String[]{"_id", "address", "body" };
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(uriSms, reqCols, null, null, null);
        if(c!=null && c.moveToFirst()){
            do {
                if(body.equals(c.getString(2)) && number.equals(c.getString(1)) ){
                    context.getContentResolver().delete(
                            Uri.parse("content://sms/" + c.getLong(0)), null, null);
                    break;
                }
            }while (c.moveToNext());
        }
    }

}
