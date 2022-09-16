package cu.entumovil.snb.ui.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.LeaderViewHolder;
import cu.entumovil.snb.core.models.Player;
import cu.entumovil.snb.core.models.SerieLeader;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.activities.LeadersActivity;
import cu.entumovil.snb.ui.activities.PlayerActivity;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;

public class LeadersAdapter extends RecyclerView.Adapter<LeaderViewHolder> implements OnRowClickListener {

    private static final String TAG = SNBApp.APP_TAG + LeadersAdapter.class.getSimpleName();

    private ArrayList<SerieLeader> leaders;

    private LeadersActivity mActivity;

    public LeadersAdapter(List<SerieLeader> aLeaders, LeadersActivity aActivity) {
        leaders = (null != aLeaders) ? new ArrayList<>(aLeaders) : new ArrayList<SerieLeader>();
        mActivity = aActivity;
    }

    @Override
    public LeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewLayout;
        LeaderViewHolder leaderViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        viewLayout = inflater.inflate(R.layout.row_leader, parent, false);
        leaderViewHolder = new LeaderViewHolder(viewLayout, this);
        return leaderViewHolder;
    }

    @Override
    public void onBindViewHolder(LeaderViewHolder holder, final int position) {
        holder.mMore.setVisibility(View.INVISIBLE);
        Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
        CustomImage roundedImage = new CustomImage(photo, true);
        holder.mItem = leaders.get(position);
        holder.img_leader_photo.setImageDrawable(roundedImage);
        holder.mName.setText(leaders.get(position).getNOMBRE());
        holder.mTit.setText(leaders.get(position).getTIT());
        holder.mSig.setText(" (" + leaders.get(position).getSIG() + ")");
        holder.mSig.setTextColor(ContextCompat.getColor(mActivity, TeamHelper.convertAcronymToColorResource(leaders.get(position).getSIG())));
        holder.mValue.setText(leaders.get(position).getValor());
        if (null != leaders.get(position).getMas()) {
            holder.mMore.setVisibility(View.VISIBLE);
            holder.mMore.setText(leaders.get(position).getMas());
        }
    }

    @Override
    public int getItemCount() {
        return (null != leaders) ? leaders.size() : 0;
    }

    public void add(SerieLeader aLeader) {
        leaders.add(aLeader);
        notifyItemInserted(leaders.size());
    }

    public void addItem(int position, SerieLeader aLeader) {
        leaders.add(position, aLeader);
        notifyItemInserted(position);
    }

    public SerieLeader removeItem(int position) {
        final SerieLeader leader = leaders.remove(position);
        notifyItemRemoved(position);
        return leader;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SerieLeader leader = leaders.remove(fromPosition);
        leaders.add(toPosition, leader);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        leaders.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<SerieLeader> aLeaders) {
        applyAndAnimateRemovals(aLeaders);
        applyAndAnimateAdditions(aLeaders);
        applyAndAnimateMovedItems(aLeaders);
    }

    private void applyAndAnimateRemovals(List<SerieLeader> newLeaders) {
        for (int i = leaders.size() - 1; i >= 0; i--) {
            final SerieLeader leader = leaders.get(i);
            if (!newLeaders.contains(leader)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SerieLeader> newLeaders) {
        for (int i = 0, count = newLeaders.size(); i < count; i++) {
            final SerieLeader leader = newLeaders.get(i);
            if (!leaders.contains(leader)) {
                addItem(i, leader);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SerieLeader> newLeaders) {
        for (int toPosition = newLeaders.size() - 1; toPosition >= 0; toPosition--) {
            final SerieLeader leader = newLeaders.get(toPosition);
            final int fromPosition = leaders.indexOf(leader);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public void onRowPress(View rowPress, int position) {
        SerieLeader leader = leaders.get(position);
        Player player = new Player();
        player.setId(leader.getId());
        player.setNombre(leader.getNOMBRE());
        player.setSigla(leader.getSIG());
        player.setFoto(leader.getFoto());
        Intent intent = new Intent().setClass(mActivity, PlayerActivity.class);
        intent.putExtra(PlayerActivity.INTENT_PLAYER_HOLDER, player);
        mActivity.startActivity(intent);
    }

}
