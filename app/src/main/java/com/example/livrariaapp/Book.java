package com.example.livrariaapp;

public class Book {
    public String title;
    public String author;
    public String status;
    public float rating;

    public Integer id;

    public Book(int id, String title, String author, String status, float rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
        this.rating = rating;
    }
    public Book(String title, String author, String status, float rating) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }

    public float getRating() {
        return rating;
    }

    public Integer getId() {
        return id;
    }
}

