package com.example.sungem.sungempharma.Medrep.Delivery;

import java.util.Date;

/**
 * Created by trebd on 8/20/2017.
 */

public class DeliveryTransactionData {
    String  dt_id, dt_status, dt_delivery_date, dt_delivery_time, dt_medrep_id, dt_or_num = "";

    public DeliveryTransactionData(String delivery_id, String sync_status, String delivery_date, String delivery_time, String medrep_id, String or_number) {
        dt_id = delivery_id;
        dt_status = sync_status;
        dt_delivery_date = delivery_date;
        dt_delivery_time = delivery_time;
        dt_medrep_id = medrep_id;
        dt_or_num = or_number;
    }

    public String getDeliveryId() {return dt_id;}

    public String getSyncStatus() {
        return dt_status;
    }

    public String getDeliveryDate() {return dt_delivery_date;}

    public String getDeliveryTime() {return dt_delivery_time;}

    public String getMedrepId() {return dt_medrep_id;}

    public String getOrNum() {return dt_or_num;}

}
