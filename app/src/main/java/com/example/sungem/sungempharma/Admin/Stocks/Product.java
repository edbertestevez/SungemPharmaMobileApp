package com.example.sungem.sungempharma.Admin.Stocks;

/**
 * Created by Jaco on 9/1/2017.
 */

public class Product {
    private String pro_id;
    private String pro_name;
    private String pro_qty;
    private String pro_desc;

    //Constructor

    public Product(String pro_id, String pro_name, String pro_qty, String pro_desc) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.pro_qty = pro_qty;
        this.pro_desc = pro_desc;
    }

    //Setter, getter

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public void setQty(String pro_qty) {
        this.pro_qty = pro_qty;
    }

    public void setPro_desc(String pro_desc) {
        this.pro_desc = pro_desc;
    }

    public String getPro_id() {
        return pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public String getPro_qty() {
        return pro_qty;
    }

    public String getPro_desc() {
        return pro_desc;
    }


}
