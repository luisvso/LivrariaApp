package com.example.livrariaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        BookAdapter.OnDeleteClickListener,
        BookAdapter.OnEditClickListener {

    private ListView listView;
    private BookAdapter adapter;
    private List<Book> bookList;
    private DatabaseHelper databaseHelper;

    private static final int ADD_BOOK_REQUEST = 1;
    private static final int EDIT_BOOK_REQUEST = 2;


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

        listView = findViewById(R.id.listView);
        bookList = new ArrayList<>();
        adapter = new BookAdapter(this, bookList);
        listView.setAdapter(adapter);

        adapter.setOnDeleteClickListener(this);
        adapter.setOnEditClickListener(this);

        databaseHelper = new DatabaseHelper(this);

        Button addBookButton = findViewById(R.id.button_add);
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivityForResult(intent, ADD_BOOK_REQUEST);
        });

        loadBooksFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooksFromDatabase();
    }

    private void loadBooksFromDatabase() {
        bookList.clear();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT b.Id_book, b.Title, a.Nome, b.Status, b.Rating " +
                "FROM Book b INNER JOIN Author a ON b.Id_author = a.Id_author";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String author = cursor.getString(2);
                int statusInt = cursor.getInt(3);
                String statusString = convertStatusIntToString(statusInt);
                float rating = cursor.getFloat(4);

                bookList.add(new Book(id, title, author, statusString, rating));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        adapter.notifyDataSetChanged();
    }

    private String convertStatusIntToString(int statusInt) {
        String[] statusOptions = getResources().getStringArray(R.array.book_status_options);
        if (statusInt >= 0 && statusInt < statusOptions.length) {
            return statusOptions[statusInt];
        }
        return "Desconhecido";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == ADD_BOOK_REQUEST || requestCode == EDIT_BOOK_REQUEST) && resultCode == RESULT_OK){
            loadBooksFromDatabase();
        }
    }

    @Override
    public void onDeleteClick(Book bookToDelete) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir o livro '" + bookToDelete.getTitle() + "'?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    int rowsAffected = databaseHelper.deleteBook(bookToDelete.getId());
                    if (rowsAffected > 0) {
                        Toast.makeText(MainActivity.this, "Livro excluído com sucesso!", Toast.LENGTH_SHORT).show();
                        loadBooksFromDatabase();
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao excluir livro.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onEditClick(Book bookToEdit) {
        Intent intent = new Intent(MainActivity.this, EditBookActivity.class);
        intent.putExtra("book_to_edit", bookToEdit);
        startActivityForResult(intent, EDIT_BOOK_REQUEST);
    }
}