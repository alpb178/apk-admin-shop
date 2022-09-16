package cu.entumovil.snb.ui.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
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
import cu.entumovil.snb.core.holders.StatsHittersViewHolder;
import cu.entumovil.snb.core.models.Hitter;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.activities.PlayerActivity;
import cu.entumovil.snb.ui.activities.StatsActivity;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;
import cu.entumovil.snb.ui.listeners.OnRowClickListener;

public class StatsHittersAdapter extends RecyclerView.Adapter implements OnRowClickListener {

    private static final String TAG = SNBApp.APP_TAG + StatsHittersAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;

    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<Hitter> hitters;

    private StatsActivity mActivity;

    private int visibleThreshold = 3;

    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    private StatsType statsType;

    public StatsHittersAdapter(List<Hitter> aHitters, StatsActivity aActivity, RecyclerView recyclerView, StatsType aStatsType) {
        hitters = (null != aHitters) ? new ArrayList<>(aHitters) : new ArrayList<Hitter>();
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
                    Log.i(TAG, "onScrolled: End reached");
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return hitters.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View viewLayout;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            viewLayout = inflater.inflate(R.layout.row_stats_hitters, parent, false);
            vh = new StatsHittersViewHolder(viewLayout, this);
        } else {
            viewLayout = inflater.inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(viewLayout);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StatsHittersViewHolder) {
            final Hitter handle = hitters.get(position);

            ((StatsHittersViewHolder) holder).img_arrow_right.setVisibility(View.INVISIBLE);
            ((StatsHittersViewHolder) holder).lbl_stats_player_team.setVisibility(View.VISIBLE);
            ((StatsHittersViewHolder) holder).layoutStatsNormalHitter.setVisibility(View.VISIBLE);
            ((StatsHittersViewHolder) holder).layoutStatsSpecialHitter.setVisibility(View.GONE);

            if (StatsType.SINGLE.equals(statsType)) {
                Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
                CustomImage roundedImage = new CustomImage(photo, true);
                ((StatsHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(roundedImage);

                if (null != handle.getFoto()) {
                    Glide.with(mActivity)
                            .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                            .asBitmap()
                            .placeholder(R.drawable.player_no_photo)
                            .error(R.drawable.player_no_photo)
                            .into(new SimpleTarget<Bitmap>(100,100) {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                    CustomImage ri = new CustomImage(resource, true);
                                    ((StatsHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(ri);
                                }
                            });
//                    ((StatsHittersViewHolder) holder).img_stats_player_photo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            PlayerDialog playerDialog = new PlayerDialog(mActivity, handle);
//                            playerDialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
//                            playerDialog.show();
//                        }
//                    });
                }

                ((StatsHittersViewHolder) holder).lbl_stats_player_team.setText(" (" + handle.getSigla() + ")");
                ((StatsHittersViewHolder) holder).lbl_stats_player_team.setTextColor(ContextCompat.getColor(mActivity, TeamHelper.convertAcronymToColorResource(handle.getSigla())));
            } else if (StatsType.COLLECTIVE.equals(statsType)) {
                ((StatsHittersViewHolder) holder).lbl_stats_player_team.setVisibility(View.INVISIBLE);
                ((StatsHittersViewHolder) holder).img_arrow_right.setVisibility(View.VISIBLE);
                ((StatsHittersViewHolder) holder).img_stats_player_photo.setImageResource(TeamHelper.convertAcronymToDrawableResource(handle.getSigla()));
            } else if (StatsType.SPECIALIZED.equals(statsType)) {
                Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
                CustomImage roundedImage = new CustomImage(photo, true);
                ((StatsHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(roundedImage);

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
                                    ((StatsHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(ri);
                                }
                            });
                }

                ((StatsHittersViewHolder) holder).layoutStatsNormalHitter.setVisibility(View.GONE);
                ((StatsHittersViewHolder) holder).layoutStatsSpecialHitter.setVisibility(View.VISIBLE);
                ((StatsHittersViewHolder) holder).lbl_stats_player_team.setVisibility(View.INVISIBLE);
            }

            ((StatsHittersViewHolder) holder).lbl_stats_player_name.setText(handle.getNombre());
            if (StatsType.SINGLE.equals(statsType) || StatsType.COLLECTIVE.equals(statsType)) {
                ((StatsHittersViewHolder) holder).lbl_stats_player_average.setText(handle.getAVE());
                ((StatsHittersViewHolder) holder).lbl_stats_player_hits.setText(String.valueOf(handle.getH()));
                ((StatsHittersViewHolder) holder).lbl_stats_player_runs.setText(String.valueOf(handle.getCA()));
                ((StatsHittersViewHolder) holder).lbl_stats_player_runsimpulsed.setText(String.valueOf(handle.getCI()));
                ((StatsHittersViewHolder) holder).lbl_stats_player_homeruns.setText(String.valueOf(handle.getHR()));
                ((StatsHittersViewHolder) holder).lbl_stats_player_obp.setText(handle.getOBP());
                ((StatsHittersViewHolder) holder).lbl_stats_player_ops.setText(handle.getOPS());
                ((StatsHittersViewHolder) holder).lbl_stats_player_ballkk.setText(String.valueOf(handle.getBB()).concat("/").concat(String.valueOf(handle.getKK())));
            } else if (StatsType.SPECIALIZED.equals(statsType)) {
                ((StatsHittersViewHolder) holder).lbl_stats_player_special_cpr.setText(handle.getCPR());
                ((StatsHittersViewHolder) holder).lbl_stats_player_special_ccr.setText(handle.getCCR());
                ((StatsHittersViewHolder) holder).lbl_stats_player_special_babip.setText(handle.getBABIP());
                ((StatsHittersViewHolder) holder).lbl_stats_player_special_db.setText(handle.getDB());
                ((StatsHittersViewHolder) holder).lbl_stats_player_special_bbcb.setText(handle.getBB_CB());
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
        return (null != hitters) ? hitters.size() : 0;
    }

    public void add(Hitter aHitter) {
        hitters.add(aHitter);
        notifyItemInserted(hitters.size());
    }

    public void addItem(int position, Hitter aHitter) {
        hitters.add(position, aHitter);
        notifyItemInserted(position);
    }

    public Hitter removeItem(int position) {
        final Hitter hitter = hitters.remove(position);
        notifyItemRemoved(position);
        return hitter;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Hitter hitter = hitters.remove(fromPosition);
        hitters.add(toPosition, hitter);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        hitters.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<Hitter> aHitters) {
        applyAndAnimateRemovals(aHitters);
        applyAndAnimateAdditions(aHitters);
        applyAndAnimateMovedItems(aHitters);
    }

    private void applyAndAnimateRemovals(List<Hitter> newHitters) {
        for (int i = hitters.size() - 1; i >= 0; i--) {
            final Hitter hitter = hitters.get(i);
            if (!newHitters.contains(hitter)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Hitter> newHitters) {
        for (int i = 0, count = newHitters.size(); i < count; i++) {
            final Hitter hitter = newHitters.get(i);
            if (!hitters.contains(hitter)) {
                addItem(i, hitter);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Hitter> newHitters) {
        for (int toPosition = newHitters.size() - 1; toPosition >= 0; toPosition--) {
            final Hitter hitter = newHitters.get(toPosition);
            final int fromPosition = hitters.indexOf(hitter);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public StatsType getStatsType() {
        return statsType;
    }

    public void setStatsType(StatsType statsType)

    {
        this.statsType = statsType;
    }

    @Override
    public void onRowPress(View rowPress, int position) {
        if (StatsType.SINGLE.equals(statsType)) {
            Intent intent = new Intent().setClass(mActivity, PlayerActivity.class);
            intent.putExtra(PlayerActivity.INTENT_PLAYER_HOLDER, hitters.get(position));
            mActivity.startActivity(intent);
        }
//        } else if (StatsType.COLLECTIVE.equals(statsType)) {
//            Hitter h = hitters.get(position);
//            mActivity.loadSelectedTeam(h);
//        }
    }
}
