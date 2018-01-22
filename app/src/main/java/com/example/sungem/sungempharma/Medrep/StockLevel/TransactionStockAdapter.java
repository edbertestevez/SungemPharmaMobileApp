package com.example.sungem.sungempharma.Medrep.StockLevel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.Payment.PaymentMainFragment;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

/**
 * Created by trebd on 8/28/2017.
 */

public class TransactionStockAdapter extends CursorAdapter {
    DBController dbController;
    Context mainContext;
    String popupmessage;
    String sync_text;
    public TransactionStockAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mainContext = context;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        dbController = new DBController(context.getApplicationContext());
        return LayoutInflater.from(context).inflate(R.layout.monitor_transaction_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final String monitor_id = cursor.getString(cursor.getColumnIndexOrThrow("monitor_id"));
        final String medrep_id = cursor.getString(cursor.getColumnIndexOrThrow("medrep_id"));
        final String client_name = dbController.getClientName(cursor.getString(cursor.getColumnIndexOrThrow("client_id")));
        final String client_id = cursor.getString(cursor.getColumnIndexOrThrow("client_id"));
        final String transaction_date = cursor.getString(cursor.getColumnIndexOrThrow("monitor_date"));
        final String transaction_time = cursor.getString(cursor.getColumnIndexOrThrow("monitor_time"));
        final String sync_status = cursor.getString(cursor.getColumnIndexOrThrow("sync_status"));


        TextView txtClient = (TextView) view.findViewById(R.id.txtClient);
        TextView txtDate = (TextView) view.findViewById(R.id.txtDate);
        final TextView txtSyncStatus = (TextView) view.findViewById(R.id.txtSyncStatus);
        final ImageView imgStatus =(ImageView) view.findViewById(R.id.imgStatus);
        final ImageView btnPopup = (ImageView) view.findViewById(R.id.btnPopup);
        Button btnView = (Button) view.findViewById(R.id.btnView);

        //final ImageButton btnDelete = (ImageButton) view.findViewById(R.id.btnDelete);


        txtClient.setText(client_name);
        txtDate.setText(transaction_date);
        if(sync_status.equals("0")){
            sync_text = "NOT SYNCED";
            txtSyncStatus.setText(sync_text);
            imgStatus.setImageResource(R.drawable.ic_error_outline_black_24dp);

        }else{
            sync_text = "SYNCED";
            txtSyncStatus.setText(sync_text);
            imgStatus.setImageResource(R.drawable.ic_save_black_24dp);
            //btnDelete.setVisibility(View.INVISIBLE);

        }


        btnPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, btnPopup);

                if(sync_status.equals("0")){
                    popupmessage = "Delete Monitor Transaction";
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_update_delete, popup.getMenu());
                }else{
                    popupmessage = "Remove transaction from list?";
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_remove, popup.getMenu());
                }

                popup.setGravity(Gravity.END);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.menu_delete:{
                                    //DIALOG KUNG I DELETE OR INDI
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage(popupmessage)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //delete selected record sa sqlite
                                                    dbController.deleteSelectedMonitorTransaction(monitor_id, client_id, medrep_id);
                                                    Toast.makeText(context, "Monitor Transaction #"+monitor_id+" deleted", Toast.LENGTH_SHORT).show();

                                                    AppCompatActivity activity = (AppCompatActivity) mainContext;
                                                    StockLevelMainFragment reloadFrag = new StockLevelMainFragment();
                                                    Bundle args = new Bundle();
                                                    //BUTANGAN KO NI PARA MAG RELOAD SA IYA GYAPON NGA TAB MAKADTO
                                                    args.putInt("TABVALUE",1);
                                                    reloadFrag.setArguments(args);
                                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, reloadFrag).addToBackStack(null).commit();

                                                    notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog

                                                }
                                            });
                                    builder.create();
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();


                            }
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TransactionSelectedActivity.class);
                intent.putExtra("CLIENT_ID",client_id);
                intent.putExtra("CLIENT_NAME",client_name);
                intent.putExtra("MONITOR_ID",""+monitor_id);
                intent.putExtra("DATE",""+transaction_date);
                context.startActivity(intent);
                Toast.makeText(context, monitor_id, Toast.LENGTH_SHORT).show();
            }
        });

    }
}