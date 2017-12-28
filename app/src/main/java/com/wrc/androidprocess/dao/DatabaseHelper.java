package com.wrc.androidprocess.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wrc.androidprocess.bean.RunningProcess;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "runningprocess.db";//默认是在data/data/包名/databases/路径下
    public static final String DATABASE_PATH = "/data/data/com.wrc.androidprocess/databases/"+TABLE_NAME;
    //Environment.getExternalStorageDirectory() + "/com.wrc.androidprocess/" + TABLE_NAME;//指定路径
    private static final int DB_VERSION = 1;

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DatabaseHelper(Context context) {
        super(context, DATABASE_PATH, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, RunningProcess.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {
//            TableUtils.dropTable(connectionSource, RunningProcess.class, true);
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (newVersion == 3) {
//            DatabaseUtil.updateColumn(database, "tab_child", "s_phone", "VARCHAR", null);
//        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
