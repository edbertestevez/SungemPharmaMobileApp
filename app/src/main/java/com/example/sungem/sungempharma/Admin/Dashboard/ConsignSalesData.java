package com.example.sungem.sungempharma.Admin.Dashboard;

/**
 * Created by trebd on 10/5/2017.
 */

public class ConsignSalesData {
    private String pro_brand;
    private String pro_generic;
    private String pro_formulation;
    private String pro_qty;
    private String pro_sales;


    //Constructor

    public ConsignSalesData(String pro_brand, String pro_generic, String pro_formulation, String pro_qty, String pro_sales) {
        this.pro_brand = pro_brand;
        this.pro_generic = pro_generic;
        this.pro_formulation = pro_formulation;
        this.pro_qty = pro_qty;
        this.pro_sales = pro_sales;
    }

    public String getPro_qty() {
        return pro_qty;
    }

    public String getPro_generic() {
        return pro_generic;
    }

    public String getPro_formulation() {
        return pro_formulation;
    }

    public String getPro_brand() {
        return pro_brand;
    }

    public String getPro_sales() {
        return pro_sales;
    }
}
