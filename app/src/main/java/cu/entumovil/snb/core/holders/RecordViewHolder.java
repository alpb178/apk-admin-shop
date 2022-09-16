package cu.entumovil.snb.core.holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = SNBApp.APP_TAG + RecordViewHolder.class.getSimpleName();

    public TextView recordName, serie, name, team, serieType, serieYear, date;

    public TextView lblFmto, lblFmto1, lblFmto2, txtCant, txtCant1, txtCant2;

    public ImageView img_player, img_calendar;

    public RecordViewHolder(View v) {
        super(v);
        recordName = (TextView) v.findViewById(R.id.recordName);
        serie = (TextView) v.findViewById(R.id.serie);
        name = (TextView) v.findViewById(R.id.name);
        team = (TextView) v.findViewById(R.id.team);
        serieType = (TextView) v.findViewById(R.id.serieType);
        serieYear = (TextView) v.findViewById(R.id.serieYear);
        img_calendar = (ImageView) v.findViewById(R.id.img_calendar);
        date = (TextView) v.findViewById(R.id.date);
        lblFmto = (TextView) v.findViewById(R.id.lblFmto);
        lblFmto1 = (TextView) v.findViewById(R.id.lblFmto1);
        lblFmto2 = (TextView) v.findViewById(R.id.lblFmto2);
        txtCant = (TextView) v.findViewById(R.id.txtCant);
        txtCant1 = (TextView) v.findViewById(R.id.txtCant1);
        txtCant2 = (TextView) v.findViewById(R.id.txtCant2);
    }

}
