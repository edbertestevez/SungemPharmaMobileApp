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

public class MonitorStockAdapter extends CursorAdapter{
    DBController dbController;
    Context mainContext;

    public MonitorStockAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.monitor_stock_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String client_id = cursor.getString(cursor.getColumnIndexOrThrow("client_id"));
        final String pro_id = cursor.getString(cursor.getColumnIndexOrThrow("pro_id"));
        final String lot_number = cursor.getString(cursor.getColumnIndexOrThrow("lot_number"));
        final String lot_expiry = cursor.getString(cursor.getColumnIndexOrThrow("lot_expiry"));
        final String qty = cursor.getString(cursor.getColumnIndexOrThrow("qty"));
        final String pro_brand = cursor.getString(cursor.getColumnIndexOrThrow("pro_brand"));
        final String pro_generic = cursor.getString(cursor.getColumnIndexOrThrow("pro_generic"));
        final String pro_formulation = cursor.getString(cursor.getColumnIndexOrThrow("pro_formulation"));
        int remain_deducted = Integer.parseInt(qty) - dbController.getClientLotDeduct(lot_number, client_id);

        TextView txtProduct = (TextView) view.findViewById(R.id.txtProduct);
        TextView txtProId = (TextView) view.findViewById(R.id.txtProId);
        TextView txtLot = (TextView) view.findViewById(R.id.txtDelivery);
        TextView txtExpiry = (TextView) view.findViewById(R.id.txtLotExpiry);
        TextView txtRemain = (TextView) view.findViewById(R.id.txtRemain);

        txtProId.setText(pro_id);
        txtProduct.setText(pro_brand);
        txtLot.setText(lot_number);
        txtExpiry.setText(lot_expiry);
        txtRemain.setText(""+remain_deducted);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
