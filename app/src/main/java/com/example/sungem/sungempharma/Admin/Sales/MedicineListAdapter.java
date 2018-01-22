package com.example.sungem.sungempharma.Admin.Sales;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sungem.sungempharma.R;

import java.util.List;

/**
 * Created by trebd on 9/10/2017.
 */

public class MedicineListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MedicineData> mMedicineList;

    //Constructor

    public MedicineListAdapter(Context mContext, List<MedicineData> mMedicineList) {
        this.mContext = mContext;
        this.mMedicineList = mMedicineList;
    }

    public void addListItemToAdapter(List<MedicineData> list){
        //Add list to current array list of data
        mMedicineList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void addSearchItemToAdapter(List<MedicineData> list){
        //Add list to current array list of data
        mMedicineList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    public void clearItemAdapter(){
        //Add list to current array list of data
        mMedicineList.clear();

    }
    @Override
    public int getCount() {
        return mMedicineList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMedicineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.medsales_item, null);//SAME MAN LANG SILA LAYOUT
        TextView txtProBrand = (TextView)v.findViewById(R.id.txtProBrand);
        TextView txtProGeneric = (TextView)v.findViewById(R.id.txtProGeneric);
        TextView txtProSales = (TextView)v.findViewById(R.id.txtProSales);
        TextView txtProFormulation = (TextView)v.findViewById(R.id.txtProFormulation);
        TextView txtProQty = (TextView)v.findViewById(R.id.txtProQty);

        final String pro_id = mMedicineList.get(position).getPro_id();
        final String pro_brand = mMedicineList.get(position).getPro_brand();
        final String pro_generic = mMedicineList.get(position).getPro_generict();
        final String pro_formulation = mMedicineList.get(position).getPro_formulation();
        final String pro_sales = mMedicineList.get(position).getPro_sales();
        final String pro_qty = mMedicineList.get(position).getPro_qty();

        //Set text for TextView
        txtProBrand.setText(pro_brand);
        txtProGeneric.setText(pro_generic);
        txtProFormulation.setText(pro_formulation);
        txtProSales.setText(pro_sales);
        if(pro_qty.equals("null")){
            txtProQty.setText("0");
        }else {
            txtProQty.setText(pro_qty);
        }

        if(mMedicineList.get(position).getPro_id() != null) {
            //Save product id to tag
            v.setTag(mMedicineList.get(position).getPro_id());
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
