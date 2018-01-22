package com.example.sungem.sungempharma.Medrep.Delivery;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DeliveryItemActivity extends AppCompatActivity {

    TextView txtDeliveryId,txtClient,txtAddress;
    ArrayList<DeliveryItemData> itemList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    RecyclerView recyclerView;
    DBController dbController;
    String delivery_id, address;
    FloatingActionButton floatEditButton;
    String client_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_item);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Delivery Information");

        txtDeliveryId = (TextView) findViewById(R.id.txtDelivery);
        txtClient = (TextView) findViewById(R.id.txtClient);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        Bundle extras = getIntent().getExtras();
        delivery_id = extras.getString("DELIVERY_ID");
        address = extras.getString("ADDRESS");
        txtDeliveryId.setText(delivery_id);
        txtClient.setText(extras.getString("CLIENT"));

        client_name = extras.getString("CLIENT");

        floatEditButton = (FloatingActionButton) findViewById(R.id.floatEditButton);
        floatEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(DeliveryItemActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_delivery_edit, null);
                final EditText et_receipt = (EditText) mView.findViewById(R.id.et_receipt);
                Button btnDeliver = (Button) mView.findViewById(R.id.btnDeliver);
                Button btnClose = (Button) mView.findViewById(R.id.btnClose);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                //KUNG DELIVER ANG CLICK
                btnDeliver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(DeliveryItemActivity.this);
                        confirmBuilder.setMessage("Save Delivery Transaction");

                        final String strReceipt = et_receipt.getText().toString().trim();

                        if (strReceipt.isEmpty() || strReceipt.length() == 0 || strReceipt.equals("") || strReceipt == null) {
                            et_receipt.setError("Please enter goods receipt number");
                        } else {
                            confirmBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> deliveryTransactionValues = new HashMap<String, String>();
                                    deliveryTransactionValues.put("delivery_id", delivery_id);
                                    deliveryTransactionValues.put("delivery_status", "0");

                                    //GET CURRENT DATE
                                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    Date currDate = Calendar.getInstance().getTime();
                                    String formatDate = df.format(currDate);
                                    //GET CURRENT TIME
                                    SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
                                    Date currTime = Calendar.getInstance().getTime();
                                    String formatTime = tf.format(currTime);

                                    deliveryTransactionValues.put("delivery_date", formatDate);
                                    deliveryTransactionValues.put("delivery_time", formatTime);
                                    deliveryTransactionValues.put("medrep_id", sharedpref.getString("USERID",""));
                                    deliveryTransactionValues.put("or_number", strReceipt);
                                    deliveryTransactionValues.put("client_name", client_name);
                                    deliveryTransactionValues.put("address", address);
                                    dbController.insertDeliveryTransactionData(deliveryTransactionValues);

                                    Toast.makeText(DeliveryItemActivity.this, "Delivery No. "+delivery_id+"has been delivered", Toast.LENGTH_SHORT).show();

                                    //RELOAD App and proceed to delivery fragment
                                    Intent intent = new Intent(DeliveryItemActivity.this, MedrepMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("PAGE","delivery");
                                    startActivity(intent);

                                }
                            });
                            confirmBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            final AlertDialog alertDialog = confirmBuilder.create();
                            alertDialog.show();
                        }
                    }
                });
                //KUNG CLOSE ANG CLICK
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        if(extras.getString("ADDRESS").length()>55){
            txtAddress.setText(extras.getString("ADDRESS")+". . .");
        }else{
            txtAddress.setText(extras.getString("ADDRESS"));
        }


        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemList = new ArrayList<DeliveryItemData>();

        displayItemList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayItemList() {
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        itemList =  dbController.getDeliveryItems(delivery_id);
       DeliveryItemAdapter adapter = new DeliveryItemAdapter(getApplicationContext(), itemList);
        recyclerView.setAdapter(adapter);
    }
}
