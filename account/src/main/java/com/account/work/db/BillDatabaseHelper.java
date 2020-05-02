package com.account.work.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.account.work.app.Contants;
import com.account.work.app.app;

/**
 * Classes for database creation and updating
 * When you use this class - the first time - call the
 * getWritableDatabase () method, it triggers it to execute the onCreate () method
 *
 * We wrote the statement that creates the database in the onCreate method.
 * Db.execSQL () will perform the operation of building the database table
 *
 * When you call the getWritableDatabase () method later, the onCreate ()
 * method will not be executed because the table has been established for the first time
 */

public class BillDatabaseHelper extends SQLiteOpenHelper {

    public static final String bills = "bills";
    public static final String budget = "budget";

    public static final String title = "title";
    public static final String money = "money";
    public static final String mainType = "mainType";
    public static final String minorType = "minorType";
    public static final String date = "date";
    public static final String addTime = "addTime";
    public static final String notes = "notes";
    // synchronization
    public static final String sync = "sync";

    public static final String value = "value";
    public static final String yearMonth = "yearMonth";
    public static final String objectId = "objectId";

    /**
     * Bill build statement
     */
    private String CREATE_ENTRY_TABLE = "CREATE TABLE " + bills + " (" +
            "_id integer primary key autoincrement, " +
            objectId + " text, " +
            title + " text, " +
            money + " real, " +
            mainType + " text, " +
            minorType + " text, " +
            date + " integer, " +
            addTime + " integer, " +
            sync + " integer, " +
            notes + " text)";

    /**
     * Statement of budget statement
     */
    private String CREATE_BUDGET_TABLE = "CREATE TABLE " + budget + " (" +
            "_id integer primary key autoincrement, " +
            value + " real," +
            yearMonth + " integer)";


    public BillDatabaseHelper() {
        super(app.context, Contants.NAME_DB_ENTRY, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // reate bill
        db.execSQL(CREATE_ENTRY_TABLE);
        // The budget sheet may be budgeted differently each month and needs to be recorded
        db.execSQL(CREATE_BUDGET_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
