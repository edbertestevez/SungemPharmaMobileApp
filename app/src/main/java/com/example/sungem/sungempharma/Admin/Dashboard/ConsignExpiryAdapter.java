package com.example.sungem.sungempharma.Admin.Dashboard;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.R;

import java.util.List;

/**
 * Created by trebd on 10/5/2017.
 */

public class ConsignExpiryAdapter extends BaseAdapter {

    private Context mContext;
    private List<ConsignExpiryData> expiryList;

    //Constructor

    public ConsignExpiryAdapter(Context mContext, List<ConsignExpiryData> expiryList) {
        this.mContext = mContext;
        this.expiryList = expiryList;
    }

    public void addListItemToAdapter(List<ConsignExpiryData> list){
        //Add list to current array list of data
        expiryList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<ConsignExpiryData> list){
        //Add list to current array list of data
        expiryList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        expiryList.clear();

    }
    @Override
    public int getCount() {
        return expiryList.size();
    }

    @Override
    public Object getItem(int position) {
        return expiryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.medrep_expiry_item, null);//SAME MAN LANG SILA LAYOUT
        final String client_name = expiryList.get(position).getClient_name();
        final String pro_brand = expiryList.get(position).getPro_brand();
        final String pro_generic = expiryList.get(position).getPro_generic();
        final String pro_formulation = expiryList.get(position).getPro_formulation();
        final String total_qty = expiryList.get(position).getPro_qty();
        final String lot_number = expiryList.get(position).getLot_number();
        final String lot_expiry = expiryList.get(position).getLot_expiry();
        final String days_remain = expiryList.get(position).getDays_remain();

        TextView txtProBrand = (TextView) v.findViewById(R.id.txtProBrand);
        TextView txtProGenericFormulation = (TextView) v.findViewById(R.id.txtProGenericFormulation);
        TextView txtQty = (TextView) v.findViewById(R.id.txtQty);
        TextView txtLot = (TextView) v.findViewById(R.id.txtLot);
        TextView txtExpiry = (TextView) v.findViewById(R.id.txtExpiry);
        TextView txtClient = (TextView) v.findViewById(R.id.txtClient);
        TextView txtDays = (TextView) v.findViewById(R.id.txtDays);

        if(Integer.parseInt(days_remain)>60){
            txtDays.setTextColor(Color.parseColor("#0000ff"));
        }else if(Integer.parseInt(days_remain)>30){
            txtDays.setTextColor(Color.parseColor("#FF4500"));
        }

        txtDays.setVisibility(View.VISIBLE);
        txtProBrand.setText(pro_brand);
        txtProGenericFormulation.setText(pro_generic+"("+pro_formulation+")");
        txtLot.setText(lot_number);
        txtQty.setText(total_qty);
        txtExpiry.setText(lot_expiry);

        if(client_name.equals("null")){
            txtClient.setText("Location: Warehouse");
        }else{
            txtClient.setText("Location: "+client_name);
        }
        txtDays.setText(days_remain+" days");

        return v;
    }


}
