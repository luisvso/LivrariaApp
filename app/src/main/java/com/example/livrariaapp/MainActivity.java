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

    private ListView listView;
    private BookAdapter adapter;
    private List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        loadBooksFromDatabase();

        Button addBookButton = findViewById(R.id.button_add);
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void loadBooksFromDatabase() {
        bookList = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Buscando os dados completos do livro
        String query = "SELECT b.Title, a.Nome, b.Status, b.Rating " +
                "FROM Book b INNER JOIN Author a ON b.Id_author = a.Id_author";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(0);
                String author = cursor.getString(1);
                String status = cursor.getString(2);
                float rating = cursor.getFloat(3);

                bookList.add(new Book(title, author, status, rating));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        adapter = new BookAdapter(this, bookList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            loadBooksFromDatabase();
            adapter.notifyDataSetChanged();
        }
    }
}
