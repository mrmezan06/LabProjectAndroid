package com.mezan.extreme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ContinueWithPhone extends AppCompatActivity {

    Button fwLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fwLogin=findViewById(R.id.fwLogin);
        fwLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    startActivity(new Intent(ContinueWithPhone.this,LoginForm.class));
                else
                    startActivity(new Intent(ContinueWithPhone.this,LoginActivity.class));
            }
        });
    }
}
