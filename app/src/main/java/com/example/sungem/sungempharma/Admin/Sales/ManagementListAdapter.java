package com.example.sungem.sungempharma.Admin.Sales;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.R;

import java.util.List;

/**
 * Created by trebd on 9/9/2017.
 */

public class ManagementListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ManagementData> mManagementList;

    //Constructor

    public ManagementListAdapter(Context mContext, List<ManagementData> mManagementList) {
        this.mContext = mContext;
        this.mManagementList = mManagementList;
    }

    public void addListItemToAdapter(List<ManagementData> list){
        //Add list to current array list of data
        mManagementList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<ManagementData> list){
        //Add list to current array list of data
        mManagementList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        mManagementList.clear();

    }
    @Override
    public int getCount() {
        return mManagementList.size();
    }

    @Override
    public Object getItem(int position) {
        return mManagementList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.management_list_layout, null);
        TextView txtClientName = (TextView)v.findViewById(R.id.txtClientName);
        TextView txtClientSales = (TextView)v.findViewById(R.id.txtClientSales);

        final String client_id = mManagementList.get(position).getClient_id();
        final String client_name = mManagementList.get(position).getClient_name();
        final String client_sales = mManagementList.get(position).getClient_sales();

        //Set text for TextView
        txtClientName.setText(client_name);
        txtClientSales.setText(client_sales);

        if(mManagementList.get(position).getClient_id() != null) {
            //Save product id to tag
            v.setTag(mManagementList.get(position).getClient_id());
        }

        /*
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, client_name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, SelectedStockActivity.class);
                intent.putExtra("PRODUCT_ID",pro_id);
                intent.putExtra("PRODUCT_NAME",pro_name);
                intent.putExtra("PRODUCT_DESC",pro_desc);
                intent.putExtra("PRODUCT_QTY",pro_qty);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        */
        return v;
    }


}
