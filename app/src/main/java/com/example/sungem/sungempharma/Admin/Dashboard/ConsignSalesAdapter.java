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

public class ConsignSalesAdapter extends BaseAdapter {

    private Context mContext;
    private List<ConsignSalesData> consignList;

    //Constructor

    public ConsignSalesAdapter(Context mContext, List<ConsignSalesData> consignList) {
        this.mContext = mContext;
        this.consignList = consignList;
    }

    public void addListItemToAdapter(List<ConsignSalesData> list){
        //Add list to current array list of data
        consignList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<ConsignSalesData> list){
        //Add list to current array list of data
        consignList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        consignList.clear();

    }
    @Override
    public int getCount() {
        return consignList.size();
    }

    @Override
    public Object getItem(int position) {
        return consignList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.medsales_item, null);//SAME MAN LANG SILA LAYOUT
        final String pro_brand = consignList.get(position).getPro_brand();
        final String pro_generic = consignList.get(position).getPro_generic();
        final String pro_formulation = consignList.get(position).getPro_formulation();
        final String qty_sold = consignList.get(position).getPro_qty();
        final String pro_sales = consignList.get(position).getPro_sales();

        TextView txtProBrand = (TextView) v.findViewById(R.id.txtProBrand);
        TextView txtProGeneric = (TextView) v.findViewById(R.id.txtProGeneric);
        TextView txtProFormulation = (TextView) v.findViewById(R.id.txtProFormulation);
        TextView txtProSales = (TextView) v.findViewById(R.id.txtProSales);
        TextView txtProQty = (TextView) v.findViewById(R.id.txtProQty);
        TextView txtQtyDesc = (TextView) v.findViewById(R.id.txtQtyDesc);

        txtQtyDesc.setVisibility(View.VISIBLE);
        txtProQty.setVisibility(View.VISIBLE);

        txtProBrand.setText(pro_brand);
        txtProGeneric.setText(pro_generic);
        txtProFormulation.setText(pro_formulation);
        txtProSales.setText(pro_sales);
        txtProQty.setText(qty_sold);

        return v;
    }

}
