package cu.entumovil.snb.core.utils;

import java.util.Calendar;

public class DayOfCalendar {
    Calendar date;
    boolean today;
    boolean available;
    boolean selected;

    public DayOfCalendar(Calendar date, boolean today, boolean available, boolean selected) {
        this.date = date;
        this.today = today;
        this.available = available;
        this.selected = selected;
    }

    public DayOfCalendar() {
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
