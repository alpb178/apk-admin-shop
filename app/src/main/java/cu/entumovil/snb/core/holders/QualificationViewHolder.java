package cu.entumovil.snb.core.holders;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class QualificationViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,
        View.OnCreateContextMenuListener, View.OnClickListener {

    private static final String TAG = SNBApp.APP_TAG + QualificationViewHolder.class.getSimpleName();

    private Context context;

    public ImageView imgTeamLogo;

    public TextView lblTeamName, lblGames, lblWins, lblLosts, lblAverage;

    private OnRowClickListener slistener;

    private OnTeamHolderLongClickListener listener;

    public QualificationViewHolder(View v, OnTeamHolderLongClickListener lListener, OnRowClickListener sListener) {
        super(v);
        context = v.getContext();
        this.slistener = sListener;
        this.listener = lListener;
        v.setOnCreateContextMenuListener(this);
        imgTeamLogo = (ImageView) v.findViewById(R.id.img_team_logo);
        imgTeamLogo.setOnLongClickListener(this);
        lblTeamName = (TextView) v.findViewById(R.id.lbl_team_name);
        lblGames = (TextView) v.findViewById(R.id.lbl_games);
        lblWins = (TextView) v.findViewById(R.id.lbl_wins);
        lblLosts = (TextView) v.findViewById(R.id.lbl_losts);
        lblAverage = (TextView) v.findViewById(R.id.lbl_average);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//        contextMenu.add(Menu.NONE, R.id.contextual_menu_favorite, Menu.NONE, context.getString(R.string.menu_contextual_favorite));
    }

    @Override
    public boolean onLongClick(View view) {
        return true;
//        return listener.onTeamRowLongPress(view, this.getAdapterPosition());
    }

    @Override
    public void onClick(View view) {
//        slistener.onRowPress(view, this.getAdapterPosition());
    }
}
