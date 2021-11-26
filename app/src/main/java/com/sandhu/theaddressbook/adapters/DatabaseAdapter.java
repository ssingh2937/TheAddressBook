package com.sandhu.theaddressbook.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import androidx.annotation.Nullable;

import com.sandhu.theaddressbook.Models.ContactListModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    DatabaseHelper databaseHelper;
    List<ContactListModel> contactArrayList;

    public DatabaseAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
        contactArrayList = new ArrayList<>();
    }

    //Add new contact
    public long insertData(String firstName, String lastName, String phone, String email, byte[] bArray) {
        SQLiteDatabase dbb = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseHelper.FIRST_NAME, firstName);
        contentValues.put(databaseHelper.LAST_NAME, lastName);
        contentValues.put(databaseHelper.PHONE, phone);
        contentValues.put(databaseHelper.EMAIL, email);
        contentValues.put(databaseHelper.IMAGE, bArray);
        long id = dbb.insert(databaseHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    //Delete contact
    public int delete(int ID)
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] whereArgs ={String.valueOf(ID)};

        int count =db.delete(databaseHelper.TABLE_NAME ,databaseHelper.UID+" = ?",whereArgs);
        return  count;
    }

    //Get contact data
    public ArrayList<ContactListModel> getData()
    {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String[] columns = {databaseHelper.UID,databaseHelper.FIRST_NAME,databaseHelper.LAST_NAME,databaseHelper.PHONE,databaseHelper.EMAIL,databaseHelper.IMAGE};
        Cursor cursor =db.query(databaseHelper.TABLE_NAME,columns,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(databaseHelper.UID));
            String first_name =cursor.getString(cursor.getColumnIndex(databaseHelper.FIRST_NAME));
            String last_name =cursor.getString(cursor.getColumnIndex(databaseHelper.LAST_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(databaseHelper.PHONE));
            String email = cursor.getString(cursor.getColumnIndex(databaseHelper.EMAIL));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(databaseHelper.IMAGE));

            ContactListModel cm = new ContactListModel(cid,first_name, last_name, phone, email, image);
            contactArrayList.add(cm);

            //buffer.append(cid+ "   " + first_name + "   " + last_name +" \n");
        }
        return (ArrayList<ContactListModel>) contactArrayList;
    }

    public ArrayList<ContactListModel> searchData(String searchString){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * from " +databaseHelper.TABLE_NAME+ " where " +databaseHelper.PHONE+ " LIKE ?" , new String[] { "%" + searchString + "%" });

        while (cursor.moveToNext()){
            int cid =cursor.getInt(cursor.getColumnIndex(databaseHelper.UID));
            String first_name =cursor.getString(cursor.getColumnIndex(databaseHelper.FIRST_NAME));
            String last_name =cursor.getString(cursor.getColumnIndex(databaseHelper.LAST_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(databaseHelper.PHONE));
            String email = cursor.getString(cursor.getColumnIndex(databaseHelper.EMAIL));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(databaseHelper.IMAGE));

            ContactListModel cm = new ContactListModel(cid,first_name, last_name, phone, email, image);
            contactArrayList.add(cm);
        }
        return (ArrayList<ContactListModel>) contactArrayList;
    }

    class DatabaseHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "addressBook";    // Database Name
        private static final String TABLE_NAME = "contacts";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";// Column I (Primary Key)
        private static final String FIRST_NAME = "FirstName";
        private static final String LAST_NAME = "LastName";
        private static final String PHONE= "Phone";
        private static final String EMAIL= "Email";
        private static final String IMAGE= "Image";

        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FIRST_NAME+" VARCHAR(255), "+LAST_NAME+" VARCHAR(225) ,"+PHONE+" VARCHAR(225),"+ EMAIL+" VARCHAR(225),"+IMAGE+" BLOB not null);";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;



        public DatabaseHelper(@Nullable Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(context + "",  e.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.e("MainActivity", "onUpgrade: ");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                Log.e("onUpgrade exception: ", e.getMessage());
            }
        }
    }


}
