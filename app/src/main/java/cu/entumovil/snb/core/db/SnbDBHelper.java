package cu.entumovil.snb.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cu.entumovil.snb.SNBApp;
import cu.entumovil.snb.core.db.models.Cache;
import cu.entumovil.snb.core.db.models.Favorite;
import cu.entumovil.snb.core.db.models.News;

public class SnbDBHelper extends OrmLiteSqliteOpenHelper {

    private static String TAG = SNBApp.APP_TAG + SnbDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "snb.db";

    private static final int DATABASE_VERSION = 1;

    private Dao<Cache, Long> cachesDao;

    private Dao<Favorite, Long> favoritesDao;

    private Dao<News, Long> newsDao;

    public SnbDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Cache.class);
            TableUtils.createTable(connectionSource, Favorite.class);
            TableUtils.createTable(connectionSource, News.class);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Cache.class, false);
            TableUtils.dropTable(connectionSource, Favorite.class, false);
            TableUtils.dropTable(connectionSource, News.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public Dao<Cache, Long> getCachesDao() throws SQLException {
        if (cachesDao == null) {
            cachesDao = getDao(Cache.class);
        }
        return cachesDao;
    }

    public Dao<Favorite, Long> getFavoritesDao() throws SQLException {
        if (favoritesDao == null) {
            favoritesDao = getDao(Favorite.class);
        }
        return favoritesDao;
    }

    public Dao<News, Long> getNewsDao() throws SQLException {
        if (newsDao == null) {
            newsDao = getDao(News.class);
        }
        return newsDao;
    }

    @Override
    public void close() {
        super.close();
        cachesDao = null;
        favoritesDao = null;
        newsDao = null;
    }

}
