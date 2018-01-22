package com.example.sungem.sungempharma.Admin.Dashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.R;

import java.util.List;

/**
 * Created by trebd on 10/4/2017.
 */

public class RestockAdapter extends BaseAdapter {

    private Context mContext;
    private List<RestockData> restockList;

    //Constructor

    public RestockAdapter(Context mContext, List<RestockData> restockList) {
        this.mContext = mContext;
        this.restockList = restockList;
    }

    public void addListItemToAdapter(List<RestockData> list){
        //Add list to current array list of data
        restockList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<RestockData> list){
        //Add list to current array list of data
        restockList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        restockList.clear();

    }
    @Override
    public int getCount() {
        return restockList.size();
    }

    @Override
    public Object getItem(int position) {
        return restockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.medrep_expiry_item, null);//SAME MAN LANG SILA LAYOUT

        final String pro_brand = restockList.get(position).getPro_brand();
        final String pro_generic = restockList.get(position).getPro_generic();
        final String pro_formulation = restockList.get(position).getPro_formulation();
        final String pro_qty = restockList.get(position).getPro_qty();
        final String pro_reorder = restockList.get(position).getPro_reorder();
        final String pending_orders = restockList.get(position).getPending_orders();
        final String actual_remain = restockList.get(position).getActual_remain();

        TextView txtProBrand = (TextView) v.findViewById(R.id.txtProBrand);
        TextView txtProGenericFormulation = (TextView) v.findViewById(R.id.txtProGenericFormulation);
        TextView txtQty = (TextView) v.findViewById(R.id.txtQty);
        TextView txtLot = (TextView) v.findViewById(R.id.txtLot);
        TextView txtExpiry = (TextView) v.findViewById(R.id.txtExpiry);
        TextView txtClient = (TextView) v.findViewById(R.id.txtClient);
        TextView txtLotDesc = (TextView) v.findViewById(R.id.txtLotDesc);
        TextView txtExpiryDesc = (TextView) v.findViewById(R.id.txtExpiryDesc);

        txtProBrand.setText(pro_brand);
        txtProGenericFormulation.setText(pro_generic+"("+pro_formulation+")");
        txtLot.setText(pro_qty);
        txtQty.setText(actual_remain);
        txtLotDesc.setText("Warehouse Stock: ");
        txtExpiryDesc.setText("Pending Orders: ");
        txtExpiry.setText(pending_orders);
        txtClient.setText("Reorder Level: "+pro_reorder);

        return v;
    }

}
