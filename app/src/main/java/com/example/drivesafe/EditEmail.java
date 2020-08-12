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

public class EditEmail extends AppCompatActivity {

    private TextInputLayout editemail,password,newemail;
    private EditText editemail1,password1,newemail1;
    private Button submit;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        firebaseAuth = FirebaseAuth.getInstance();

        editemail = findViewById(R.id.til_oldemail);
        editemail1 = findViewById(R.id.til_oldemail1);
        password = findViewById(R.id.til_emailpassword);
        password1 = findViewById(R.id.til_emailpassword1);
        newemail = findViewById(R.id.til_newemail);
        newemail1 = findViewById(R.id.til_newemail1);
        submit = findViewById(R.id.b_chngemailsub);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        editemail.getEditText().setText(user.getEmail());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editemail.getEditText().getText().toString().trim().isEmpty())
                {

                    editemail.setErrorEnabled(true);
                    editemail.setError("Invalid Email Address");
                    editemail1.setError("This field should not be empty!");
                    editemail.requestFocus();

                }

                else if (password.getEditText().getText().toString().trim().isEmpty())
                {

                    password.setErrorEnabled(true);
                    password.setError("Invalid Password");
                    password1.setError("This field should not be empty!");
                    password.requestFocus();

                }

                else if (newemail.getEditText().getText().toString().trim().isEmpty())
                {

                    newemail.setErrorEnabled(true);
                    newemail.setError("Invalid Email Address");
                    newemail1.setError("This field should not be empty!");
                    newemail.requestFocus();

                }

                else
                {

                        editemail.setErrorEnabled(false);
                        password.setErrorEnabled(false);
                        newemail.setErrorEnabled(false);

                    final ProgressDialog progressDialog = new ProgressDialog(EditEmail.this);
                    progressDialog.setTitle("Updating your Email Address.");
                    progressDialog.setMessage("Please wait while we verify your credentials.");
                    progressDialog.show();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String uid = user.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Owners").child(uid);
                    AuthCredential credential = EmailAuthProvider.getCredential(editemail.getEditText().getText().toString().trim(),password.getEditText().getText().toString().trim());
                    user.reauthenticate(credential).addOnCompleteListener(EditEmail.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.updateEmail(newemail.getEditText().getText().toString().trim()).addOnCompleteListener(EditEmail.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            databaseReference.child("EmailId").setValue(newemail.getEditText().getText().toString());

                                            progressDialog.dismiss();

                                            Intent intent = new Intent(EditEmail.this, SettingsActivity.class);
                                            startActivity(intent);
                                            finish();

                                            Toast.makeText(EditEmail.this, "Email Address updated successfully.", Toast.LENGTH_LONG).show();

                                        } else {

                                            progressDialog.dismiss();
                                            Toast.makeText(EditEmail.this, "Error: Please check your Internet Connection.", Toast.LENGTH_LONG).show();

                                        }

                                    }
                                });
                            }

                            else
                            {

                                progressDialog.dismiss();
                                Toast.makeText(EditEmail.this,"Your credentials did not match.",Toast.LENGTH_LONG).show();

                            }

                        }
                    });

                }




            }
        });



    }



}
