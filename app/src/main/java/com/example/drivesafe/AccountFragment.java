package com.example.drivesafe;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.security.KeyStore;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;


public class AccountFragment extends Fragment {


    private ImageView ownerImage;
    private TextView mainOwnerName,vehicle_name;
    private Button Use;
    private ListView usersList;
    private Spinner vehicleList;

    private FirebaseAuth firebaseAuth;

    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;



    public AccountFragment()
    {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_account,container,false);

        firebaseAuth = FirebaseAuth.getInstance();

        ownerImage = (ImageView) view.findViewById(R.id.ownerImage);
        mainOwnerName = view.findViewById(R.id.tv_mainOwnerName);
        usersList = view.findViewById(R.id.users_list);
        usersList.setClickable(true);
        Use = view.findViewById(R.id.b_use);

        vehicleList = view.findViewById(R.id.vehicle_select);

        mainOwnerName.setText("Welcome Owner!");



        ArrayList<String> vehicleArray = new ArrayList<>();
        vehicleArray.add("MH12AB1234");
        vehicleArray.add("MH12AB2345");
        vehicleArray.add("MH12AB3456");
        vehicleArray.add("MH12AB4567");
        vehicleArray.add("MH12AB5678");

        @SuppressLint("ResourceType") ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,vehicleArray);
        vehicleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleList.setAdapter(vehicleAdapter);

        Use.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Get an instance of KeyguardManager and FingerprintManager//
                    keyguardManager =
                            (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
                    fingerprintManager =
                            (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);

                }

                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                if (!fingerprintManager.isHardwareDetected())
                    Toast.makeText(v.getContext(), "Your device does not have fingerprint sensor..", Toast.LENGTH_SHORT).show();

                else
                {
                    //Toast.makeText(OwnerLogin.this, "Successful ", Toast.LENGTH_SHORT).show();

                    FingerPrintHandler fingerPrintHandler = new FingerPrintHandler(getContext());
                    fingerPrintHandler.startAuth(fingerprintManager,null,1);

                }



            }
        });



        //ownerImage.setImageResource(R.drawable.ic_launcher_background);
        return view;

    }




}
