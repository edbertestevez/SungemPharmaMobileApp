package com.example.sungem.sungempharma.Medrep.StockLevel;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Admin.AdminMainActivity;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryAdapter;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryData;
import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.LoginActivity;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientStockFragment extends Fragment {

    DBController dbController;
    GlobalFunctions globalFunctions;
    ArrayList<ClientStockData> clientList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    View view;
    ListView listView;

    public ClientStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_stock, container, false);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        clientList = new ArrayList<ClientStockData>();
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        displayClientList();

        return view;
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        getActivity().registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayClientList();
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
            if(intent.hasExtra("MONITOR_UPDATE")){
                displayClientList();
            }

        }
    };

    public void displayClientList() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        clientList = dbController.getClientStock(sharedpref.getString("USERID",""));
        if(!clientList.isEmpty()) {
            listView = (ListView) view.findViewById(R.id.listview);

            Cursor cursor = dbController.getClientStockCursor(sharedpref.getString("USERID", ""));
            ClientStockAdapter clientStockAdapter = new ClientStockAdapter(getContext(), cursor);
            listView.setAdapter(clientStockAdapter);

        }
    }



}
