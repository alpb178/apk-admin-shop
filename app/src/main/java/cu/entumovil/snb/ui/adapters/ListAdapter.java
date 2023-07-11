package cu.entumovil.snb.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.holders.ListViewHolder;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;
import cu.entumovil.snb.ui.activities.ActivityListUser;
import cu.entumovil.snb.ui.activities.admin.CreateJewel;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> implements
        OnRowClickListener, OnTeamHolderLongClickListener {

    private ArrayList<JewelCatalog> teams;
    private int position;
    private final Context context;

    public ListAdapter(ArrayList<JewelCatalog> aTeam, Context context) {
        teams = (null != aTeam) ? new ArrayList<>(aTeam) : new ArrayList<JewelCatalog>();
        this.context = context;
    }

    public void setPositions(ArrayList<JewelCatalog> aTeams) {
        teams = aTeams;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup aTeam, int viewType) {
        View viewLayout;
        ListViewHolder qualificationViewHolder;
        LayoutInflater inflater = LayoutInflater.from(aTeam.getContext());
        viewLayout = inflater.inflate(R.layout.row_list_catalog, aTeam, false);
        qualificationViewHolder = new ListViewHolder(viewLayout, this, this);
        return qualificationViewHolder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        JewelCatalog handle = teams.get(position);
        Glide.with(context)
                .load(handle.getImage_path())
                .placeholder(R.drawable.splash)
                .error(R.drawable.splash)
                .into(holder.image_jewel);
        holder.model_jewel.setText(String.valueOf(handle.getModel()));
        holder.weight_jewel.setText(handle.getPrice() + " " + handle.getMeasure_unit_price_symbol() + " / " +
                handle.getCarats() + " " + handle.getMeasure_unit_carats_symbol());
        holder.count_pcs.setText(handle.getAvailability() + " pcs");
        holder.text_jewel_code.setText("Código: " + handle.getCode());
        holder.cardView.setOnClickListener(view -> holder.container_button.setVisibility(View.VISIBLE));
        holder.btn_jewel_pending.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), ActivityListUser.class);
                    intent.putExtra("jewel", handle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();

                }
        );
        holder.btn_jewel_update.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), CreateJewel.class);
                    intent.putExtra("jewel", handle);
                    view.getContext().startActivity(intent);
                    ((Activity) view.getContext()).finish();

                }
        );


    }

    @Override
    public int getItemCount() {
        return (null != teams) ? teams.size() : 0;
    }

    public void addItem(int position, JewelCatalog aTeam) {
        teams.add(position, aTeam);
        notifyItemInserted(position);
    }

    public JewelCatalog removeItem(int position) {
        final JewelCatalog team = teams.remove(position);
        notifyItemRemoved(position);
        return team;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final JewelCatalog team = teams.remove(fromPosition);
        teams.add(toPosition, team);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        teams.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<JewelCatalog> aTeams) {
        applyAndAnimateRemovals(aTeams);
        applyAndAnimateAdditions(aTeams);
        applyAndAnimateMovedItems(aTeams);
    }

    private void applyAndAnimateRemovals(ArrayList<JewelCatalog> newTeams) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            final JewelCatalog rent = teams.get(i);
            if (!newTeams.contains(rent)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<JewelCatalog> newTeams) {
        for (int i = 0, count = newTeams.size(); i < count; i++) {
            final JewelCatalog team = newTeams.get(i);
            if (!teams.contains(team)) {
                addItem(i, team);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<JewelCatalog> newTeams) {
        for (int toPosition = newTeams.size() - 1; toPosition >= 0; toPosition--) {
            final JewelCatalog team = newTeams.get(toPosition);
            final int fromPosition = teams.indexOf(team);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean onTeamRowLongPress(View teamHolderRow, int position) {
        return false;
    }

    @Override
    public void onRowPress(View rowPress, int position) {
    }
}
