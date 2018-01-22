package com.example.sungem.sungempharma.Admin.Sales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import java.util.Calendar;

public class SalesReportActivity extends AppCompatActivity {

    String start_date, end_date;
    GlobalFunctions globalFunctions;
    String tab="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar c = Calendar.getInstance();
        int curr_year = c.get(Calendar.YEAR);
        int curr_month = c.get(Calendar.MONTH);

        globalFunctions = new GlobalFunctions(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("PAGE").equals("report")) {
            setTitle("Sales Report");
        }else if(bundle.getString("PAGE").equals("consign")) {
            setTitle("Consignment Sales for "+globalFunctions.stringMonth(curr_month)+" "+curr_year);
        }else if(bundle.getString("PAGE").equals("dashboard")){
            if(bundle.getString("TAB").equals("1")) {
                tab="1";
            }else{
                tab="0";
            }
            setTitle("Sales for "+globalFunctions.stringMonth(curr_month));
        }

        start_date = bundle.getString("START_DATE");
        end_date = bundle.getString("END_DATE");



        getSupportFragmentManager().beginTransaction().replace(R.id.report_frame, new SalesReportFragment()).commit();
    }

    public String tabSelected(){
        return tab;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getStartDate(){
        return start_date;
    }

    public String getEndDate(){
        return end_date;
    }
}
