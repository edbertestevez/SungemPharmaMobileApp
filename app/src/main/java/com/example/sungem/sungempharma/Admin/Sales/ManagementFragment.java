package com.example.sungem.sungempharma.Admin.Sales;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.sungem.sungempharma.Admin.Stocks.Product;
import com.example.sungem.sungempharma.Admin.Stocks.ProductListAdapter;
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
public class ManagementFragment extends Fragment {

    View view;
    private TextView mTextMessage;
    String retrieve_url,search_URL,scrollCheck = "all";
    Button btnLogout;
    GlobalFunctions globalFunctions;
    AlertDialog.Builder builder;
    public static final String SHAREDPREF = "medrepInfo";

    private ListView lvSales;
    private ManagementListAdapter adapter;
    private ArrayList<ManagementData> mManagementList;
    public Handler mHandler;

    public View ftView;
    public boolean isLoading = false, isSearch = false;
    public int currentId=10;
    public int pageCounter = 1;
    SearchView searchView;

    //EDBERT
    String startDate, endDate;
    int managementSales = 0;
    TextView txtTotalSales;

    public ManagementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_management, container, false);

        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());
        //retrieve URL
        retrieve_url = globalFunctions.adminUrl() + "sales_report.php";
        //search URL
        search_URL = globalFunctions.adminUrl() + "search_sales_report.php";

        lvSales = view.findViewById(R.id.listview_sales);
        LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);

        mHandler = new ManagementFragment.MyHandler();
        mManagementList = new ArrayList<ManagementData>();
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

        getManagementSales();

        //setHasOptionsMenu(true);

        return view;
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.menuSearch);

        super.onCreateOptionsMenu(menu, inflater);

        final SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                pageCounter = 1;
                isSearch = true;
                adapter.clearItemAdapter();
                Toast.makeText(getActivity(), searchView.getQuery(), Toast.LENGTH_SHORT).show();
                Thread thread = new Thread(new StocksFragment.ThreadSearchRecords(query));
                //Start Thread
                thread.start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(!query.equals(null)|| !query.equals("")) {
                    //adapter.getFilter().filter(newText);
                    pageCounter = 1;
                    isSearch = true;
                    adapter.clearItemAdapter();
                    Thread thread = new Thread(new ManagementFragment.ThreadSearchRecords(query));
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
            ArrayList<ManagementData> lstResult = searchRecords(getParameter());
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

    private ArrayList<ManagementData> searchRecords(final String parameter){
        final ArrayList<ManagementData>lst = new ArrayList<>();
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
                                    String client_id = jsonObject.getString("client_id");
                                    String client_name = jsonObject.getString("client_name");
                                    String client_sales = jsonObject.getString("sales");

                                    mManagementList.add(new ManagementData(client_id, client_name, client_sales));
                                    adapter.notifyDataSetChanged();

                                    adapter = new ManagementListAdapter(getActivity().getApplicationContext(), mManagementList);
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
                    params.put("type", "management");
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
    */
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
            ArrayList<ManagementData> lstResult = getMoreData(getParameter());
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

    private ArrayList<ManagementData> getMoreData(final String parameter) {

        final ArrayList<ManagementData> lst = new ArrayList<>();
        if(!isSearch && parameter == "all") {
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
                                        String client_id = jsonObject.getString("client_id");
                                        String client_name = jsonObject.getString("client_name");
                                        String client_sales = jsonObject.getString("sales");

                                        lst.add(new ManagementData(client_id, client_name, client_sales));
                                        adapter.notifyDataSetChanged();

                                        adapter = new ManagementListAdapter(getActivity().getApplicationContext(), mManagementList);
                                        //IMPORTANT! PARA INDI MAG RESET ANG POSITION SANG LIST VIEW SAKA BALIK SA FIRST RECORD KAY KATALAKA
                                        if(lvSales.getAdapter()==null){
                                            lvSales.setAdapter(adapter);
                                        }else{
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
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
                        params.put("type", "management");
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
                                        String client_id = jsonObject.getString("client_id");
                                        String client_name = jsonObject.getString("client_name");
                                        String client_sales = jsonObject.getString("sales");

                                        mManagementList.add(new ManagementData(client_id, client_name, client_sales));

                                        adapter.notifyDataSetChanged();
                                        adapter = new ManagementListAdapter(getActivity().getApplicationContext(), mManagementList);
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
                        params.put("type", "management");
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
                    adapter.addListItemToAdapter((ArrayList<ManagementData>)msg.obj);
                    //Remove loading view after update listview
                    lvSales.removeFooterView(ftView);
                    isLoading=false;
                    break;
                case 2:
                    //Update data adapter and UI
                    adapter.addSearchItemToAdapter((ArrayList<ManagementData>)msg.obj);
                    //Remove loading view after update listview
                    lvSales.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;
            }
        }
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

    public void getManagementSales(){
        if(globalFunctions.isNetworkAvailable()==true) {
            //Connect to HTTPRequestToSendData and get result pfre
            StringRequest stringRequest = new StringRequest(Request.Method.POST, retrieve_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            try {
                                Log.e("Check : ",response);
                                JSONArray jsonArray = new JSONArray(response);
                                for(int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //Based on php file
                                    String client_id = jsonObject.getString("client_id");
                                    String client_name = jsonObject.getString("client_name");
                                    String client_sales = jsonObject.getString("sales");
                                    managementSales = jsonObject.getInt("overall");

                                    mManagementList.add(new ManagementData(client_id, client_name, client_sales));

                                    adapter = new ManagementListAdapter(getActivity().getApplicationContext(), mManagementList);
                                    //IMPORTANT! PARA INDI MAG RESET ANG POSITION SANG LIST VIEW SAKA BALIK SA FIRST RECORD KAY KATALAKA
                                    if(lvSales.getAdapter()==null){
                                        lvSales.setAdapter(adapter);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                //set total value
                                txtTotalSales.setText("P"+managementSales);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "ERROR OCCURED", Toast.LENGTH_SHORT).show();
                }
            }) {
                //PARAMETERS SA POST ACTION NA DI PRE
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("start",  startDate);
                    params.put("end", endDate);
                    params.put("type", "management");
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
