package com.example.drivesafe;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.M)
public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;


    private boolean flag = false;

    ProgressDialog progressDialog1 = null;

    int act;


    public FingerPrintHandler(Context context){

        this.context = context;

    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject, int activity){

        act = activity;

        CancellationSignal cancellationSignal = new CancellationSignal();

        fingerprintManager.authenticate(cryptoObject, cancellationSignal ,0,this,null);


        progressDialog1 = new ProgressDialog(context);
        progressDialog1.setTitle("Scan your fingerprint.");
        progressDialog1.setMessage("Please wait while we verify your credentials!");
        progressDialog1.show();

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.flag = false;
        progressDialog1.dismiss();

    }

    @Override
    public void onAuthenticationFailed() {

        progressDialog1.dismiss();
        Toast.makeText(context, "Access Denied.", Toast.LENGTH_SHORT).show();

        this.flag = false;


    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        this.progressDialog1.dismiss();

        if (this.act == 0)
        {

            Toast.makeText(context, "Credentilas Verified Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,OwnerMainActivityMain.class);
            context.startActivity(intent);



        }

        else
        {

            Toast.makeText(context, "Welcome User!\nPlease Enter further Details to gain permission from the Owner.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,CarOperation.class);
            //Add New Activity for User Main Page to Enter Details.

            context.startActivity(intent);

        }


    }



}
