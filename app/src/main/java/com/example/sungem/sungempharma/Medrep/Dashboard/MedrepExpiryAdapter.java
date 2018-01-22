package com.example.sungem.sungempharma.Medrep.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.Medrep.StockLevel.StockItemLotActivity;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import org.w3c.dom.Text;

/**
 * Created by trebd on 10/3/2017.
 */

public class MedrepExpiryAdapter extends CursorAdapter {

    DBController dbController;
    Context mainContext;

    public MedrepExpiryAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.medrep_expiry_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String client_id = cursor.getString(cursor.getColumnIndexOrThrow("client_id"));
        final String pro_brand = cursor.getString(cursor.getColumnIndexOrThrow("pro_brand"));
        final String pro_generic = cursor.getString(cursor.getColumnIndexOrThrow("pro_generic"));
        final String pro_formulation = cursor.getString(cursor.getColumnIndexOrThrow("pro_formulation"));
        final String total_qty = cursor.getString(cursor.getColumnIndexOrThrow("qty"));
        final String lot_number = cursor.getString(cursor.getColumnIndexOrThrow("lot_number"));
        final String lot_expiry = cursor.getString(cursor.getColumnIndexOrThrow("lot_expiry"));

        final String client_name = dbController.getClientName(client_id);

        TextView txtProBrand = (TextView) view.findViewById(R.id.txtProBrand);
        TextView txtProGenericFormulation = (TextView) view.findViewById(R.id.txtProGenericFormulation);
        TextView txtQty = (TextView) view.findViewById(R.id.txtQty);
        TextView txtLot = (TextView) view.findViewById(R.id.txtLot);
        TextView txtExpiry = (TextView) view.findViewById(R.id.txtExpiry);
        TextView txtClient = (TextView) view.findViewById(R.id.txtClient);

        txtProBrand.setText(pro_brand);
        txtProGenericFormulation.setText(pro_generic+"("+pro_formulation+")");
        txtLot.setText(lot_number);
        txtQty.setText(total_qty);
        txtExpiry.setText(lot_expiry);
        txtClient.setText("Location: "+client_name);


    }
}

