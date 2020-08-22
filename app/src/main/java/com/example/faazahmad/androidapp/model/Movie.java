package com.example.faazahmad.androidapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
    private String title, thumbnailUrl, year, rating;
    private ArrayList<String> genre;

    public Movie() {
    }

    public Movie(String name, String thumbnailUrl, String year, String rating,
                 ArrayList<String> genre) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

}