package cu.entumovil.snb.core.utils;

import cu.entumovil.snb.R;

public class TeamHelper {

    public static int convertAcronymToDrawableResource(String acronym) {
        int drawableId = 0;
        switch (acronym.toUpperCase()) {
            case "MTZ":
            case "MATANZAS":
                drawableId = R.drawable.ic_mtz;
                break;
            case "CAV":
            case "CIEGO DE AVILA":
                drawableId = R.drawable.ic_cav;
                break;
            case "VCL":
            case "VILLA CLARA":
                drawableId = R.drawable.ic_vcl;
                break;
            case "GRA":
            case "GRANMA":
                drawableId = R.drawable.ic_gra;
                break;
            case "HOL":
            case "HOLGUIN":
                drawableId = R.drawable.ic_hol;
                break;
            case "CMG":
            case "CAMAGUEY":
                drawableId = R.drawable.ic_cmg;
                break;
            case "IJV":
            case "LA ISLA":
                drawableId = R.drawable.ic_ijv;
                break;
            case "LTU":
            case "LAS TUNAS":
                drawableId = R.drawable.ic_ltu;
                break;
            case "GTM":
            case "GUANTANAMO":
                drawableId = R.drawable.ic_gtm;
                break;
            case "IND":
            case "INDUSTRIALES":
                drawableId = R.drawable.ic_ind;
                break;
            case "PRI":
            case "PINAR DEL RIO":
                drawableId = R.drawable.ic_pri;
                break;
            case "ART":
            case "ARTEMISA":
                drawableId = R.drawable.ic_art;
                break;
            case "CFG":
            case "CIENFUEGOS":
                drawableId = R.drawable.ic_cfg;
                break;
            case "SCU":
            case "SANTIAGO DE CUBA":
                drawableId = R.drawable.ic_scu;
                break;
            case "SSP":
            case "SANCTI SPIRITUS":
                drawableId = R.drawable.ic_ssp;
                break;
            case "MAY":
            case "MAYABEQUE":
                drawableId = R.drawable.ic_may;
                break;
        }
        return drawableId;
    }

    public static int convertAcronymToColorResource(String acronym) {
        int colorId = 0;
        switch (acronym.toUpperCase()) {
            case "MTZ":
            case "MATANZAS":
                colorId = R.color.md_red_900_30;
                break;
            case "CAV":
            case "CIEGO DE AVILA":
                colorId = R.color.md_orange_200_50;
                break;
            case "VCL":
            case "VILLA CLARA":
                colorId = R.color.md_orange_deep_600_50;
                break;
            case "GRA":
            case "GRANMA":
                colorId = R.color.md_blue_light_500_50;
                break;
            case "HOL":
            case "HOLGUIN":
                colorId = R.color.md_blue_light_200_50;
                break;
            case "CMG":
            case "CAMAGUEY":
                colorId = R.color.md_blue_light_200_50;
                break;
            case "IJV":
            case "LA ISLA":
                colorId = R.color.md_black_100_35;
                break;
            case "LTU":
            case "LAS TUNAS":
                colorId = R.color.md_green_900_50;
                break;
            case "GTM":
            case "GUANTANAMO":
                colorId = R.color.md_orange_200_50;
                break;
            case "IND":
            case "INDUSTRIALES":
                colorId = R.color.md_blue_light_800_50;
                break;
            case "PRI":
            case "PINAR DEL RIO":
                colorId = R.color.md_green_900_50;
                break;
            case "ART":
            case "ARTEMISA":
                colorId = R.color.md_blue_light_800_50;
                break;
            case "CFG":
            case "CIENFUEGOS":
                colorId = R.color.md_green_900_50;
                break;
            case "SCU":
            case "SANTIAGO DE CUBA":
                colorId = R.color.md_red_900_50;
                break;
            case "SSP":
            case "SANCTI SPIRITUS":
                colorId = R.color.md_blue_light_900_50;
                break;
            case "MAY":
            case "MAYABEQUE":
                colorId = R.color.md_red_500_50;
                break;
        }
        return colorId;
    }

    public static String fromAcronym(String acronym) {
        String name = null;
        switch (acronym.toUpperCase()) {
            case "MTZ":
                name = "Matanzas";
                break;
            case "CAV":
                name = "Ciego de Ávila";
                break;
            case "VCL":
                name = "Villa Clara";
                break;
            case "GRA":
                name = "Granma";
                break;
            case "HOL":
                name = "Holguin";
                break;
            case "CMG":
                name = "Camagüey";
                break;
            case "IJV":
                name = "La Isla";
                break;
            case "LTU":
                name = "Las Tunas";
                break;
            case "GTM":
                name = "Guantanamo";
                break;
            case "IND":
                name = "Industriales";
                break;
            case "PRI":
                name = "Pinar del Río";
                break;
            case "ART":
                name = "Artemisa";
                break;
            case "CFG":
                name = "Cienfuegos";
                break;
            case "SCU":
                name = "Santiago de Cuba";
                break;
            case "SSP":
                name = "Sancti Spíritus";
                break;
            case "MAY":
                name = "Mayabeque";
                break;
        }
        return name;
    }

}
