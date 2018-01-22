package com.example.sungem.sungempharma.Medrep.Dashboard;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.sungem.sungempharma.Medrep.StockLevel.StockItemAdapter;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockItemData;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

public class MedrepExpiryActivity extends AppCompatActivity {

    String medrep_id;
    DBController dbController;
    GlobalFunctions globalFunctions;
    private StockItemAdapter adapter;
    ListView listview_expiry;
    private ArrayList<StockItemData> mExpiryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medrep_expiry);

        Bundle bundle = getIntent().getExtras();
        medrep_id = bundle.getString("MEDREP_ID");
        dbController = new DBController(getApplicationContext());
        globalFunctions = new GlobalFunctions(getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Expiring Consigned Medicines");
        //setTitle(medrep_id);
        displayItemsList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayItemsList() {
        listview_expiry = (ListView) findViewById(R.id.listview_expiry);

        Cursor cursor = dbController.getExpiryStockItemListCursor(medrep_id);
        MedrepExpiryAdapter medrepExpiryAdapter = new MedrepExpiryAdapter(this, cursor);
        listview_expiry.setAdapter(medrepExpiryAdapter);

    }
}
