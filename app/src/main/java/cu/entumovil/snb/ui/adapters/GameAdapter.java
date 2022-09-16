package cu.entumovil.snb.ui.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.GameViewHolder;
import cu.entumovil.snb.core.models.Game;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.activities.GameDetailsActivity;
import cu.entumovil.snb.ui.activities.MainActivity;
import cu.entumovil.snb.ui.listeners.OnGameRowClickListener;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> implements OnGameRowClickListener {

    private static final String TAG = SNBApp.APP_TAG + GameAdapter.class.getSimpleName();

    private ArrayList<Game> games;

    private MainActivity mActivity;

    private boolean today;

    public GameAdapter(ArrayList<Game> aGame, MainActivity aActivity) {
        games = (null != aGame) ? new ArrayList<>(aGame) : new ArrayList<Game>();
        mActivity = aActivity;
    }

    public void setGames(ArrayList<Game> aGames) {
        games = aGames;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup aGame, int viewType) {
        View viewLayout;
        GameViewHolder gameViewHolder;
        LayoutInflater inflater = LayoutInflater.from(aGame.getContext());

        viewLayout = inflater.inflate(R.layout.row_game, aGame, false);
        gameViewHolder = new GameViewHolder(viewLayout, this);
        return gameViewHolder;
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        final Game handle = games.get(position);
        holder.lbl_game_date.setText(isToday() ? Utils.convert24hrsTo12Hrs(handle.getHora()) : handle.getFecha().replace('/','.'));
        holder.lbl_game_current_ininng.setText(Utils.gameStatusWrapper(handle.getEstado()));
        holder.img_team_logo_visitor.setAlpha(1f);
        holder.img_team_logo_homeclub.setAlpha(1f);
        holder.img_team_logo_visitor.setImageResource(TeamHelper.convertAcronymToDrawableResource(handle.getVS()));
        holder.img_team_logo_homeclub.setImageResource(TeamHelper.convertAcronymToDrawableResource(handle.getHC()));
        holder.lbl_game_team_visitor.setText(handle.getVS());
        holder.lbl_game_runs_visitor.setText(String.valueOf(handle.getCVS()));
        holder.lbl_game_team_homeclub.setText(handle.getHC());
        holder.lbl_game_runs_homeclub.setText(String.valueOf(handle.getCHC()));
        if (handle.getCVS() > handle.getCHC()) {
            holder.lbl_game_team_visitor.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_800)));
            holder.lbl_game_runs_visitor.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_800)));
            holder.lbl_game_team_homeclub.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_500)));
            holder.lbl_game_runs_homeclub.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_500)));
        } else if (handle.getCVS() < handle.getCHC()) {
            holder.lbl_game_team_visitor.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_500)));
            holder.lbl_game_runs_visitor.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_500)));
            holder.lbl_game_team_homeclub.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_800)));
            holder.lbl_game_runs_homeclub.setTextColor(ColorStateList.valueOf(mActivity.getResources().getColor(R.color.md_grey_800)));
        }

//        holder.lbl_game_team_visitor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Team team = new Team(handle.getIdPerdedor(), handle.getPerdedor(), new ArrayList<Player>());
//                Intent i = new Intent().setClass(mActivity, TeamActivity.class);
//                i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
//                mActivity.startActivity(i);
//            }
//        });
//        holder.img_team_logo_visitor.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Team team = new Team(handle.getIdPerdedor(), handle.getPerdedor(), new ArrayList<Player>());
//                Intent i = new Intent().setClass(mActivity, TeamActivity.class);
//                i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
//                mActivity.startActivity(i);
//            }
//        });
//        holder.lbl_game_team_homeclub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Team team = new Team(handle.getIdganador(), handle.getGanador(), new ArrayList<Player>());
//                Intent i = new Intent().setClass(mActivity, TeamActivity.class);
//                i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
//                mActivity.startActivity(i);
//            }
//        });
//        holder.img_team_logo_homeclub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Team team = new Team(handle.getIdganador(), handle.getGanador(), new ArrayList<Player>());
//                Intent i = new Intent().setClass(mActivity, TeamActivity.class);
//                i.putExtra(TeamActivity.INTENT_TEAM_HOLDER, team);
//                mActivity.startActivity(i);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return (null != games) ? games.size() : 0;
    }

    public void addItem(int position, Game aGame) {
        games.add(position, aGame);
        notifyItemInserted(position);
    }

    public Game removeItem(int position) {
        final Game game = games.remove(position);
        notifyItemRemoved(position);
        return game;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Game game = games.remove(fromPosition);
        games.add(toPosition, game);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        games.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<Game> aGames) {
        applyAndAnimateRemovals(aGames);
        applyAndAnimateAdditions(aGames);
        applyAndAnimateMovedItems(aGames);
    }

    private void applyAndAnimateRemovals(ArrayList<Game> newGames) {
        for (int i = games.size() - 1; i >= 0; i--) {
            final Game game = games.get(i);
            if (!newGames.contains(game)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Game> newGames) {
        for (int i = 0, count = newGames.size(); i < count; i++) {
            final Game game = newGames.get(i);
            if (!games.contains(game)) {
                addItem(i, game);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Game> newGames) {
        for (int toPosition = newGames.size() - 1; toPosition >= 0; toPosition--) {
            final Game game = newGames.get(toPosition);
            final int fromPosition = games.indexOf(game);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public void onGameRowClick(View gameHolderRow, int position) {
        position = (position > -1) ? position : 0;
        Game game = games.get(position);
        if (!game.getEstado().equals("Progr")) {
            Intent i = new Intent(mActivity, GameDetailsActivity.class);
            i.putExtra(GameDetailsActivity.INTENT_GAME_HOLDER, game);
            mActivity.startActivity(i);
        }
        else
        {
            final Dialog d = new Dialog(mActivity);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_details);
            Button b = (Button) d.findViewById(R.id.btnOk);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            d.show();
        }
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }
}
