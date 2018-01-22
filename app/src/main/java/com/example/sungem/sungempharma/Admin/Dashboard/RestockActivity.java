package com.example.sungem.sungempharma.Admin.Dashboard;

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
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestockActivity extends AppCompatActivity {

    GlobalFunctions globalFunctions;
    String restock_url;
    private ArrayList<RestockData> restockList;
    private RestockAdapter adapter;
    private ListView listRestock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Needs Restock Products");

        listRestock = (ListView) findViewById(R.id.listRestock);

        globalFunctions = new GlobalFunctions(getApplicationContext());
        restock_url = globalFunctions.adminUrl()+"restock_list.php";

        restockList = new ArrayList<RestockData>();

        getRestockList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getRestockList(){
        if(globalFunctions.isNetworkAvailable()==true) {
            //Connect to HTTPRequestToSendData and get result pfre
            StringRequest stringRequest = new StringRequest(Request.Method.POST, restock_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            try {
                                Log.e("Check : ",response);
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Based on php file
                                    String pro_brand = jsonObject.getString("pro_brand");
                                    String pro_generic = jsonObject.getString("pro_generic");
                                    String pro_formulation = jsonObject.getString("pro_formulation");
                                    String pro_total_qty = jsonObject.getString("pro_total_qty");
                                    String pro_reorder_level = jsonObject.getString("pro_reorder_level");
                                    String pending_orders = jsonObject.getString("pending_orders");
                                    String actual_remain = jsonObject.getString("actual_remain");

                                    restockList.add(new RestockData(pro_brand, pro_generic, pro_formulation, pro_total_qty, pro_reorder_level, pending_orders, actual_remain));

                                    adapter = new RestockAdapter(getApplicationContext(), restockList);
                                    listRestock.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RestockActivity.this, "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RestockActivity.this, "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                }
            }) {
                //PARAMETERS SA POST ACTION NA DI PRE
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    return params;
                }
            };
            //Send request sa singleton
            MySingleton.getInstance(RestockActivity.this).addToRequestque(stringRequest);
        }
        else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
