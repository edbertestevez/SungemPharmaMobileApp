package com.example.sungem.sungempharma.Medrep.Dashboard;

/**
 * Created by trebd on 10/3/2017.
 */

public class SalesMedrepData {
    String  c_name, p_amt, p_means, p_date, p_medrep_id = "";

    public SalesMedrepData(String client_name, String payment_amount, String payment_means, String payment_date, String medrep_id) {
        c_name = client_name;
        p_amt = payment_amount;
        p_means = payment_means;
        p_date = payment_date;
        p_medrep_id = medrep_id;
    }

    public String getC_name() {return c_name;}
    public String getP_amt() {return p_amt;}
    public String getP_means() {return p_means;}
    public String getP_date() {return p_date;}
    public String getP_medrep_id() {return p_medrep_id;}


}
