package com.wrc.androidprocess.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * 用户数据表
 * Created by wrc on 2017/11/25/025.
 *
 */
@DatabaseTable(tableName = "tab_runningProcess")
public class RunningProcess  {
    /**
     * 检索ID
     */
    public static  final String id_Field = "id";
    /**
     * 进程名称
     */
    public static  final String process_Field = "processName";


    /**
     * 检索ID
     */
    @DatabaseField(useGetSet=true,generatedId=true,columnName=id_Field)
    private int id;


    /**
     * 进程名称
     */
    @DatabaseField(useGetSet=true, columnName = process_Field)
    private String processName;


    // 一个套餐可以对应多个主题
//    @ForeignCollectionField(eager = true) // 必须
//    public ForeignCollection<其他对象> themes;
    
    public RunningProcess(){}
    
    
    
    public int getId() {
        
        return id;
    }
    
    
    public void setId(int id) {
        
        this.id = id;
    }
    
    
    public String getProcessName() {
        
        return processName;
    }
    
    
    public void setProcessName(String processName) {
        
        this.processName = processName;
    }
    
    
}
