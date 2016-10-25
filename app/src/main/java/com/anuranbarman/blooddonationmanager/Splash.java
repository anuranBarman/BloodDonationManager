package com.anuranbarman.blooddonationmanager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;



/**
 * Created by anuran on 23/10/16.
 */

public class Splash extends AppCompatActivity {

    TextView name,nameSecond;
    SharedPreferences sharedPreferences;

    boolean isLogged;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        sharedPreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE);
        isLogged=sharedPreferences.getBoolean("isLogged",false);
        name=(TextView)findViewById(R.id.appName);
        nameSecond=(TextView)findViewById(R.id.appNameSecond);
        Typeface type= Typeface.createFromAsset(getAssets(),"font.ttf");
        name.setTypeface(type);
        nameSecond.setTypeface(type);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isLogged){

                    Intent i = new Intent(Splash.this,Home.class);
                    startActivity(i);
                    finish();
                }else{

                    Intent i = new Intent(Splash.this,Login.class);
                    startActivity(i);
                    finish();
                }

            }
        },3000);
    }


}
