package com.example.employee.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "employee_mgt";
    public static final String TABLE_NAME = "list_table";
    private static final String C_ID = "id";
    private static final String C_FNAME = "fName";
    private static final String C_LNAME = "lName";
    private static final String C_NUMBER = "Number";
    private static final String C_STATUS = "Status";
    private static final String C_DATE = "date";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_FNAME + " TEXT,"
            + C_LNAME + " TEXT,"
            + C_NUMBER + " INTEGER,"
            + C_STATUS + " TEXT,"
            + C_DATE + " TEXT"
            + ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int addEmployee(String name, String lname, int eno, String estatus, String edate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_FNAME, name);
        cv.put(C_LNAME, lname);
        cv.put(C_NUMBER, eno);
        cv.put(C_STATUS, estatus);
        cv.put(C_DATE, edate);
        int id = (int) db.insert(TABLE_NAME, null, cv);
        db.close();
        return id;
    }

    public boolean updateEmployee(String id, String name, String lname, int eno, String estatus, String edate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_FNAME, name);
        cv.put(C_LNAME, lname);
        cv.put(C_NUMBER, eno);
        cv.put(C_STATUS, estatus);
        cv.put(C_DATE, edate);
        db.update(TABLE_NAME, cv, C_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return true;
    }

    public ArrayList<Employee> getEmployee() {
        ArrayList<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Employee emp = new Employee();
                emp.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
                emp.setfName(cursor.getString(cursor.getColumnIndex(C_FNAME)));
                emp.setlName(cursor.getString(cursor.getColumnIndex(C_LNAME)));
                emp.setNumber(cursor.getInt(cursor.getColumnIndex(C_NUMBER)));
                emp.setStatus(cursor.getString(cursor.getColumnIndex(C_STATUS)));
                emp.setDate(cursor.getString(cursor.getColumnIndex(C_DATE)));

                list.add(emp);

            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        Log.e(">db:", "got all invoice");
        return list;
    }

    public void deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, C_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteAll() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + TABLE_NAME);
        database.close();
    }

    /*query for sorting by name*/
    public ArrayList<Employee> getAllEmpStatusASC(String order) {

        ArrayList<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,null,
                null,
                null,
                null,
                null,
                C_STATUS + " "+order);

        if (cursor.moveToFirst()) {
            do {
                Employee emp = new Employee();
                emp.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
                emp.setfName(cursor.getString(cursor.getColumnIndex(C_FNAME)));
                emp.setlName(cursor.getString(cursor.getColumnIndex(C_LNAME)));
                emp.setNumber(cursor.getInt(cursor.getColumnIndex(C_NUMBER)));
                emp.setStatus(cursor.getString(cursor.getColumnIndex(C_STATUS)));
                emp.setDate(cursor.getString(cursor.getColumnIndex(C_DATE)));

                list.add(emp);

            } while (cursor.moveToNext());

        }

        cursor.close();
        db.close();
        Log.e(">db:", "sorting through status done");
        return list;
    }

    /*query for sorting by number*/
    public ArrayList<Employee> getAllEmpNumberASC(String order) {

        ArrayList<Employee> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,null,
                null,
                null,
                null,
                null,
                C_NUMBER + " "+order);

        if (cursor.moveToFirst()) {
            do {
                Employee emp = new Employee();
                emp.setId(cursor.getInt(cursor.getColumnIndex(C_ID)));
                emp.setfName(cursor.getString(cursor.getColumnIndex(C_FNAME)));
                emp.setlName(cursor.getString(cursor.getColumnIndex(C_LNAME)));
                emp.setNumber(cursor.getInt(cursor.getColumnIndex(C_NUMBER)));
                emp.setStatus(cursor.getString(cursor.getColumnIndex(C_STATUS)));
                emp.setDate(cursor.getString(cursor.getColumnIndex(C_DATE)));

                list.add(emp);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.e(">db:", "sorting through status done");
        return list;
    }
}
