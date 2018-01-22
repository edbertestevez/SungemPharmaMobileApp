package com.example.sungem.sungempharma.Medrep.StockLevel;

/**
 * Created by trebd on 8/25/2017.
 */

public class StockItemData {
    String  si_client, si_pro_id, si_pro_generic, si_pro_brand, si_pro_formulation, si_lot, si_expiry, si_qty, si_medrep_id = "";

    public StockItemData(String client_id, String pro_id, String pro_brand, String pro_generic, String pro_formulaton, String lot_number, String expiry_date, String qty, String medrep_id) {
        si_client = client_id;
        si_pro_id = pro_id;
        si_pro_generic = pro_brand;
        si_pro_brand = pro_generic;
        si_pro_formulation = pro_formulaton;
        si_lot = lot_number;
        si_expiry = expiry_date;
        si_qty = qty;
        si_medrep_id = medrep_id;
    }

    public String getSi_client() {return si_client;}

    public String getSi_pro_id() {
        return si_pro_id;
    }

    public String getSi_pro_brand() {
        return si_pro_generic;
    }

    public String getSi_pro_generic() {
        return si_pro_brand;
    }

    public String getSi_pro_formulation() {return si_pro_formulation;}

    public String getSi_lot() {return si_lot;}

    public String getSi_expiry() {return si_expiry;}

    public String getSi_qty() {return si_qty;}

    public String getSi_medrep_id() {return si_medrep_id;}
}
