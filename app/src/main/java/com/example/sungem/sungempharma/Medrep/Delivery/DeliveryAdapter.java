package com.example.sungem.sungempharma.Medrep.Delivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trebd on 8/20/2017.
 */

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {

    private ArrayList<DeliveryData> deliveryList;
    private Context context;
    public DBController dbController;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public DeliveryAdapter(Context context, ArrayList<DeliveryData> deliveryList) {
        this.context = context;
        this.deliveryList = deliveryList;
        dbController = new DBController(this.context);
        sharedpref = this.context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
    }

    @Override
    public DeliveryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryAdapter.ViewHolder holder, int position) {
        DeliveryData currDelivery = deliveryList.get(position);
        holder.delivery_id.setText(currDelivery.getDeliveryId());
        holder.order_id.setText(currDelivery.getOrderId());
        holder.client_name.setText(currDelivery.getClientName());

    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView delivery_id;
        public TextView order_id;
        public TextView client_name;
        public TextView client_address;
        public Button btnView;

        public ViewHolder(final View itemView) {
            super(itemView);

            delivery_id = itemView.findViewById(R.id.txtDelivery);
            order_id = itemView.findViewById(R.id.txtOrder);
            client_name = itemView.findViewById(R.id.txtClient);
            client_address = itemView.findViewById(R.id.txtAddress);
            btnView = itemView.findViewById(R.id.btnView);

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    DeliveryData selectedDelivery = deliveryList.get(position);

                    Intent intent = new Intent(context, DeliveryItemActivity.class);
                    intent.putExtra("DELIVERY_ID",selectedDelivery.getDeliveryId());
                    intent.putExtra("CLIENT",selectedDelivery.getClientName());
                    intent.putExtra("ADDRESS",selectedDelivery.getClientAddress());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    /*Snackbar.make(v, "You selected position: " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
*/
                    /* SAMPLE LANG NI MAGPASULOD SA DELIVERY
                    HashMap<String, String> deliveryTransactionValues = new HashMap<String, String>();
                    deliveryTransactionValues.put("delivery_id", selectedDelivery.getDeliveryId());
                    deliveryTransactionValues.put("delivery_status", "0");

                    //GET CURRENT DATE
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date currDate = Calendar.getInstance().getTime();
                    String formatDate = df.format(currDate);
                    //GET CURRENT TIME
                    SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
                    Date currTime = Calendar.getInstance().getTime();
                    String formatTime = tf.format(currTime);

                    deliveryTransactionValues.put("delivery_date", formatDate);
                    deliveryTransactionValues.put("delivery_time", formatTime);
                    deliveryTransactionValues.put("medrep_id", sharedpref.getString("USERID",""));
                    deliveryTransactionValues.put("or_number", "1234");
                    dbController.insertDeliveryTransactionData(deliveryTransactionValues);
*/
                }
            });
        }
    }
}