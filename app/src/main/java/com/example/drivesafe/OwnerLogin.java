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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerLogin extends AppCompatActivity {

    private TextInputLayout ownerEmail,ownerPassword;
    private EditText ownerEmail1,ownerPassword1;
    private Button ownerLogin,ownerSignup;

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        firebaseAuth = FirebaseAuth.getInstance();

        ownerEmail = findViewById(R.id.til_ownerEmail);
        ownerEmail1 = findViewById(R.id.til_ownerEmail1);
        ownerPassword = findViewById(R.id.til_ownerPassword);
        ownerPassword1 = findViewById(R.id.til_ownerPassword1);
        ownerLogin = findViewById(R.id.b_ownerlogin);
       ownerSignup = findViewById(R.id.b_ownerSignup);

        ownerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OwnerLogin.this,OwnerSignUp.class);
                startActivity(intent);
                finish();

            }
        });

        ownerLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (ownerEmail.getEditText().getText().toString().trim().isEmpty())
                {

                    ownerEmail.setErrorEnabled(true);
                    ownerEmail.setError("Invalid Email Address");
                    ownerEmail1.setError("This field should not be empty!");
                    ownerEmail.requestFocus();

                }

                else if(ownerPassword.getEditText().getText().toString().trim().isEmpty())
                {

                    ownerPassword.setErrorEnabled(true);
                    ownerPassword.setError("Invalid Password");
                    ownerPassword1.setError("This field should not be empty!");
                    ownerPassword1.setText(null);
                    ownerEmail.requestFocus();

                }

                else {

                    final ProgressDialog progressDialog = new ProgressDialog(OwnerLogin.this);
                    progressDialog.setTitle("Logging In.");
                    progressDialog.setMessage("Please wait while we verify your credentials.");
                    progressDialog.show();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Get an instance of KeyguardManager and FingerprintManager//
                        keyguardManager =
                                (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        fingerprintManager =
                                (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

                    }



                    ownerEmail.setErrorEnabled(false);
                    ownerPassword.setErrorEnabled(false);

                    String owneremail = ownerEmail.getEditText().getText().toString();
                    String ownerpwd = ownerPassword.getEditText().getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(owneremail,ownerpwd).addOnCompleteListener(OwnerLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                progressDialog.dismiss();

                                if (!fingerprintManager.isHardwareDetected()) {
                                    // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                                    Toast.makeText(OwnerLogin.this, "your device does not have fingerprint sensor", Toast.LENGTH_SHORT).show();
                                }

                                else
                                {
                                    //Toast.makeText(OwnerLogin.this, "Successful ", Toast.LENGTH_SHORT).show();

                                    FingerPrintHandler fingerPrintHandler = new FingerPrintHandler(OwnerLogin.this);
                                    fingerPrintHandler.startAuth(fingerprintManager,null,0);

                                }


                            }
                            else
                            {

                                progressDialog.dismiss();
                                Toast.makeText(OwnerLogin.this,"Please check your Credentials or Internet Connection!",Toast.LENGTH_LONG).show();

                            }

                        }
                    });


                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(OwnerLogin.this, TypeSelect.class);
        startActivity(intent);
        finish();

    }
}
