package com.example.drivesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private Button change_img,change_email,change_pwd,logout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();


        change_img = (Button)findViewById(R.id.b_changeImage);
        change_email = (Button)findViewById(R.id.b_changeEmail);
        change_pwd = (Button)findViewById(R.id.b_changePassword);
        logout = findViewById(R.id.b_logout);
        change_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,EditImage.class);
                startActivity(i);
            }
        });

        change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, EditEmail.class);
                startActivity(i);
            }
        });

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,EditPassword.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this,TypeSelect.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(SettingsActivity.this,OwnerMainActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);




    }



}
