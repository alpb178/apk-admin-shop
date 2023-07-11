package cu.entumovil.snb.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.holders.UserViewHolder;
import cu.entumovil.snb.core.models.jewel.Jewel;
import cu.entumovil.snb.core.models.jewelCatalog.JewelCatalog;
import cu.entumovil.snb.core.models.User;
import cu.entumovil.snb.ui.activities.ActivitySalesDetails;
import cu.entumovil.snb.ui.activities.admin.DetailsUser;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> implements
        OnRowClickListener, OnTeamHolderLongClickListener {

    private ArrayList<User> teams;
    private JewelCatalog jewel;
    private int position;
    private Context context;

    public UserAdapter(ArrayList<User> aTeam, JewelCatalog jewel, Context context ) {
        teams = (null != aTeam) ? new ArrayList<>(aTeam) : new ArrayList<User>();
        this.context=context;
        this.jewel=jewel;
    }

    public void setPositions(ArrayList<User> aTeams) {
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
        User handle = teams.get(position);
        holder.image_jewel.setVisibility(View.GONE);
        holder.name.setText( " " + handle.getUsername() + " \n\n  Telefóno: "+handle.getPhone());
        holder.cardView.setOnClickListener(view -> {
            Intent intent;
            if(jewel==null)
            {
                intent = new Intent(view.getContext(), DetailsUser.class);
                intent.putExtra("user", handle);
            }
          else{
                intent = new Intent(view.getContext(), ActivitySalesDetails.class);
                intent.putExtra("jewel",new Jewel(jewel,handle));
                intent.putExtra("client", handle);
            }
            view.getContext().startActivity(intent);
            ((Activity) view.getContext()).finish();
        });
    }

    @Override
    public int getItemCount() {
        return (null != teams) ? teams.size() : 0;
    }

    public void addItem(int position, User aTeam) {
        teams.add(position, aTeam);
        notifyItemInserted(position);
    }

    public User removeItem(int position) {
        final User team = teams.remove(position);
        notifyItemRemoved(position);
        return team;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final User team = teams.remove(fromPosition);
        teams.add(toPosition, team);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        teams.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<User> aTeams) {
        applyAndAnimateRemovals(aTeams);
        applyAndAnimateAdditions(aTeams);
        applyAndAnimateMovedItems(aTeams);
    }

    private void applyAndAnimateRemovals(ArrayList<User> newTeams) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            final User rent = teams.get(i);
            if (!newTeams.contains(rent)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<User> newTeams) {
        for (int i = 0, count = newTeams.size(); i < count; i++) {
            final User team = newTeams.get(i);
            if (!teams.contains(team)) {
                addItem(i, team);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<User> newTeams) {
        for (int toPosition = newTeams.size() - 1; toPosition >= 0; toPosition--) {
            final User team = newTeams.get(toPosition);
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
