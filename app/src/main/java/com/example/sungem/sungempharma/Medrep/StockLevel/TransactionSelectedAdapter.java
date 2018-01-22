package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

/**
 * Created by trebd on 8/28/2017.
 */

public class TransactionSelectedAdapter extends CursorAdapter {
    DBController dbController;
    Context mainContext;

    public TransactionSelectedAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.monitor_transaction_selected_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String monitor_id = cursor.getString(cursor.getColumnIndexOrThrow("monitor_id"));
        final String pro_brand = cursor.getString(cursor.getColumnIndexOrThrow("pro_name"));
        final String lot_number = cursor.getString(cursor.getColumnIndexOrThrow("lot_number"));
        final String qty_sold = cursor.getString(cursor.getColumnIndexOrThrow("qty_sold"));
        final String qty_withdrawn = cursor.getString(cursor.getColumnIndexOrThrow("qty_withdrawn"));

        TextView txtProduct = (TextView) view.findViewById(R.id.txtProduct);
        TextView txtLot = (TextView) view.findViewById(R.id.txtDelivery);
        TextView txtSold = (TextView) view.findViewById(R.id.txtSold);
        TextView txtWithdrawn = (TextView) view.findViewById(R.id.txtWithdrawn);

        txtProduct.setText(pro_brand);
        txtLot.setText(lot_number);
        txtSold.setText(qty_sold);
        txtWithdrawn.setText(qty_withdrawn);
    }
}