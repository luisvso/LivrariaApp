package com.example.livrariaapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class AddBookActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAuthor;
    private ChipGroup chipGroupStatus;
    private Chip chipLendo, chipLido, chipNlido;
    private RatingBar ratingBar;
    private Button buttonSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextTitle = findViewById(R.id.textNameBook);
        editTextAuthor = findViewById(R.id.textAuthor);
        chipGroupStatus = findViewById(R.id.chipGroup);
        chipLendo = findViewById(R.id.chipLendo);
        chipLido = findViewById(R.id.chipLido);
        chipNlido = findViewById(R.id.chipNlido);
        ratingBar = findViewById(R.id.ratingBar);
        buttonSave = findViewById(R.id.buttonUpdate);

        dbHelper = new DatabaseHelper(this);

        buttonSave.setOnClickListener(v -> saveNewBook());
    }

    private void saveNewBook() {
        String title = editTextTitle.getText().toString().trim();
        String authorName = editTextAuthor.getText().toString().trim();
        float rating = ratingBar.getRating();

        String status = "";
        int checkedChipId = chipGroupStatus.getCheckedChipId();
        if (checkedChipId == R.id.chipLendo) {
            status = "Lendo";
        } else if (checkedChipId == R.id.chipLido) {
            status = "Lido";
        } else if (checkedChipId == R.id.chipNlido) {
            status = "Não Lido";
        } else {
            Toast.makeText(this, R.string.messageSelectStatus, Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.isEmpty() || authorName.isEmpty()) {
            Toast.makeText(this, R.string.messageTitleAuthorEmpty, Toast.LENGTH_SHORT).show();
            return;
        }

        int authorId = getOrCreateAuthor(authorName);
        if (authorId == -1) {
            Toast.makeText(this, R.string.messageErrorProcessingAuthor, Toast.LENGTH_SHORT).show();
            return;
        }
        long newRowId = addBook(title, authorId, status, rating);
        if (newRowId != -1) {
            Toast.makeText(this, R.string.messageBookAdded, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, R.string.messageErrorAddingBook, Toast.LENGTH_SHORT).show();
        }
    }
    private long addBook(String title, int authorId, String status, float rating) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Id_author", authorId);
        values.put("Title", title);
        values.put("Status", dbHelper.convertStatusToInt(status));
        values.put("Rating", rating);
        long newRowId = db.insert("Book", null, values);
        db.close();
        return newRowId;
    }

    private int getOrCreateAuthor(String authorName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int authorId = -1;
        Cursor cursor = db.query("Author", new String[]{"Id_author"}, "Nome = ?",
                new String[]{authorName}, null, null, null);
        if (cursor.moveToFirst()) {
            authorId = cursor.getInt(0);
        } else {
            ContentValues values = new ContentValues();
            values.put("Nome", authorName);
            long newAuthorId = db.insert("Author", null, values);
            if (newAuthorId != -1) {
                authorId = (int) newAuthorId;
            }
        }
        cursor.close();
        db.close();

        return authorId;
    }
}