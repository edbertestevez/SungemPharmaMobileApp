package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trebd on 8/25/2017.
 */

public class ClientStockAdapter  extends CursorAdapter {
    DBController dbController;
    Context mainContext;

    public ClientStockAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.stocklist_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String client_id = cursor.getString(cursor.getColumnIndexOrThrow("client_id"));
        final String client_name = cursor.getString(cursor.getColumnIndexOrThrow("client_name"));
        final String client_address = cursor.getString(cursor.getColumnIndexOrThrow("client_address"));
        final String consign_qty = cursor.getString(cursor.getColumnIndexOrThrow("consign_qty"));
        final int total_qty;

        TextView txtClientName = (TextView) view.findViewById(R.id.txtClientName);
        TextView txtClientAddress = (TextView) view.findViewById(R.id.txtClientAddress);
        TextView txtClientQty = (TextView) view.findViewById(R.id.txtClientQty);
        total_qty = Integer.parseInt(consign_qty) - dbController.getTransactionTotalDeduct(client_id);
        txtClientName.setText(client_name);
        txtClientAddress.setText(client_address);
        txtClientQty.setText(""+total_qty);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StockListActivity.class);
                intent.putExtra("CLIENT_ID",client_id);
                intent.putExtra("CLIENT_NAME",client_name);
                intent.putExtra("CLIENT_TOTAL",""+total_qty);
                context.startActivity(intent);

            }
        });
    }
}
