package com.example.imagesqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "profileImageDB.db";
    private static final String TABLE_NAME = "profileImage";
    private static final String COLUMN_ID = "photoID";
    private static final String COLUMN_PHOTO_URI = "photoURI";
    private Context context;


    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PHOTO_URI + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //DB 생성

    public void saveImageToDatabase(Uri imageUri) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO_URI, imageUri.toString());
        long rowId = db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Uri loadImageFromDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        Uri imageUri = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{COLUMN_PHOTO_URI}, null, null, null, null, COLUMN_ID + " DESC", "1");
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(COLUMN_PHOTO_URI);
                String imageUriString = cursor.getString(columnIndex);
                imageUri = Uri.parse(imageUriString);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        return imageUri;
    }

}
