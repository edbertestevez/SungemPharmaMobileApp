package com.example.sungem.sungempharma.Medrep.Delivery;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryMainFragment extends Fragment {

    private BottomNavigationViewEx.TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    String parameters;
    DBController dbController;
    GlobalFunctions globalFunctions;
    View view;
    Intent serviceIntent;

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public DeliveryMainFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delivery_main, container, false);
        mTabAdapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Delivery");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());

        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());


        //AFTER NI SIYA DELETE SANG TRANSACTON RECORD KA DELIVERY
        if (getArguments() != null) {
            if(getArguments().getInt("TABVALUE")==1){
                tabLayout.getTabAt(1).select();
            }else if(getArguments().getInt("TABVALUE")==0){
                tabLayout.getTabAt(0).select();
            }
        }

        setHasOptionsMenu(true);

        //ANG FLOATING ACTION BUTTON MAG UPLOAD MANUAL SANG SYNC DELIVERY
        FloatingActionButton fab = ((MedrepMainActivity) getActivity()).getFloatingActionButton();
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "UPLOAD DELIVERY CLICKED", Toast.LENGTH_SHORT).show();
                //UPLOAD TRANSACTIONS FUNCTION
                uploadDeliveryTransactions();

            }
        });

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
                syncDelivery();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        BottomNavigationViewEx.TabAdapter adapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        adapter.addFragment(new PendingDeliveryFragment(), "Pending Delivery");
        adapter.addFragment(new TransactionDeliveryFragment(), "Transactions");
        viewPager.setAdapter(adapter);
    }

    private void syncDelivery() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Synching Delivery Record....");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "delivery/display.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request
                            try {
                                //CLEAR SQLiteDelivery First
                                dbController.deleteOldDeliverySync(sharedpref.getString("USERID", ""));
                                dbController.deleteDeliveryItems(sharedpref.getString("USERID", ""));

                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                HashMap<String, String> deliveryValues = new HashMap<String, String>(); // delivery general
                                HashMap<String, String> itemValues = new HashMap<String, String>(); // delivery items
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);

                                    deliveryValues.put("delivery_id", obj.getString("delivery_id"));
                                    deliveryValues.put("order_id", obj.getString("order_id"));
                                    deliveryValues.put("client_name", obj.getString("client_name"));
                                    deliveryValues.put("client_address", obj.getString("client_address"));
                                    deliveryValues.put("medrep_id", sharedpref.getString("USERID", ""));
                                    //LOOP THROUGH ITEMS ARRAY
                                    JSONArray arrayProducts = new JSONArray(obj.getString("items"));
                                    for (int x = 0; x < arrayProducts.length(); x++) {
                                        JSONObject objItem = (JSONObject) arrayProducts.get(x);
                                        itemValues.put("delivery_id", obj.getString("delivery_id"));
                                        itemValues.put("product_id", objItem.getString("pro_id"));
                                        itemValues.put("product_name", objItem.getString("pro_brand") + " " + objItem.getString("pro_generic"));
                                        itemValues.put("lot_number", objItem.getString("lot_number"));
                                        itemValues.put("expiry_date", objItem.getString("expiry_date"));
                                        itemValues.put("quantity", objItem.getString("qty_total"));
                                        itemValues.put("medrep_id", obj.getString("medrep_id"));
                                        dbController.insertDeliveryItem(itemValues);
                                    }
                                    Log.i("RESPONSE: ", arrayProducts.toString());
                                    dbController.insertDeliverySync(deliveryValues);
                                }
                                //REPLACE CURRENT DISPLAY :D
                                (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content_frame, new DeliveryMainFragment()).commit();
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
        }else{
            Toast.makeText(getActivity(), "No Internet Connnection", Toast.LENGTH_SHORT).show();
        }
    }

    //UPLOAD UNSYNCED TRANSACTION TO THE SERVER
    private void uploadDeliveryTransactions() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading Delivery Transactions");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "delivery/uploadtransactions.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(parameters);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface confirmDialog, int which) {
                                confirmDialog.dismiss();
                            }
                        });
                        final AlertDialog confirmDialog = builder.create();
                        confirmDialog.show();*/
                            progressDialog.dismiss();
                            //response sang request
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //RESPONSE SANG SERVER
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Toast.makeText(getActivity(), obj.getString("delivery_id") + " [" + obj.getString("status") + "]", Toast.LENGTH_SHORT).show();

                                    //UPDATE delivery transaction status
                                    dbController.updateDeliverySyncStatus(obj.getString("delivery_id"), obj.getString("status"));
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

                    params.put("transaction_list", dbController.getUnsyncedDeliveryTransactions(sharedpref.getString("USERID", "")));
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    parameters = String.valueOf(params);
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
