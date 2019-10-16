package com.mezan.extreme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingScreen extends AppCompatActivity {

    ImageView animObj ;
    ConstraintLayout rootView;
    private boolean isFirstAnimation = false;
    static boolean isFirst = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_loading_screen);

        //#8A1569 --> Car Background
        //#02683A --> Food Background

        animObj = findViewById(R.id.splash);
        rootView=findViewById(R.id.rootView);


//        animObj.setImageResource(R.drawable.food);
//        rootView.setBackgroundColor(Color.parseColor("#02683A"));
//        clockwise();


        animObj.setImageResource(R.drawable.car);
        rootView.setBackgroundColor(Color.parseColor("#8A1569"));
//        animObj.animate().translationXBy(0f).translationYBy(0f).rotationBy(3600).setDuration(2000);

        final Animation clockwise = AnimationUtils.loadAnimation(this,R.anim.clockwise);
        final Animation zoom = AnimationUtils.loadAnimation(this,R.anim.myanim);
        clockwise.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    animObj.clearAnimation();
                    Intent intent;
                    if(isFirst)
                       intent = new Intent(LoadingScreen.this, ModuleActivity.class);
                    else
                        intent = new Intent(LoadingScreen.this, UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                isFirstAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        zoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animObj.clearAnimation();
                animObj.startAnimation(zoom);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animObj.startAnimation(clockwise);

    }



}
