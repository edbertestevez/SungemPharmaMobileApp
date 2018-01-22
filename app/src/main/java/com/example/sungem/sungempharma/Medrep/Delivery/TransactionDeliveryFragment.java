package com.example.sungem.sungempharma.Medrep.Delivery;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDeliveryFragment extends Fragment {

    RecyclerView recyclerView;
    DBController dbController;
    TextView txtError;
    ArrayList<DeliveryTransactionData> transactionList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public TransactionDeliveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_delivery, container, false);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        transactionList = new ArrayList<DeliveryTransactionData>();

        txtError = (TextView) view.findViewById(R.id.txtError);

        //Display transactions made sa phone delivery
        displayDeliveryTransactionList();
        return view;
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        getActivity().registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayDeliveryTransactionList();
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
                displayDeliveryTransactionList();
            }

        }
    };
/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            displayDeliveryTransactionList();
        }
    }
*/
    public void displayDeliveryTransactionList() {
        sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        transactionList =  dbController.getDeliveryTransaction(sharedpref.getString("USERID",""));
        if(!transactionList.isEmpty()) {
            DeliveryTransactionAdapter adapter = new DeliveryTransactionAdapter(getActivity().getApplicationContext(), transactionList);
            recyclerView.setAdapter(adapter);
            txtError.setText("");
        }else{
            txtError.setText("No Transactions Made");
        }
    }

}
