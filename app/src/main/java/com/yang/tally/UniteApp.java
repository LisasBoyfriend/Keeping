package com.yang.tally;

import android.app.Application;

import com.yang.tally.db.DBManager;
import com.yang.tally.db.TypeBean;

import java.util.ArrayList;
import java.util.List;

//表示全局应用的类
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DBManager.initDB(getApplicationContext());
    }


}
