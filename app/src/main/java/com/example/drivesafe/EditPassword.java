package com.example.drivesafe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class EditPassword extends AppCompatActivity {

    private TextInputLayout til_oldpassword,til_newpassword,til_confirmpassword,til_email;
    private EditText til_oldpassword1,til_newpassword1,til_confirmpassword1,til_email1;
    private Button chngpwdsub;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        firebaseAuth = FirebaseAuth.getInstance();



        til_oldpassword = findViewById(R.id.til_oldpassword);
        til_oldpassword1 = findViewById(R.id.til_oldpassword1);
        til_newpassword = findViewById(R.id.til_newpassword);
        til_newpassword1 = findViewById(R.id.til_newpassword1);
        til_confirmpassword = findViewById(R.id.til_confirmpassword);
        til_confirmpassword1 = findViewById(R.id.til_confirmpassword1);
        til_email = findViewById(R.id.til_pwdemail);
        til_email1 = findViewById(R.id.til_pwdemail1);
        chngpwdsub = findViewById(R.id.b_chngpwdsub);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        til_email.getEditText().setText(firebaseUser.getEmail());

        chngpwdsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (til_oldpassword.getEditText().getText().toString().trim().isEmpty())
                {

                    til_oldpassword.setErrorEnabled(true);
                    til_oldpassword.setError("Invalid Email Address");
                    til_oldpassword1.setError("This field should not be empty!");
                    til_oldpassword.requestFocus();

                }

                else if (til_newpassword.getEditText().getText().toString().trim().isEmpty() || til_newpassword.getEditText().getText().toString().trim().length()<6)
                {

                    til_newpassword.setErrorEnabled(true);
                    til_newpassword.setError("Invalid Password");
                    til_newpassword1.setError("The Password Criteria did not match!");
                    til_newpassword.requestFocus();

                }

                else if (til_confirmpassword.getEditText().getText().toString().trim().isEmpty()||!til_confirmpassword.getEditText().getText().toString().trim().equals(til_newpassword.getEditText().getText().toString().trim()))
                {

                    til_confirmpassword.setErrorEnabled(true);
                    til_confirmpassword.setError("Invalid Password");
                    til_confirmpassword1.setError("Passwords did not match!");
                    til_confirmpassword.requestFocus();

                }

                else {

                    til_oldpassword.setErrorEnabled(false);
                    til_newpassword.setErrorEnabled(false);
                    til_confirmpassword.setErrorEnabled(false);

                    final ProgressDialog progressDialog = new ProgressDialog(EditPassword.this);
                    progressDialog.setTitle("Updating your Password.");
                    progressDialog.setMessage("Please wait while we verify your credentials.");
                    progressDialog.show();

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Owners").child(uid);
                    AuthCredential credential1 = EmailAuthProvider.getCredential(til_email.getEditText().getText().toString().trim(), til_oldpassword.getEditText().getText().toString().trim());
                    firebaseUser.reauthenticate(credential1).addOnCompleteListener(EditPassword.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                firebaseUser.updatePassword(til_newpassword.getEditText().getText().toString().trim()).addOnCompleteListener(EditPassword.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            databaseReference.child("Password").setValue(til_newpassword.getEditText().getText().toString());

                                            progressDialog.dismiss();

                                            Intent intent = new Intent(EditPassword.this, SettingsActivity.class);
                                            startActivity(intent);
                                            finish();

                                            Toast.makeText(EditPassword.this, "Password updated successfully.", Toast.LENGTH_LONG).show();

                                        }

                                        else
                                        {

                                            progressDialog.dismiss();
                                            Toast.makeText(EditPassword.this, "Error: Please check your Internet Connection.", Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });

                            }

                            else
                            {

                                progressDialog.dismiss();
                                Toast.makeText(EditPassword.this,"Your credentials did not match.",Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }

            }
        });





    }
}
