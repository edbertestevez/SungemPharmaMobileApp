package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

/**
 * Created by trebd on 8/25/2017.
 */

public class StockItemAdapter  extends CursorAdapter {

    DBController dbController;
    Context mainContext;

    public StockItemAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.prodlist_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String client_id = cursor.getString(cursor.getColumnIndexOrThrow("client_id"));
        final String pro_id = cursor.getString(cursor.getColumnIndexOrThrow("pro_id"));
        final String pro_brand = cursor.getString(cursor.getColumnIndexOrThrow("pro_brand"));
        final String pro_generic = cursor.getString(cursor.getColumnIndexOrThrow("pro_generic"));
        final String pro_formulation = cursor.getString(cursor.getColumnIndexOrThrow("pro_formulation"));

        final int total_qty = (dbController.getProductTotal(pro_id,client_id)) - dbController.getTransactionProductDeduct(pro_id,client_id);
        TextView txtProBrand = (TextView) view.findViewById(R.id.txtProBrand);
        TextView txtProGeneric = (TextView) view.findViewById(R.id.txtProGeneric);
        TextView txtProFormulation = (TextView) view.findViewById(R.id.txtProFormulation);
        TextView txtQty = (TextView) view.findViewById(R.id.txtProdQty);
        Button btnDetails = (Button) view.findViewById(R.id.btnDetails);


        txtProBrand.setText(pro_brand);
        txtProGeneric.setText(pro_generic);
        txtProFormulation.setText(pro_formulation);
        txtQty.setText(""+total_qty);

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainContext, StockItemLotActivity.class);
                intent.putExtra("CLIENT_ID",client_id);
                intent.putExtra("PRO_ID",pro_id);
                intent.putExtra("PRO_BRAND",pro_brand);
                intent.putExtra("PRO_GENERIC",pro_generic);
                intent.putExtra("PRO_FORMULATION",pro_formulation);
                intent.putExtra("TOTAL_QTY",total_qty);
                mainContext.startActivity(intent);
            }
        });
    }
}
