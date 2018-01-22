package com.example.sungem.sungempharma.Admin.Dashboard;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.sungem.sungempharma.Admin.Sales.SalesReportFragment;
import com.example.sungem.sungempharma.Others.BottomNavigationViewEx;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

public class AdminExpiryActivity extends AppCompatActivity {

    GlobalFunctions globalFunctions;
    //private ArrayList<RestockData> restockList;
    //private RestockAdapter adapter;
    private ListView listExpiry;
    String start_date, end_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Expiring Products");

        Bundle bundle = getIntent().getExtras();
        start_date = bundle.getString("START_DATE");
        end_date = bundle.getString("END_DATE");


        globalFunctions = new GlobalFunctions(getApplicationContext());
        getSupportFragmentManager().beginTransaction().replace(R.id.report_frame, new AdminExpiryFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
