package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.ui.listeners.OnGameRowClickListener;

public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + GameViewHolder.class.getSimpleName();

    private Context context;

    public OnGameRowClickListener listener;

    public TextView lbl_game_date, lbl_game_current_ininng;

    public TextView lbl_game_team_visitor, lbl_game_runs_visitor;

    public TextView lbl_game_team_homeclub, lbl_game_runs_homeclub;

    public ImageView img_team_logo_visitor, img_team_logo_homeclub;

    public GameViewHolder(View v, OnGameRowClickListener aListener) {
        super(v);
        listener = aListener;
        context = v.getContext();
        itemView.setOnClickListener(this);
        lbl_game_date = (TextView) v.findViewById(R.id.lbl_game_date);
        lbl_game_current_ininng = (TextView) v.findViewById(R.id.lbl_game_current_ininng);
        lbl_game_runs_visitor = (TextView) v.findViewById(R.id.lbl_game_runs_visitor);
        img_team_logo_visitor = (ImageView) v.findViewById(R.id.img_team_logo_visitor);
        lbl_game_team_visitor = (TextView) v.findViewById(R.id.lbl_game_team_visitor);
        img_team_logo_homeclub = (ImageView) v.findViewById(R.id.img_team_logo_homeclub);
        lbl_game_team_homeclub = (TextView) v.findViewById(R.id.lbl_game_team_homeclub);
        lbl_game_runs_homeclub = (TextView) v.findViewById(R.id.lbl_game_runs_homeclub);
    }

    @Override
    public void onClick(View v) {
        listener.onGameRowClick(v, this.getAdapterPosition());
    }
}
