package com.example.sungem.sungempharma.Medrep.StockLevel;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionStockFragment extends Fragment {

    DBController dbController;
    GlobalFunctions globalFunctions;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    View view;
    ListView listMonitorTransaction;
    TextView txtError;

    public TransactionStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction_stock, container, false);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        txtError = (TextView) view.findViewById(R.id.txtError);
        displayMonitorTransaction();
        return view;
    }

    public void displayMonitorTransaction() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        listMonitorTransaction = (ListView) view.findViewById(R.id.listMonitorTransaction);

        Cursor cursor = dbController.getMonitorTransactionDataCursor(sharedpref.getString("USERID", ""));
        if (cursor.moveToFirst()) {
            txtError.setText("");
        }else{
            txtError.setText("No Monitor Transactions");
        }

        TransactionStockAdapter transactionStockAdapter = new TransactionStockAdapter(getContext(), cursor);
        listMonitorTransaction.setAdapter(transactionStockAdapter);

    }

}
