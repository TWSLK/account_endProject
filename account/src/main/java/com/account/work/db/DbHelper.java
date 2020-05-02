package com.account.work.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.account.work.model.Bill;
import com.account.work.model.Budget;
import com.account.work.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobObject;

import static com.account.work.db.BillDatabaseHelper.date;
import static com.account.work.db.BillDatabaseHelper.objectId;

/**
 * Database operation assistant to add, delete, find, update methods are written here
 * Later to add or delete to the database, you can directly call this class inside the method
 */

public class DbHelper {

    public DbHelper() {
    }

    /**
     * Query all
     */
    public ArrayList<Bill> queryAll() {
        LogUtils.d(this, "queryAll");
        SQLiteDatabase database = openDb();
        Cursor cursor = database.query(BillDatabaseHelper.bills, null, null, null, null, null,
                date + " desc");
        ArrayList<Bill> billList = cusorGetBillList(cursor);

        cursor.close();
        closeDb(database);

        return billList;
    }


    /**
     * Query the corresponding bills according to the date
     */
    public ArrayList<Bill> queryByDate(int date) {
        LogUtils.d(this, "queryByDate" + date);
        String sql = "SELECT * FROM bills WHERE date = ? ORDER BY addTime DESC";
        return queryBills(sql, new String[]{date + ""});
    }

    /**
     * Data in the range of query dates
     *
     * @param startDate Start date
     * @param endDate   End date
     */
    public ArrayList<Bill> queryByRange(int startDate, int endDate) {
        LogUtils.d(this, "queryByRange" + startDate + "-" + endDate);
        String sql = "SELECT * FROM bills WHERE date >= ? AND date <= ? ORDER BY addTime DESC";
        return queryBills(sql, new String[]{startDate + "", endDate + ""});
    }

    /**
     * Querying data greater than or equal to the specified date
     *
     * @param date
     * @return
     */
    public ArrayList<Bill> queryGreaterThan(int date) {
        LogUtils.d(this, "queryGreaterThan" + date);
        String sql = "SELECT * FROM bills WHERE date >= ? ORDER BY addTime DESC";
        return queryBills(sql, new String[]{date + ""});
    }

    /**
     * Query data less than the specified date
     *
     * @param date
     * @return
     */
    public ArrayList<Bill> queryLessThan(int date) {
        LogUtils.d(this, "queryLessThan" + date);
        String sql = "SELECT * FROM bills WHERE date <= ? ORDER BY addTime DESC";
        return queryBills(sql, new String[]{date + ""});
    }

    /**
     * Query all data that is not synchronized to the network
     */
    public ArrayList<Bill> queryAllNoSync() {
        LogUtils.d(this, "queryAllNoSync" + date);
        String sql = "SELECT * FROM bills WHERE objectId = ? ORDER BY addTime DESC";
        return queryBills(sql, new String[]{"0"});
    }

    /**
     * Custom SQL query
     */
    public ArrayList<Bill> queryBills(String sql, String[] args) {
        SQLiteDatabase database = openDb();
        Cursor cursor = database.rawQuery(sql, args);
        ArrayList<Bill> billList = cusorGetBillList(cursor);
        cursor.close();
        closeDb(database);
        return billList;
    }

    /**
     * Query budget
     */
    public Budget queryBudget(int yearMonth) {
        LogUtils.d(this, "queryBudget" + yearMonth);
        Budget budget = null;

        SQLiteDatabase database = openDb();
        Cursor cursor = database.query(BillDatabaseHelper.budget,
                null,
                BillDatabaseHelper.yearMonth + " = ?",
                new String[]{yearMonth + ""},
                null, null, null);

        while (cursor.moveToNext()) {
            float value = cursor.getInt(cursor.getColumnIndex(BillDatabaseHelper.value));
            budget = new Budget();
            budget.setValue(value);
            budget.setYearMonth(yearMonth);
        }
        cursor.close();
        closeDb(database);
        return budget;
    }

    /**
     * Add to
     */
    public void insert(Bill bill) {
        LogUtils.d(this, "insert");
        SQLiteDatabase database = openDb();

        ContentValues values = new ContentValues();
        values.put(BillDatabaseHelper.title, bill.getTitle());
        values.put(BillDatabaseHelper.addTime, bill.getAddTime());
        values.put(date, bill.getDate());
        values.put(BillDatabaseHelper.money, bill.getMoney());
        values.put(BillDatabaseHelper.mainType, bill.getMainType());
        values.put(BillDatabaseHelper.minorType, bill.getMinorType());
        values.put(BillDatabaseHelper.notes, bill.getNotes());
        values.put(BillDatabaseHelper.sync, bill.getSync());

        String objectId = bill.getObjectId();
        objectId = TextUtils.isEmpty(objectId) ? "0" : objectId;
        values.put(BillDatabaseHelper.objectId, objectId);

        database.insert(BillDatabaseHelper.bills, null, values);
        closeDb(database);
    }

    /**
     * Batch add to database
     */
    public void insertAll(List<? extends BmobObject> bills) {
        LogUtils.d(this, "insertAll");
        if (bills == null || bills.isEmpty()) {
            return;
        }
        for (BmobObject bill : bills) {
            insert((Bill) bill);
        }
    }

    /**
     * Update objectId
     */
    public void updateObjectId(Bill bill) {
        LogUtils.d(this, "updateObjectId");
        SQLiteDatabase database = openDb();
        ContentValues values = new ContentValues();
        values.put(objectId, bill.getObjectId());

        database.update(BillDatabaseHelper.bills, values, "addTime = ?", new String[]{bill.getAddTime() + ""});
        closeDb(database);
    }

    /**
     * Batch update object ID
     */
    public void updateAllId(List<? extends BmobObject> bills) {
        LogUtils.d(this, "updateAllId");
        if (bills == null || bills.isEmpty()) {
            return;
        }
        for (BmobObject bill : bills) {
            updateObjectId((Bill) bill);
        }
    }

    /**
     * Add budget
     */
    public void addBudget(Budget budget) {
        LogUtils.d(this, "addBudget");
        ContentValues values = new ContentValues();
        values.put(BillDatabaseHelper.value, budget.getValue());
        values.put(BillDatabaseHelper.yearMonth, budget.getYearMonth());

        SQLiteDatabase database = openDb();
        database.insert(BillDatabaseHelper.budget, null, values);
        closeDb(database);
    }

    /**
     * Delete
     */
    public void delete(Bill bill) {
        LogUtils.d(this, "delete");
        SQLiteDatabase database = openDb();
        database.delete(BillDatabaseHelper.bills, "addTime = ?",
                new String[]{bill.getAddTime() + ""});
        closeDb(database);
    }

    /**
     * To update
     */
    public void update(Bill bill) {
        LogUtils.d(this, "update");
        SQLiteDatabase database = openDb();
        ContentValues values = new ContentValues();
        values.put(BillDatabaseHelper.title, bill.getTitle());
        values.put(date, bill.getDate());
        values.put(BillDatabaseHelper.money, bill.getMoney());
        values.put(BillDatabaseHelper.mainType, bill.getMainType());
        values.put(BillDatabaseHelper.minorType, bill.getMinorType());
        values.put(BillDatabaseHelper.notes, bill.getNotes());
        values.put(BillDatabaseHelper.sync, bill.getSync());
        values.put(objectId, bill.getObjectId());

        database.update(BillDatabaseHelper.bills, values, "addTime = ?",
                new String[]{bill.getAddTime() + ""});

        closeDb(database);
    }

    /**
     * Update budget
     */
    public void updateBudget(Budget budget) {
        LogUtils.d(this, "updateBudget");
        SQLiteDatabase database = openDb();
        ContentValues values = new ContentValues();
        values.put(BillDatabaseHelper.value, budget.getValue());

        // the number of rows affected
        int number = database.update(BillDatabaseHelper.budget, values, BillDatabaseHelper.yearMonth + " = ?",
                new String[]{budget.getYearMonth() + ""});

        if (number == 0) {
            addBudget(budget);
        }

        closeDb(database);
    }

    /**
     * Open
     */
    private SQLiteDatabase openDb() {
        return new BillDatabaseHelper().getWritableDatabase();
    }

    /**
     * Close
     */
    private void closeDb(SQLiteDatabase database) {
        database.close();
    }

    /**
     * Get array according to cusor
     */
    private ArrayList<Bill> cusorGetBillList(Cursor cursor) {
        ArrayList<Bill> billList = new ArrayList<Bill>();
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(BillDatabaseHelper.title));
            String mainType = cursor.getString(cursor.getColumnIndex(BillDatabaseHelper.mainType));
            String minorType = cursor.getString(cursor.getColumnIndex(BillDatabaseHelper.minorType));
            String notes = cursor.getString(cursor.getColumnIndex(BillDatabaseHelper.notes));
            float money = cursor.getFloat(cursor.getColumnIndex(BillDatabaseHelper.money));
            int date = cursor.getInt(cursor.getColumnIndex(BillDatabaseHelper.date));
            long addTime = cursor.getLong(cursor.getColumnIndex(BillDatabaseHelper.addTime));
            int sync = cursor.getInt(cursor.getColumnIndex(BillDatabaseHelper.sync));
            String objectId = cursor.getString(cursor.getColumnIndex(BillDatabaseHelper.objectId));

            Bill bill = new Bill()
                    .setTitle(title)
                    .setDate(date)
                    .setAddTime(addTime)
                    .setMoney(money)
                    .setMainType(mainType)
                    .setMinorType(minorType)
                    .setNotes(notes)
                    .setSync(sync);

            if (!objectId.equals("0")) {
                bill.setObjectId(objectId);
            }

            billList.add(bill);

            System.out.println(bill.getTitle());
        }
        return billList;
    }


}
