package com.example.sungem.sungempharma.Medrep.Delivery;

/**
 * Created by trebd on 8/21/2017.
 */

public class DeliveryItemData {
    String  d_id, d_product, d_name, d_lot, d_expiry, d_qty, d_medrep= "";

    public DeliveryItemData(String delivery_id, String product_id, String product_name, String lot_number, String expiry, String quantity, String medrep_id) {
        d_id = delivery_id;
        d_product = product_id;
        d_name = product_name;
        d_lot = lot_number;
        d_expiry = expiry;
        d_qty = quantity;
        d_medrep = medrep_id;
    }

    public String getDeliveryId() {return d_id;}

    public String getProductId() {
        return d_product;
    }

    public String getProductName() {
        return d_name;
    }

    public String getLotNumber() {
        return d_lot;
    }

    public String getExpiry() {
        return d_expiry;
    }

    public String getQuantity() {return d_qty;}

    public String getMedrep() {return d_medrep;}
}
