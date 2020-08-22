package com.example.faazahmad.androidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.faazahmad.androidapp.adapter.CustomAdapter;
import com.example.faazahmad.androidapp.model.Movie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    private static final String url = "https://api.androidhive.info/json/movies.json";
    private List<Movie> movieList = new ArrayList<Movie>();
    private CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button reload = findViewById(R.id.button);
        Button logout = findViewById(R.id.logout);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        lv = (ListView) findViewById(R.id.userlist);
        adapter = new CustomAdapter(this, movieList);
        lv.setAdapter(adapter);

        populateList();
        reload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                populateList();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                sharedpreferences.edit().putBoolean("logged", false).apply();
                goToLogin();
            }
        });

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Movie movie = (Movie) adapter.getItem(position);
                // Launching new Activity on selecting single List Item
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                // sending data to new activity
                i.putExtra("movie", movie);
                startActivity(i);

            }
        });

      }

    private JsonArrayRequest fetchMovieData() {
        return new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("Log", response.toString());

                            movieList.clear();
                            // Parsing json
                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    JSONObject obj = response.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.setTitle(obj.getString("title"));
                                    movie.setThumbnailUrl(obj.getString("image"));
                                    movie.setRating("Rating: "+obj.get("rating").toString());
                                    movie.setYear(""+ obj.getInt("releaseYear"));

                                    // Genre is json array
                                    JSONArray genreArry = obj.getJSONArray("genre");
                                    ArrayList<String> genre = new ArrayList<String>();
                                    for (int j = 0; j < genreArry.length(); j++) {
                                        genre.add((String) genreArry.get(j));
                                    }
                                    movie.setGenre(genre);

                                    // adding movie to movies array
                                    movieList.add(movie);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                            Collections.shuffle(movieList);
                            // notifying list adapter about data changes
                            // so that it renders the list view with updated data
                            adapter.notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error", "Error: " + error.getMessage());
                    //       hidePDialog();

                }
            });
    }

    private void populateList() {
        if (online()) {
            new GetMovies().execute();
        } else {
            read();
        }
    }

   private class GetMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            AppController.getInstance().addToRequestQueue(fetchMovieData());
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            save(movieList);
        }
    }


    private Boolean read() {
        Toast.makeText(getApplicationContext(), "Reading Local Databases.", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String json = sharedpreferences.getString("movieList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Local Database is empty.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Type type = new TypeToken<List<Movie> >() {}.getType();
            List<Movie> movies = gson.fromJson(json, type);
            if(movies.isEmpty()){
                Toast.makeText(getApplicationContext(), "Local List is empty.", Toast.LENGTH_SHORT).show();
                return false;
            }
            Log.i("list",""+movies.size());
            movieList.clear();
            movieList.addAll(movies);
            Collections.shuffle(movieList);
            adapter.notifyDataSetChanged();
            return true;
        }
    }
    private void save(List<Movie> movieList){
        Gson gson = new Gson();
        String json = gson.toJson(movieList);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Toast.makeText(getApplicationContext(), "Clearing Previous Records from DB.", Toast.LENGTH_SHORT).show();
        editor.remove("movieList");
        editor.commit();

        Toast.makeText(getApplicationContext(), "Saving Latest Records.", Toast.LENGTH_SHORT).show();
        editor.putString("movieList",json);
        editor.commit();
    }


    public void goToLogin(){
        Intent i = new Intent(this,Login.class);
        startActivity(i);
    }

    public Boolean online() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
