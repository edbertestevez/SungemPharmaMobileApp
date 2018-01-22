package com.example.sungem.sungempharma.Medrep.Performance;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Medrep.Dashboard.SalesMedrepActivity;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformanceMainFragment extends Fragment {

    DBController dbController;
    GlobalFunctions globalFunctions;
    View view;
    Button btnSales;

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    ImageView myImage;
    TextView txtMedrep, txtSales, txtCommission, txtDate, txtMonth, txtProducts, txtDelivery, txtMedSales;

    public PerformanceMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_performance_main, container, false);

        txtMedrep = (TextView) view.findViewById(R.id.txtMedrep);
        txtSales = (TextView) view.findViewById(R.id.txtProSales);
        txtCommission = (TextView) view.findViewById(R.id.txtCommission);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtMonth = (TextView) view.findViewById(R.id.txtMonth);
        txtProducts = (TextView) view.findViewById(R.id.txtProducts);
        txtDelivery = (TextView) view.findViewById(R.id.txtDelivery);
        btnSales = (Button) view.findViewById(R.id.btnSales);
        myImage = (ImageView) view.findViewById(R.id.myImage);
        txtMedSales = (TextView) view.findViewById(R.id.txtMedSales);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Performance");

        setHasOptionsMenu(true);

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        String userimg = sharedpref.getString("USERIMAGE","");
        if(!userimg.equals("")) {
            Picasso.with(getActivity().getApplicationContext()).load(globalFunctions.externalUrl() + userimg).into(myImage);
        }else{
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.logo_orig).into(myImage);
        }

        String[] month = {"null","January","February","March","April","May","June","July","August","September","October","November","December"};
        SimpleDateFormat df = new SimpleDateFormat("d, yyyy");
        Date currDate = Calendar.getInstance().getTime();
        String date_today = df.format(currDate);

        DateFormat monthFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        int currmonth = Integer.parseInt(monthFormat.format(date));

        txtMedrep.setText(sharedpref.getString("USERNAME",""));
        txtDate.setText(month[currmonth]+" "+date_today);

        FloatingActionButton fab = ((MedrepMainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        displayPerformance();

        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SalesMedrepActivity.class);
                intent.putExtra("MEDREP_ID",sharedpref.getString("USERID",""));
                startActivity(intent);
            }
        });
        return view;
    }

    //BUTTONS SA MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.activity_medrep_main_actions, menu);
        super.onCreateOptionsMenu(menu, menuInflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sync: {
                displayPerformance();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void displayPerformance() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Retrieving Performance Info");
            progressDialog.show();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "performance.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Log.i("STRING REQUEST: ", obj.toString());
                                    if (obj.getString("total_sales").equals("null")) {
                                        txtSales.setText("P" + "0");
                                    } else {
                                        txtSales.setText("P" + obj.getString("total_sales"));
                                    }

                                    if (obj.getString("total_commission").equals("null")) {
                                        txtCommission.setText("P" + "0");
                                    } else {
                                        txtCommission.setText("P" + obj.getString("total_commission"));
                                    }

                                    if (obj.getString("total_delivery").equals("null")) {
                                        txtDelivery.setText("0");
                                    } else {
                                        txtDelivery.setText(obj.getString("total_delivery"));
                                    }

                                    if (obj.getString("total_products").equals("null")) {
                                        txtProducts.setText("0");
                                    } else {
                                        txtProducts.setText(obj.getString("total_products"));
                                    }

                                    if (obj.getString("medicine_sales").equals("null")) {
                                        txtMedSales.setText("P0");
                                    } else {
                                        txtMedSales.setText("P"+obj.getString("medicine_sales"));
                                    }
                                }
                                Toast.makeText(getActivity(), "Record Complete", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "No Record Available", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Syncing Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("No internet connection");
            builder.setMessage("Please connect to a network");
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}
