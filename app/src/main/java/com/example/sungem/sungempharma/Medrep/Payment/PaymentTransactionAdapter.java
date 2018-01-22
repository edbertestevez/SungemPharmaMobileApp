package com.example.sungem.sungempharma.Medrep.Payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryMainFragment;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryTransactionData;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by trebd on 8/23/2017.
 */

public class PaymentTransactionAdapter  extends RecyclerView.Adapter<PaymentTransactionAdapter.ViewHolder>{

    private ArrayList<PaymentTransactionData> paymentList;
    private Context context, newContext;
    public DBController dbController;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    String popupmessage;

    public PaymentTransactionAdapter(Context context, ArrayList<PaymentTransactionData> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        dbController = new DBController(this.context);
        sharedpref = this.context.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
    }

    @Override
    public PaymentTransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_transaction_item, parent, false);
        return new PaymentTransactionAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(PaymentTransactionAdapter.ViewHolder holder, int position) {
        PaymentTransactionData currPayment = paymentList.get(position);

        holder.invoice_id.setText(currPayment.getPInvoice());
        holder.client_name.setText(currPayment.getPClient());
        String paymode_name;
        String status_str;
        if(currPayment.getPMode().equals("40")){
            paymode_name = "Cash";
        }else{
            paymode_name = "Post Dated Cheque";
        }
        if(currPayment.getP_status().equals("0")){
            status_str = "NOT SYNCED";
            Picasso.with(context).load(R.drawable.ic_error_outline_black_24dp).into(holder.imgStatus);
        }else{
            status_str = "SYNCED";
        }
        holder.paymode_name.setText(paymode_name);
        holder.payment_amount.setText("(P"+currPayment.getPAmount()+")");
        holder.sync_status.setText(status_str);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView invoice_id;
        public TextView client_name;
        public TextView paymode_name;
        public TextView payment_amount;
        public TextView sync_status;
        public ImageView imgStatus;
        public ImageView imgPop;
        public Button btnView;

        public ViewHolder(final View itemView) {
            super(itemView);
            newContext = itemView.getContext();

            invoice_id = itemView.findViewById(R.id.txtInvoiceId);
            client_name = itemView.findViewById(R.id.txtClientName);
            paymode_name = itemView.findViewById(R.id.txtPaymode);
            payment_amount = itemView.findViewById(R.id.txtAmount);
            sync_status = itemView.findViewById(R.id.txtStatus);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            imgPop = itemView.findViewById(R.id.imgPop);
            btnView = itemView.findViewById(R.id.btnView);

            imgPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final int position = getAdapterPosition();
                    final PaymentTransactionData selectedTransaction = paymentList.get(position);

                    PopupMenu popup = new PopupMenu(view.getContext(), imgPop);

                    if(selectedTransaction.getP_status().equals("0")){
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_delete, popup.getMenu());
                        popupmessage = "Delete Payment Transaction";
                    }else{
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_remove, popup.getMenu());
                        popupmessage = "Remove transaction from list?";
                    }

                    popup.setGravity(Gravity.END);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.menu_delete:{
                                    final int position = getAdapterPosition();
                                    final PaymentTransactionData selectedTransaction = paymentList.get(position);

                                    //DIALOG KUNG I DELETE OR INDI
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setMessage(popupmessage)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //delete selected record sa sqlite
                                                    dbController.deleteSelectedPaymentTransaction(selectedTransaction.getPId());
                                                    Toast.makeText(context, "Payment Transaction #"+selectedTransaction.getPId()+" deleted", Toast.LENGTH_SHORT).show();

                                                    FragmentManager manager = ((AppCompatActivity)newContext).getSupportFragmentManager();

                                                    PaymentMainFragment reloadFrag = new PaymentMainFragment();
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

            //VIEW PAYMENT DETAILS
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View clickView) {
                    final int position = getAdapterPosition();
                    final PaymentTransactionData selectedTransaction = paymentList.get(position);
                    String paymode, pdc_num, pdc_date, pdc_bank;

                    //DIALOG KUNG I DELETE OR INDI
                    AlertDialog.Builder builder = new AlertDialog.Builder(clickView.getContext());
                    builder.setTitle("Payment Information");

                    if(selectedTransaction.getPMode().equals("40")){
                        paymode="Cash";
                        pdc_num = "N/A";
                        pdc_date = "N/A";
                        pdc_bank = "N/A";
                    }else{
                        paymode="Post Dated Cheque";
                        pdc_num = selectedTransaction.getPdcNumber();
                        pdc_date = selectedTransaction.getPdcDate();
                        pdc_bank = selectedTransaction.getPdcBank();
                    }
                    builder.setMessage(
                            "\nMode of Payment:  " + paymode + "\n\n" + "Date Received:  " + selectedTransaction.getPDate() + "\n\n" + "Total Amount:  P" + selectedTransaction.getPAmount()
                                    + "\n\n" + "PDC No.:  " + pdc_num + "\n\n" + "PDC Date:  " + pdc_date
                                    + "\n\n" + "Bank:  "+ pdc_bank
                    )
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    builder.create();
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        }
    }
}

