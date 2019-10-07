package com.mezan.extreme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInterface extends AppCompatActivity {

    Button btnSignOut;
    TextView data;
    DatabaseReference mUserDB;
    String fullContent="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);


        btnSignOut=findViewById(R.id.btnSignOut);
        data=findViewById(R.id.contentData);



        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserDB = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            fetchDataFromFirebase();


        }
        else {
            startActivity(new Intent(UserInterface.this,LoginActivity.class));
        }



        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
    private void fetchDataFromFirebase() {


        // mChatMessagesDB = FirebaseDatabase.getInstance().getReference().child("chat").child(mChatObject.getChatId()).child("messages");
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String content="" ;
                    if(dataSnapshot.child("name").getValue() != null){
                        content += "Name:"+dataSnapshot.child("name").getValue()+"\n";
                    }
                    if(dataSnapshot.child("mobile").getValue() != null){
                        content += "Mobile:"+dataSnapshot.child("mobile").getValue()+"\n";
                        // Log.d("Mobile",fMobile);

                    }
                    if (dataSnapshot.child("password").getValue() != null){
                        content += "Password:"+ dataSnapshot.child("password").getValue()+"\n";
                        //Log.d("Password",fPassword);
                    }
                    if(dataSnapshot.child("address").getValue() != null){
                        content += "Address:"+dataSnapshot.child("address").getValue()+"\n";
                    }

                    if(dataSnapshot.child("gender").getValue() != null){
                        content += "Gender:"+dataSnapshot.child("gender").getValue()+"\n";
                    }
                    if(dataSnapshot.child("bod").getValue() != null){
                        content += "BOD:"+dataSnapshot.child("bod").getValue()+"\n";
                    }
                    if(dataSnapshot.child("created").getValue() != null){
                        content += "Created At:"+dataSnapshot.child("created").getValue()+"\n";
                    }

                    fullContent = content;
                    data.setText(fullContent);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
