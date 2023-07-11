package cu.entumovil.snb.core.holders;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,
        View.OnCreateContextMenuListener, View.OnClickListener {

    private final Context context;
    private final OnRowClickListener slistener;
    private final OnTeamHolderLongClickListener listener;
    public ImageView image_jewel;
    public TextView name;
    public CardView cardView;


    public UserViewHolder(View v, OnTeamHolderLongClickListener lListener, OnRowClickListener sListener) {
        super(v);
        context = v.getContext();
        this.slistener = sListener;
        this.listener = lListener;

        v.setOnCreateContextMenuListener(this);
        image_jewel = v.findViewById(R.id.image_jewel);
        name = v.findViewById(R.id.text_name_last_name);
        cardView = v.findViewById(R.id.card_view_user);
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
        slistener.onRowPress(view, this.getAdapterPosition());
    }
}
