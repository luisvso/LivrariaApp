package com.example.livrariaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textPublicationDate), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Button addBookButton = findViewById(R.id.button_add);
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivity(intent);
        });

        List<String> booklist = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT Title FROM Book", null);

        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(0);
                booklist.add(titulo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, booklist);
        listView.setAdapter(adapter);
    }
}