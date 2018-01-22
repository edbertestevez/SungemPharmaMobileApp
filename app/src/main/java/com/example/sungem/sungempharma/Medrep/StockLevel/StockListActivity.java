package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.Payment.PaymentTransactionData;
import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MainSelectionActivity;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

public class StockListActivity extends AppCompatActivity {

    String client_id, client_name, client_total;
    SimpleCursorAdapter clientStockAdapter;
    DBController dbController;
    GlobalFunctions globalFunctions;
    ArrayList<StockItemData> itemList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    ListView listProducts;
    FloatingActionButton floatMonitorBtn;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_list);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        client_id = bundle.getString("CLIENT_ID");
        client_name = bundle.getString("CLIENT_NAME");
        client_total = bundle.getString("CLIENT_TOTAL");

        (StockListActivity.this).getSupportActionBar().setTitle(client_name);

        dbController = new DBController(getApplicationContext());
        globalFunctions = new GlobalFunctions(getApplicationContext());

        displayItemsList();

        floatMonitorBtn = (FloatingActionButton) findViewById(R.id.floatMonitorBtn);
        floatMonitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StockListActivity.this, MonitorStockActivity.class);
                intent.putExtra("CLIENT_ID", client_id);
                intent.putExtra("CLIENT_NAME", client_name);
                intent.putExtra("CLIENT_TOTAL", client_total);
                startActivity(intent);
            }
        });

        serviceIntent = new Intent(this, SungemService.class);
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        startService(serviceIntent);
        registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayItemsList();
        super.onResume();
    }

    @Override
    public void onPause() {
        unregisterReceiver(serviceBroadcastReceiver);
        super.onPause();
    }

    private ServiceBroadcastReceiver serviceBroadcastReceiver = new ServiceBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("MONITOR_UPDATE")){
                displayItemsList();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayItemsList() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

            listProducts = (ListView) findViewById(R.id.listProducts);

            Cursor cursor = dbController.getStockItemListCursor(client_id);
            StockItemAdapter stockItemAdapter = new StockItemAdapter(this, cursor);
            listProducts.setAdapter(stockItemAdapter);

    }
}
