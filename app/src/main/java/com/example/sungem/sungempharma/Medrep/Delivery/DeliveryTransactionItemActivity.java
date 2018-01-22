package com.example.sungem.sungempharma.Medrep.Delivery;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DeliveryTransactionItemActivity extends AppCompatActivity {

    String delivery_id;
    ArrayList<DeliveryItemData> itemList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    RecyclerView recyclerView;
    DBController dbController;
    TextView txtDelivery, txtClient, txtAddress, txtDate, txtTime, txtOr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_transaction_item);

        txtDelivery = (TextView) findViewById(R.id.txtDelivery);
        txtClient = (TextView) findViewById(R.id.txtClient);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtOr = (TextView) findViewById(R.id.txtOr);

        Bundle extras = getIntent().getExtras();
        delivery_id = extras.getString("DELIVERY_ID");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Delivery No."+delivery_id);

        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemList = new ArrayList<DeliveryItemData>();

        txtDelivery.setText(delivery_id);

        displayTransactionDetail();
        displayDeliveryItem();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayDeliveryItem() {
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        itemList =  dbController.getDeliveryItems(delivery_id);
        DeliveryItemAdapter adapter = new DeliveryItemAdapter(getApplicationContext(), itemList);
        recyclerView.setAdapter(adapter);
    }

    public void displayTransactionDetail() {
        txtClient.setText(dbController.getDeliveryClient(delivery_id));
        txtDate.setText(dbController.getDeliveryDate(delivery_id));
        txtTime.setText(dbController.getDeliveryTime(delivery_id));
        txtOr.setText(dbController.getDeliveryOr(delivery_id));
        txtAddress.setText(dbController.getDeliveryAddress(delivery_id));
    }
}
