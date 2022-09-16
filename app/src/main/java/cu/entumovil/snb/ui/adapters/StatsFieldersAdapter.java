package cu.entumovil.snb.ui.adapters;

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
import cu.entumovil.snb.core.holders.StatsFieldersViewHolder;
import cu.entumovil.snb.core.models.Fielder;
import cu.entumovil.snb.core.utils.CustomImage;
import cu.entumovil.snb.core.utils.StatsType;
import cu.entumovil.snb.core.utils.TeamHelper;
import cu.entumovil.snb.ui.activities.StatsActivity;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;

public class StatsFieldersAdapter extends RecyclerView.Adapter {

    private static final String TAG = SNBApp.APP_TAG + StatsFieldersAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;

    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<Fielder> fielders;

    private StatsActivity mActivity;

    private int visibleThreshold = 3;

    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    private StatsType statsType;

    public StatsFieldersAdapter(List<Fielder> aFielders, StatsActivity aActivity, RecyclerView recyclerView, StatsType aStatsType) {
        fielders = (null != aFielders) ? new ArrayList<>(aFielders) : new ArrayList<Fielder>();
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
        return fielders.get(position) != null ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
    }

    public void setFielders(ArrayList<Fielder> aFielders) {
        fielders = aFielders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View viewLayout;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            viewLayout = inflater.inflate(R.layout.row_stats_fielders, parent, false);
            vh = new StatsFieldersViewHolder(viewLayout);
        } else {
            viewLayout = inflater.inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(viewLayout);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof StatsFieldersViewHolder) {
            final Fielder handle = fielders.get(position);
            Bitmap photo = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.player_no_photo);
            CustomImage roundedImage = new CustomImage(photo, true);
            ((StatsFieldersViewHolder) holder).img_stats_player_photo.setImageDrawable(roundedImage);

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
                                ((StatsFieldersViewHolder) holder).img_stats_player_photo.setImageDrawable(ri);
                            }
                        });
            }
            ((StatsFieldersViewHolder) holder).lbl_stats_player_name.setText(handle.getNombre());
            ((StatsFieldersViewHolder) holder).lbl_stats_player_team.setText(" (" + handle.getSigla() + ")");
            ((StatsFieldersViewHolder) holder).lbl_stats_player_team.setTextColor(ContextCompat.getColor(mActivity, TeamHelper.convertAcronymToColorResource(handle.getSigla())));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_jj.setText(String.valueOf(handle.getJJ()));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_ga.setText(String.valueOf(handle.getGA()));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_inn.setText(String.valueOf(handle.getINN()));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_o.setText(String.valueOf(handle.getO()));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_a.setText(String.valueOf(handle.getA()));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_e.setText(String.valueOf(handle.getE()));
            ((StatsFieldersViewHolder) holder).lbl_stats_player_average.setText(handle.getAVE());
            ((StatsFieldersViewHolder) holder).lbl_stats_player_dp.setText(String.valueOf(handle.getDP()));
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
        return (null != fielders) ? fielders.size() : 0;
    }

    public void add(Fielder aFielder) {
        fielders.add(aFielder);
        notifyItemInserted(fielders.size());
    }

    public void addItem(int position, Fielder aFielder) {
        fielders.add(position, aFielder);
        notifyItemInserted(position);
    }

    public Fielder removeItem(int position) {
        final Fielder fielder = fielders.remove(position);
        notifyItemRemoved(position);
        return fielder;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Fielder fielder = fielders.remove(fromPosition);
        fielders.add(toPosition, fielder);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        fielders.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<Fielder> aFielders) {
        applyAndAnimateRemovals(aFielders);
        applyAndAnimateAdditions(aFielders);
        applyAndAnimateMovedItems(aFielders);
    }

    private void applyAndAnimateRemovals(List<Fielder> newFielders) {
        for (int i = fielders.size() - 1; i >= 0; i--) {
            final Fielder fielder = fielders.get(i);
            if (!newFielders.contains(fielder)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Fielder> newFielders) {
        for (int i = 0, count = newFielders.size(); i < count; i++) {
            final Fielder fielder = newFielders.get(i);
            if (!fielders.contains(fielder)) {
                addItem(i, fielder);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Fielder> newFielders) {
        for (int toPosition = newFielders.size() - 1; toPosition >= 0; toPosition--) {
            final Fielder fielder = newFielders.get(toPosition);
            final int fromPosition = fielders.indexOf(fielder);
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

}
