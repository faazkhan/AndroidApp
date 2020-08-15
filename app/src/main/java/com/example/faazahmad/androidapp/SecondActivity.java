package com.example.faazahmad.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView nameTxt = (TextView) findViewById(R.id.name);
        TextView idTxt = (TextView) findViewById(R.id.id);
        TextView addressTxt = (TextView) findViewById(R.id.address);
        TextView phoneTxt = (TextView) findViewById(R.id.phone);
        TextView emailTxt = (TextView) findViewById(R.id.email);
        TextView genderTxt = (TextView) findViewById(R.id.gender);

        Intent i = getIntent();
        // getting attached intent data
        HashMap<String, String> contact = (HashMap<String, String>) i.getSerializableExtra("contact");
        // displaying selected product name
        idTxt.setText(contact.get("id"));
        nameTxt.setText(contact.get("name"));
        addressTxt.setText(contact.get("address"));
        phoneTxt.setText(contact.get("phone"));
        emailTxt.setText(contact.get("email"));
        genderTxt.setText(contact.get("gender"));
    }
}
