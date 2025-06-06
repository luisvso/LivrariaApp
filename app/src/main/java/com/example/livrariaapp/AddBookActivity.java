package com.example.livrariaapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import android.database.Cursor;

public class AddBookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textPublicationDate), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        // Pega o titulo
        EditText inputTitle = findViewById(R.id.textNameBook);

        //Pega o Autor
        EditText inputAuthor = findViewById(R.id.textAuthor);

        // Pega o Status Lido, Lendo ou Não lido
        ChipGroup chipGroup = findViewById(R.id.chipGroup);

        // insere o livro no banco de dados ao clicar no botão save
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> {

            String title = inputTitle.getText().toString().trim();
            String author = inputAuthor.getText().toString().trim();

            // Captura o chip selecionado
            int selectedChipId = chipGroup.getCheckedChipId();
            String status = "";
            if (selectedChipId != -1) {
                Chip selectedChip = findViewById(selectedChipId);
                status = selectedChip.getText().toString();
            }

            int stars = 0;

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();


            // Buscar autor no banco
            long authorId = -1;
            Cursor cursor = db.rawQuery("SELECT Id_author FROM Author WHERE Nome = ?", new String[]{author});
            if (cursor.moveToFirst()) {
                authorId = cursor.getLong(0);
            }
            cursor.close();

            // Se autor não existe, insere
            if (authorId == -1) {
                ContentValues authorValues = new ContentValues();
                authorValues.put("Nome_Author", author);
                authorId = db.insert("Author", null, authorValues);
            }

            ContentValues values = new ContentValues();
            // armazena o valor da variavel titulo na coluna da tabela
            values.put("Title", title);
            values.put("Id_author", authorId);
            values.put("Status", status);
            values.put("Rating", stars);

            long result = db.insert("Book", null, values);

            // 1 = sucesso e -1 = erro
            if (result != -1) {
                Toast.makeText(this, "Livro add com sucesso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Erro ao adicionar livro", Toast.LENGTH_SHORT).show();
            }
            db.close();
        });
    }

}