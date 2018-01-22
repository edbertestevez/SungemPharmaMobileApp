package com.example.sungem.sungempharma.Others;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sungem.sungempharma.R;

public class MainSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnMedrep, btnAdmin;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_selection);
        setTitle("Select Login");

        btnMedrep = (Button) findViewById(R.id.btnMedrep);
        btnAdmin = (Button) findViewById(R.id.btnAdmin);

        btnMedrep.setOnClickListener(this);
        btnAdmin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        switch (view.getId()) {
            case R.id.btnMedrep: {
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("ACCESS", "medrep");
                editor.commit();
                break;
            }
            case R.id.btnAdmin: {
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("ACCESS", "admin");
                editor.commit();
                break;
            }
        }
        intent = new Intent(MainSelectionActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}