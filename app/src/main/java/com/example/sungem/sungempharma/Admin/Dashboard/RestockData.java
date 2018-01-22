package com.example.sungem.sungempharma.Admin.Dashboard;

/**
 * Created by trebd on 10/4/2017.
 */

public class RestockData {
    private String pro_brand;
    private String pro_generic;
    private String pro_formulation;
    private String pro_qty;
    private String pro_reorder;
    private String pending_orders;
    private String actual_remain;

    //Constructor

    public RestockData(String pro_brand, String pro_generic, String pro_formulation, String pro_qty, String pro_reorder, String pending_orders, String actual_remain) {
        this.pro_brand = pro_brand;
        this.pro_generic = pro_generic;
        this.pro_qty = pro_qty;
        this.pro_formulation = pro_formulation;
        this.pro_reorder=pro_reorder;
        this.pending_orders=pending_orders;
        this.actual_remain=actual_remain;
    }



    public String getPro_brand() {
        return pro_brand;
    }

    public String getPro_generic() {
        return pro_generic;
    }

    public String getPro_formulation() {
        return pro_formulation;
    }

    public String getActual_remain() {
        return actual_remain;
    }

    public String getPending_orders() {
        return pending_orders;
    }

    public String getPro_qty() {
        return pro_qty;
    }

    public String getPro_reorder() {
        return pro_reorder;
    }

}
