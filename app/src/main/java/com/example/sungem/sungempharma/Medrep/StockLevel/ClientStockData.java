package com.example.sungem.sungempharma.Medrep.StockLevel;

/**
 * Created by trebd on 8/24/2017.
 */

public class ClientStockData {
    String  c_id, c_name, c_address, c_total_stock, c_medrep_id = "";

    public ClientStockData(String client_id, String client_name, String client_address, String total_stock, String medrep_id) {
        c_id = client_id;
        c_name = client_name;
        c_address = client_address;
        c_total_stock = total_stock;
        c_medrep_id = medrep_id;
    }

    public String getClientId() {return c_id;}

    public String getClientName() {
        return c_name;
    }

    public String getClientAddress() {
        return c_address;
    }

    public String getClientStock() {
        return c_total_stock;
    }

    public String getClientMedrep() {return c_medrep_id;}
}
