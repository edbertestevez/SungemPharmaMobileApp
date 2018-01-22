package com.example.sungem.sungempharma.Admin.Sales;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Admin.Stocks.StocksFragment;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MedicineFragment extends Fragment {
    View view;
    private TextView mTextMessage;
    String retrieve_url,search_URL,scrollCheck = "all";

    GlobalFunctions globalFunctions;
    AlertDialog.Builder builder;
    public static final String SHAREDPREF = "medrepInfo";

    private ListView lvSales;
    private MedicineListAdapter adapter;
    private ArrayList<MedicineData> mMedicineList;
    public Handler mHandler;

    public View ftView;
    public boolean isLoading = false, isSearch = false;
    public int currentId=10;
    public int pageCounter = 1;
    SearchView searchView;

    //EDBERT
    String startDate, endDate;
    int medsales = 0;
    TextView txtTotalSales;

    public MedicineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_medicine, container, false);

        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());
        //retrieve URL
        retrieve_url = globalFunctions.adminUrl() + "sales_report.php";
        //search URL
        search_URL = globalFunctions.adminUrl() + "search_sales_report.php";

        lvSales = view.findViewById(R.id.listview_sales);
        LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);

        mHandler = new MedicineFragment.MyHandler();
        mMedicineList = new ArrayList<MedicineData>();
        builder = new AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);

        try {
            startDate = changeDateFormat(((SalesReportActivity)getActivity()).getStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            endDate = changeDateFormat(((SalesReportActivity)getActivity()).getEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtTotalSales = (TextView) view.findViewById(R.id.txtTotalSales);
        setHasOptionsMenu(true);

        lvSales.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //check when scroll to last item in listview, in this tut, init data is listview = 10 item
                if(view.getLastVisiblePosition() == totalItemCount-1 && lvSales.getCount() >=10 && isLoading == false){
                    isLoading = true;
                    pageCounter++;
                    Thread thread = new Thread(new MedicineFragment.ThreadGetMoreData(scrollCheck));
                    //Start Thread
                    thread.start();
                }
            }
        });

        getMedicineSales();
        
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.menuSearch);

        super.onCreateOptionsMenu(menu, inflater);

        final SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*pageCounter = 1;
                isSearch = true;
                adapter.clearItemAdapter();
                Toast.makeText(getActivity(), searchView.getQuery(), Toast.LENGTH_SHORT).show();
                Thread thread = new Thread(new StocksFragment.ThreadSearchRecords(query));
                //Start Thread
                thread.start();*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(!query.equals(null)|| !query.equals("")) {
                    //adapter.getFilter().filter(newText);
                    pageCounter = 1;
                    isSearch = true;
                    adapter.clearItemAdapter();
                    Thread thread = new Thread(new MedicineFragment.ThreadSearchRecords(query));
                    //Start Thread
                    thread.start();
                    return false;
                }else{
                    item.collapseActionView();
                    return false;
                }
            }
        });
    }

    public class ThreadGetMoreData implements Runnable{
        private String parameter;

        public ThreadGetMoreData(String parameter) {
            this.parameter = parameter;
        }

        public String getParameter() {
            return parameter;
        }

        @Override
        public void run() {
            //Add footer view after get data
            mHandler.sendEmptyMessage(0);
            //Search more data
            ArrayList<MedicineData> lstResult = getMoreData(getParameter());
            //Delay time to show loading footer when debug, remove it when release
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            //Send the result to Handle
            Message msg = mHandler.obtainMessage(1, lstResult);
            mHandler.sendMessage(msg);

        }
    }

    private ArrayList<MedicineData> getMoreData(final String parameter) {
        final ArrayList<MedicineData> lst = new ArrayList<>();
        if(isSearch==false && parameter == "all") {
            if (globalFunctions.isNetworkAvailable()) {
                //Connect to HTTPRequestToSendData and get result pfre
                StringRequest stringRequest = new StringRequest(Request.Method.POST, retrieve_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //response sang request
                                try {
                                    Log.e("Check : ",response);
                                    JSONArray jsonArray = new JSONArray(response);

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        //Based on php file
                                        String pro_id = jsonObject.getString("pro_id");
                                        String pro_brand = jsonObject.getString("pro_brand");
                                        String pro_generic = jsonObject.getString("pro_generic");
                                        String pro_formulation = jsonObject.getString("pro_formulation");
                                        String pro_sales = jsonObject.getString("pro_sales");
                                        String pro_qty = jsonObject.getString("pro_qty");
                                        medsales = jsonObject.getInt("total_sales");
                                        lst.add(new MedicineData(pro_id, pro_brand, pro_generic, pro_formulation, pro_sales,pro_qty));
                                        adapter.notifyDataSetChanged();

                                        adapter = new MedicineListAdapter(getActivity().getApplicationContext(), mMedicineList);
                                        //IMPORTANT! PARA INDI MAG RESET ANG POSITION SANG LIST VIEW SAKA BALIK SA FIRST RECORD KAY KATALAKA
                                        if(lvSales.getAdapter()==null){
                                            lvSales.setAdapter(adapter);
                                        }else{
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), "Last Record Reached", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    //PARAMETERS SA POST ACTION NA DI PRE
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("start",  startDate);
                        params.put("end", endDate);
                        params.put("type", "medicine");
                        params.put("page", String.valueOf(pageCounter));
                        return params;
                    }
                };
                //Send request sa singleton
                MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
            } else {
                builder.setTitle("No internet connection");
                builder.setMessage("Please connect to a network");
                displayAlert("no connection");
            }
        }else{
            if(globalFunctions.isNetworkAvailable()==true) {
                //Connect to HTTPRequestToSendData and get result pfre
                StringRequest stringRequest = new StringRequest(Request.Method.POST, search_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //response sang request
                                try {
                                    Log.e("Check : ",response);
                                    Log.e("Query check : ",parameter);
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        //Based on php file
                                        String pro_id = jsonObject.getString("pro_id");
                                        String pro_brand = jsonObject.getString("pro_brand");
                                        String pro_generic = jsonObject.getString("pro_generic");
                                        String pro_formulation = jsonObject.getString("pro_formulation");
                                        String pro_sales = jsonObject.getString("pro_sales");
                                        String pro_qty = jsonObject.getString("pro_qty");
                                        medsales = jsonObject.getInt("total_sales");
                                        mMedicineList.add(new MedicineData(pro_id, pro_brand, pro_generic, pro_formulation, pro_sales,pro_qty));
                                        adapter.notifyDataSetChanged();
                                        adapter = new MedicineListAdapter(getActivity().getApplicationContext(), mMedicineList);
                                        //IMPORTANT! PARA INDI MAG RESET ANG POSITION SANG LIST VIEW SAKA BALIK SA FIRST RECORD KAY KATALAKA
                                        if(lvSales.getAdapter()==null){
                                            lvSales.setAdapter(adapter);
                                        }else{
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    //PARAMETERS SA POST ACTION NA DI PRE
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("start",  startDate);
                        params.put("end", endDate);
                        params.put("type", "medicine");
                        params.put("q", parameter);
                        params.put("page", String.valueOf(pageCounter));
                        return params;
                    }
                };
                //Send request sa singleton
                MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
            }
            else{
                builder.setTitle("No internet connection");
                builder.setMessage("Please connect to a network");
                displayAlert("no connection");
            }
        }

        return lst;
    }

    //FOR ALERT BOX
    public void displayAlert(final String code){
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Gina pang return na response ka server or input based sa displayAlert(parameter)
                switch (code){
                    case "input_error":
                        //ACTIONS KUNG BLANGKO ANG FIELDS
                        break;
                    case "retrieve_failed":
                        //ACTION
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public String changeDateFormat(String strDate) throws ParseException {
        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat oldFormat = new SimpleDateFormat("MM/dd/yy");
        Date oldDate = oldFormat.parse(strDate);
        String newDate = newFormat.format(oldDate);
        return newDate;
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //Add loading view during search processing
                    lvSales.addFooterView(ftView);
                    break;
                case 1:
                    //Update data adapter and UI
                    adapter.addListItemToAdapter((ArrayList<MedicineData>)msg.obj);
                    //Remove loading view after update listview
                    lvSales.removeFooterView(ftView);
                    isLoading=false;
                    break;
                case 2:
                    //Update data adapter and UI
                    adapter.addSearchItemToAdapter((ArrayList<MedicineData>)msg.obj);
                    //Remove loading view after update listview
                    lvSales.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;
            }
        }
    }

    public class ThreadSearchRecords implements Runnable{

        private String parameter;

        public ThreadSearchRecords(String parameter) {
            this.parameter = parameter;
        }

        public String getParameter() {
            return parameter;
        }


        @Override
        public void run() {
            //Add footer view after get data
            mHandler.sendEmptyMessage(0);
            //Search more data
            ArrayList<MedicineData> lstResult = searchRecords(getParameter());
            //Delay time to show loading footer when debug, remove it when release
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            //Send the result to Handle
            Message msg = mHandler.obtainMessage(2, lstResult);
            mHandler.sendMessage(msg);

        }
    }

    private ArrayList<MedicineData> searchRecords(final String parameter){
        final ArrayList<MedicineData>lst = new ArrayList<>();
        if(globalFunctions.isNetworkAvailable()==true) {
            //Connect to HTTPRequestToSendData and get result pfre
            StringRequest stringRequest = new StringRequest(Request.Method.POST, search_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            try {
                                Log.e("Check : ",response);
                                Log.e("Query check : ",parameter);
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Based on php file
                                    String pro_id = jsonObject.getString("pro_id");
                                    String pro_brand = jsonObject.getString("pro_brand");
                                    String pro_generic = jsonObject.getString("pro_generic");
                                    String pro_formulation = jsonObject.getString("pro_formulation");
                                    String pro_sales = jsonObject.getString("pro_sales");
                                    String pro_qty = jsonObject.getString("pro_qty");
                                    medsales = jsonObject.getInt("total_sales");
                                    mMedicineList.add(new MedicineData(pro_id, pro_brand, pro_generic, pro_formulation, pro_sales,pro_qty));
                                    adapter.notifyDataSetChanged();

                                    adapter = new MedicineListAdapter(getActivity().getApplicationContext(), mMedicineList);
                                    //IMPORTANT! PARA INDI MAG RESET ANG POSITION SANG LIST VIEW SAKA BALIK SA FIRST RECORD KAY KATALAKA
                                    if(lvSales.getAdapter()==null){
                                        lvSales.setAdapter(adapter);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                //PARAMETERS SA POST ACTION NA DI PRE
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("start",  startDate);
                    params.put("end", endDate);
                    params.put("type", "medicine");
                    params.put("q", parameter);
                    params.put("page", String.valueOf(pageCounter));
                    return params;
                }
            };
            //Send request sa singleton
            MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
        }
        else{
            builder.setTitle("No internet connection");
            builder.setMessage("Please connect to a network");
            displayAlert("no connection");
        }
        return lst;
    }
    
    public void getMedicineSales(){
        if(globalFunctions.isNetworkAvailable()==true) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Retrieving data....");
            progressDialog.show();
            //Connect to HTTPRequestToSendData and get result pfre
            StringRequest stringRequest = new StringRequest(Request.Method.POST, retrieve_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            progressDialog.dismiss();
                            try {

                                Log.e("Check : ",response);
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Based on php file
                                    String pro_id = jsonObject.getString("pro_id");
                                    String pro_brand = jsonObject.getString("pro_brand");
                                    String pro_generic = jsonObject.getString("pro_generic");
                                    String pro_formulation = jsonObject.getString("pro_formulation");
                                    String pro_sales = jsonObject.getString("pro_sales");
                                    String pro_qty = jsonObject.getString("pro_qty");
                                    medsales = jsonObject.getInt("total_sales");
                                    mMedicineList.add(new MedicineData(pro_id, pro_brand, pro_generic, pro_formulation, pro_sales,pro_qty));

                                    adapter = new MedicineListAdapter(getActivity().getApplicationContext(), mMedicineList);
                                    //IMPORTANT! PARA INDI MAG RESET ANG POSITION SANG LIST VIEW SAKA BALIK SA FIRST RECORD KAY KATALAKA
                                    if(lvSales.getAdapter()==null){
                                        lvSales.setAdapter(adapter);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                //set total value
                                txtTotalSales.setText("P"+medsales);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                //PARAMETERS SA POST ACTION NA DI PRE
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("start",  startDate);
                    params.put("end", endDate);
                    params.put("type", "medicine");
                    params.put("page", String.valueOf(pageCounter));
                    return params;
                }
            };
            //Send request sa singleton
            MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
        }
        else{
            builder.setTitle("No internet connection");
            builder.setMessage("Please connect to a network");
            displayAlert("no connection");
        }
    }
}
