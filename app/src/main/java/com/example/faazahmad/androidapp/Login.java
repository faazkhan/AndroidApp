package com.example.faazahmad.androidapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.faazahmad.androidapp.MainActivity.MyPREFERENCES;

public class Login extends AppCompatActivity {

    Button loginButton;
    EditText username,pwd;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginButton = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        pwd = (EditText) findViewById(R.id.editText2);

        sp = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);

        if(sp.getBoolean("logged",false)){
            goToMainActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("admin") &&
                        pwd.getText().toString().equals("admin")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...",Toast.LENGTH_SHORT).show();

                    sp.edit().putBoolean("logged",true).apply();
                    goToMainActivity();
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

}
