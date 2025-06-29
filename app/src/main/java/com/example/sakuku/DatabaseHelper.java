package com.example.sakuku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SakuKuDB";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Tabel Transaksi
    private static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TYPE = "type"; // 'income' or 'expense'
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DESCRIPTION = "description";

    // Tabel Pengingat
    private static final String TABLE_REMINDERS = "reminders";
    private static final String COLUMN_REMINDER_ID = "reminder_id";
    private static final String COLUMN_REMINDER_NAME = "reminder_name";
    private static final String COLUMN_REMINDER_DATE = "reminder_date";
    public static final String COLUMN_REMINDER_TIME = "reminder_time";
    public static final String COLUMN_REMINDER_AMOUNT = "reminder_amount";
    public static final String COLUMN_STATUS = "status"; // Active/Inactive

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat Tabel Transaksi
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Membuat Tabel Pengingat
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + " ("
                + COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_REMINDER_NAME + " TEXT, "
                + COLUMN_REMINDER_DATE + " TEXT, "
                + COLUMN_REMINDER_TIME + " TEXT, "
                + COLUMN_REMINDER_AMOUNT + " REAL, "
                + COLUMN_STATUS + " TEXT)";
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Menghapus tabel jika ada dan membuat yang baru
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        onCreate(db);
    }

    // Menambahkan Transaksi Baru
    public long addTransaction(String type, double amount, String category, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return result;
    }

    // Menambahkan Pengingat Baru
    public void addReminder(String reminderName, String reminderDate, String reminderTime, double amount, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REMINDER_NAME, reminderName);
        values.put(COLUMN_REMINDER_DATE, reminderDate);
        values.put(COLUMN_REMINDER_TIME, reminderTime);
        values.put(COLUMN_REMINDER_AMOUNT, amount);
        values.put(COLUMN_STATUS, status);

        db.insert(TABLE_REMINDERS, null, values);
        db.close();
    }

    // Mendapatkan Semua Transaksi
    public Cursor getAllTransactions() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_DATE + " DESC";
        return db.rawQuery(selectQuery, null);
    }

    // Mendapatkan Semua Pengingat
    public List<Reminder> getAllReminders() {
        List<Reminder> reminderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_TIME));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_AMOUNT));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                reminderList.add(new Reminder(id, name, date, time, status, amount));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reminderList;
    }

    // New method to get total income (example)
    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;
        String selectQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'income'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalIncome;
    }

    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpense = 0;
        String selectQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalExpense;
    }

    public Cursor getTransactionsByTypeAndMonth(String type, String monthYear) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TYPE + " = ? AND " + COLUMN_DATE + " LIKE ?";
        String[] selectionArgs = {type, "%" + monthYear};
        return db.query(TABLE_TRANSACTIONS, null, selection, selectionArgs, null, null, COLUMN_DATE + " ASC");
    }

    // Method to calculate current balance
    public double getCurrentBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public Cursor searchTransactions(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DESCRIPTION + " LIKE ? OR " + COLUMN_CATEGORY + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%"};
        return db.query(TABLE_TRANSACTIONS, null, selection, selectionArgs, null, null, COLUMN_DATE + " DESC");
    }

    public Cursor getTransactionsByType(String type) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TYPE + " = ?";
        String[] selectionArgs = {type};
        return db.query(TABLE_TRANSACTIONS, null, selection, selectionArgs, null, null, COLUMN_DATE + " DESC");
    }

    // New method to get total expense by category and filter
    public double getTotalExpenseByCategory(String category, String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COLUMN_TYPE + " = 'expense' AND " + COLUMN_CATEGORY + " = ?";

        String dateSelection = "";
        String[] selectionArgs;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        if (filter.equals("monthly")) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String firstDayOfMonth = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String lastDayOfMonth = dateFormat.format(calendar.getTime());
            dateSelection = " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            query += dateSelection;
            selectionArgs = new String[]{category, firstDayOfMonth, lastDayOfMonth};
        } else if (filter.equals("weekly")) {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            String firstDayOfWeek = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            String lastDayOfWeek = dateFormat.format(calendar.getTime());
            dateSelection = " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            query += dateSelection;
            selectionArgs = new String[]{category, firstDayOfWeek, lastDayOfWeek};
        } else if (filter.equals("yearly")) {
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            String firstDayOfYear = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String lastDayOfYear = dateFormat.format(calendar.getTime());
            dateSelection = " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            query += dateSelection;
            selectionArgs = new String[]{category, firstDayOfYear, lastDayOfYear};
        } else {
            selectionArgs = new String[]{category};
        }

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    // New method to get total expense by filter
    public double getTotalExpense(String filter) {
        SQLiteDatabase db = this.getReadableDatabase();
        double total = 0;
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS +
                " WHERE " + COLUMN_TYPE + " = 'expense'";

        String dateSelection = "";
        String[] selectionArgs = null;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        if (filter.equals("monthly")) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String firstDayOfMonth = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String lastDayOfMonth = dateFormat.format(calendar.getTime());
            dateSelection = " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            query += dateSelection;
            selectionArgs = new String[]{firstDayOfMonth, lastDayOfMonth};
        } else if (filter.equals("weekly")) {
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
            String firstDayOfWeek = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_WEEK, 6);
            String lastDayOfWeek = dateFormat.format(calendar.getTime());
            dateSelection = " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            query += dateSelection;
            selectionArgs = new String[]{firstDayOfWeek, lastDayOfWeek};
        } else if (filter.equals("yearly")) {
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            String firstDayOfYear = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            String lastDayOfYear = dateFormat.format(calendar.getTime());
            dateSelection = " AND " + COLUMN_DATE + " BETWEEN ? AND ?";
            query += dateSelection;
            selectionArgs = new String[]{firstDayOfYear, lastDayOfYear};
        }

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    // New methods for StatisticsActivity
    public Map<String, Double> getMonthlyIncome() {
        Map<String, Double> monthlyIncome = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT STRFTIME('%m/%Y', " + COLUMN_DATE + "), SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'income' GROUP BY STRFTIME('%m/%Y', " + COLUMN_DATE + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                monthlyIncome.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return monthlyIncome;
    }

    public Map<String, Double> getMonthlyExpense() {
        Map<String, Double> monthlyExpense = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT STRFTIME('%m/%Y', " + COLUMN_DATE + "), SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense' GROUP BY STRFTIME('%m/%Y', " + COLUMN_DATE + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                monthlyExpense.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return monthlyExpense;
    }

    public Map<String, Double> getWeeklyIncome() {
        Map<String, Double> weeklyIncome = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT STRFTIME('%W/%Y', " + COLUMN_DATE + "), SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'income' GROUP BY STRFTIME('%W/%Y', " + COLUMN_DATE + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                weeklyIncome.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return weeklyIncome;
    }

    public Map<String, Double> getWeeklyExpense() {
        Map<String, Double> weeklyExpense = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT STRFTIME('%W/%Y', " + COLUMN_DATE + "), SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense' GROUP BY STRFTIME('%W/%Y', " + COLUMN_DATE + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                weeklyExpense.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return weeklyExpense;
    }

    public Map<String, Double> getYearlyIncome() {
        Map<String, Double> yearlyIncome = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT STRFTIME('%Y', " + COLUMN_DATE + "), SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'income' GROUP BY STRFTIME('%Y', " + COLUMN_DATE + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                yearlyIncome.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return yearlyIncome;
    }

    public Map<String, Double> getYearlyExpense() {
        Map<String, Double> yearlyExpense = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT STRFTIME('%Y', " + COLUMN_DATE + "), SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense' GROUP BY STRFTIME('%Y', " + COLUMN_DATE + ")";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                yearlyExpense.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return yearlyExpense;
    }

    public Map<String, Double> getMonthlyExpenseByCategory() {
        Map<String, Double> monthlyExpenseByCategory = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense' GROUP BY " + COLUMN_CATEGORY;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                monthlyExpenseByCategory.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return monthlyExpenseByCategory;
    }

    public Map<String, Double> getWeeklyExpenseByCategory() {
        Map<String, Double> weeklyExpenseByCategory = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense' GROUP BY " + COLUMN_CATEGORY;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                weeklyExpenseByCategory.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return weeklyExpenseByCategory;
    }

    public Map<String, Double> getYearlyExpenseByCategory() {
        Map<String, Double> yearlyExpenseByCategory = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 'expense' GROUP BY " + COLUMN_CATEGORY;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                yearlyExpenseByCategory.put(cursor.getString(0), cursor.getDouble(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return yearlyExpenseByCategory;
    }
}