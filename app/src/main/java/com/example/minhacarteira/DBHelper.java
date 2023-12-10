package com.example.minhacarteira;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE = "db";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DBSchema.BudgetTbl.NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBSchema.BudgetTbl.Cols.uuid + " TEXT," +
                DBSchema.BudgetTbl.Cols.description + " TEXT," +
                DBSchema.BudgetTbl.Cols.date + " TEXT," +
                DBSchema.BudgetTbl.Cols.value + " REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBSchema.BudgetTbl.NAME);
        onCreate(db);
    }
}
