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

        EditText inputTitle = findViewById(R.id.textNameBook);

        // insere o livro no banco de dados ao clicar no botão save
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> {

            String title = inputTitle.getText().toString();

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            // armazena o valor da variavel titulo na coluna da tabela
            values.put("Title", title);
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