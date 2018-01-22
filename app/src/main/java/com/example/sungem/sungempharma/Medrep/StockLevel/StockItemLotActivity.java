package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

public class StockItemLotActivity extends AppCompatActivity {

    String pro_id, pro_brand, pro_generic, pro_formulation, client_id;
    int total_qty;
    TextView txtProdName,txtFormulation, txtTotal;
    SimpleCursorAdapter clientStockAdapter;
    DBController dbController;
    GlobalFunctions globalFunctions;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    ListView lotList;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_item_lot);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtProdName = (TextView) findViewById(R.id.txtProdName);
        txtFormulation = (TextView) findViewById(R.id.txtFormulation);
        txtTotal = (TextView) findViewById(R.id.txtTotal);

        Bundle bundle = getIntent().getExtras();
        client_id = bundle.getString("CLIENT_ID");
        pro_id = bundle.getString("PRO_ID");
        pro_brand = bundle.getString("PRO_BRAND");
        pro_generic = bundle.getString("PRO_GENERIC");
        pro_formulation = bundle.getString("PRO_FORMULATION");
        total_qty = bundle.getInt("TOTAL_QTY");

        (StockItemLotActivity.this).getSupportActionBar().setTitle(pro_brand);
        txtProdName.setText(pro_brand+" "+pro_generic);
        txtFormulation.setText(pro_formulation);
        txtTotal.setText(""+total_qty);

        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getApplicationContext());
        globalFunctions = new GlobalFunctions(getApplicationContext());

        displayLotItems();

        serviceIntent = new Intent(this, SungemService.class);
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        startService(serviceIntent);
        registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayLotItems();
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
                displayLotItems();
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

    public void displayLotItems() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

            lotList = (ListView) findViewById(R.id.lotList);

            Cursor cursor = dbController.getSelectedProductLotListCursor(pro_id, client_id);
            StockItemLotAdapter stockItemLotAdapter = new StockItemLotAdapter(this, cursor);
            lotList.setAdapter(stockItemLotAdapter);
        }
}
