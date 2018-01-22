package com.example.sungem.sungempharma.Medrep.Payment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryItemActivity;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class InvoiceItemActivity extends AppCompatActivity {

    String invoice_id, client_name, total, paid, remaining, date_due, str_paid;
    TextView txtInvoice,txtClient, txtTotal, txtPaid, txtRemaining,txtDue,txtPaymentsMade;
    ArrayList<InvoicePaymentData> paymentList;
    ArrayList<InvoicePaymentData> paymentTransactionList;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    RecyclerView recyclerView;
    DBController dbController;
    EditText etPdcDate;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Invoice Information");

        dbController = new DBController(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        invoice_id = extras.getString("INVOICE_ID");
        client_name = extras.getString("CLIENT");
        total = extras.getString("TOTAL");
        //str_paid = extras.getString("PAID");
        str_paid = String.valueOf(dbController.getInvoicePaymentsLoaded(invoice_id));
        paid = String.valueOf(dbController.getInvoicePaymentsLoaded(invoice_id)+(dbController.getPayTransactionTotal(invoice_id)));
        /************NOTICE ME EDBERT PLEASE UPDATE MO NI**************/
        remaining = String.valueOf(Double.parseDouble(total)-Double.parseDouble(paid));
        date_due = extras.getString("DATE_DUE");

        txtInvoice = (TextView) findViewById(R.id.txtInvoice);
        txtClient = (TextView) findViewById(R.id.txtClient);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtPaid = (TextView) findViewById(R.id.txtPaid);
        txtRemaining = (TextView) findViewById(R.id.txtRemaining);
        txtDue = (TextView) findViewById(R.id.txtDue);
        txtPaymentsMade = (TextView) findViewById(R.id.txtPaymentsMade);

        txtInvoice.setText(invoice_id);
        txtClient.setText(client_name);
        txtTotal.setText("P"+total);
        txtPaid.setText("P"+paid);
        txtRemaining.setText("P"+remaining);
        txtDue.setText(date_due);

        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        paymentList = new ArrayList<InvoicePaymentData>();
        paymentTransactionList = new ArrayList<InvoicePaymentData>();

        displayInvoicePaymentRecords();

        FloatingActionButton floatPaymentBtn = (FloatingActionButton) findViewById(R.id.floatPaymentBtn);
        floatPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.WHITE);
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(InvoiceItemActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_payment_edit, null);

                mBuilder.setTitle("Payment Transaction");
                RadioGroup groupMode = (RadioGroup) mView.findViewById(R.id.radioGroupMode);
                final RadioButton rdoCash = (RadioButton) mView.findViewById(R.id.rdoCash);
                final RadioButton rdoPdc = (RadioButton) mView.findViewById(R.id.rdoPdc);
                final EditText etAmount = (EditText) mView.findViewById(R.id.etAmount);
                final ImageButton btnDate = (ImageButton) mView.findViewById(R.id.btnDate);

                final TextView txtPdcError = (TextView) mView.findViewById(R.id.txtPdcError);
                final TextView txtDateError = (TextView) mView.findViewById(R.id.txtDateError);
                final TextView txtBankError = (TextView) mView.findViewById(R.id.txtBankError);
                final EditText etPdc = (EditText) mView.findViewById(R.id.etPdc);
                final EditText etBank = (EditText) mView.findViewById(R.id.etBank);
                Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
                Button btnSave = (Button) mView.findViewById(R.id.btnSave);
                etPdcDate  = (EditText) mView.findViewById(R.id.etPdcDate);
                rdoCash.setChecked(true);//DEFAULT
                etPdcDate.setText("");//DEFAULT
                etPdc.setEnabled(false);
                btnDate.setEnabled(false);//DEFAULT
                etBank.setEnabled(false);//DEFAULT

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                myCalendar = Calendar.getInstance();
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updatePdcDate();
                    }

                };


                btnDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DatePickerDialog(InvoiceItemActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                rdoCash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etPdc.setText("");
                        etPdc.setEnabled(false);
                        etPdcDate.setText("");
                        etPdcDate.setEnabled(false);
                        btnDate.setEnabled(false);
                        txtPdcError.setText("N/A");
                        txtDateError.setText("N/A");
                        etBank.setEnabled(false);
                        txtBankError.setText("N/A");
                    }
                });

                rdoPdc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etPdc.setEnabled(true);
                        btnDate.setEnabled(true);
                        txtPdcError.setText("*");
                        btnDate.setEnabled(true);
                        txtDateError.setText("*");
                        etBank.setEnabled(true);
                        txtBankError.setText("*");
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String pay_amount = etAmount.getText().toString();
                        final String pdc_number = etPdc.getText().toString();
                        final String pdc_date = etPdcDate.getText().toString();
                        final String pdc_bank = etBank.getText().toString();

                        if(rdoCash.isChecked()){
                            if(pay_amount.equals("")){
                                etAmount.setError("Please enter amount");
                            }else if(Double.parseDouble(pay_amount)<=0){
                                etAmount.setError("Please enter valid amount");
                            }else if(Double.parseDouble(pay_amount)>Double.parseDouble(remaining)){
                                etAmount.setError("Payment should not be greater than remaining balance");
                            }else {

                                //CONFIRMATION
                                AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceItemActivity.this);
                                builder.setMessage("Save Payment Transaction");

                                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface confirmDialog, int which) {
                                        HashMap<String, String> transactionValue = new HashMap<String, String>();
                                        transactionValue.put("invoice_id", invoice_id); //BASED ON DATABASE SERVER (CASH)
                                        transactionValue.put("client_name", client_name);
                                        transactionValue.put("paymode_id", "40");

                                        //GET CURRENT DATE
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        Date currDate = Calendar.getInstance().getTime();
                                        String pay_date = df.format(currDate);
                                        //GET CURRENT TIME
                                        SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
                                        Date currTime = Calendar.getInstance().getTime();
                                        String pay_time = tf.format(currTime);

                                        transactionValue.put("pay_amount", pay_amount);
                                        transactionValue.put("pdc_number", pdc_number);
                                        transactionValue.put("pdc_date", pdc_date);
                                        transactionValue.put("pdc_bank", pdc_bank);
                                        transactionValue.put("pay_date", pay_date);
                                        transactionValue.put("pay_time", pay_time);
                                        transactionValue.put("payment_transaction_status", "0");
                                        transactionValue.put("medrep_id", sharedpref.getString("USERID", ""));
                                        dbController.insertPaymentTransaction(transactionValue);

                                        confirmDialog.dismiss();
                                        Toast.makeText(InvoiceItemActivity.this, "Payment Transaction successfully recorded", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        //UPDATE DISPLAY OF RECORDS
                                        txtPaid.setText(String.valueOf(dbController.getInvoicePaymentsLoaded(invoice_id)+(dbController.getPayTransactionTotal(invoice_id))));
                                        txtRemaining.setText(String.valueOf(Double.parseDouble(total)-(dbController.getInvoicePaymentsLoaded(invoice_id))-(dbController.getPayTransactionTotal(invoice_id))));
                                        remaining = String.valueOf(String.valueOf(Double.parseDouble(total)-(dbController.getInvoicePaymentsLoaded(invoice_id))-(dbController.getPayTransactionTotal(invoice_id))));
                                        displayInvoicePaymentRecords();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface confirmDialog, int which) {
                                        confirmDialog.dismiss();
                                    }
                                });
                                final AlertDialog confirmDialog = builder.create();
                                confirmDialog.show();
                            }
                        }else if(rdoPdc.isChecked()){
                            if(pay_amount.equals("")){
                                etAmount.setError("Please enter amount");
                            }else if(Double.parseDouble(pay_amount)<=0){
                                etAmount.setError("Please enter valid amount");
                            }else if(Double.parseDouble(pay_amount)>Double.parseDouble(remaining)){
                                etAmount.setError("Payment should not be greater than remaining balance");
                            }else if(pdc_number.equals("")){
                                etPdc.setError("Please enter PDC Number");
                            }else if(pdc_date.equals("")){
                                etPdcDate.setError("Please enter PDC Date");
                            }else if(pdc_bank.equals("")){
                                etBank.setError("Please enter bank for PDC");
                            }else{

                                //CONFIRMATION
                                AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceItemActivity.this);
                                builder.setMessage("Save Payment Transaction");

                                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface confirmDialog, int which) {
                                        HashMap<String, String> transactionValue = new HashMap<String, String>();
                                        transactionValue.put("invoice_id", invoice_id); //BASED ON DATABASE SERVER (CASH)
                                        transactionValue.put("client_name", client_name);
                                        transactionValue.put("paymode_id", "41");

                                        //GET CURRENT DATE
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                        Date currDate = Calendar.getInstance().getTime();
                                        String pay_date = df.format(currDate);
                                        //GET CURRENT TIME
                                        SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
                                        Date currTime = Calendar.getInstance().getTime();
                                        String pay_time = tf.format(currTime);

                                        transactionValue.put("pay_amount", pay_amount);
                                        transactionValue.put("pdc_number", pdc_number);
                                        transactionValue.put("pdc_date", pdc_date);
                                        transactionValue.put("pdc_bank", pdc_bank);
                                        transactionValue.put("pay_date", pay_date);
                                        transactionValue.put("pay_time", pay_time);
                                        transactionValue.put("payment_transaction_status", "0");
                                        transactionValue.put("medrep_id", sharedpref.getString("USERID", ""));
                                        dbController.insertPaymentTransaction(transactionValue);

                                        confirmDialog.dismiss();
                                        Toast.makeText(InvoiceItemActivity.this, "Payment Transaction successfully recorded", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();

                                        //UPDATE DISPLAY OF RECORDS and remaining value
                                        txtPaid.setText(String.valueOf(dbController.getInvoicePaymentsLoaded(invoice_id)+(dbController.getPayTransactionTotal(invoice_id))));
                                        txtRemaining.setText(String.valueOf(Double.parseDouble(total)-(dbController.getInvoicePaymentsLoaded(invoice_id))-(dbController.getPayTransactionTotal(invoice_id))));
                                        remaining = String.valueOf(String.valueOf(Double.parseDouble(total)-(dbController.getInvoicePaymentsLoaded(invoice_id))-(dbController.getPayTransactionTotal(invoice_id))));
                                        displayInvoicePaymentRecords();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface confirmDialog, int which) {
                                        confirmDialog.dismiss();
                                    }
                                });
                                final AlertDialog confirmDialog = builder.create();
                                confirmDialog.show();
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayInvoicePaymentRecords() {
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        paymentList = dbController.getInvoicePayments(invoice_id);

        if (!paymentList.isEmpty()) {
            InvoicePaymentAdapter adapter = new InvoicePaymentAdapter(this.getApplicationContext(), paymentList);
            recyclerView.setAdapter(adapter);
            txtPaymentsMade.setText("PAYMENTS MADE");
            txtPaymentsMade.setTextColor(Color.BLACK);
        }else{
            txtPaymentsMade.setText("NO PAYMENTS MADE");
            txtPaymentsMade.setTextColor(Color.RED);

        }
    }

    public void updatePdcDate(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etPdcDate.setText(sdf.format(myCalendar.getTime()));
    }
}
