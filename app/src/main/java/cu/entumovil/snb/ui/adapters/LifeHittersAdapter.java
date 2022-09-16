package cu.entumovil.snb.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import cu.entumovil.snb.core.holders.LifeHittersViewHolder;
import cu.entumovil.snb.core.holders.LoadingViewHolder;
import cu.entumovil.snb.core.models.LifeTime;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.ui.activities.LifetimeActivity;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;

public class LifeHittersAdapter extends RecyclerView.Adapter {

    private static final String TAG = SNBApp.APP_TAG + LifeHittersAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;

    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<LifeTime> hitters;

    private LifetimeActivity mActivity;

    private int visibleThreshold = 3;

    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    public LifeHittersAdapter(List<LifeTime> aLifeTimes, LifetimeActivity aActivity, RecyclerView recyclerView) {
        hitters = (null != aLifeTimes) ? new ArrayList<>(aLifeTimes) : new ArrayList<LifeTime>();
        mActivity = aActivity;
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
            vh = new LifeHittersViewHolder(viewLayout);
        } else {
            viewLayout = inflater.inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(viewLayout);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LifeHittersViewHolder) {
            LifeTime handle = hitters.get(position);
            ((LifeHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(null);
            ((LifeHittersViewHolder) holder).img_arrow_right.setVisibility(View.INVISIBLE);
            ((LifeHittersViewHolder) holder).lbl_stats_player_team.setVisibility(View.VISIBLE);
            Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
            CustomImage roundedImage = new CustomImage(photo, true);
            ((LifeHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(roundedImage);

            Glide.with(mActivity)
                    .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                    .asBitmap()
                    .placeholder(R.drawable.player_no_photo)
                    .error(R.drawable.player_no_photo)
                    .into(new SimpleTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            CustomImage ri = new CustomImage(resource, true);
                            ((LifeHittersViewHolder) holder).img_stats_player_photo.setImageDrawable(ri);
                        }
                    });

            ((LifeHittersViewHolder) holder).lbl_stats_player_name.setText(handle.getNombre());
            ((LifeHittersViewHolder) holder).lbl_stats_player_team.setText("");
            ((LifeHittersViewHolder) holder).lbl_stats_player_average.setText(handle.getAVE());
            ((LifeHittersViewHolder) holder).lbl_stats_player_hits.setText(handle.getH());
            ((LifeHittersViewHolder) holder).lbl_stats_player_runs.setText(handle.getCA());
            ((LifeHittersViewHolder) holder).lbl_stats_player_runsimpulsed.setText(handle.getCI());
            ((LifeHittersViewHolder) holder).lbl_stats_player_homeruns.setText(handle.getHR());
            ((LifeHittersViewHolder) holder).lbl_stats_player_obp.setText(handle.getOBP());
            ((LifeHittersViewHolder) holder).lbl_stats_player_ops.setText(handle.getOPS());
            ((LifeHittersViewHolder) holder).lbl_stats_player_ballkk.setText(handle.getBB_SO());
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

    public void add(LifeTime aLifeTime) {
        hitters.add(aLifeTime);
        notifyItemInserted(hitters.size());
    }

    public void addItem(int position, LifeTime aLifeTime) {
        hitters.add(position, aLifeTime);
        notifyItemInserted(position);
    }

    public LifeTime removeItem(int position) {
        final LifeTime hitter = hitters.remove(position);
        notifyItemRemoved(position);
        return hitter;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final LifeTime hitter = hitters.remove(fromPosition);
        hitters.add(toPosition, hitter);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        hitters.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<LifeTime> aLifeTimes) {
        applyAndAnimateRemovals(aLifeTimes);
        applyAndAnimateAdditions(aLifeTimes);
        applyAndAnimateMovedItems(aLifeTimes);
    }

    private void applyAndAnimateRemovals(List<LifeTime> newHitters) {
        for (int i = hitters.size() - 1; i >= 0; i--) {
            final LifeTime hitter = hitters.get(i);
            if (!newHitters.contains(hitter)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<LifeTime> newHitters) {
        for (int i = 0, count = newHitters.size(); i < count; i++) {
            final LifeTime hitter = newHitters.get(i);
            if (!hitters.contains(hitter)) {
                addItem(i, hitter);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<LifeTime> newHitters) {
        for (int toPosition = newHitters.size() - 1; toPosition >= 0; toPosition--) {
            final LifeTime hitter = newHitters.get(toPosition);
            final int fromPosition = hitters.indexOf(hitter);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
