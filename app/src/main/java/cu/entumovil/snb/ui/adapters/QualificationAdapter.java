package cu.entumovil.snb.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.QualificationViewHolder;
import cu.entumovil.snb.core.models.Qualification;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.activities.MainActivity;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;
import cu.entumovil.snb.ui.listeners.OnTeamHolderLongClickListener;

public class QualificationAdapter extends RecyclerView.Adapter<QualificationViewHolder> implements
        OnRowClickListener, OnTeamHolderLongClickListener {

    private static final String TAG = SNBApp.APP_TAG + QualificationAdapter.class.getSimpleName();

    private ArrayList<Qualification> teams;

    private MainActivity mActivity;

    private CustomImage roundedImage;

    private int position;

    public QualificationAdapter(ArrayList<Qualification> aTeam, MainActivity aActivity) {
        teams = (null != aTeam) ? new ArrayList<>(aTeam) : new ArrayList<Qualification>();
        mActivity = aActivity;
    }

    public void setPositions(ArrayList<Qualification> aTeams) {
        teams = aTeams;
    }

    @Override
    public QualificationViewHolder onCreateViewHolder(ViewGroup aTeam, int viewType) {
        View viewLayout;
        QualificationViewHolder qualificationViewHolder;
        LayoutInflater inflater = LayoutInflater.from(aTeam.getContext());

        viewLayout = inflater.inflate(R.layout.row_qualification, aTeam, false);
        qualificationViewHolder = new QualificationViewHolder(viewLayout, this, this);
        return qualificationViewHolder;
    }

    @Override
    public void onBindViewHolder(QualificationViewHolder holder, int position) {
        Qualification handle = teams.get(position);
        holder.imgTeamLogo.setImageResource(TeamHelper.convertAcronymToDrawableResource(handle.getSigla()));
        holder.lblTeamName.setText(TeamHelper.fromAcronym(handle.getSigla()));
        holder.lblGames.setText(String.valueOf(handle.getJJ()));
        holder.lblWins.setText(String.valueOf(handle.getJG()));
        holder.lblLosts.setText(String.valueOf(handle.getJP()));
        holder.lblAverage.setText(handle.getPRO());
    }

    @Override
    public int getItemCount() {
        return (null != teams) ? teams.size() : 0;
    }

    public void addItem(int position, Qualification aTeam) {
        teams.add(position, aTeam);
        notifyItemInserted(position);
    }

    public Qualification removeItem(int position) {
        final Qualification team = teams.remove(position);
        notifyItemRemoved(position);
        return team;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Qualification team = teams.remove(fromPosition);
        teams.add(toPosition, team);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        teams.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<Qualification> aTeams) {
        applyAndAnimateRemovals(aTeams);
        applyAndAnimateAdditions(aTeams);
        applyAndAnimateMovedItems(aTeams);
    }

    private void applyAndAnimateRemovals(ArrayList<Qualification> newTeams) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            final Qualification rent = teams.get(i);
            if (!newTeams.contains(rent)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Qualification> newTeams) {
        for (int i = 0, count = newTeams.size(); i < count; i++) {
            final Qualification team = newTeams.get(i);
            if (!teams.contains(team)) {
                addItem(i, team);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Qualification> newTeams) {
        for (int toPosition = newTeams.size() - 1; toPosition >= 0; toPosition--) {
            final Qualification team = newTeams.get(toPosition);
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
//        position = (position > -1) ? position : 0;
//        Qualification q = teams.get(position);
//        Team team = new Team(0, q.getEquipo(), new ArrayList<Player>());
//        Intent i = new Intent().setClass(mActivity, TeamActivity.class);
//        i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
//        mActivity.startActivity(i);
//        mActivity.setResult(Activity.RESULT_OK);
    }
}
