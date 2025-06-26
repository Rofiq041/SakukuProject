package com.example.sakuku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SakuKuDB";
    private static final int DATABASE_VERSION = 1;

    // Tabel Transaksi
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DESCRIPTION = "description";

    // Tabel Pengingat
    private static final String TABLE_REMINDERS = "reminders";
    private static final String COLUMN_REMINDER_ID = "reminder_id";
    private static final String COLUMN_REMINDER_NAME = "reminder_name";
    private static final String COLUMN_REMINDER_DATE = "reminder_date";
    private static final String COLUMN_STATUS = "status"; // Active/Inactive

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat Tabel Transaksi
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " REAL, " // Changed to REAL for numeric operations
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

        // Membuat Tabel Pengingat
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + " ("
                + COLUMN_REMINDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_REMINDER_NAME + " TEXT, "
                + COLUMN_REMINDER_DATE + " TEXT, "
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
    // Modified to accept double for amount
    public void addTransaction(String amountStr, String category, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            double amount = Double.parseDouble(amountStr);
            values.put(COLUMN_AMOUNT, amount);
        } catch (NumberFormatException e) {
            // Handle invalid amount input, perhaps log or show a toast
            return;
        }
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);

        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
    }

    // Menambahkan Pengingat Baru
    public void addReminder(String reminderName, String reminderDate, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REMINDER_NAME, reminderName);
        values.put(COLUMN_REMINDER_DATE, reminderDate);
        values.put(COLUMN_STATUS, status);

        db.insert(TABLE_REMINDERS, null, values);
        db.close();
    }

    // Mendapatkan Semua Transaksi
    public ArrayList<String> getAllTransactions() {
        ArrayList<String> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_TRANSACTIONS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String transaction = "ID: " + cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)) +
                        " Amount: " + cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)) +
                        " Category: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)) +
                        " Date: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)) +
                        " Description: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                transactions.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactions;
    }

    // Mendapatkan Semua Pengingat
    public ArrayList<String> getAllReminders() {
        ArrayList<String> reminders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String reminder = "Reminder: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_NAME)) +
                        " Date: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REMINDER_DATE)) +
                        " Status: " + cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS));
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reminders;
    }

    // New method to get total income (example)
    public double getTotalIncome() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;
        // Assuming "income" as a category or type of transaction.
        // You might need a "type" column in your transactions table if you want to distinguish income/expense clearly.
        // For now, let's assume all recorded transactions are expenses for simplicity, or we filter by category.
        // For a true income/expense, you would need a "type" column (e.g., "income", "expense").
        // Let's modify addTransaction to include a type.
        // For this example, if the category starts with "Income", consider it income. This is a weak approach, better to have a dedicated column.
        String selectQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_CATEGORY + " LIKE 'Income%'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalIncome;
    }

    // New method to get total expense (example)
    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalExpense = 0;
        String selectQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_CATEGORY + " NOT LIKE 'Income%'"; // simplistic
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return totalExpense;
    }

    // Method to calculate current balance
    public double getCurrentBalance() {
        return getTotalIncome() - getTotalExpense();
    }
}