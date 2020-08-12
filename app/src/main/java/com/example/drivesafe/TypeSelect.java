package com.example.drivesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class TypeSelect extends AppCompatActivity {

    private Button owner,user;


    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);

        mAuth = FirebaseAuth.getInstance();

        owner = (Button)findViewById(R.id.b_Upload);
        user = (Button)findViewById(R.id.b_OwnerUser);

        owner.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(TypeSelect.this,OwnerLogin.class);
                startActivity(intent);

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TypeSelect.this,UserLogin.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {

            Intent intent = new Intent(TypeSelect.this,OwnerMainActivityMain.class);
            startActivity(intent);
            finish();

        }



    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {

            Intent intent = new Intent(TypeSelect.this,OwnerMainActivityMain.class);
            startActivity(intent);
            finish();

        }

    }

    public void onBackPressed() {


        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        finish();

    }



}