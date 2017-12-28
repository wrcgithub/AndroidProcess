package com.wrc.androidprocess.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.wrc.androidprocess.bean.RunningProcess;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 定义数据访问对象，对指定的用户数据表进行增删改查操作
 * Created by wrc on 2017/11/25/025.
 */
public class RunningProcessDao {

    private Dao<RunningProcess, Integer> processDao;
    private DatabaseHelper dbHelper;

    /**
     * 构造方法
     * 获得数据库帮助类实例，通过传入Class对象得到相应的Dao
     * @param context
     */
    public RunningProcessDao(Context context) {
        try {
            dbHelper = DatabaseHelper.getHelper(context);
            processDao = dbHelper.getDao(RunningProcess.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一条记录
     * @param theme
     */
    public void add(RunningProcess theme) {
        try {
            processDao.create(theme);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     * @param theme
     */
    public void delete(RunningProcess theme) {
        try {
            processDao.delete(theme);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 删除集合中的所有记录
     * @param list
     */
    public void deleteList(List<RunningProcess> list) {
        try {
            processDao.delete(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新一条记录
     * @param theme
     */
    public void update(RunningProcess theme) {
        try {
            processDao.update(theme);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询一条记录
     * @param id
     * @return
     */
    public RunningProcess queryForId(int id) {
        RunningProcess theme = null;
        try {
            theme = processDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theme;
    }
    /**
     * 查询一条记录  根据包名
     * @param packageName
     * @return
     */
    public List<RunningProcess> queryForPackage(String packageName) {
        List<RunningProcess> theme = null;
        try {
            theme = processDao.queryBuilder().where().eq(RunningProcess.process_Field, packageName).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theme;
    }
    /**
     * 查询所有记录
     * @return
     */
    public List<RunningProcess> queryForAll() {
        List<RunningProcess> themes = new ArrayList<RunningProcess>();
        try {
            themes = processDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return themes;
    }


}
