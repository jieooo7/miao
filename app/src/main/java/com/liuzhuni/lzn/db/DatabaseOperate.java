package com.liuzhuni.lzn.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Lee on 2015/5/30.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-30
 * Time: 17:40
 */
public class DatabaseOperate {

    public static void insert(SQLiteDatabase db, DbModel model) {
        delete(db);

        db.beginTransaction(); // 事务处理
        try {//insert or ignore into
            db.execSQL(
                    "INSERT INTO goods_record VALUES(null, ?,?,?,?)",
                    new Object[]{model.getType(), model.getBody_id(),model.getDate(),model.getBody()});
            db.setTransactionSuccessful(); // 成功
        } finally {
            db.endTransaction(); // 结束事务
        }

    }
    public static void update(SQLiteDatabase db, int bodyId) {

        db.beginTransaction(); // 事务处理
        try {//insert or ignore into
            db.execSQL("UPDATE goods_record SET body = 'false' WHERE body_id="+Integer.toString(bodyId)+" and type=4");
            db.setTransactionSuccessful(); // 成功
        } finally {
            db.endTransaction(); // 结束事务
        }

    }
    public static void updateFlag(SQLiteDatabase db, int bodyId) {

        db.beginTransaction(); // 事务处理
        try {//insert or ignore into
            db.execSQL("UPDATE goods_record SET date =101 WHERE body_id="+Integer.toString(bodyId)+" and type=3");
            db.setTransactionSuccessful(); // 成功
        } finally {
            db.endTransaction(); // 结束事务
        }

    }


    public static void delete(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT id FROM goods_record", null);
        int count = c.getCount();

        if (count > 1000) {
            int limit = count - 1000;
            Cursor cc = db.rawQuery("SELECT id FROM goods_record ORDER BY id ASC limit " + Integer.toString(limit), null);
            while (cc.moveToNext()) {
                db.execSQL("DELETE FROM goods_record WHERE id=" +Integer.toString(cc.getInt(cc.getColumnIndex("id"))));//子查询无法使用
//                String[] args = {String.valueOf(cc.getInt(cc.getColumnIndex("message_id")))};
//                db.delete("goods_record","message_id=?",args);
            }
            cc.close();
        }
        c.close();

    }

    public static int getCount(SQLiteDatabase db){

        Cursor c = db.rawQuery("SELECT id FROM goods_record", null);
        return c.getCount();
    }


    public static List<DbModel> getDb(SQLiteDatabase db,int id,int offset) { //返回 5条数据 由大到小排列
        Cursor c=null;

           c = db.rawQuery("SELECT * FROM goods_record ORDER BY id DESC limit "+String.valueOf(50)+" Offset "+String.valueOf(50*id+offset) , null);
        List<DbModel> list = new ArrayList<DbModel>();
        while (c.moveToNext()) {
            list.add(new DbModel(c.getInt(c.getColumnIndex("type")), c.getInt(c.getColumnIndex("body_id")),c.getLong(c.getColumnIndex("date")),c.getString(c.getColumnIndex("body"))));
        }

        c.close();

        return list;
    }




}
