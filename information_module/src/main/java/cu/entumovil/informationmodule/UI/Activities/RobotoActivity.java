package cu.entumovil.informationmodule.UI.Activities;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import cu.entumovil.informationmodule.R;
import cu.entumovil.informationmodule.Util.ModuleUtil;

public class RobotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roboto);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
        for (int i = 0; i < 22; i++) {
            TextView textView = new TextView(this);
            textView.setPadding(0,6,0,0);
            textView.setTextSize(14f);
            String text="";
            Typeface typeface = null;
            switch (i+1){
                case 1: {
                    text+= "ROBOTO_LIGHT";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_LIGHT);
                    break;
                }
                case 2: {
                    text+= "ROBOTO_BOLD";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_BOLD);
                    break;
                }
                case 3: {
                    text+= "ROBOTO_BLACK";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_BLACK);
                    break;
                }
                case 4: {
                    text+= "ROBOTO_BLACK_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_BLACK_ITALIC);
                    break;
                }
                case 5: {
                    text+= "ROBOTO_BOLD_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_BOLD_ITALIC);
                    break;
                }
                case 6: {
                    text+= "ROBOTO_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_ITALIC);
                    break;
                }
                case 7: {
                    text+= "ROBOTO_LIGHT_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_LIGHT_ITALIC);
                    break;
                }
                case 8: {
                    text+= "ROBOTO_MEDIUM";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_MEDIUM);
                    break;
                }
                case 9: {
                    text+= "ROBOTO_MEDIUM_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_MEDIUM_ITALIC);
                    break;
                }
                case 10: {
                    text+= "ROBOTO_REGULAR";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_REGULAR);
                    break;
                }
                case 11: {
                    text+= "ROBOTO_THIN";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_THIN);
                    break;
                }
                case 12: {
                    text+= "ROBOTO_THIN_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_THIN_ITALIC);
                    break;
                }
                case 13: {
                    text+= "ROBOTO_CONDENSED_BOLD";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_CONDENSED_BOLD);
                    break;
                }
                case 14: {
                    text+= "ROBOTO_CONDENSED_BOLD_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_CONDENSED_BOLD_ITALIC);
                    break;
                }
                case 15: {
                    text+= "ROBOTO_CONDENSED_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_CONDENSED_ITALIC);
                    break;
                }
                case 16: {
                    text+= "ROBOTO_CONDENSED_LIGHT";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_CONDENSED_LIGHT);
                    break;
                }
                case 17: {
                    text+= "ROBOTO_CONDENSED_LIGHT_ITALIC";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_CONDENSED_LIGHT_ITALIC);
                    break;
                }
                case 18: {
                    text+= "ROBOTO_CONDENSED_REGULAR";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_CONDENSED_REGULAR);
                    break;
                }
                case 19: {
                    text+= "ROBOTO_SLAB_BOLD";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_SLAB_BOLD);
                    break;
                }
                case 20: {
                    text+= "ROBOTO_SLAB_LIGHT";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_SLAB_LIGHT);
                    break;
                }
                case 21: {
                    text+= "ROBOTO_SLAB_REGULAR";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_SLAB_REGULAR);
                    break;
                }
                case 22: {
                    text+= "ROBOTO_SLAB_THIN";
                    typeface = ModuleUtil.getTypefaceRoboto(RobotoActivity.this,ModuleUtil.ROBOTO_SLAB_THIN);
                    break;
                }
            }
            textView.setText(text);
            textView.setTypeface(typeface);
            linearLayout.addView(textView);
        }
    }
}
