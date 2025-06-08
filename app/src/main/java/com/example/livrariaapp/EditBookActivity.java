package com.example.livrariaapp;

import android.content.ContentValues;
import android.content.Intent;
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

public class EditBookActivity extends AppCompatActivity {

    private EditText editTextTitle, editTextAuthor;
    private ChipGroup chipGroupStatus;
    private Chip chipLendo, chipLido, chipNlido;
    private RatingBar ratingBar;
    private Button buttonUpdate;
    private DatabaseHelper dbHelper;

    private Book bookToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTextTitle = findViewById(R.id.textNameBook);
        editTextAuthor = findViewById(R.id.textAuthor);
        chipGroupStatus = findViewById(R.id.chipGroup);
        chipLendo = findViewById(R.id.chipLendo);
        chipLido = findViewById(R.id.chipLido);
        chipNlido = findViewById(R.id.chipNlido);
        ratingBar = findViewById(R.id.ratingBar);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("book_to_edit")) {
            bookToEdit = (Book) intent.getSerializableExtra("book_to_edit");
            if (bookToEdit != null) {
                editTextTitle.setText(bookToEdit.getTitle());
                editTextAuthor.setText(bookToEdit.getAuthor());
                ratingBar.setRating(bookToEdit.getRating());

                String currentStatus = bookToEdit.getStatus().toLowerCase().trim();
                if (currentStatus.equals("lendo")) {
                    chipLendo.setChecked(true);
                } else if (currentStatus.equals("lido")) {
                    chipLido.setChecked(true);
                } else if (currentStatus.equals("não lido")) {
                    chipNlido.setChecked(true);
                }


            } else {
                Toast.makeText(this, R.string.messageBookNotFound, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.messageNoBookSpecified, Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonUpdate.setOnClickListener(v -> updateBook());
    }

    private void updateBook() {
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

        bookToEdit.setTitle(title);
        bookToEdit.setAuthor(authorName);
        bookToEdit.setStatus(status);
        bookToEdit.setRating(rating);

        int rowsAffected = dbHelper.updateBook(bookToEdit, authorId);
        if (rowsAffected > 0) {
            Toast.makeText(this, R.string.messageBookUpdated, Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, R.string.messageErrorUpdatingBook, Toast.LENGTH_SHORT).show();
        }
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