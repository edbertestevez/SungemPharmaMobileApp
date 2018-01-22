package com.example.sungem.sungempharma.Medrep.Dashboard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Admin.Sales.ManagementData;
import com.example.sungem.sungempharma.Admin.Sales.ManagementListAdapter;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SalesMedrepActivity extends AppCompatActivity {

    String medrep_id;
    DBController dbController;
    GlobalFunctions globalFunctions;
    private SalesMedrepAdapter adapter;
    ListView listview;
    private ArrayList<SalesMedrepData> mSalesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_medrep);

        Calendar c = Calendar.getInstance();
        int curr_year = c.get(Calendar.YEAR);
        int curr_month = c.get(Calendar.MONTH);

        Bundle bundle = getIntent().getExtras();
        medrep_id = bundle.getString("MEDREP_ID");
        dbController = new DBController(getApplicationContext());
        globalFunctions = new GlobalFunctions(getApplicationContext());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Sales for the Month of "+globalFunctions.stringMonth(curr_month));
        //setTitle(medrep_id);
        listview = (ListView) findViewById(R.id.listview);


        mSalesList = new ArrayList<SalesMedrepData>();
        displaySales();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displaySales() {
        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(SalesMedrepActivity.this);
            progressDialog.setMessage("Retrieving Sales Info");
            progressDialog.show();


            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "medrep_sales_info.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Log.i("STRING REQUEST: ", obj.toString());
                                    String client_name = obj.getString("client_name");
                                    String payment_amt = obj.getString("payment_amt");
                                    String payment_means = obj.getString("payment_means");
                                    String payment_date = obj.getString("payment_date");

                                   mSalesList.add(new SalesMedrepData(client_name, payment_amt, payment_means,payment_date,medrep_id));
                                }
                                adapter = new SalesMedrepAdapter(getApplicationContext(), mSalesList);
                                listview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();


                                Toast.makeText(SalesMedrepActivity.this, "Record Complete", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(SalesMedrepActivity.this, "No Record Available", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(SalesMedrepActivity.this, "Syncing Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("medrep_id", medrep_id);
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(SalesMedrepActivity.this).addToRequestque(stringRequest);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SalesMedrepActivity.this);
            builder.setTitle("No internet connection");
            builder.setMessage("Please connect to a network");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
