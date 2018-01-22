package com.example.sungem.sungempharma.Medrep.Payment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryData;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryItemActivity;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trebd on 8/21/2017.
 */

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder>{

    private ArrayList<PaymentData> invoiceList;
    private Context context;
    public DBController dbController;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    public InvoiceAdapter(Context context, ArrayList<PaymentData> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
        dbController = new DBController(this.context);
        sharedpref = this.context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
    }

    @Override
    public InvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(InvoiceAdapter.ViewHolder holder, int position) {
        PaymentData currInvoice = invoiceList.get(position);
        holder.invoice_id.setText(currInvoice.getPInvoice());
        holder.client_name.setText(currInvoice.getPClient());
        holder.amount_remaining.setText("P"+currInvoice.getPRemaining());
        holder.date_due.setText(currInvoice.getPDue());
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView invoice_id;
        public TextView client_name;
        public TextView amount_remaining;
        public TextView date_due;
        public Button btnView;

        public ViewHolder(final View itemView) {
            super(itemView);

            invoice_id = itemView.findViewById(R.id.txtInvoiceId);
            client_name = itemView.findViewById(R.id.txtClient);
            amount_remaining = itemView.findViewById(R.id.txtRemaining);
            date_due = itemView.findViewById(R.id.txtDue);
            btnView = itemView.findViewById(R.id.btnView);

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    PaymentData selectedInvoice = invoiceList.get(position);

                    Intent intent = new Intent(context, InvoiceItemActivity.class);
                    intent.putExtra("INVOICE_ID",selectedInvoice.getPInvoice());
                    intent.putExtra("CLIENT",selectedInvoice.getPClient());
                    intent.putExtra("TOTAL",selectedInvoice.getPTotal());
                    intent.putExtra("PAID",selectedInvoice.getPpaid());
                    intent.putExtra("REMAINING",selectedInvoice.getPRemaining());
                    intent.putExtra("DATE_DUE",selectedInvoice.getPDue());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
    }
}
