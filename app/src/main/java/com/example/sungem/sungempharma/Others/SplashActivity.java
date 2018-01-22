package com.example.sungem.sungempharma.Others;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.sungem.sungempharma.Admin.AdminMainActivity;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.R;

public class SplashActivity extends AppCompatActivity {

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 3000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    finish();

                    // get the SharedPreferences object
                    sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
                    // Retrieve the saved values
                    boolean flag = sharedpref.getBoolean("LOGGED", false);
                    String access = sharedpref.getString("ACCESS","");
                    if(flag == false) {
                        intent = new Intent(SplashActivity.this, MainSelectionActivity.class);
                    }else{
                        if(access.equals("admin")){
                            intent = new Intent(SplashActivity.this, AdminMainActivity.class);
                        }else if(access.equals("medrep")){
                            intent = new Intent(SplashActivity.this, MedrepMainActivity.class);
                        }
                    }
                    startActivity(intent);
                }
            }
        };
        splashThread.start();
    }
}

