package cu.entumovil.snb.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.holders.UserViewHolder;
import cu.entumovil.snb.core.models.jewel.Jewel;
import cu.entumovil.snb.ui.activities.ActivitySalesDetails;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class UserSaleAdapter extends RecyclerView.Adapter<UserViewHolder> implements
        OnRowClickListener, OnTeamHolderLongClickListener {

    private final SwipeRefreshLayout swipeRefreshLayout;
    private final Context context;
    private ArrayList<Jewel> teams;
    private int position;

    public UserSaleAdapter(ArrayList<Jewel> aTeam, SwipeRefreshLayout swipeRefreshLayout, Context context) {
        teams = (null != aTeam) ? new ArrayList<>(aTeam) : new ArrayList<Jewel>();
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;

    }

    public void setPositions(ArrayList<Jewel> aTeams) {
        teams = aTeams;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup aTeam, int viewType) {
        View viewLayout;
        UserViewHolder qualificationViewHolder;
        LayoutInflater inflater = LayoutInflater.from(aTeam.getContext());
        viewLayout = inflater.inflate(R.layout.row_list_user, aTeam, false);
        qualificationViewHolder = new UserViewHolder(viewLayout, this, this);
        return qualificationViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Jewel handle = teams.get(position);
        Glide.with(context)
                .load(handle.getJewl_catalogue().getImage_path())
                .placeholder(R.drawable.splash)
                .error(R.drawable.splash)
                .into(holder.image_jewel);
        holder.name.setText("Username: " + handle.getUsers_permissions_client().getUsername() + "\n\n" +
                "Modelo: " + handle.getJewl_catalogue().getModel() +
                "\n" +
                "Código: " + handle.getJewl_catalogue().getCode());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ActivitySalesDetails.class);
            intent.putExtra("jewel", handle);
            view.getContext().startActivity(intent);
            ((Activity) view.getContext()).finish();

        });
    }

    @Override
    public int getItemCount() {
        return (null != teams) ? teams.size() : 0;
    }

    public void addItem(int position, Jewel aTeam) {
        teams.add(position, aTeam);
        notifyItemInserted(position);
    }

    public Jewel removeItem(int position) {
        final Jewel team = teams.remove(position);
        notifyItemRemoved(position);
        return team;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Jewel team = teams.remove(fromPosition);
        teams.add(toPosition, team);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        teams.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<Jewel> aTeams) {
        applyAndAnimateRemovals(aTeams);
        applyAndAnimateAdditions(aTeams);
        applyAndAnimateMovedItems(aTeams);
    }

    private void applyAndAnimateRemovals(ArrayList<Jewel> newTeams) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            final Jewel rent = teams.get(i);
            if (!newTeams.contains(rent)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Jewel> newTeams) {
        for (int i = 0, count = newTeams.size(); i < count; i++) {
            final Jewel team = newTeams.get(i);
            if (!teams.contains(team)) {
                addItem(i, team);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Jewel> newTeams) {
        for (int toPosition = newTeams.size() - 1; toPosition >= 0; toPosition--) {
            final Jewel team = newTeams.get(toPosition);
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
//        this.position = position;
        return false;
    }

    @Override
    public void onRowPress(View rowPress, int position) {


    }
}
