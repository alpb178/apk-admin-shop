package cu.entumovil.snb.core.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.core.models.jewel.Jewel;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;
import okhttp3.OkHttpClient;

public class Utils {

    public static String convert24hrsTo12Hrs(String hour) {
        String[] s = hour.split(":");
        Calendar today = Calendar.getInstance();
        Calendar newHour = Calendar.getInstance();
        newHour.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Integer.parseInt(s[0]), Integer.parseInt(s[1]));

        int h = (newHour.get(Calendar.HOUR) == 0) ? 12 : newHour.get(Calendar.HOUR);
        String m = (String.valueOf(newHour.get(Calendar.MINUTE)).length() == 1)
                ? "0" + newHour.get(Calendar.MINUTE)
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

    public static String getSplitDate(String date) {
        String[] parts = date.split("T");
        return parts[0];
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

    public static Bitmap getBitmapFromURL(String src) {
        final Bitmap[] bmp = new Bitmap[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bmp[0] = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                    bmp[0] = null;
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bmp[0];
    }

    public static JewelCatalog RetrieveJewelCatalog(JsonObject element) {
        int id = element.getAsJsonObject().get("id").getAsInt();
        JsonObject mu_weight = element.getAsJsonObject("measure_unit_weight");
        JsonObject mu_price = element.getAsJsonObject("measure_unit_price");
        JsonObject mu_carats = element.getAsJsonObject("measure_unit_carats");
        JsonObject mu_large = element.getAsJsonObject("measure_unit_large");
        //----------------------------------------------------------------------
        String code = element.get("code").getAsString();
        String model = element.get("model").getAsString();
        Integer image= null;
        if (element.getAsJsonObject().get("image") != null) {
            image=element.getAsJsonObject().get("image").getAsInt();
        }
        String image_path = element.get("img_path").getAsString();
        Float weight = element.get("weight").getAsFloat();
        int id_measure_unit_weight = mu_weight.get("id").getAsInt();
        String measure_unit_weight = mu_weight.get("symbol").getAsString();
        Float carats = element.get("carats").getAsFloat();
        int id_measure_unit_carats = mu_carats.get("id").getAsInt();
        String measure_unit_carats = mu_carats.get("symbol").getAsString();
        Float price = element.get("price").getAsFloat();
        int id_measure_unit_price = mu_price.get("id").getAsInt();
        String measure_unit_price = mu_price.get("symbol").getAsString();
        String description = element.get("description").getAsString();
        float large = element.get("large").getAsFloat();
        int id_measure_unit_large = mu_large.get("id").getAsInt();
        String measure_unit_large = mu_large.get("symbol").getAsString();
        int availability = element.get("availability").getAsInt();
        String created_at = Utils.getSplitDate(element.get("createdAt").getAsString());
        String update_at = Utils.getSplitDate(element.get("updatedAt").getAsString());
        boolean isDelete = element.get("isDelete").getAsBoolean();
        //--------------------------------------------------------------------------------------------

        JewelCatalog jewelCatalog = new JewelCatalog(id, code, model, image, image_path, weight, id_measure_unit_weight, measure_unit_weight, carats,
                id_measure_unit_carats, measure_unit_carats, price, id_measure_unit_price, measure_unit_price, large, id_measure_unit_large, measure_unit_large,
                description, created_at, update_at, availability, 1, "", isDelete);

        return jewelCatalog;

    }

    public static User RetrieveUser(JsonElement element) {
        String lastName = "";
        int id = element.getAsJsonObject().get("id").getAsInt();
        String username = element.getAsJsonObject().get("username").getAsString();
        String email = element.getAsJsonObject().get("email").getAsString();
        String rol = element.getAsJsonObject().get("rol").getAsString();
        String vendor = element.getAsJsonObject().get("vendedor").getAsString();
        String phone = element.getAsJsonObject().get("phone").getAsString();
        String name = element.getAsJsonObject().get("name").getAsString();
        if (element.getAsJsonObject().get("last_name") == null) {
            lastName = element.getAsJsonObject().get("lastName").getAsString();
        } else {
            lastName = element.getAsJsonObject().get("last_name").getAsString();
        }
        int age = element.getAsJsonObject().get("age").getAsInt();
        int count_jewel = element.getAsJsonObject().get("count_jewl").getAsInt();
        String genre = element.getAsJsonObject().get("genre").getAsString();
        Boolean isBlocked = element.getAsJsonObject().get("blocked").getAsBoolean();
        User user = new User(id, username, email, phone, rol, name, lastName, age, genre, count_jewel, vendor, isBlocked);

        return user;

    }

    public static Jewel RetrieveJewel(JsonObject element) {
        int id = element.get("id").getAsInt();
        String code = element.get("code").getAsString();
        int count = element.get("count").getAsInt();
        String created_at = Utils.getSplitDate(element.get("createdAt").getAsString());
        String update_at = Utils.getSplitDate(element.get("updatedAt").getAsString());
        String status = element.get("status").getAsString();

        JsonObject jewel_catalog = element.get("jewl_catalogue").getAsJsonObject();

        JewelCatalog jewelCatalog = RetrieveJewelCatalog(jewel_catalog);

        JsonObject jewel_client = element.get("users_permissions_client").getAsJsonObject();
        User client = RetrieveUser(jewel_client);


        JsonObject jewel_vendor = element.get("users_permissions_vendor").getAsJsonObject();
        User vendor = RetrieveUser(jewel_vendor);

        return new Jewel(id, code, count, status, created_at, update_at, jewelCatalog, client, vendor);


    }

    public static OkHttpClient client(){

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .build();

        return client;
    }

}
