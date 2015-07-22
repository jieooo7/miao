package com.liuzhuni.lzn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private final static int DBVERSION=1;

    public DataBaseHelper(Context context, String name) {
        super(context, name,null,DBVERSION);
    }
//	public DataBaseHelper(Context context, String name) {
//		super(context, DBNAME, null,DBVERSION);
//	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS goods_record (id INTEGER PRIMARY KEY AUTOINCREMENT,type INTEGER,body_id INTEGER,date INTEGER,body TEXT)");
//		db.execSQL("CREATE UNIQUE INDEX unique_index_id ON goods_record (id)");//建立索引
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("ALTER TABLE test ADD COLUMN other STRING");

	}



}
