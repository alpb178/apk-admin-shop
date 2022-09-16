package cu.entumovil.snb.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cu.entumovil.snb.R;
import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Cache;
import cu.entumovil.snb.core.managers.GameManager;
import cu.entumovil.snb.core.models.Game;
import cu.entumovil.snb.core.utils.DayDrawableHelper;
import cu.entumovil.snb.core.utils.DayOfCalendar;
import cu.entumovil.snb.core.utils.Utils;
import cu.entumovil.snb.ui.activities.MainActivity;
import cu.entumovil.snb.ui.adapters.CalendarAdapter;
import cu.entumovil.snb.ui.adapters.GameAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GamesFragment extends Fragment {

    private static final String TAG = SNBApp.APP_TAG + GamesFragment.class.getSimpleName();

    private Context context;

    private RecyclerView gameRecyclerView;

    private GameAdapter gameAdapter;

    public ArrayList<Game> games;

    private View rootView;

    private LinearLayout layoutNotDayGames;

    private String gameDate;

    private DatePickerDialog gameDatePickerDialog;

    private Dialog calendarDialog;

    private Calendar selectedDateGame;

    private LinearLayout linearLayout, mainCalendarLayout;

    private MenuItem item;

    private boolean today;

    private SwipeRefreshLayout swipeRefresh;

    private Dao<Cache, Long> dao;

    private Gson gson;

    public GamesFragment() { }

    public static GamesFragment newInstance() {
        GamesFragment fragment = new GamesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        rootView = view;
        context = view.getContext();
        setHasOptionsMenu(true);
        initRecyclerView();
        setDateTimeField();

        layoutNotDayGames = (LinearLayout) view.findViewById(R.id.layoutNotDayGames);
        gameRecyclerView.setVisibility(View.GONE);
        layoutNotDayGames.setVisibility(View.VISIBLE);

        gameDate = Utils.formatDate(null, true);
        selectedDateGame = Calendar.getInstance();
        gson = new Gson();
        try {
            dao = SNBApp.DB_HELPER.getCachesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        retrieveData();
        return view;
    }

    private void initRecyclerView() {
        gameRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_game_list);
        gameAdapter = new GameAdapter(games, (MainActivity) getActivity());
        gameRecyclerView.setAdapter(gameAdapter);
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        gameRecyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshGames);
        swipeRefresh.setColorSchemeResources(
                R.color.colorPrimaryDark,
                R.color.colorAccent,
                R.color.md_grey_400
        );
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
            }
        });
    }

    private void retrieveData() {
          try {
            Log.println(Log.ASSERT,"Game","Try 1");
            swipeRefresh.setRefreshing(true);
            final GameManager gameManager = new GameManager();
            Log.println(Log.ASSERT,"Game Date",gameDate.toString());
            final Call<ArrayList<Game>> call = gameManager.gamesOnDay(gameDate);
            call.enqueue(new Callback<ArrayList<Game>>() {

                @Override
                public void onResponse(Call<ArrayList<Game>> call, Response<ArrayList<Game>> response) {
                       if (null != response.body()) {
                        games = response.body();
                        gameAdapter.setToday(isToday());
                        gameAdapter.clear();
                        layoutNotDayGames.setVisibility(View.GONE);
                        gameRecyclerView.setVisibility(View.VISIBLE);
                        gameAdapter.animateTo(games);
                     //   Log.println(Log.ASSERT,"Game Stadium",games.get(1).getEstadio().toString());
                        try {
                            popCache();
                            Map<String, Object> fieldValues = new HashMap<>();
                            fieldValues.put("guiType", "games");
                            fieldValues.put("selectedDate", Utils.formatDate(selectedDateGame.getTime(), true));
                            List q = dao.queryForFieldValues(fieldValues);
                            if (!q.isEmpty()) {
                                DeleteBuilder<Cache, ?> deleteBuilder = dao.deleteBuilder();
                                deleteBuilder.where().eq("id", ((Cache) q.get(0)).getId());
                                deleteBuilder.delete();
                            }
                            Cache cache = new Cache("games", gson.toJson(games));
                            cache.setSelectedDate(Utils.formatDate(selectedDateGame.getTime(), true));
                            dao.create(cache);
                        } catch (SQLException e) {
                            Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                            Log.e(TAG, e.getMessage());
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Game>> call,  Throwable throwable) {
                    try {

                        Map<String, Object> fieldValues = new HashMap<>();
                        fieldValues.put("guiType", "games");
                        fieldValues.put("selectedDate", Utils.formatDate(selectedDateGame.getTime(), true));
                        List q = dao.queryForFieldValues(fieldValues);
                          if (!q.isEmpty()) {
                            Type listType = new TypeToken<ArrayList<Game>>(){}.getType();
                            games = gson.fromJson(((Cache) q.get(0)).getJson(), listType);
                            gameAdapter.setToday(isToday());
                            gameAdapter.clear();
                            layoutNotDayGames.setVisibility(View.GONE);
                            gameRecyclerView.setVisibility(View.VISIBLE);
                            gameAdapter.animateTo(games);
                            gameRecyclerView.scrollToPosition(0);
                        } else {
                            gameRecyclerView.setVisibility(View.GONE);
                            layoutNotDayGames.setVisibility(View.VISIBLE);
                        }
                    } catch (SQLException e) {
                        Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, e.getMessage());
                    }
                    swipeRefresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            try {
                Log.println(Log.ASSERT,"Game","Try 2");
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.put("guiType", "games");
                fieldValues.put("selectedDate", Utils.formatDate(selectedDateGame.getTime(), true));
                List q = dao.queryForFieldValues(fieldValues);
                if (!q.isEmpty()) {
                    Type listType = new TypeToken<ArrayList<Game>>(){}.getType();
                    games = gson.fromJson(((Cache) q.get(0)).getJson(), listType);
                    gameAdapter.setToday(isToday());
                    gameAdapter.clear();
                    layoutNotDayGames.setVisibility(View.GONE);
                    gameRecyclerView.setVisibility(View.VISIBLE);
                    gameAdapter.animateTo(games);
                    gameRecyclerView.scrollToPosition(0);
                } else {
                    gameRecyclerView.setVisibility(View.GONE);
                    layoutNotDayGames.setVisibility(View.VISIBLE);
                }
            } catch (SQLException ex) {
                Toast.makeText(getActivity(), getString(R.string.message_unexpected_error), Toast.LENGTH_SHORT).show();
                Log.e(TAG, ex.getMessage());
            }
            swipeRefresh.setRefreshing(false);
            Log.e(TAG, e.getMessage());
        }
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        gameDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                gameDate = Utils.formatDate(newDate.getTime(), true);

                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_item);
                ((ImageView) item.getActionView()).setImageResource(DayDrawableHelper.convertDayToDrawableResource(newDate.get(Calendar.DAY_OF_MONTH)));
                item.getActionView().startAnimation(animation);
                retrieveData();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Calendar calendar = Calendar.getInstance();
        item = menu.findItem(R.id.action_calendar);
        ((ImageView) item.getActionView()).setImageResource(DayDrawableHelper.convertDayToDrawableResource(calendar.get(Calendar.DAY_OF_MONTH)));
        item.getActionView().setPadding(18,12,0,24);
        item.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog(selectedDateGame);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.action_calendar);
        item.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_calendar) {
            showCalendarDialog(selectedDateGame);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public String toString() {
        return SNBApp.application.getResources().getString(R.string.label_tab_gameday);
    }

    private void initializeCalendar(int month, Dialog dialog, final Calendar selectedDate) {
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.day_list);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(7,StaggeredGridLayoutManager.VERTICAL);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.YEAR,selectedDate.get(Calendar.YEAR));

        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int firstDayOfMonth = calendar.getMinimum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat dateFormatMonthYear = new SimpleDateFormat("MMMM/yyyy");
        TextView year = (TextView) dialog.findViewById(R.id.year);
        year.setText(dateFormatMonthYear.format(calendar.getTime()).replace("/"," de "));

        calendar.set(Calendar.DAY_OF_MONTH,firstDayOfMonth);
        int firstDayOfWeekOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        final List<DayOfCalendar>calendarList = new ArrayList<>();
        boolean setToday = false;
        if(firstDayOfWeekOfMonth-1!=0) {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            int lastDayOfBeforeMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int startDay = lastDayOfBeforeMonth - (firstDayOfWeekOfMonth-2);

            for (int i = 0 ; i < (firstDayOfWeekOfMonth-1); i++) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), startDay);

                DayOfCalendar dayOfCalendar = new DayOfCalendar();
                dayOfCalendar.setDate(newDate);
                dayOfCalendar.setAvailable(false);

                calendarList.add(dayOfCalendar);
                startDay++;
            }
        }
        int dayValue = 1;
        int startValue = calendarList.size();

        boolean available = true;

        boolean nextMonth = false;
        calendar.set(Calendar.MONTH, month);
        int positionToday=0;
        for (int i = startValue; i < 42; i++) {

            final Calendar newDate = Calendar.getInstance();
            if(nextMonth==false) {
                newDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), dayValue);
            }else {
                newDate.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, dayValue);
            }

            DayOfCalendar dayOfCalendar = new DayOfCalendar();
            dayOfCalendar.setDate(newDate);
            dayOfCalendar.setAvailable(available);

            if(setToday==false) {
                Date today = new Date();
                if (newDate.getTime().equals(today)) {
                    positionToday = startValue + i;
                    dayOfCalendar.setToday(true);
                    setToday = true;
                }
            }
            calendarList.add(dayOfCalendar);
            if(dayValue==lastDayOfMonth){
                dayValue = 1 ;
                nextMonth = true;
                available = false;
            }else {
                dayValue++;
            }
        }

        final CalendarAdapter adapter = new CalendarAdapter(calendarList,dialog.getContext(),selectedDate);
        adapter.setDayOfCalendars(calendarList);
        adapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectedDateGame = calendarList.get(position).getDate();
                adapter.setSelectedDate(selectedDateGame,position);
                animateLayout(R.anim.slide_up);
                gameDate = Utils.formatDate(selectedDateGame.getTime(), true);
                retrieveData();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void showCalendarDialog(final Calendar selectedDate) {
        calendarDialog = new Dialog(context);
        // it remove the dialog title
        calendarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        calendarDialog.setContentView(R.layout.calendar_dialog);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mainCalendarLayout = (LinearLayout) calendarDialog.findViewById(R.id.mainCalendarLayout);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(0, 0, 0, 0);
            mainCalendarLayout.setLayoutParams(params);
        }

        // set the background partial transparent
        calendarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        linearLayout = (LinearLayout) calendarDialog.findViewById(R.id.linear_calendar);
        Animation animation = AnimationUtils.loadAnimation(calendarDialog.getContext(),R.anim.slide_down);
        linearLayout.startAnimation(animation);
        final int[] currentMonth = {selectedDate.get(Calendar.MONTH)};
        initializeCalendar(currentMonth[0],calendarDialog,selectedDate);
        ImageView nextMonth = (ImageView) calendarDialog.findViewById(R.id.next_month);
        ImageView prevMonth = (ImageView) calendarDialog.findViewById(R.id.prev_month);
        TextView todayText = ((TextView) calendarDialog.findViewById(R.id.today_text));
        todayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDateGame = Calendar.getInstance();
                currentMonth[0] = Calendar.getInstance().get(Calendar.MONTH);
                initializeCalendar(currentMonth[0],calendarDialog,Calendar.getInstance());
                animateLayout(R.anim.slide_up_waiting);
                gameDate = Utils.formatDate(selectedDateGame.getTime(), true);
                retrieveData();
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth[0]++;
                initializeCalendar(currentMonth[0],calendarDialog,selectedDate);
            }
        });
        prevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth[0]--;
                initializeCalendar(currentMonth[0],calendarDialog,selectedDate);
            }
        });
        Window window = calendarDialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.TOP;
        param.width = ActionBar.LayoutParams.MATCH_PARENT;
        // it dismiss the dialog when click outside the dialog frame
        calendarDialog.setCanceledOnTouchOutside(true);
        // initialize the item of the dialog box, whose id is demo1
        View demodialog =(View) calendarDialog.findViewById(R.id.close);
        // it call when click on the item whose id is demo1.
        demodialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                animateLayout(R.anim.slide_up);
            }
        });
        calendarDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_item);
                ((ImageView) item.getActionView()).setImageResource(DayDrawableHelper.convertDayToDrawableResource(selectedDateGame.get(Calendar.DAY_OF_MONTH)));
                item.getActionView().startAnimation(animation);
            }
        });

        calendarDialog.show();
    }

    public void animateLayout(int animation){
        Animation animationDialog = AnimationUtils.loadAnimation(calendarDialog.getContext(),animation);
        animationDialog.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                calendarDialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearLayout.startAnimation(animationDialog);
    }

    public boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        today = gameDate.equals(Utils.formatDate(calendar.getTime(), true));
        return today;
    }

    private void popCache() {
        try {
            QueryBuilder<Cache, Long> queryBuilder = dao.queryBuilder();
            Where<Cache, Long> where = queryBuilder.where();
            long count = where.eq("guiType", "games").countOf();
            if (count >= Integer.valueOf(getString(R.string.COUNT_ELEMENT_CACHE))) {
                queryBuilder.reset();
                where.reset();
                where.eq("guiType", "games");
                Cache c = queryBuilder.queryForFirst();
                DeleteBuilder<Cache, ?> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq("id", c.getId());
                deleteBuilder.delete();
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }



}
