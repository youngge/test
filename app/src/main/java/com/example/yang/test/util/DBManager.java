package com.example.yang.test.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yang.test.bean.ConversationBean;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Administrator on 2017/1/18.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * add ConversationBean
     */
    public void add(List<ConversationBean> list) {
        db.beginTransaction();  //开始事务
        try {
            for (ConversationBean bean : list) {
                db.execSQL("INSERT INTO conversation(type,content) VALUES( ?, ?)", new Object[]{bean.getType(), bean.getContent()});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * add ConversationBean
     */
    public void add(ConversationBean bean) {
        db.beginTransaction();  //开始事务
        try {
                db.execSQL("INSERT INTO conversation(type,content) VALUES( ?, ?)", new Object[]{bean.getType(), bean.getContent()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    public List<ConversationBean> query() {
        ArrayList<ConversationBean> list = new ArrayList<>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            ConversationBean bean = new ConversationBean();
            bean.setType(c.getInt(c.getColumnIndex("type")));
            bean.setContent(c.getString(c.getColumnIndex("content")));
            list.add(bean);
        }
        c.close();
        return list;
    }

    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM conversation", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
