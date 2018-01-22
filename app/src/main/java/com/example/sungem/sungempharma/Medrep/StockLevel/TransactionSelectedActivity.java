package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import org.w3c.dom.Text;

public class TransactionSelectedActivity extends AppCompatActivity {

    String client_id, client_name, monitor_id, monitor_date;
    DBController dbController;
    GlobalFunctions globalFunctions;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    ListView listTransactionItems;
    TextView txtClient, txtDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_selected);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        client_id = bundle.getString("CLIENT_ID");
        client_name = bundle.getString("CLIENT_NAME");
        monitor_id = bundle.getString("MONITOR_ID");
        monitor_date = bundle.getString("DATE");

        txtClient = (TextView) findViewById(R.id.txtClient);
        txtDate = (TextView) findViewById(R.id.txtDate);

        txtClient.setText(client_name);
        txtDate.setText(monitor_date);

        this.getSupportActionBar().setTitle("Monitor #"+monitor_id);

        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getApplicationContext());
        globalFunctions = new GlobalFunctions(getApplicationContext());

        displaySelectedTransactionProducts();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displaySelectedTransactionProducts() {
        listTransactionItems = (ListView) findViewById(R.id.listTransactionItems);

        Cursor cursor = dbController.getSelectedTransactionItemsCursor(monitor_id);
        TransactionSelectedAdapter transactionSelectedAdapter = new TransactionSelectedAdapter(this, cursor);
        listTransactionItems.setAdapter(transactionSelectedAdapter);
    }
}
