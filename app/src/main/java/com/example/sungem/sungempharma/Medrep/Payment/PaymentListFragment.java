package com.example.sungem.sungempharma.Medrep.Payment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
public class PaymentListFragment extends Fragment {

    RecyclerView recyclerView;
    DBController dbController;
    GlobalFunctions globalFunctions;
    TextView txtError;
    ArrayList<PaymentData> invoiceList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public PaymentListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        invoiceList = new ArrayList<PaymentData>();
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        txtError = (TextView) view.findViewById(R.id.txtError);
        //Display list function
        displayInvoiceList();

        return view;
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        getActivity().registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayInvoiceList();
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
            if(intent.hasExtra("PAYMENT_UPDATE")){
                displayInvoiceList();
            }
        }
    };

    public void displayInvoiceList() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        invoiceList =  dbController.getAllInvoice(sharedpref.getString("USERID",""));
        if(!invoiceList.isEmpty()) {
            InvoiceAdapter adapter = new InvoiceAdapter(getActivity().getApplicationContext(), invoiceList);
            recyclerView.setAdapter(adapter);
            txtError.setText("");
        }else{
            txtError.setText("No Unpaid Invoice");
        }
    }
}
