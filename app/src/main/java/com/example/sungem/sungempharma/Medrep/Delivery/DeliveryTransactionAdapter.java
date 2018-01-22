package com.example.sungem.sungempharma.Medrep.Delivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockLevelMainFragment;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by trebd on 8/20/2017.
 */

public class DeliveryTransactionAdapter extends RecyclerView.Adapter<DeliveryTransactionAdapter.ViewHolder> {

    private ArrayList<DeliveryTransactionData> deliveryTransactionList;
    public Context context;
    public DBController dbController;
    private Context newContext;
    public DeliveryTransactionAdapter(Context context, ArrayList<DeliveryTransactionData> deliveryTransactionList) {
        this.context = context;
        this.deliveryTransactionList = deliveryTransactionList;
        dbController = new DBController(this.context);
    }


    @Override
    public DeliveryTransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_transaction_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryTransactionAdapter.ViewHolder holder, int position) {
        DeliveryTransactionData currDelivery = deliveryTransactionList.get(position);
        holder.delivery_id.setText(currDelivery.getDeliveryId());
        String str_status;
        if(currDelivery.getSyncStatus().equals("0")){
            str_status = "NOT SYNCED";
            Picasso.with(context).load(R.drawable.ic_error_outline_black_24dp).into(holder.imgStatus);
            //holder.imgStatus.setImageResource(R.drawable.ic_sync_black);
        }else{
            str_status = "SYNCED";
        }
        holder.delivery_status.setText(str_status);
        holder.delivery_date.setText(currDelivery.getDeliveryDate());
    }

    @Override
    public int getItemCount() {
        return deliveryTransactionList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView delivery_id;
        public TextView delivery_status;
        public TextView delivery_date;
        public ImageView imgStatus;
        public Button btnTransaction;
        public ImageView imgPop;
        String popupmessage;

        public ViewHolder(final View itemView) {
            super(itemView);
            newContext = itemView.getContext();

            delivery_id = itemView.findViewById(R.id.txt_delivery_id);
            delivery_status = itemView.findViewById(R.id.txt_status);
            delivery_date = itemView.findViewById(R.id.txt_date);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            btnTransaction = itemView.findViewById(R.id.btnTransaction);
            imgPop = itemView.findViewById(R.id.imgPop);

            imgPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final int position = getAdapterPosition();
                    final DeliveryTransactionData selectedTransaction = deliveryTransactionList.get(position);

                    PopupMenu popup = new PopupMenu(view.getContext(), imgPop);

                    if(selectedTransaction.getSyncStatus().equals("0")){
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_delete, popup.getMenu());
                        popupmessage = "Delete Delivery Transaction?";
                    }else{
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_remove, popup.getMenu());
                        popupmessage = "Remove from transaction list?";
                    }

                    popup.setGravity(Gravity.END);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_delete:{
                                    //DIALOG KUNG I DELETE OR INDI
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setMessage(popupmessage)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //delete selected record sa sqlite
                                                    dbController.deleteSelectedDeliveryTransaction(selectedTransaction.getDeliveryId());
                                                    Toast.makeText(context, "Delivery Transaction #"+selectedTransaction.getDeliveryId()+" deleted", Toast.LENGTH_SHORT).show();

                                                    FragmentManager manager = ((AppCompatActivity)newContext).getSupportFragmentManager();

                                                    DeliveryMainFragment reloadFrag = new DeliveryMainFragment();
                                                    Bundle args = new Bundle();
                                                    //BUTANGAN KO NI PARA MAG RELOAD SA IYA GYAPON NGA TAB MAKADTO
                                                    args.putInt("TABVALUE",1);
                                                    reloadFrag.setArguments(args);
                                                    manager.beginTransaction().replace(R.id.content_frame, reloadFrag).addToBackStack(null).commit();

                                                    notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog
                                                    dialog.dismiss();
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


            btnTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    DeliveryTransactionData selectedDelivery = deliveryTransactionList.get(position);
                    Intent intent = new Intent(context, DeliveryTransactionItemActivity.class);
                    intent.putExtra("DELIVERY_ID",selectedDelivery.getDeliveryId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}

