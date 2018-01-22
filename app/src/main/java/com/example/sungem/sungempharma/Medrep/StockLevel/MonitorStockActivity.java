package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MonitorStockActivity extends AppCompatActivity {

    String client_id, client_name, client_total;
    DBController dbController;
    TextView txtClient, txtQty;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    ListView listMonitor;
    Button btnSave;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_stock);

        this.getSupportActionBar().setTitle("Monitor Consignment");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtClient = (TextView) findViewById(R.id.txtClient);
        txtQty = (TextView) findViewById(R.id.txtQty);
        listMonitor = (ListView) findViewById(R.id.listMonitor);
        btnSave = (Button) findViewById(R.id.btnSave);

        Bundle bundle = getIntent().getExtras();
        client_id = bundle.getString("CLIENT_ID");
        client_name = bundle.getString("CLIENT_NAME");
        client_total = bundle.getString("CLIENT_TOTAL");

        txtClient.setText(client_name);
        txtQty.setText(client_total);

        dbController = new DBController(getApplicationContext());
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        displayMonitorStock();

        serviceIntent = new Intent(this, SungemService.class);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v;

                int listLength = listMonitor.getChildCount();
                int ctrError=0;

                String[] errorArray = new String[listLength];
                final String[] productArray = new String[listLength];
                final String[] lotArray = new String[listLength];
                final String[] soldArray = new String[listLength];
                final String[] withdrawArray = new String[listLength];
                final String[] productNameArray = new String[listLength];


                for (int i = 0; i < listLength; i++)
                {
                    v = listMonitor.getChildAt(i);
                    TextView txtProId = (TextView) v.findViewById(R.id.txtProId);
                    TextView txtProname = (TextView) v.findViewById(R.id.txtProduct);
                    TextView txtLot = (TextView) v.findViewById(R.id.txtDelivery);
                    TextView txtExpiry = (TextView) v.findViewById(R.id.txtLotExpiry);
                    TextView txtRemain = (TextView) v.findViewById(R.id.txtRemain);
                    EditText etSold = (EditText) v.findViewById(R.id.etSold);
                    EditText etWithdraw = (EditText) v.findViewById(R.id.etWithdraw);

                    int intRemain;
                    int intWithdraw;
                    int intSold;


                    if(etSold.getText().toString().equals("") || etSold.getText().toString().isEmpty()){
                        intSold = 0;
                    }else{
                        intSold = Integer.parseInt(etSold.getText().toString());
                    }

                    if(etWithdraw.getText().toString().equals("") || etWithdraw.getText().toString().isEmpty()){
                        intWithdraw = 0;
                    }else{
                        intWithdraw = Integer.parseInt(etWithdraw.getText().toString());
                    }

                    intRemain = Integer.parseInt(txtRemain.getText().toString());

                    //CHAKTO TANAN SA MO NI NA ROW (INSERT SA ARRAY) gwa dialog sang errors
                    if(intRemain>=(intSold + intWithdraw)){
                        //INSERT SA ARRAY
                        errorArray[i]="";
                        productArray[i] = txtProId.getText().toString();
                        lotArray[i] = txtLot.getText().toString();
                        soldArray[i] = String.valueOf(intSold);
                        productNameArray[i] = txtProname.getText().toString();
                        withdrawArray[i]= String.valueOf(intWithdraw);
                    }else{
                        errorArray[i] = txtProname.getText().toString()+" ["+txtLot.getText().toString()+"] exceeds by ("+(intRemain-(intSold + intWithdraw))+")";
                        ctrError++;
                    }
                }

                //IF MAY ERROR
                if(ctrError>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Please check");

                    String errorMessage="";
                    for (int i = 0; i < errorArray.length; i++) {
                        if(!(errorArray[i].equals("")||errorArray[i].equals(null))){
                            errorMessage+=errorArray[i]+"\n";
                        }
                    }
                    builder.setMessage(errorMessage)
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.create();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{

                    //CONFIRMATION
                    AlertDialog.Builder builder = new AlertDialog.Builder(MonitorStockActivity.this);
                    builder.setMessage("Save Monitor Transaction");

                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface confirmDialog, int which) {
                            HashMap<String, String> transactionValue = new HashMap<String, String>();

                            //INSERT 1 MAIN MONITOR RECORD
                            String monitor_id;

                            if(dbController.getNewMonitorId().equals("0")){
                                monitor_id = "1";
                            }else{
                                monitor_id = dbController.getNewMonitorId();
                            }
                            transactionValue.put("monitor_id",monitor_id ); //MAX ID +1
                            transactionValue.put("client_id", client_id);
                            transactionValue.put("client_name", client_name);
                            //GET CURRENT DATE
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            Date currDate = Calendar.getInstance().getTime();
                            String monitor_date = df.format(currDate);
                            //GET CURRENT TIME
                            SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
                            Date currTime = Calendar.getInstance().getTime();
                            String monitor_time = tf.format(currTime);

                            transactionValue.put("monitor_date", monitor_date);
                            transactionValue.put("monitor_time", monitor_time);
                            transactionValue.put("medrep_id", sharedpref.getString("USERID",""));
                            dbController.insertMonitorMain(transactionValue);

                            //INSERT ALL ITEMS SANG MONITOR
                            HashMap<String, String> itemValue = new HashMap<String, String>();
                            for (int i = 0; i < productArray.length; i++) {
                                itemValue.put("monitor_id",monitor_id ); //MAX ID +1
                                itemValue.put("pro_id", productArray[i]);
                                itemValue.put("lot_number", lotArray[i]);
                                itemValue.put("qty_sold", soldArray[i]);
                                itemValue.put("qty_withdrawn", withdrawArray[i]);
                                itemValue.put("client_id", client_id);
                                itemValue.put("medrep_id", sharedpref.getString("USERID",""));
                                itemValue.put("monitor_date", monitor_date);
                                itemValue.put("monitor_time", monitor_time);
                                itemValue.put("pro_name", productNameArray[i]);
                                dbController.insertMonitorItems(itemValue);

                            }

                            Toast.makeText(MonitorStockActivity.this, "Monitor #"+monitor_id+" Added", Toast.LENGTH_SHORT).show();
                            confirmDialog.dismiss();
                            //RELOAD App and proceed to delivery fragment
                            Intent intent = new Intent(MonitorStockActivity.this, MedrepMainActivity.class);
                            intent.putExtra("PAGE","stocklevel");
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface confirmDialog, int which) {
                            confirmDialog.dismiss();
                        }
                    });
                    final AlertDialog confirmDialog = builder.create();
                    confirmDialog.show();
                }

                }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //SYNC SANG CURRENT IF MAY NAG CHANGE GULPI

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        startService(serviceIntent);
        registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayMonitorStock();
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
                displayMonitorStock();
            }

        }
    };

    public void displayMonitorStock() {
        listMonitor = (ListView) findViewById(R.id.listMonitor);

        Cursor cursor = dbController.getMonitorItemListCursor(client_id);
        MonitorStockAdapter monitorStockAdapter = new MonitorStockAdapter(this, cursor);
        listMonitor.setAdapter(monitorStockAdapter);
    }
}
