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
 * Created by Jaco on 9/1/2017.
 */

public class ProductListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Product> mProductList;

    //Constructor

    public ProductListAdapter(Context mContext, List<Product> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    public void addListItemToAdapter(List<Product> list){
        //Add list to current array list of data
        mProductList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<Product> list){
        //Add list to current array list of data
        mProductList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        mProductList.clear();

    }
    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.stock_list_layout, null);
        TextView tvProName = (TextView)v.findViewById(R.id.tv_proname);
        TextView tvProQty = (TextView)v.findViewById(R.id.tv_proqty);
        TextView tvProDesc = (TextView)v.findViewById(R.id.tv_prodesc);

        final String pro_id = mProductList.get(position).getPro_id();
        final String pro_name = mProductList.get(position).getPro_name();
        final String pro_qty = mProductList.get(position).getPro_qty();
        final String pro_desc = mProductList.get(position).getPro_desc();

        //Set text for TextView
        tvProName.setText(pro_name);
        tvProQty.setText(pro_qty);
        tvProDesc.setText(pro_desc);

        if(mProductList.get(position).getPro_id() != null) {
            //Save product id to tag
            v.setTag(mProductList.get(position).getPro_id());
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, pro_name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, SelectedStockActivity.class);
                intent.putExtra("PRODUCT_ID",pro_id);
                intent.putExtra("PRODUCT_NAME",pro_name);
                intent.putExtra("PRODUCT_DESC",pro_desc);
                intent.putExtra("PRODUCT_QTY",pro_qty);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return v;
    }


}
