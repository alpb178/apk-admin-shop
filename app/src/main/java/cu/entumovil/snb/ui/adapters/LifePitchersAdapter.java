package cu.entumovil.snb.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import cu.entumovil.snb.core.holders.LifePitchersViewHolder;
import cu.entumovil.snb.core.holders.LoadingViewHolder;
import cu.entumovil.snb.core.models.LifeTime;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.ui.activities.LifetimeActivity;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;

public class LifePitchersAdapter extends RecyclerView.Adapter {

    private static final String TAG = SNBApp.APP_TAG + LifePitchersAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;

    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<LifeTime> pitchers;

    private LifetimeActivity mActivity;

    private int visibleThreshold = 3;

    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    public LifePitchersAdapter(List<LifeTime> aLifeTimes, LifetimeActivity aActivity, RecyclerView recyclerView) {
        pitchers = (null != aLifeTimes) ? new ArrayList<>(aLifeTimes) : new ArrayList<LifeTime>();
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

    public void setPitchers(ArrayList<LifeTime> aPitchers) {
        pitchers = aPitchers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View viewLayout;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            viewLayout = inflater.inflate(R.layout.row_stats_pitchers, parent, false);
            vh = new LifePitchersViewHolder(viewLayout);
        } else {
            viewLayout = inflater.inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(viewLayout);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof LifePitchersViewHolder) {
            LifeTime handle = pitchers.get(position);
            Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
            CustomImage roundedImage = new CustomImage(photo, true);
            ((LifePitchersViewHolder) holder).img_stats_player_photo.setImageDrawable(roundedImage);

            Glide.with(mActivity)
                    .load(mActivity.getResources().getString(R.string.IMAGE_URL_PALYER).concat(handle.getFoto()))
                    .asBitmap()
                    .placeholder(R.drawable.player_no_photo)
                    .error(R.drawable.player_no_photo)
                    .into(new SimpleTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            CustomImage ri = new CustomImage(resource, true);
                            ((LifePitchersViewHolder) holder).img_stats_player_photo.setImageDrawable(ri);
                        }
                    });

            ((LifePitchersViewHolder) holder).lbl_stats_player_name.setText(handle.getNombre());
            ((LifePitchersViewHolder) holder).lbl_stats_player_team.setText("");
            ((LifePitchersViewHolder) holder).lbl_stats_player_jg.setText(handle.getJG());
            ((LifePitchersViewHolder) holder).lbl_stats_player_jp.setText(handle.getJP());
            ((LifePitchersViewHolder) holder).lbl_stats_player_js.setText(handle.getJS());
            ((LifePitchersViewHolder) holder).lbl_stats_player_average.setText(handle.getAVE_Op());
            ((LifePitchersViewHolder) holder).lbl_stats_player_runsallowed.setText(handle.getCL());
            ((LifePitchersViewHolder) holder).lbl_stats_player_pcl.setText(handle.getPCL());
            ((LifePitchersViewHolder) holder).lbl_stats_player_whip.setText(handle.getWHIP());
            ((LifePitchersViewHolder) holder).lbl_stats_player_kkball.setText(handle.getSO_BB());
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

    public void add(LifeTime aPitcher) {
        pitchers.add(aPitcher);
        notifyItemInserted(pitchers.size());
    }

    public void addItem(int position, LifeTime aPitcher) {
        pitchers.add(position, aPitcher);
        notifyItemInserted(position);
    }

    public LifeTime removeItem(int position) {
        final LifeTime pitcher = pitchers.remove(position);
        notifyItemRemoved(position);
        return pitcher;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final LifeTime pitcher = pitchers.remove(fromPosition);
        pitchers.add(toPosition, pitcher);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        pitchers.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<LifeTime> aPitchers) {
        applyAndAnimateRemovals(aPitchers);
        applyAndAnimateAdditions(aPitchers);
        applyAndAnimateMovedItems(aPitchers);
    }

    private void applyAndAnimateRemovals(List<LifeTime> newPitchers) {
        for (int i = pitchers.size() - 1; i >= 0; i--) {
            final LifeTime pitcher = pitchers.get(i);
            if (!newPitchers.contains(pitcher)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<LifeTime> newPtichers) {
        for (int i = 0, count = newPtichers.size(); i < count; i++) {
            final LifeTime pitcher = newPtichers.get(i);
            if (!pitchers.contains(pitcher)) {
                addItem(i, pitcher);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<LifeTime> newPitchers) {
        for (int toPosition = newPitchers.size() - 1; toPosition >= 0; toPosition--) {
            final LifeTime pitcher = newPitchers.get(toPosition);
            final int fromPosition = pitchers.indexOf(pitcher);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
