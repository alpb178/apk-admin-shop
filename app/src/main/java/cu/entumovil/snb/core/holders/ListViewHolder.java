package cu.entumovil.snb.core.holders;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import cu.entumovil.snb.R;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener,
        View.OnCreateContextMenuListener, View.OnClickListener {

    private final Context context;
    private final OnRowClickListener slistener;
    private final OnTeamHolderLongClickListener listener;
    public ImageView image_jewel;
    public TextView model_jewel, weight_jewel,count_pcs,text_jewel_code;
    public Button btn_jewel_pending,btn_jewel_delete,btn_jewel_update;
    public CardView cardView;
    public LinearLayout container_button;

    public ListViewHolder(View v, OnTeamHolderLongClickListener lListener, OnRowClickListener sListener) {
        super(v);
        context = v.getContext();
        this.slistener = sListener;
        this.listener = lListener;
        v.setOnCreateContextMenuListener(this);
        image_jewel =v.findViewById(R.id.image_jewel);
        model_jewel =  v.findViewById(R.id.text_jewel_model);
        weight_jewel = v.findViewById(R.id.text_jewel_weight);
        text_jewel_code=  v.findViewById(R.id.text_jewel_code);
        cardView=v.findViewById(R.id.card_view_alex_gold_list);
        btn_jewel_pending=v.findViewById(R.id.btn_jewel_pending);
        btn_jewel_update=v.findViewById(R.id.btn_jewel_update);
        btn_jewel_delete=v.findViewById(R.id.btn_jewel_delete);
        count_pcs=v.findViewById(R.id.count_pcs);
        container_button=v.findViewById(R.id.container_button);
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
