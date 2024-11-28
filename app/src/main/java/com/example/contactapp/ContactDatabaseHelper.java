package com.example.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

public class ContactDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHOTO = "photo";  // Add photo URI column

    public ContactDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SURNAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHOTO + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_SURNAME, contact.getSurname());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_PHOTO, contact.getPhotoUri() != null ? contact.getPhotoUri().toString() : null);
        return db.insert(TABLE_CONTACTS, null, values);
    }

    public int updateContact(int id, Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_SURNAME, contact.getSurname());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_EMAIL, contact.getEmail());
        values.put(COLUMN_PHOTO, contact.getPhotoUri() != null ? contact.getPhotoUri().toString() : null);
        return db.update(TABLE_CONTACTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
                String photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)); // Retrieve photo URI
                Uri photoUri = photo != null ? Uri.parse(photo) : null;
                contactList.add(new Contact(id, name, surname, phone, email, photoUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }
}
