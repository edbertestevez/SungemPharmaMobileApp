package com.example.sungem.sungempharma.Admin.Dashboard;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Admin.Sales.ManagementData;
import com.example.sungem.sungempharma.Medrep.Dashboard.MedrepExpiryAdapter;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockItemAdapter;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockItemData;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsignExpiryFragment extends Fragment {
    View view;
    GlobalFunctions globalFunctions;
    private ConsignExpiryAdapter adapter;
    ListView listview_expiry;
    private ArrayList<ConsignExpiryData> expiryList;

    public ConsignExpiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_consign_expiry, container, false);
        globalFunctions = new GlobalFunctions(getContext());

        listview_expiry = (ListView) view.findViewById(R.id.listview_expiry);
        expiryList = new ArrayList<ConsignExpiryData>();

        displayItemsList();

        return view;
    }

    public void displayItemsList() {
        if(globalFunctions.isNetworkAvailable()==true) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Retrieving data....");
            progressDialog.show();
            //Connect to HTTPRequestToSendData and get result pfre
            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.adminUrl()+"expiry_consign.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
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
                                    String lot_number = jsonObject.getString("lot_number");
                                    String lot_qty = jsonObject.getString("lot_qty");
                                    String lot_expiry = jsonObject.getString("lot_expiry");
                                    String days_remain = jsonObject.getString("days_remain");
                                    String client_name = jsonObject.getString("client_name");

                                    expiryList.add(new ConsignExpiryData(pro_brand, pro_generic, pro_formulation, lot_qty, client_name, lot_number, lot_expiry, days_remain));

                                    adapter = new ConsignExpiryAdapter(getContext(), expiryList);
                                    listview_expiry.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
            MySingleton.getInstance(getContext()).addToRequestque(stringRequest);
        }
        else{
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
