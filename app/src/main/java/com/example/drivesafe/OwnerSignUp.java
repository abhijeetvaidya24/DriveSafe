package com.example.drivesafe;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerSignUp extends AppCompatActivity {

    private TextInputLayout ownerName,vehicleRegNo,vehicleChassiNo,emailAdd,phoneNo,password,conPassword;
    private EditText ownerName1,vehicleRegNo1,vehicleChassiNo1,emailAdd1,phoneNo1,password1,conPassword1;
    private Button submit,login;
    private String TAG;

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;



    public OwnerSignUp() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_sign_up);



        mAuth = FirebaseAuth.getInstance();


        // Read from the database

        ownerName = findViewById(R.id.til_OwnerName);
        vehicleRegNo = findViewById(R.id.til_numPlate);
        vehicleChassiNo = findViewById(R.id.til_ChassiNum);
        emailAdd = findViewById(R.id.til_Email);
        phoneNo = findViewById(R.id.til_phoneNum);
        password = findViewById(R.id.til_password);
        conPassword = findViewById(R.id.til_conPassword);
        submit = findViewById(R.id.b_Submit);
        ownerName1 = findViewById(R.id.til_ownerName1);
        vehicleRegNo1 = findViewById(R.id.til_numPlate1);
        vehicleChassiNo1 = findViewById(R.id.til_ChassiNum1);
        emailAdd1 = findViewById(R.id.til_Email1);
        phoneNo1 = findViewById(R.id.til_phoneNum1);
        password1 = findViewById(R.id.til_password1);
        conPassword1 = findViewById(R.id.til_conPassword1);
        login = findViewById(R.id.b_login);



            submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (ownerName.getEditText().getText().toString().trim().isEmpty() || ownerName.getEditText().getText().toString().trim().length() > 30) {

                    ownerName.setErrorEnabled(true);
                    ownerName.setError("Invalid Name");
                    ownerName1.setError("Please Enter a Valid Name.");
                    ownerName.requestFocus();

                } else if (vehicleRegNo.getEditText().getText().toString().trim().isEmpty() || vehicleRegNo.getEditText().getText().toString().trim().length() > 10) {

                    vehicleRegNo.setErrorEnabled(true);
                    vehicleRegNo.setError("Invalid Registration No.");
                    vehicleRegNo1.setError("Please Enter a Valid Registration Number.");
                    vehicleRegNo.requestFocus();

                } else if (vehicleChassiNo.getEditText().getText().toString().trim().isEmpty() || vehicleChassiNo.getEditText().getText().toString().trim().length() > 17) {

                    vehicleChassiNo.setErrorEnabled(true);
                    vehicleChassiNo.setError("Invalid Chassi No.");
                    vehicleChassiNo1.setError("Please Enter a Valid chassi Number.");
                    ownerName.requestFocus();

                } else if (emailAdd.getEditText().getText().toString().trim().isEmpty()) {

                    emailAdd.setErrorEnabled(true);
                    emailAdd.setError("Invalid Email");
                    emailAdd1.setError("Please Enter a Valid Email Address.");
                    emailAdd.requestFocus();

                }

                else if(phoneNo.getEditText().getText().toString().trim().isEmpty()||phoneNo.getEditText().getText().toString().trim().length()!=10)
                {

                    phoneNo.setErrorEnabled(true);
                    phoneNo.setError("Invalid Phone Number");
                    phoneNo1.setError("Please Enter a Valid Number...");
                    ownerName.requestFocus();

                }

                else if (password.getEditText().getText().toString().trim().isEmpty() || password.getEditText().getText().toString().trim().length() < 6) {

                    password.setErrorEnabled(true);
                    password.setError("Invalid Password");
                    password1.setError("Please Enter a Stronger Password.");
                    password1.setText(null);
                    password.requestFocus();

                } else if (conPassword.getEditText().getText().toString().trim().isEmpty() || !conPassword.getEditText().getText().toString().trim().equals(password.getEditText().getText().toString().trim())) {

                    conPassword.setErrorEnabled(true);
                    conPassword.setError("Incorrect Password");
                    conPassword1.setError("Passwords do not Match!");
                    conPassword1.setText(null);
                    conPassword.requestFocus();

                } else {

                    ownerName.setErrorEnabled(false);
                    vehicleRegNo.setErrorEnabled(false);
                    vehicleChassiNo.setErrorEnabled(false);
                    emailAdd.setErrorEnabled(false);
                    phoneNo.setErrorEnabled(false);
                    password.setErrorEnabled(false);
                    conPassword.setErrorEnabled(false);



                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Get an instance of KeyguardManager and FingerprintManager//
                        keyguardManager =
                                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        fingerprintManager =
                                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                    }

                    final String email = emailAdd.getEditText().getText().toString().trim();
                    String pass = password.getEditText().getText().toString().trim();


                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(OwnerSignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                final ProgressDialog progressDialog1 = new ProgressDialog(OwnerSignUp.this);
                                progressDialog1.setTitle("Saving your Details.");
                                progressDialog1.setMessage("Please wait while we verify your credentials!");
                                progressDialog1.show();

                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = firebaseUser.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Owners").child(uid);

                                HashMap<String,String> userMap = new HashMap<>();
                                userMap.put("Name",ownerName.getEditText().getText().toString());
                                userMap.put("VehicleNo",vehicleRegNo.getEditText().getText().toString());
                                userMap.put("PhoneNo",phoneNo.getEditText().getText().toString());
                                userMap.put("ChassiNo",vehicleChassiNo.getEditText().getText().toString());
                                userMap.put("EmailId",emailAdd.getEditText().getText().toString());
                                userMap.put("Password",password.getEditText().getText().toString());

                                databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            progressDialog1.dismiss();

                                        }
                                        else{
                                            Toast.makeText(OwnerSignUp.this, "Error!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                                progressDialog1.dismiss();

                                mAuth.getCurrentUser();

                                if (!fingerprintManager.isHardwareDetected()) {
                                    // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                                    Toast.makeText(OwnerSignUp.this, "Your device does not have fingerprint sensor..", Toast.LENGTH_SHORT).show();
                                }

                                else
                                {
                                    //Toast.makeText(OwnerLogin.this, "Successful ", Toast.LENGTH_SHORT).show();

                                    FingerPrintHandler fingerPrintHandler = new FingerPrintHandler(OwnerSignUp.this);
                                    fingerPrintHandler.startAuth(fingerprintManager,null,0);

                                }

                            }
                            else {


                                Toast.makeText(OwnerSignUp.this,"Please check your Credentials or Internet Connection!",Toast.LENGTH_LONG).show();

                            }


                        }
                    });
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OwnerSignUp.this,OwnerLogin.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OwnerSignUp.this, TypeSelect.class);
        startActivity(intent);
        finish();
    }
}
