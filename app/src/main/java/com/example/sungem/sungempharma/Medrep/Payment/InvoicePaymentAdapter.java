package com.example.sungem.sungempharma.Medrep.Payment;

import android.content.Context;
import android.content.Intent;
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
 * Created by trebd on 8/22/2017.
 */

public class InvoicePaymentAdapter extends RecyclerView.Adapter<InvoicePaymentAdapter.ViewHolder>{

    private ArrayList<InvoicePaymentData> paymentList;
    private Context context;
    public DBController dbController;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public InvoicePaymentAdapter(Context context, ArrayList<InvoicePaymentData> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        dbController = new DBController(this.context);
        sharedpref = this.context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
    }

    @Override
    public InvoicePaymentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list_item, parent, false);
        return new InvoicePaymentAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(InvoicePaymentAdapter.ViewHolder holder, int position) {
        InvoicePaymentData currInvoice = paymentList.get(position);
        String paymode;
        if(currInvoice.getIPPaymode().equals("40")){
            paymode="Cash";
        }else if(currInvoice.getIPPaymode().equals("41")){
            paymode="Post Dated Cheque";
        }else{
            paymode = currInvoice.getIPPaymode();
        }
        holder.paymode_name.setText(paymode);
        holder.payment_amount.setText("(P"+currInvoice.getIPAmount()+")");
        holder.payment_date.setText(currInvoice.getIPDate());
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView paymode_name;
        public TextView payment_amount;
        public TextView payment_date;

        public ViewHolder(final View itemView) {
            super(itemView);

            paymode_name = itemView.findViewById(R.id.txtPaymode);
            payment_amount = itemView.findViewById(R.id.txtAmount);
            payment_date = itemView.findViewById(R.id.txtPaydate);

        }
    }
}
