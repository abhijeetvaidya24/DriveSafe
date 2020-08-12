package com.example.drivesafe;

import android.app.KeyguardManager;
import android.app.ProgressDialog;
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


import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;
import java.util.Iterator;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import androidx.appcompat.app.AppCompatActivity;

public class UserLogin extends AppCompatActivity {

    private TextInputLayout userName,userEmail,userPhone,userVehNo;
    private EditText userName1,userEmail1,userPhone1,userVehNo1;
    private Button userSubmit;

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    public String uid;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        userName = findViewById(R.id.til_userName);
        userName1 = findViewById(R.id.til_userName1);
        userEmail = findViewById(R.id.til_userEmail);
        userEmail1 = findViewById(R.id.til_userEmail1);
        userPhone = findViewById(R.id.til_userPhone);
        userPhone1 = findViewById(R.id.til_userPhone1);
        userVehNo = findViewById(R.id.til_VehNo);
        userVehNo1 = findViewById(R.id.til_VehNo1);
        userSubmit = findViewById(R.id.b_userSubmit);



        userSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (userName.getEditText().getText().toString().trim().isEmpty() || userName.getEditText().getText().toString().trim().length() > 50) {

                    userName.setErrorEnabled(true);
                    userName.setError("Invalid Name");
                    userName1.setError("Please Enter a Valid Name.");
                    userName1.requestFocus();

                }
                else if (userEmail.getEditText().getText().toString().trim().isEmpty()) {

                    userEmail.setErrorEnabled(true);
                    userEmail.setError("Invalid Email");
                    userEmail1.setError("Please Enter a Valid Email Address.");
                    userEmail.requestFocus();

                }

                else if(userPhone.getEditText().getText().toString().trim().isEmpty()||userPhone.getEditText().getText().toString().trim().length()!=10)
                {

                    userPhone.setErrorEnabled(true);
                    userPhone.setError("Invalid Phone Number");
                    userPhone1.setError("Please Enter a Valid Number...");
                    userPhone.requestFocus();

                }

                else if(userVehNo.getEditText().getText().toString().trim().isEmpty()||userVehNo.getEditText().getText().toString().trim().length()!=10)
                {

                    userVehNo.setErrorEnabled(true);
                    userVehNo.setError("Invalid Phone Number");
                    userVehNo1.setError("Please Enter a Valid Number...");
                    userVehNo.requestFocus();

                }
                else
                {

                    userVehNo.setErrorEnabled(false);
                    userPhone.setErrorEnabled(false);
                    userEmail.setErrorEnabled(false);
                    userName.setErrorEnabled(false);

                    String name = userName.getEditText().getText().toString();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Get an instance of KeyguardManager and FingerprintManager//
                        keyguardManager =
                                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        fingerprintManager =
                                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                    }

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Owners");

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Iterator<DataSnapshot> iterator = dataSnapshot

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if (!fingerprintManager.isHardwareDetected()) {
                        // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//

                        Toast.makeText(UserLogin.this, "Your device does not have fingerprint sensor..", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        //Toast.makeText(OwnerLogin.this, "Successful ", Toast.LENGTH_SHORT).show();

                        FingerPrintHandler fingerPrintHandler = new FingerPrintHandler(UserLogin.this);
                        fingerPrintHandler.startAuth(fingerprintManager,null,1);

                    }



                }


            }
        });

    }


}