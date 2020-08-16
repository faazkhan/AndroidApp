package com.example.faazahmad.androidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button reload = findViewById(R.id.button);
        Button logout = findViewById(R.id.logout);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.userlist);

        populateList();

        reload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                lv.setAdapter(null);
                populateList();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                sharedpreferences.edit().putBoolean("logged",false).apply();
                goToLogin();
            }
        });

        // listening to single list item on click
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                // sending data to new activity
                i.putExtra("contact", contactList.get(position));
                startActivity(i);

            }
        });


}

    private void populateList() {
        if (online()) {
            new GetContacts().execute();
        } else {
            read();
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://api.androidhive.info/contacts/";
            String jsonStr = sh.makeServiceCall(url);

            Log.e("", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");
                    contactList.clear();

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);

                        // adding contact to contact list
                        contactList.add(contact);

                    }

                } catch (final JSONException e) {
                    Log.e("", "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Log.e("", "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            save(contactList);
            setAdapter();
        }
    }

    private void setAdapter() {
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
                R.layout.list_item, new String[]{"name"},
                new int[]{R.id.name});
        lv.setAdapter(adapter);
    }

    private Boolean read() {
        Toast.makeText(getApplicationContext(), "Reading Local Databases.", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        String json = sharedpreferences.getString("contactList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Local Database is empty.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Type type = new TypeToken<ArrayList<HashMap<String, String>> >() {}.getType();
            contactList = gson.fromJson(json, type);
            if(contactList.isEmpty()){
                return false;
            }
            setAdapter();
            return true;
        }
    }
    private void save(ArrayList<HashMap<String, String>> contactList){
        Gson gson = new Gson();
        String json = gson.toJson(contactList);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Toast.makeText(getApplicationContext(), "Clearing Previous Records from DB.", Toast.LENGTH_SHORT).show();
        editor.remove("contactList");
        editor.commit();

        Toast.makeText(getApplicationContext(), "Saving Latest Records.", Toast.LENGTH_SHORT).show();
        editor.putString("contactList",json);
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
