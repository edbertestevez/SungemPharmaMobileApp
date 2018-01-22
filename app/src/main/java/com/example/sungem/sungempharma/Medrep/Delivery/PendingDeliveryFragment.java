package com.example.sungem.sungempharma.Medrep.Delivery;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingDeliveryFragment extends Fragment {

    RecyclerView recyclerView;
    DBController dbController;
    GlobalFunctions globalFunctions;
    TextView txtError;
    ArrayList<DeliveryData> deliveryList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public PendingDeliveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_delivery, container, false);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        deliveryList = new ArrayList<DeliveryData>();
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        txtError = (TextView) view.findViewById(R.id.txtError);
        //Display list function
        displayDeliveryList();


        return view;
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        getActivity().registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayDeliveryList();
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(serviceBroadcastReceiver);
        super.onPause();
    }

    private ServiceBroadcastReceiver serviceBroadcastReceiver = new ServiceBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            //CHECKER IF MAY UPDATE SA DELIVERY
            if(intent.hasExtra("DELIVERY_UPDATE")){
                displayDeliveryList();
            }

        }
    };

    public void displayDeliveryList() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        deliveryList =  dbController.getAllDelivery(sharedpref.getString("USERID",""));
        if(!deliveryList.isEmpty()) {
            DeliveryAdapter adapter = new DeliveryAdapter(getActivity().getApplicationContext(), deliveryList);
            recyclerView.setAdapter(adapter);
            txtError.setText("");
        }else{
            txtError.setText("No Pending Delivery");
        }
    }
}
