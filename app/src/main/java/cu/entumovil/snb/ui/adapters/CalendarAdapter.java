package cu.entumovil.snb.ui.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cu.entumovil.snb.R;
import cu.entumovil.snb.core.utils.DayOfCalendar;

/**
 * Created by user on 6/8/2017.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private Context context;
    private List<DayOfCalendar>dayOfCalendars = new ArrayList<>();
    private int selectedPosition;
    private OnItemClickListener onItemClickListener;
    private Calendar selectedDate;


    public CalendarAdapter(List<DayOfCalendar> days, Context context, Calendar selectedDate) {
        this.context = context;
        this.selectedDate = selectedDate;
        dayOfCalendars = days;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        this.selectedDate = dayOfCalendars.get(selectedPosition).getDate();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_row,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("d");
        holder.day.setText(simpleDateFormatDay.format(dayOfCalendars.get(position).getDate().getTime()));
        if(simpleDateFormat.format(dayOfCalendars.get(position).getDate().getTime()).equals(simpleDateFormat.format(selectedDate.getTime())) ){
            dayOfCalendars.get(position).setSelected(true);
        }else {
            dayOfCalendars.get(position).setSelected(false);
        }

        if(dayOfCalendars.get(position).isToday()){
//            if(!dayOfCalendars.get(position).isSelected()) {
                holder.day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.today));
//            }
//            holder.day.setTextColor(context.getResources().getColor(android.R.color.white));
        }
        if(!dayOfCalendars.get(position).isAvailable()){
            if(dayOfCalendars.get(position).isToday()){
                holder.day.setTextColor(context.getResources().getColor(R.color.enabled_days));
            }else {
                holder.day.setTextColor(context.getResources().getColor(R.color.disabled_days));
            }
        }else {
            if(dayOfCalendars.get(position).isSelected() ){
                holder.day.setTextColor(context.getResources().getColor(android.R.color.white));
                holder.day.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selected_date));
            }else{
                holder.day.setTextColor(context.getResources().getColor(R.color.enabled_days));
            }
            holder.day.setOnClickListener(holder);
        }

    }

    @Override
    public int getItemCount() {
        return dayOfCalendars.size();
    }

    public List<DayOfCalendar> getDayOfCalendars() {
        return dayOfCalendars;
    }

    public void setDayOfCalendars(List<DayOfCalendar> dayOfCalendars) {
        this.dayOfCalendars = dayOfCalendars;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setNewSelectedDay(int position, View view) {
        dayOfCalendars.get(selectedPosition).setSelected(false);
        dayOfCalendars.get(position).setSelected(true);
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Calendar selectedDate, int position) {
        this.selectedDate = selectedDate;
        dayOfCalendars.get(selectedPosition).setSelected(false);
        dayOfCalendars.get(position).setSelected(true);
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView day;
        public ViewHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.day_text);

        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(itemView,getPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

}
