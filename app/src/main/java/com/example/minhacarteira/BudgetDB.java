package com.example.minhacarteira;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class BudgetDB {
    private Context context;
    private SQLiteDatabase db;

    public BudgetDB(Context _context) {
        context = _context.getApplicationContext();
        db = new DBHelper(context).getWritableDatabase();
    }

    private static ContentValues getContentValues(Budget budget) {
        ContentValues values = new ContentValues();
        values.put(DBSchema.BudgetTbl.Cols.uuid, budget.getUUID().toString());
        values.put(DBSchema.BudgetTbl.Cols.description, budget.getDescription());
        values.put(DBSchema.BudgetTbl.Cols.date, budget.getData().toString());
        values.put(DBSchema.BudgetTbl.Cols.value, budget.getValue().toString());
        return values;
    }

    public void insertBudget(Budget budget) {
        ContentValues values = getContentValues(budget);
        db.insert(DBSchema.BudgetTbl.NAME, null, values);
    }

    public ArrayList<Budget> selectSpending(String where, String[] args) {
        ArrayList<Budget> spending = new ArrayList<>();
        Cursor cursor = db.query(DBSchema.BudgetTbl.NAME, null, where, args, null, null,  null);

        if(cursor != null) {
            try {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.uuid)));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.description));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.date));
                    String value = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.value));
                    Budget budget = new Budget(uuid, date, value, description);
                    spending.add(budget);
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
        }

        return spending;
    }

    public ArrayList<Budget> selectSpending() {
        ArrayList<Budget> spending = new ArrayList<>();
        Cursor cursor = db.query(DBSchema.BudgetTbl.NAME, null, null, null, null, null,  null);

        if(cursor != null) {
            try {
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.uuid)));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.description));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.date));
                    String value = cursor.getString(cursor.getColumnIndexOrThrow(DBSchema.BudgetTbl.Cols.value));
                    Budget budget = new Budget(uuid, date, value, description);
                    spending.add(budget);
                    cursor.moveToNext();
                }
            } catch (Exception e) {
                Log.d("ERROR", e.getLocalizedMessage());
            } finally {
                cursor.close();
            }
        }

        return spending;
    }

    public void close() {
        db.close();
    }
}

