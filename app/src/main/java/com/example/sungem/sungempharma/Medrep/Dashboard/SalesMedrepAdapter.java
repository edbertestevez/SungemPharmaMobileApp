package com.example.sungem.sungempharma.Medrep.Dashboard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.R;

import java.util.List;

/**
 * Created by trebd on 10/3/2017.
 */

public class SalesMedrepAdapter extends BaseAdapter {

    private Context mContext;
    private List<SalesMedrepData> salesList;

    //Constructor

    public SalesMedrepAdapter(Context mContext, List<SalesMedrepData> salesList) {
        this.mContext = mContext;
        this.salesList = salesList;
    }

    public void addListItemToAdapter(List<SalesMedrepData> list){
        //Add list to current array list of data
        salesList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<SalesMedrepData> list){
        //Add list to current array list of data
        salesList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        salesList.clear();

    }
    @Override
    public int getCount() {
        return salesList.size();
    }

    @Override
    public Object getItem(int position) {
        return salesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.stock_list_layout, null);
        TextView txtClientName = (TextView)v.findViewById(R.id.tv_proname);
        TextView txtSales = (TextView)v.findViewById(R.id.tv_proqty);
        TextView txtDateMeans = (TextView)v.findViewById(R.id.tv_prodesc);

        final String client_name = salesList.get(position).getC_name();
        final String pay_sales = salesList.get(position).getP_amt();
        final String date_means = salesList.get(position).getP_date()+" ("+salesList.get(position).getP_means()+")";

        //Set text for TextView
        txtClientName.setText(client_name);
        txtSales.setText(pay_sales);
        txtDateMeans.setText(date_means);
        
        return v;
    }


}
