package com.example.sungem.sungempharma.Medrep.Payment;


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
import android.widget.TextView;

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
public class TransactionPaymentFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    DBController dbController;
    GlobalFunctions globalFunctions;
    TextView txtError;
    ArrayList<PaymentTransactionData> paymentList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public TransactionPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction_payment, container, false);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        paymentList = new ArrayList<PaymentTransactionData>();
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        txtError = (TextView) view.findViewById(R.id.txtError);

        displayPaymentTransactionList();

        return view;
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        getActivity().registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayPaymentTransactionList();
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
            //CHECKER IF MAY UPDATE SA PAYMENT
            if(intent.hasExtra("PAYMENT_UPLOAD_UPDATE")){
                displayPaymentTransactionList();
            }
        }
    };


    public void displayPaymentTransactionList() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        paymentList =  dbController.getAllPaymentTransactions(sharedpref.getString("USERID",""));
        if(!paymentList.isEmpty()) {
            PaymentTransactionAdapter adapter = new PaymentTransactionAdapter(getActivity().getApplicationContext(), paymentList);
            recyclerView.setAdapter(adapter);
            txtError.setText("");
        }else{
            txtError.setText("No Payment Transactions");
        }
    }

}
