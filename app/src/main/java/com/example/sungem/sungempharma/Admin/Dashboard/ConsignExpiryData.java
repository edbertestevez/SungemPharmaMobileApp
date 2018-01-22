package com.example.sungem.sungempharma.Admin.Dashboard;

/**
 * Created by trebd on 10/5/2017.
 */

public class ConsignExpiryData {
    private String pro_brand;
    private String pro_generic;
    private String pro_formulation;
    private String pro_qty;
    private String client_name;
    private String lot_number;
    private String lot_expiry;
    private String days_remain;


    //Constructor

    public ConsignExpiryData(String pro_brand, String pro_generic, String pro_formulation, String pro_qty, String client_name, String lot_number, String lot_expiry, String days_remain) {
        this.pro_brand = pro_brand;
        this.pro_generic = pro_generic;
        this.pro_formulation = pro_formulation;
        this.pro_qty = pro_qty;
        this.client_name = client_name;
        this.lot_number = lot_number;
        this.lot_expiry = lot_expiry;
        this.days_remain = days_remain;

    }

    public String getPro_qty() {
        return pro_qty;
    }


    public String getClient_name() {
        return client_name;
    }

    public String getLot_expiry() {
        return lot_expiry;
    }

    public String getLot_number() {
        return lot_number;
    }

    public String getDays_remain() {
        return days_remain;
    }

    public String getPro_brand() {
        return pro_brand;
    }

    public String getPro_formulation() {
        return pro_formulation;
    }

    public String getPro_generic() {
        return pro_generic;
    }

    public void setPro_generic(String pro_generic) {
        this.pro_generic = pro_generic;
    }

}
