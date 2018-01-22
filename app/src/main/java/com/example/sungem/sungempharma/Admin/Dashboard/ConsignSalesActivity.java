package com.example.sungem.sungempharma.Admin.Dashboard;

import android.app.ProgressDialog;
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

public class ConsignSalesActivity extends AppCompatActivity {

    String start_date, end_date;
    GlobalFunctions globalFunctions;
    private ArrayList<ConsignSalesData> consignList;
    private ConsignSalesAdapter adapter;
    ListView listSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consign_sales);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Consignment Sales");

        Bundle bundle = getIntent().getExtras();
        start_date = bundle.getString("START_DATE");
        end_date = bundle.getString("END_DATE");

        Calendar c = Calendar.getInstance();
        int curr_year = c.get(Calendar.YEAR);
        int curr_month = c.get(Calendar.MONTH);

        consignList = new ArrayList<ConsignSalesData>();
        listSales = (ListView) findViewById(R.id.list_sales);

        globalFunctions = new GlobalFunctions(getApplicationContext());
        setTitle("Consignment Sales for "+globalFunctions.stringMonth(curr_month));

        getConsignmentSales();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getConsignmentSales(){
        if(globalFunctions.isNetworkAvailable()==true) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(ConsignSalesActivity.this);
            progressDialog.setMessage("Retrieving data....");
            progressDialog.show();
            //Connect to HTTPRequestToSendData and get result pfre
            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.adminUrl()+"consign_sales.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            progressDialog.dismiss();
                            try {
                                Log.e("Check : ",response);
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Based on php file
                                    String pro_brand = jsonObject.getString("pro_brand");
                                    String pro_generic = jsonObject.getString("pro_generic");
                                    String pro_formulation = jsonObject.getString("pro_formulation");
                                    String pro_qty = jsonObject.getString("qty_sold");
                                    String pro_sales = jsonObject.getString("pro_sales");

                                    consignList.add(new ConsignSalesData(pro_brand, pro_generic, pro_formulation, pro_qty, pro_sales));

                                    adapter = new ConsignSalesAdapter(getApplicationContext(), consignList);
                                    listSales.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                //PARAMETERS SA POST ACTION NA DI PRE
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("start",  start_date);
                    params.put("end", end_date);
                    return params;
                }
            };
            //Send request sa singleton
            MySingleton.getInstance(ConsignSalesActivity.this).addToRequestque(stringRequest);
        }
        else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
