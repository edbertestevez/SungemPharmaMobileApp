package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.Intent;
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

public class StockItemLotAdapter  extends CursorAdapter {

    DBController dbController;
    Context mainContext;

    public StockItemLotAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.prodlist_lot_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String client_id = cursor.getString(cursor.getColumnIndexOrThrow("client_id"));
        final String lot_number = cursor.getString(cursor.getColumnIndexOrThrow("lot_number"));
        final String lot_expiry = cursor.getString(cursor.getColumnIndexOrThrow("lot_expiry"));
        final String lot_qty = cursor.getString(cursor.getColumnIndexOrThrow("qty"));

        final int total_qty = Integer.parseInt(lot_qty) - dbController.getClientLotDeduct(lot_number, client_id);

        TextView txtLotNumber = (TextView) view.findViewById(R.id.txtLotNumber);
        TextView txtLotExpiry = (TextView) view.findViewById(R.id.txtLotExpiry);
        TextView txtLotQty = (TextView) view.findViewById(R.id.txtLotQty);

        txtLotNumber.setText(lot_number);
        txtLotExpiry.setText(lot_expiry);
        txtLotQty.setText(""+total_qty);

    }
}
