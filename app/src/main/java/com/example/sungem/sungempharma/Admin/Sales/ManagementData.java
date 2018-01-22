package com.example.sungem.sungempharma.Admin.Sales;

/**
 * Created by trebd on 9/9/2017.
 */

public class ManagementData {
    private String client_id;
    private String client_name;
    private String client_sales;

    //Constructor

    public ManagementData(String client_id, String client_name, String client_sales) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_sales = client_sales;
    }

    //Setter, getter

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public void setClient_sales(String client_sales) {
        this.client_sales = client_sales;
    }


    public String getClient_id() {
        return client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getClient_sales() {
        return client_sales;
    }

}
