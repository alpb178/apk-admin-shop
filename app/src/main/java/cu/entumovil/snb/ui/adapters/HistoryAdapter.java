package cu.entumovil.snb.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.HistoryViewHolder;
import cu.entumovil.snb.core.models.History;
import cu.entumovil.snb.ui.activities.HistoryActivity;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private static final String TAG = SNBApp.APP_TAG + HistoryAdapter.class.getSimpleName();

    private ArrayList<History> lHistories;

    private HistoryActivity mActivity;

    private int position;

    public HistoryAdapter(ArrayList<History> aHistories, HistoryActivity aActivity) {
        lHistories = (null != aHistories) ? new ArrayList<>(aHistories) : new ArrayList<History>();
        mActivity = aActivity;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup aHistories, int viewType) {
        View viewLayout;
        HistoryViewHolder newsViewHolder;
        LayoutInflater inflater = LayoutInflater.from(aHistories.getContext());

        viewLayout = inflater.inflate(R.layout.row_history, aHistories, false);
        newsViewHolder = new HistoryViewHolder(viewLayout);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {
        final History handle = lHistories.get(position);
        holder.img_hitory.setVisibility(View.GONE);
        holder.txt_history_resume.setText(handle.getTit());
        holder.txt_history_description.setText(handle.getDesc());
        if (null != handle.getFoto()) {
            Glide.with(mActivity)
                    .load(mActivity.getResources().getString(R.string.IMAGE_URL_HISTORY).concat(handle.getFoto()))
                    .fitCenter()
                    .placeholder(R.drawable.img_default_news)
                    .error(R.drawable.img_default_news)
                    .into(holder.img_hitory);
            holder.img_hitory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return (null != lHistories) ? lHistories.size() : 0;
    }

    public void addItem(int position, History aHistory) {
        lHistories.add(position, aHistory);
        notifyItemInserted(position);
    }

    public History removeItem(int position) {
        final History history = lHistories.remove(position);
        notifyItemRemoved(position);
        return history;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final History history = lHistories.remove(fromPosition);
        lHistories.add(toPosition, history);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void clear() {
        lHistories.clear();
        notifyDataSetChanged();
    }

    public void animateTo(ArrayList<History> aHistories) {
        applyAndAnimateRemovals(aHistories);
        applyAndAnimateAdditions(aHistories);
        applyAndAnimateMovedItems(aHistories);
    }

    private void applyAndAnimateRemovals(ArrayList<History> aHistories) {
        for (int i = lHistories.size() - 1; i >= 0; i--) {
            final History history = lHistories.get(i);
            if (!aHistories.contains(history)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<History> aHistories) {
        for (int i = 0, count = aHistories.size(); i < count; i++) {
            final History history = aHistories.get(i);
            if (!lHistories.contains(history)) {
                addItem(i, history);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<History> aHistories) {
        for (int toPosition = aHistories.size() - 1; toPosition >= 0; toPosition--) {
            final History history = aHistories.get(toPosition);
            final int fromPosition = lHistories.indexOf(history);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public int getPosition() {
        return position;
    }

}
