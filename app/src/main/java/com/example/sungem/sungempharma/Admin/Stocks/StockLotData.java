package com.example.sungem.sungempharma.Admin.Stocks;

/**
 * Created by trebd on 9/9/2017.
 */

public class StockLotData {
    private String pro_id;
    private String lot_number;
    private String lot_qty;
    private String lot_expiry;

    //Constructor

    public StockLotData(String pro_id, String lot_number, String lot_qty, String lot_expiry) {
        this.pro_id = pro_id;
        this.lot_number = lot_number;
        this.lot_qty = lot_qty;
        this.lot_expiry = lot_expiry;
    }

    //Setter, getter

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public void setLot_number(String lot_number) {
        this.lot_number = lot_number;
    }

    public void setLot_qty(String lot_qty) {
        this.lot_qty = lot_qty;
    }

    public void setLot_expiry(String lot_expiry) {
        this.lot_expiry = lot_expiry;
    }

    public String getPro_id() {
        return pro_id;
    }

    public String getLot_number() {
        return lot_number;
    }

    public String getLot_qty() {
        return lot_qty;
    }

    public String getLot_expiry() {
        return lot_expiry;
    }

}
