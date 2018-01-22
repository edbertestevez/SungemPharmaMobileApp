package com.example.sungem.sungempharma.Admin.Sales;

/**
 * Created by trebd on 9/10/2017.
 */

public class MedicineData {
    private String pro_id;
    private String pro_brand;
    private String pro_generic;
    private String pro_formulation;
    private String pro_sales;
    private String pro_qty;


    //Constructor

    public MedicineData(String pro_id, String pro_brand, String pro_generic, String pro_formulation, String pro_sales, String pro_qty) {
        this.pro_id = pro_id;
        this.pro_brand = pro_brand;
        this.pro_generic = pro_generic;
        this.pro_sales = pro_sales;
        this.pro_formulation = pro_formulation;
        this.pro_qty = pro_qty;
    }

    //Setter, getter

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public void setPro_brand(String pro_brand) {
        this.pro_brand = pro_brand;
    }

    public void setPro_generic(String pro_generic) {
        this.pro_generic = pro_generic;
    }

    public void setPro_formulation(String pro_formulation) {
        this.pro_formulation = pro_formulation;
    }

    public void setPro_sales(String pro_sales) {
        this.pro_sales = pro_sales;
    }

    public String getPro_id() {
        return pro_id;
    }

    public String getPro_brand() {
        return pro_brand;
    }

    public String getPro_generict() {
        return pro_generic;
    }

    public String getPro_formulation() {
        return pro_formulation;
    }

    public String getPro_sales() {
        return pro_sales;
    }

    public String getPro_qty() {
        return pro_qty;
    }
}
