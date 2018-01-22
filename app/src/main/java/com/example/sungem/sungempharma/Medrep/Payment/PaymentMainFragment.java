package com.example.sungem.sungempharma.Medrep.Payment;


import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMainFragment extends Fragment {

    private BottomNavigationViewEx.TabAdapter mTabAdapter;
    private ViewPager mViewPager;

    DBController dbController;
    GlobalFunctions globalFunctions;
    View view;

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public PaymentMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_payment_main, container, false);
        mTabAdapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Payments");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());

        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            if(getArguments().getInt("TABVALUE")==1){
                tabLayout.getTabAt(1).select();
            }else if(getArguments().getInt("TABVALUE")==0){
                tabLayout.getTabAt(0).select();
            }
        }

        //ANG FLOATING ACTION BUTTON MAG UPLOAD MANUAL SANG SYNC PAYMENT
        FloatingActionButton fab = ((MedrepMainActivity) getActivity()).getFloatingActionButton();
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "UPLOAD PAYMENT CLICKED", Toast.LENGTH_SHORT).show();
                //UPLOAD TRANSACTIONS FUNCTION
                uploadPaymentTransactions();

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
                syncPayment();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager){
        BottomNavigationViewEx.TabAdapter adapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        adapter.addFragment(new PaymentListFragment(),"Payment List");
        adapter.addFragment(new TransactionPaymentFragment(),"Transactions");
        viewPager.setAdapter(adapter);
    }

    private void syncPayment() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Synching Payment Record....");
            progressDialog.show();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "payment/display.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request

                            try {
                                //CLEAR SQLiteDelivery First
                                dbController.deleteOldPaymentSync(sharedpref.getString("USERID", ""));
                                dbController.deleteInvoicePayment(sharedpref.getString("USERID", ""));


                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                HashMap<String, String> invoiceValues = new HashMap<String, String>(); // invoice general
                                HashMap<String, String> paymentValues = new HashMap<String, String>(); // invoicepayments
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Log.i("STRING REQUEST: ", obj.toString());
                                    invoiceValues.put("invoice_id", obj.getString("invoice_id"));
                                    invoiceValues.put("client_name", obj.getString("client_name"));
                                    invoiceValues.put("amount_total", obj.getString("amount_total"));
                                    invoiceValues.put("amount_paid", obj.getString("amount_paid"));
                                    invoiceValues.put("amount_remaining", obj.getString("amount_remaining"));
                                    invoiceValues.put("date_due", obj.getString("date_due"));
                                    invoiceValues.put("medrep_id", sharedpref.getString("USERID", ""));

                                    JSONArray arrayPayments = obj.getJSONArray("payments");
                                    for (int x = 0; x < arrayPayments.length(); x++) {
                                        JSONObject objItem = (JSONObject) arrayPayments.get(x);
                                        paymentValues.put("invoice_id", obj.getString("invoice_id"));
                                        paymentValues.put("paymode_name", objItem.getString("paymode_name"));
                                        paymentValues.put("payment_amount", objItem.getString("payment_amount"));
                                        paymentValues.put("payment_date", objItem.getString("payment_date"));
                                        paymentValues.put("medrep_id", sharedpref.getString("USERID", ""));
                                        dbController.insertInvoicePayment(paymentValues);
                                    }
                                    dbController.insertInvoiceSync(invoiceValues);
                                }
                                //REPLACE CURRENT DISPLAY :D
                                (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content_frame, new PaymentMainFragment()).commit();
                                Toast.makeText(getActivity(), "Syncing Complete", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "No unpaid invoice records available", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    //UPLOAD UNSYNCED TRANSACTION TO THE SERVER
    private void uploadPaymentTransactions() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Uploading Payment Transactions");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "payment/uploadtransactions.php",
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

                                    //UPDATE payment transaction status
                                    dbController.updatePaymentSyncStatus(obj.getString("primary_id"), obj.getString("invoice_id"), obj.getString("status"));
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

                    params.put("transaction_list", dbController.getUnsyncedPaymentTransactions(sharedpref.getString("USERID", "")));
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
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
