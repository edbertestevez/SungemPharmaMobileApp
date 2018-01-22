package com.example.sungem.sungempharma.Medrep.Delivery;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trebd on 8/21/2017.
 */

public class DeliveryItemAdapter extends RecyclerView.Adapter<DeliveryItemAdapter.ViewHolder>{
    private ArrayList<DeliveryItemData> itemList;
    private Context context;
    public DBController dbController;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public DeliveryItemAdapter(Context context, ArrayList<DeliveryItemData> itemList) {
        this.context = context;
        this.itemList = itemList;
        dbController = new DBController(this.context);
        sharedpref = this.context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
    }

    @Override
    public DeliveryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item_selected, parent, false);
        return new DeliveryItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryItemAdapter.ViewHolder holder, int position) {
        DeliveryItemData currItem = itemList.get(position);
        holder.product_name.setText(currItem.getProductName());
        holder.lot_number.setText(currItem.getLotNumber());
        holder.expiry.setText(currItem.getExpiry());
        holder.quantity.setText(currItem.getQuantity());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name;
        public TextView lot_number;
        public TextView expiry;
        public TextView quantity;

        public ViewHolder(final View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.txtProBrand);
            lot_number = itemView.findViewById(R.id.txtDelivery);
            expiry = itemView.findViewById(R.id.txtLotExpiry);
            quantity = itemView.findViewById(R.id.txtQuantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    DeliveryItemData selectedDelivery = itemList.get(position);
                }
            });
        }
    }
}
