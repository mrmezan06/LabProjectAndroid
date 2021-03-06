package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

public class UserLoginActivity extends AppCompatActivity {

    private EditText Mobile, mCode;
    private Button mSend;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationId;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.acitivity_user_register);

        //Check User Logged in or Not
        UserLoggedIn();


        Mobile = findViewById(R.id.phonenumber);
        Mobile.setSelection(Mobile.getText().length());

        mCode = findViewById(R.id.Code);
        mSend = findViewById(R.id.Confirm);
        constraintLayout = findViewById(R.id.rootConstraint);
        mCode.setVisibility(View.GONE);

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
                    //SentCodeToUser();
                    StartPhoneNumberVerification();
                }


            }


        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("getSMSCode","Code:"+phoneAuthCredential.getSmsCode());
                 SignInWithPhoneCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

               Log.d("getSMSCodeError",e.getMessage());
            }

            @Override
            public void onCodeSent(String VerificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(VerificationId, forceResendingToken);
                mVerificationId = VerificationId;
                mCode.setVisibility(View.VISIBLE);
                mSend.setText("Verify");

            }
        };




    }
    private void SignInWithPhoneCredential(PhoneAuthCredential phoneAuthCredential) {
        //its checked exactly what i sent
       // Log.d("getSMSCode","Code:"+phoneAuthCredential.getSmsCode());


        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(getApplicationContext(), "User Logged In Successfully", Toast.LENGTH_LONG).show();
                    if (user != null) {

                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("UserAuth");
                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(user.getUid(),"Accepted");
                                mUserDB.updateChildren(userMap);
                                UserLoggedIn();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }
        });
    }
    private void UserLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            /*startActivity(new Intent(UserLoginActivity.this, LoginForm.class));
            finish();
            return;*/

            checkUserAuth();

        }else {
            Toast.makeText(getApplicationContext(),"Wrong Verification Code",Toast.LENGTH_LONG).show();
            Log.d("ValidationOfCode","Wrong Verification Code");
        }
    }
    private void checkUserAuth() {
        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("UserAuth");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if (dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue() != null){
                        Log.d("USERUID",dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString());
                        if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().equals("Accepted")) {
                            Log.d("USERUID",dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue().toString());
                            startActivity(new Intent(UserLoginActivity.this, UserInterface.class));
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void VerifyPhoneNumberWithCode() {

        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mCode.getText().toString());
            SignInWithPhoneCredential(credential);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Insert Verification Code!",Toast.LENGTH_LONG).show();
        }




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
        // ARE WE CONNECTED TO THE iNET
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {

            return true;

        } else {
            return false;
        }

    }
}
