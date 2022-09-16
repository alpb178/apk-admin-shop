package cu.entumovil.informationmodule.UI.Fragments;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import cu.entumovil.informationmodule.R;
import cu.entumovil.informationmodule.Util.ModuleUtil;


/**
 * Created by alejandro.guerra on 04/11/2016.
 */
public class AboutFragment extends Fragment {
    private final static String COPYRIGHT_SYMBOL = "©";

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    public AboutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about,container, false);
        this.getActivity().setTitle(R.string.about_title);
        PackageInfo pInfo = null;

        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView version = (TextView) rootView.findViewById(R.id.app_version);
        TextView appName = (TextView) rootView.findViewById(R.id.lbl_app_name);
        if(ModuleUtil.appNameLocal!=null){
            appName.setText(ModuleUtil.appNameLocal);
        }
        if(ModuleUtil.appNameColorLocal!=-1){
            appName.setTextColor(rootView.getResources().getColor(ModuleUtil.appNameColorLocal));
        }

        appName.setTypeface(ModuleUtil.getTypefaceRoboto(rootView.getContext(),ModuleUtil.ROBOTO_LIGHT));
        version.setTypeface(ModuleUtil.getTypefaceRoboto(rootView.getContext(),ModuleUtil.ROBOTO_LIGHT));
        if(ModuleUtil.vertionLocal!=null){
            version.setText(getString(R.string.app_version_label) + " " +ModuleUtil.vertionLocal);
        }else {
            version.setText(getString(R.string.app_version_label) + " " + pInfo.versionName);
        }

        TextView copy = (TextView) rootView.findViewById(R.id.app_copyright);
        TextView copy2 = (TextView) rootView.findViewById(R.id.copyright2);

        if(ModuleUtil.vertionAndCopyRightColorLocal!=-1){
            copy.setTextColor(rootView.getResources().getColor(ModuleUtil.vertionAndCopyRightColorLocal));
            copy2.setTextColor(rootView.getResources().getColor(ModuleUtil.vertionAndCopyRightColorLocal));
            version.setTextColor(rootView.getResources().getColor(ModuleUtil.vertionAndCopyRightColorLocal));
        }

        TextView details = (TextView) rootView.findViewById(R.id.lbl_details);
        if(ModuleUtil.descriptionWordTextColorLocal!=-1){
            details.setTextColor(rootView.getResources().getColor(ModuleUtil.descriptionWordTextColorLocal));
        }

        if(ModuleUtil.logo!=-1) {
            ImageView logo = (ImageView) rootView.findViewById(R.id.logo);
            logo.setImageResource(ModuleUtil.logo);
        }

        if(ModuleUtil.ic_desoftLocal!=-1){
            ImageView desoftIcon = (ImageView) rootView.findViewById(R.id.logo_desfot);
            desoftIcon.setImageResource(ModuleUtil.ic_desoftLocal);
        }
        if(ModuleUtil.ic_entumovilLocal!=-1){
            ImageView entuMovilIcon = (ImageView) rootView.findViewById(R.id.logo_entumovil);
            entuMovilIcon.setImageResource(ModuleUtil.ic_entumovilLocal);
        }
      /*  if(ModuleUtil.ic_etecsaLocal!=-1){
            ImageView etecsaIcon = (ImageView) rootView.findViewById(R.id.logo_etecsa);
            etecsaIcon.setImageResource(ModuleUtil.ic_etecsaLocal);
        }*/


        details.setTypeface(ModuleUtil.getTypefaceRoboto(rootView.getContext(),ModuleUtil.ROBOTO_LIGHT));
        copy.setTypeface(ModuleUtil.getTypefaceRoboto(rootView.getContext(),ModuleUtil.ROBOTO_LIGHT));
        copy2.setTypeface(ModuleUtil.getTypefaceRoboto(rootView.getContext(),ModuleUtil.ROBOTO_LIGHT));

        copy.setText(ModuleUtil.copyRightTextCreationYearLocal + " " + getString(R.string.app_owner));
        return rootView;
    }

    public static String copyrightRange(Activity activity) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        String strRange = activity.getString(R.string.app_copyright_start_year);
        if(ModuleUtil.copyRightTextCreationYearLocal!=null){
            strRange = ModuleUtil.copyRightTextCreationYearLocal;
        }
        if ((currentYear - Integer.parseInt(strRange)) > 1) {
            strRange = activity.getString(R.string.app_copyright_start_year) + "-" + String.valueOf(currentYear);
        }
        return COPYRIGHT_SYMBOL + " " + strRange;
    }
}
