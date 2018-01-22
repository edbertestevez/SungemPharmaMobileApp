package com.example.sungem.sungempharma.Medrep.Payment;

/**
 * Created by trebd on 8/21/2017.
 */

public class PaymentData {
    String  p_invoice, p_client, p_total, p_paid, p_remaining, p_due, p_medrep_id = "";

    public PaymentData(String invoice_id, String client_name, String total, String paid, String remaining, String due, String medrep_id) {
        p_invoice = invoice_id;
        p_client = client_name;
        p_total = total;
        p_paid = paid;
        p_remaining = remaining;
        p_due = due;
        p_medrep_id = medrep_id;
    }

    public String getPInvoice() {return p_invoice;}

    public String getPClient() {return p_client;}

    public String getPTotal() {return p_total;}

    public String getPpaid() {return p_paid;}

    public String getPRemaining() {return p_remaining;}

    public String getPDue() {return p_due;}

    public String getPMedrep(){return p_medrep_id;}
}
