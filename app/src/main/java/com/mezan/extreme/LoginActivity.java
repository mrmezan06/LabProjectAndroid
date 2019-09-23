package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText Mobile, mCode;
    private Button mSend;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationId;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        //Check User Logged in or Not
        UserLoggedIn();


        Mobile = findViewById(R.id.phonenumber);
        Mobile.setSelection(Mobile.getText().length());

        mCode = findViewById(R.id.Code);
        mSend = findViewById(R.id.Confirm);
        constraintLayout = findViewById(R.id.rootConstraint);

        if (!isInternetConnection()) {
            Toast.makeText(getApplicationContext(), "No Internet Connection Available!", Toast.LENGTH_LONG).show();
            mSend.setEnabled(false);
        } else {
            mSend.setEnabled(true);
        }


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mVerificationId != null) {
                    VerifyPhoneNumberWithCode();
                } else {
                    StartPhoneNumberVerification();
                }


            }


        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                SignInWithPhoneCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String VerificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerificationId, forceResendingToken);
                mVerificationId = VerificationId;
                mSend.setText("Verify Code");

            }
        };


    }


    private void SignInWithPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(getApplicationContext(), "User Logged In Successfully", Toast.LENGTH_LONG).show();
                    if (user != null) {
                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", user.getPhoneNumber());
                                    userMap.put("name", user.getPhoneNumber());
                                    mUserDB.updateChildren(userMap);
                                }
                                UserLoggedIn();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    //UserLoggedIn();
                }
            }
        });
    }

    private void UserLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
            finish();
            return;
        }
    }

    private void VerifyPhoneNumberWithCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mCode.getText().toString());
        SignInWithPhoneCredential(credential);
    }

    private void StartPhoneNumberVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Mobile.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,
                mCallBacks
        );
    }

    public boolean isInternetConnection() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }

    }
}
