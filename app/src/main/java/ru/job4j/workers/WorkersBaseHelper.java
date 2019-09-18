package ru.job4j.workers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkersBaseHelper extends SQLiteOpenHelper {
    public static final String DB = "workers.db";
    public static final int VERSION = 7;

    public WorkersBaseHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbSchema.SpecialitiesTable.CREATE_TABLE);
        db.execSQL(DbSchema.WorkersTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.SpecialitiesTable.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbSchema.WorkersTable.NAME);
        onCreate(db);
    }
}