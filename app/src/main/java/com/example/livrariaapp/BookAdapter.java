package com.example.livrariaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_layout, parent, false);
        }

        Book book = getItem(position);

        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        TextView tvAutor = convertView.findViewById(R.id.tvAutor);
        Chip chipStatus = convertView.findViewById(R.id.chipStatus);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

        tvTitulo.setText(book.title);
        tvAutor.setText(book.author);
        chipStatus.setText(book.status);
        ratingBar.setRating(book.rating);

        // Opcional: mudar cor do chip conforme status
        // chipStatus.setChipBackgroundColor(...)

        return convertView;
    }
}

