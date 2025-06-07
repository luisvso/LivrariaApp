package com.example.livrariaapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableAuthor = "CREATE TABLE Author (" +
                "Id_author INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Nome TEXT NOT NULL);";


        String createTableBook = "CREATE TABLE Book (" +
                "Id_book INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Id_author INTEGER," +
                "Title TEXT NOT NULL," +
                "Status INTEGER," +
                "Rating REAL," +
                "FOREIGN KEY(Id_author) REFERENCES Author(Id_author));";

        db.execSQL(createTableAuthor);
        db.execSQL(createTableBook);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Author");
        db.execSQL("DROP TABLE IF EXISTS Book");
        onCreate(db);
    }

    public int deleteBook(int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Book", "Id_book=?", new String[]{String.valueOf(bookId)});
        db.close();
        return rowsDeleted;
    }
}
