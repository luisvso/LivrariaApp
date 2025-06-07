package com.example.livrariaapp;

import java.io.Serializable;

public class Book implements Serializable {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}