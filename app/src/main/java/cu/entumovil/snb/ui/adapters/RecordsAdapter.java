package cu.entumovil.snb.ui.adapters;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.holders.LoadingViewHolder;
import cu.entumovil.snb.core.holders.RecordViewHolder;
import cu.entumovil.snb.core.models.Record;
import cu.entumovil.snb.ui.activities.RecordActivity;
import cu.entumovil.snb.ui.listeners.OnLoadMoreListener;

public class RecordsAdapter extends RecyclerView.Adapter {

    private static final String TAG = SNBApp.APP_TAG + LeadersAdapter.class.getSimpleName();

    private final int VIEW_TYPE_ITEM = 0;

    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<Record> records;

    private RecordActivity mActivity;

    private int visibleThreshold = 3;

    private int lastVisibleItem, totalItemCount;

    private boolean loading;

    private OnLoadMoreListener onLoadMoreListener;

    public RecordsAdapter(List<Record> aRecords, RecordActivity aActivity, RecyclerView recyclerView) {
        records = (null != aRecords) ? new ArrayList<>(aRecords) : new ArrayList<Record>();
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View viewLayout;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_ITEM) {
            viewLayout = inflater.inflate(R.layout.row_record, parent, false);
            vh = new RecordViewHolder(viewLayout);
        } else {
            viewLayout = inflater.inflate(R.layout.layout_loading_item, parent, false);
            vh = new LoadingViewHolder(viewLayout);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecordViewHolder) {
            final Record handle = records.get(position);
            String str = (null != handle.getNumRomano()) ? handle.getNumRomano() : handle.getNumSerie();
            ((RecordViewHolder) holder).recordName.setText(handle.getNombreRecord());
            ((RecordViewHolder) holder).serie.setText("Serie: ".concat(str));
            ((RecordViewHolder) holder).name.setVisibility(View.GONE);
            ((RecordViewHolder) holder).serieType.setVisibility(View.GONE);
            ((RecordViewHolder) holder).img_calendar.setVisibility(View.GONE);
            ((RecordViewHolder) holder).date.setVisibility(View.GONE);
            ((RecordViewHolder) holder).lblFmto.setVisibility(View.GONE);
            ((RecordViewHolder) holder).lblFmto1.setVisibility(View.GONE);
            ((RecordViewHolder) holder).lblFmto2.setVisibility(View.GONE);
            ((RecordViewHolder) holder).txtCant1.setVisibility(View.GONE);
            ((RecordViewHolder) holder).txtCant2.setVisibility(View.GONE);
            if (null != handle.getRecordista()) {
                ((RecordViewHolder) holder).name.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).name.setText(handle.getRecordista());
                ((RecordViewHolder) holder).team.setText("(" + handle.getSiglaEq() + ")");
            } else {
                ((RecordViewHolder) holder).name.setVisibility(View.GONE);
                ((RecordViewHolder) holder).team.setVisibility(View.GONE);
            }
            if (null != handle.getTipoSerie()) {
                ((RecordViewHolder) holder).serieType.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).serieType.setText(handle.getTipoSerie() + ":");
            }
            ((RecordViewHolder) holder).serieYear.setText(handle.getSerieAno());
            if (null != handle.getFecha()) {
                ((RecordViewHolder) holder).img_calendar.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).date.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).date.setText(handle.getFecha());
            }
            ((RecordViewHolder) holder).txtCant.setText(handle.getCant());
            if (null != handle.getFmtoCANT()) {
                ((RecordViewHolder) holder).lblFmto.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).lblFmto.setText(handle.getFmtoCANT());
            }
            if (null != handle.getFmtoCANT1()) {
                ((RecordViewHolder) holder).lblFmto1.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).lblFmto2.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).lblFmto1.setText(handle.getFmtoCANT1());
                ((RecordViewHolder) holder).lblFmto2.setText(handle.getFmtoCANT2());
                ((RecordViewHolder) holder).txtCant1.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).txtCant2.setVisibility(View.VISIBLE);
                ((RecordViewHolder) holder).txtCant1.setText(handle.getCant1());
                ((RecordViewHolder) holder).txtCant2.setText(handle.getCant2());
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
        return (null != records) ? records.size() : 0;
    }

    public void add(Record aRecord) {
        records.add(aRecord);
        notifyItemInserted(records.size());
    }

    public void clear() {
        records.clear();
        notifyDataSetChanged();
    }

    public void animateTo(List<Record> aRecords) {
        applyAndAnimateRemovals(aRecords);
        applyAndAnimateAdditions(aRecords);
        applyAndAnimateMovedItems(aRecords);
    }

    private void applyAndAnimateRemovals(List<Record> newRecords) {
        for (int i = records.size() - 1; i >= 0; i--) {
            final Record record = records.get(i);
            if (!newRecords.contains(record)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Record> newRecords) {
        for (int i = 0, count = newRecords.size(); i < count; i++) {
            final Record record = newRecords.get(i);
            if (!records.contains(record)) {
                addItem(i, record);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Record> newRecords) {
        for (int toPosition = newRecords.size() - 1; toPosition >= 0; toPosition--) {
            final Record record = newRecords.get(toPosition);
            final int fromPosition = records.indexOf(record);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(int position, Record aLeader) {
        records.add(position, aLeader);
        notifyItemInserted(position);
    }

    public Record removeItem(int position) {
        final Record record = records.remove(position);
        notifyItemRemoved(position);
        return record;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Record record = records.remove(fromPosition);
        records.add(toPosition, record);
        notifyItemMoved(fromPosition, toPosition);
    }
}
