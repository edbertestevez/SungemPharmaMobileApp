package com.example.sungem.sungempharma.Medrep.Payment;

/**
 * Created by trebd on 8/22/2017.
 */

public class InvoicePaymentData {
    String  p_invoice, p_mode, p_amount, p_date, p_medrep_id = "";

    public InvoicePaymentData(String invoice_id, String paymode, String amount, String date,  String medrep_id) {
        p_invoice = invoice_id;
        p_mode = paymode;
        p_amount = amount;
        p_date = date;
        p_medrep_id = medrep_id;
    }

    public String getIPInvoice() {return p_invoice;}

    public String getIPPaymode() {return p_mode;}

    public String getIPAmount() {return p_amount;}

    public String getIPDate() {return p_date;}

    public String getIPMedrep() {return p_medrep_id;}

}
