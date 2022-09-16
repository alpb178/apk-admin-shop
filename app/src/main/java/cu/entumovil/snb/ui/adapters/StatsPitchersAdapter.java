package cu.entumovil.snb.ui.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.LoadingViewHolder;
import cu.entumovil.snb.core.holders.StatsPitchersViewHolder;
import cu.entumovil.snb.core.models.Pitcher;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.activities.PlayerActivity;
import cu.entumovil.snb.ui.activities.StatsActivity;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;

public class StatsPitchersAdapter extends RecyclerView.Adapter implements OnRowClickListener {

    private static final String TAG = SNBApp.APP_TAG + StatsPitchersAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;

    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<Pitcher> pitchers;

    private StatsActivity mActivity;

    private int visibleThreshold = 3;

    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    private StatsType statsType;

    public StatsPitchersAdapter(List<Pitcher> aPitchers, StatsActivity aActivity, RecyclerView recyclerView, StatsType aStatsType) {
        pitchers = (null != aPitchers) ? new ArrayList<>(aPitchers) : new ArrayList<Pitcher>();
        mActivity = aActivity;
        statsType = aStatsType;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    loading = true;
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return pitchers.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    public void setPitchers(ArrayList<Pitcher> aPitchers) {
        pitchers = aPitchers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View viewLayout;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            viewLayout = inflater.inflate(R.layout.row_stats_pitchers, parent, false);
            vh = new StatsPitchersViewHolder(viewLayout, this);
        } else {
            viewLayout = inflater.inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(viewLayout);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StatsPitchersViewHolder) {

            ((StatsPitchersViewHolder) holder).layoutStatsNormalPitcher.setVisibility(View.VISIBLE);
            ((StatsPitchersViewHolder) holder).layoutStatsSpecialPitcher.setVisibility(View.GONE);

            final Pitcher handle = pitchers.get(position);
            Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
            CustomImage roundedImage = new CustomImage(photo, true);
            ((StatsPitchersViewHolder) holder).img_stats_player_photo.setImageDrawable(roundedImage);

            if (null != handle.getFoto()) {
                Glide.with(mActivity)
                        .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                        .asBitmap()
                        .placeholder(R.drawable.player_no_photo)
                        .error(R.drawable.player_no_photo)
                        .into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                CustomImage ri = new CustomImage(resource, true);
                                ((StatsPitchersViewHolder) holder).img_stats_player_photo.setImageDrawable(ri);
                            }
                        });
            }
            ((StatsPitchersViewHolder) holder).lbl_stats_player_name.setText(handle.getNombre());

            if (StatsType.SINGLE.equals(statsType)) {
                ((StatsPitchersViewHolder) holder).lbl_stats_player_team.setText(" (" + handle.getSigla() + ")");
                ((StatsPitchersViewHolder) holder).lbl_stats_player_team.setTextColor(ContextCompat.getColor(mActivity, TeamHelper.convertAcronymToColorResource(handle.getSigla())));
                ((StatsPitchersViewHolder) holder).lbl_stats_player_jg.setText(String.valueOf(handle.getJG()));
                ((StatsPitchersViewHolder) holder).lbl_stats_player_jp.setText(String.valueOf(handle.getJP()));
                ((StatsPitchersViewHolder) holder).lbl_stats_player_js.setText(String.valueOf(handle.getJS()));
                ((StatsPitchersViewHolder) holder).lbl_stats_player_average.setText(handle.getAVEBATEO());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_runsallowed.setText(String.valueOf(handle.getCL()));
                ((StatsPitchersViewHolder) holder).lbl_stats_player_pcl.setText(handle.getPCL());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_whip.setText(handle.getWHIP());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_kkball.setText(String.valueOf(handle.getSO()).concat("/").concat(String.valueOf(handle.getBB())));
            } else if (StatsType.SPECIALIZED.equals(statsType)) {
                ((StatsPitchersViewHolder) holder).lbl_stats_player_team.setVisibility(View.INVISIBLE);
                ((StatsPitchersViewHolder) holder).layoutStatsNormalPitcher.setVisibility(View.GONE);
                ((StatsPitchersViewHolder) holder).layoutStatsSpecialPitcher.setVisibility(View.VISIBLE);
                ((StatsPitchersViewHolder) holder).lbl_stats_player_special_h9.setText(handle.getH9());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_special_obp.setText(handle.getOBP());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_special_ops.setText(handle.getOPS());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_special_bb9.setText(handle.getBB_9());
                ((StatsPitchersViewHolder) holder).lbl_stats_player_special_so9.setText(handle.getSO_9());
            }
        } else {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return (null != pitchers) ? pitchers.size() : 0;
    }

    public void add(Pitcher aPitcher) {
        pitchers.add(aPitcher);
        notifyItemInserted(pitchers.size());
    }

    public void addItem(int position, Pitcher aPitcher) {
        pitchers.add(position, aPitcher);
        notifyItemInserted(position);
    }

    public Pitcher removeItem(int position) {
        final Pitcher pitcher = pitchers.remove(position);
        notifyItemRemoved(position);
        return pitcher;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Pitcher pitcher = pitchers.remove(fromPosition);
        pitchers.add(toPosition, pitcher);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        pitchers.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<Pitcher> aPitchers) {
        applyAndAnimateRemovals(aPitchers);
        applyAndAnimateAdditions(aPitchers);
        applyAndAnimateMovedItems(aPitchers);
    }

    private void applyAndAnimateRemovals(List<Pitcher> newPitchers) {
        for (int i = pitchers.size() - 1; i >= 0; i--) {
            final Pitcher pitcher = pitchers.get(i);
            if (!newPitchers.contains(pitcher)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Pitcher> newPtichers) {
        for (int i = 0, count = newPtichers.size(); i < count; i++) {
            final Pitcher pitcher = newPtichers.get(i);
            if (!pitchers.contains(pitcher)) {
                addItem(i, pitcher);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Pitcher> newPitchers) {
        for (int toPosition = newPitchers.size() - 1; toPosition >= 0; toPosition--) {
            final Pitcher pitcher = newPitchers.get(toPosition);
            final int fromPosition = pitchers.indexOf(pitcher);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public StatsType getStatsType() {
        return statsType;
    }

    public void setStatsType(StatsType statsType) {
        this.statsType = statsType;
    }

    @Override
    public void onRowPress(View rowPress, int position) {
        if (StatsType.SINGLE.equals(statsType)) {
            Intent intent = new Intent().setClass(mActivity, PlayerActivity.class);
            intent.putExtra(PlayerActivity.INTENT_PLAYER_HOLDER, pitchers.get(position));
            mActivity.startActivity(intent);
        }
    }

}
