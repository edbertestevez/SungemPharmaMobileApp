package com.example.sungem.sungempharma.Admin.Stocks;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryMainFragment;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectedStockActivity extends AppCompatActivity {

    String pro_id, pro_name, pro_desc, pro_qty;
    TextView txtProdName, txtDescription, txtWarehouse, txtOrdered, txtAvailable;
    ListView listAvailable;
    GlobalFunctions globalFunctions;
    private StockListAdapter adapter;
    private ArrayList<StockLotData> mLotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_stock);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Stocks Details");

        Bundle extras = getIntent().getExtras();
        pro_id = extras.getString("PRODUCT_ID");
        pro_name = extras.getString("PRODUCT_NAME");
        pro_desc = extras.getString("PRODUCT_DESC");
        pro_qty = extras.getString("PRODUCT_QTY");

        txtProdName = (TextView) findViewById(R.id.txtProdName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtWarehouse = (TextView) findViewById(R.id.txtWarehouse);
        txtOrdered = (TextView) findViewById(R.id.txtOrdered);
        txtAvailable = (TextView) findViewById(R.id.txtAvailable);
        listAvailable = (ListView) findViewById(R.id.listAvailable);
        globalFunctions = new GlobalFunctions(getApplicationContext());

        txtProdName.setText(pro_name);
        txtDescription.setText(pro_desc);

        if(Integer.parseInt(pro_qty)<0){
            txtAvailable.setText("("+pro_qty+")");
        }else{
            txtAvailable.setText(pro_qty);
        }



        mLotList = new ArrayList<StockLotData>();
        listAvailable.requestDisallowInterceptTouchEvent(true);
        getSelectedStock();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSelectedStock() {

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Retrieving data....");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.adminUrl() + "selected_stock.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);

                                    String product_id = obj.getString("pro_id");
                                    String lot_number = obj.getString("lot_number");
                                    String lot_qty = obj.getString("lot_qty");
                                    String lot_expiry = obj.getString("lot_expiry");

                                    txtWarehouse.setText(obj.getString("warehouse_total"));
                                    txtOrdered.setText(obj.getString("ordered_total"));

                                    if(!lot_number.equals("null")){
                                        mLotList.add(new StockLotData(product_id, lot_number, lot_qty, lot_expiry));
                                    }

                                    adapter = new StockListAdapter(getApplicationContext(), mLotList);
                                    listAvailable.setAdapter(adapter);
                                }



                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "No records available", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Syncing Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("pro_id", pro_id);
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
        }else{
            Toast.makeText(getApplicationContext(), "No Internet Connnection", Toast.LENGTH_SHORT).show();
        }
    }
}
