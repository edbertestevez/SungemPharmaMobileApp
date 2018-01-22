package com.example.sungem.sungempharma.Admin.Stocks;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.R;

import java.util.List;

/**
 * Created by trebd on 9/9/2017.
 */

public class StockListAdapter  extends BaseAdapter {

    private Context mContext;
    private List<StockLotData> mLotList;

    //Constructor

    public StockListAdapter(Context mContext, List<StockLotData> mLotList) {
        this.mContext = mContext;
        this.mLotList = mLotList;
    }

    public void addListItemToAdapter(List<StockLotData> list){
        //Add list to current array list of data
        mLotList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<StockLotData> list){
        //Add list to current array list of data
        mLotList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        mLotList.clear();

    }
    @Override
    public int getCount() {
        return mLotList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.stock_lot_avail_item, null);
        TextView txtLotNumber = (TextView)v.findViewById(R.id.txtLotNumber);
        TextView txtLotQty = (TextView)v.findViewById(R.id.txtLotQty);
        TextView txtLotExpiry = (TextView)v.findViewById(R.id.txtLotExpiry);

        final String pro_id = mLotList.get(position).getPro_id();
        final String lot_number = mLotList.get(position).getLot_number();
        final String lot_qty = mLotList.get(position).getLot_qty();
        final String lot_expiry = mLotList.get(position).getLot_expiry();

        //Set text for TextView
        txtLotNumber.setText(lot_number);
        txtLotQty.setText(lot_qty);
        txtLotExpiry.setText(lot_expiry);

        if(mLotList.get(position).getPro_id() != null) {
            //Save product id to tag
            v.setTag(mLotList.get(position).getPro_id());
        }


        return v;
    }


}
