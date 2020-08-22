package com.example.faazahmad.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.faazahmad.androidapp.model.Movie;

public class SecondActivity extends AppCompatActivity {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView titleTxt = (TextView) findViewById(R.id.title);
        TextView ratingTxt = (TextView) findViewById(R.id.rating);
        TextView genreTxt = (TextView) findViewById(R.id.genre);
        TextView yearTxt = (TextView) findViewById(R.id.year);
        NetworkImageView thumbNail = (NetworkImageView) findViewById(R.id.thumbnail);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        Intent i = getIntent();
        // getting attached intent data
        Movie movie = (Movie) i.getSerializableExtra("movie");
        // displaying selected product name
        titleTxt.setText(movie.getTitle());
        genreTxt.setText(movie.getGenre().toString());
        yearTxt.setText(movie.getYear());
        thumbNail.setImageUrl(movie.getThumbnailUrl(), imageLoader);
        ratingTxt.setText(movie.getRating());
    }
}
