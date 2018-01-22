package com.example.sungem.sungempharma.Medrep.Delivery;

/**
 * Created by trebd on 8/19/2017.
 */

public class DeliveryData {
    String  d_id, d_ordid, d_name, d_address, d_medrep_id = "";

    public DeliveryData(String delivery_id, String order_id, String client_name, String address, String medrep_id) {
        d_id = delivery_id;
        d_ordid = order_id;
        d_name = client_name;
        d_address = address;
        d_medrep_id = medrep_id;
    }

    public String getDeliveryId() {return d_id;}

    public String getOrderId() {
        return d_ordid;
    }

    public String getClientName() {
        return d_name;
    }

    public String getClientAddress() {return d_address;}

    public String getMedrepId() {return d_medrep_id;}
}
