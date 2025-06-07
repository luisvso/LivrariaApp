package com.example.livrariaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {

    public interface OnDeleteClickListener {
        void onDeleteClick(Book book);
    }

    private OnDeleteClickListener deleteListener;


    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    public interface OnEditClickListener {
        void onEditClick(Book book);
    }

    private OnEditClickListener editListener;

    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editListener = listener;
    }


    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_layout, parent, false);
        }

        Book book = getItem(position);

        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        TextView tvAutor = convertView.findViewById(R.id.tvAutor);
        Chip chipStatus = convertView.findViewById(R.id.chipStatus);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);

        ImageButton btnDelete = convertView.findViewById(R.id.button_deleteBook);
        ImageButton btnEdit = convertView.findViewById(R.id.button_editBook);

        tvTitulo.setText(book.getTitle());
        tvAutor.setText(book.getAuthor());
        chipStatus.setText(book.getStatus());
        ratingBar.setRating(book.getRating());

        switch (book.getStatus().toLowerCase().trim()) {
            case "lido":
                chipStatus.setChipBackgroundColorResource(R.color.verde_lido);
                break;
            case "lendo":
                chipStatus.setChipBackgroundColorResource(R.color.amarelo_lendo);
                break;
            case "não lido":
                chipStatus.setChipBackgroundColorResource(R.color.vermelho_nLido);
                break;
            default:
                chipStatus.setChipBackgroundColorResource(R.color.white);
                break;
        }

        Log.d("BookAdapter", "Título: " + book.getTitle() + ", Rating: " + book.getRating());

        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null && book != null) {
                    deleteListener.onDeleteClick(book);
                }
            });
        }

        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (editListener != null && book != null) {
                    editListener.onEditClick(book);
                }
            });
        }
        return convertView;
    }
}