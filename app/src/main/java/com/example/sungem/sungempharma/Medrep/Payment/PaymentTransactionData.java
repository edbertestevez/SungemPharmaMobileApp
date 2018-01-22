package com.example.sungem.sungempharma.Medrep.Payment;

/**
 * Created by trebd on 8/21/2017.
 */

public class PaymentTransactionData {
    String p_id, p_invoice, p_client, p_amount, p_mode, p_pdc_number, p_pdc_date, p_pdc_bank, p_date, p_time, p_status, p_medrep_id = "";

    public PaymentTransactionData(String payment_id, String invoice_id, String client_name, String amount, String paymode, String pdc_number, String pdc_date, String pdc_bank, String date, String time, String sync_status, String medrep_id) {
        p_id = payment_id;
        p_invoice = invoice_id;
        p_client = client_name;
        p_amount = amount;
        p_mode = paymode;
        p_pdc_number = pdc_number;
        p_pdc_date = pdc_date;
        p_pdc_bank = pdc_bank;
        p_date = date;
        p_time = time;
        p_status = sync_status;
        p_medrep_id = medrep_id;
    }


    public String getPId() {return p_id;}

    public String getPInvoice() {return p_invoice;}

    public String getPClient() {return p_client;}

    public String getPAmount() {return p_amount;}

    public String getPdcNumber() {return p_pdc_number;}

    public String getPdcDate() {return p_pdc_date;}

    public String getPdcBank() {return p_pdc_bank;}

    public String getPMode() {return p_mode;}

    public String getPDate() {return p_date;}

    public String getPTime() {return p_time;}

    public String getP_status() {return p_status;}

    public String getPMedrep(){return p_medrep_id;}
}
