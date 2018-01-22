package com.example.sungem.sungempharma.Medrep.StockLevel;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Others.BottomNavigationViewEx;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockLevelMainFragment extends Fragment {

    private BottomNavigationViewEx.TabAdapter mTabAdapter;
    private ViewPager mViewPager;

    DBController dbController;
    GlobalFunctions globalFunctions;
    View view;
    String parameter;

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public StockLevelMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_stock_level_main, container, false);
        mTabAdapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Stock Level Inventory");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());

        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());
        setHasOptionsMenu(true);

        //ANG FLOATING ACTION BUTTON MAG UPLOAD MANUAL SANG SYNC PAYMENT
        FloatingActionButton fab = ((MedrepMainActivity) getActivity()).getFloatingActionButton();
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "UPLOAD MONITORING CLICKED", Toast.LENGTH_SHORT).show();
                //UPLOAD TRANSACTIONS FUNCTION
                uploadMonitorTransactions();
            }
        });

        if (getArguments() != null) {
            if(getArguments().getInt("TABVALUE")==1){
                tabLayout.getTabAt(1).select();
            }else if(getArguments().getInt("TABVALUE")==0){
                tabLayout.getTabAt(0).select();
            }
        }

        return view;
    }

    //BUTTONS SA MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.activity_medrep_main_actions, menu);
        super.onCreateOptionsMenu(menu, menuInflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sync: {
                syncStockLevel();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager){
        BottomNavigationViewEx.TabAdapter adapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        adapter.addFragment(new ClientStockFragment(),"Client Stocks");
        adapter.addFragment(new TransactionStockFragment(),"Transactions");
        viewPager.setAdapter(adapter);
    }

    private void syncStockLevel() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        //DIALOG BAR MAG LOADING
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Synching Stock Level Inventory Record....");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "stocklevel/display.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        //response sang request
                        try {
                            //CLEAR SQLiteDelivery First
                            dbController.deleteOldClientStock(sharedpref.getString("USERID",""));
                            dbController.deleteOldStockItems(sharedpref.getString("USERID",""));

                            JSONArray jsonArray = new JSONArray(response);
                            //Log.i("RESPONSE: ",jsonArray.toString());
                            HashMap<String, String> stockLevelGeneral = new HashMap<String, String>(); // stocklevel general
                            HashMap<String, String> itemValues = new HashMap<String, String>(); // stocklevel items
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = (JSONObject) jsonArray.get(i);

                                stockLevelGeneral.put("client_id", obj.getString("client_id"));
                                stockLevelGeneral.put("client_name", obj.getString("client_name"));
                                stockLevelGeneral.put("client_address", obj.getString("client_address"));
                                stockLevelGeneral.put("consign_qty", obj.getString("consign_qty"));
                                stockLevelGeneral.put("medrep_id", sharedpref.getString("USERID",""));
                                //LOOP THROUGH PRODUCTS ARRAY
                                JSONArray arrayProducts = new JSONArray(obj.getString("products"));

                                for (int x = 0; x < arrayProducts.length(); x++){
                                    JSONObject objItem = (JSONObject) arrayProducts.get(x);
                                    itemValues.put("client_id", obj.getString("client_id")); //halin sa value sa babaw
                                    itemValues.put("pro_id",objItem.getString("pro_id"));
                                    itemValues.put("pro_brand",objItem.getString("pro_brand"));
                                    itemValues.put("pro_generic",objItem.getString("pro_generic"));
                                    itemValues.put("pro_formulation",objItem.getString("pro_formulation"));
                                    itemValues.put("lot_number",objItem.getString("lot_number"));
                                    itemValues.put("expiry_date",objItem.getString("expiry_date"));
                                    itemValues.put("quantity", objItem.getString("quantity"));
                                    itemValues.put("medrep_id", sharedpref.getString("USERID",""));
                                    dbController.insertStockItem(itemValues);
                                }

                                dbController.insertClientStock(stockLevelGeneral);
                            }
                            //REPLACE CURRENT DISPLAY :D
                            (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content_frame, new StockLevelMainFragment()).commit();
                            Toast.makeText(getActivity(), "Syncing Complete", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            Toast.makeText(getActivity().getApplicationContext(), "No records available", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display records after response
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Syncing Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

                params.put("action", "getlist");
                params.put("medrep_id", sharedpref.getString("USERID", ""));
                return params;
            }
        };
        //Send request
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);
    }



    //UPLOAD UNSYNCED TRANSACTION TO THE SERVER --> UPDATE NI INTO MONITOR STOCK NA CONTENT
    private void uploadMonitorTransactions() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading Monitoring Transactions");
            progressDialog.show();

            if(dbController.getUnsyncedMonitorTransactions(sharedpref.getString("USERID", "")).equals("")){
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("NO UNSYNCED TRANSACTIONS");

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface confirmDialog, int which) {
                        confirmDialog.dismiss();
                    }
                });
                final AlertDialog confirmDialog = builder.create();
                confirmDialog.show();
            }

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "stocklevel/uploadtransactions.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //RESPONSE SANG SERVER
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Toast.makeText(getActivity(), obj.getString("monitor_id") + " [" + obj.getString("status") + "]", Toast.LENGTH_SHORT).show();

                                    //UPDATE delivery transaction status
                                    dbController.updateMonitoringSyncStatus(obj.getString("monitor_id"), obj.getString("status"));

                                }
                                Toast.makeText(getActivity(), "Uploading Complete", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "An error occured", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Uploading Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

                    params.put("transaction_list", dbController.getUnsyncedMonitorTransactions(sharedpref.getString("USERID", "")));

                    parameter = String.valueOf(params);
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
