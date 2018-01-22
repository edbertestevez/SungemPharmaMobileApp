package com.example.sungem.sungempharma.Admin.Dashboard;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Admin.Sales.SalesReportActivity;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    TextView dashTitle, txtConsignSales, txtManagementSales, txtRestock, txtExpiry, txtMedicineSales;
    GlobalFunctions globalFunctions;

    Button btnRestock, btnExpiry, btnMedSales, btnConSales, btnManSales;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_dashboard);

        dashTitle = (TextView) view.findViewById(R.id.dashTitle);
        txtConsignSales = (TextView) view.findViewById(R.id.txtConsignSales);
        txtManagementSales = (TextView) view.findViewById(R.id.txtManagementSales);
        txtRestock = (TextView) view.findViewById(R.id.txtRestock);
        txtExpiry = (TextView) view.findViewById(R.id.txtExpiry);
        txtMedicineSales = (TextView) view.findViewById(R.id.txtMedicineSales);

        btnRestock = (Button) view.findViewById(R.id.btnRestock);
        btnExpiry = (Button) view.findViewById(R.id.btnExpiry);
        btnMedSales = (Button) view.findViewById(R.id.btnMedsales);
        btnConSales = (Button) view.findViewById(R.id.btnConSales);
        btnManSales = (Button) view.findViewById(R.id.btnManSales);

        btnMedSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthMedSales();
            }
        });

        btnManSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthManSales();
            }
        });

        btnConSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMonthConSales();
            }
        });

        btnRestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RestockActivity.class);
                getActivity().startActivity(intent);
            }
        });

        btnExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                Calendar c = Calendar.getInstance();   // this takes current date
                c.set(Calendar.DAY_OF_MONTH, 1);
                Date calendarStart = c.getTime();
                String startDate = sdf.format(calendarStart.getTime());

                c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date calendarEnd = c.getTime();
                String endDate = sdf.format(calendarEnd.getTime());

                Intent intent = new Intent(getActivity(), AdminExpiryActivity.class);
                intent.putExtra("START_DATE",startDate);
                intent.putExtra("END_DATE",endDate);

                getActivity().startActivity(intent);
            }
        });

        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        displayDashboard();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    public void getMonthManSales(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date calendarStart = c.getTime();
        String startDate = sdf.format(calendarStart.getTime());

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date calendarEnd = c.getTime();
        String endDate = sdf.format(calendarEnd.getTime());

        Intent intent = new Intent(getActivity(), SalesReportActivity.class);
        intent.putExtra("START_DATE",startDate);
        intent.putExtra("END_DATE",endDate);
        intent.putExtra("PAGE","dashboard");
        intent.putExtra("TAB","1");
        getActivity().startActivity(intent);
    }

    public void getMonthMedSales(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date calendarStart = c.getTime();
        String startDate = sdf.format(calendarStart.getTime());

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date calendarEnd = c.getTime();
        String endDate = sdf.format(calendarEnd.getTime());

        Intent intent = new Intent(getActivity(), SalesReportActivity.class);
        intent.putExtra("START_DATE",startDate);
        intent.putExtra("END_DATE",endDate);
        intent.putExtra("PAGE","dashboard");
        intent.putExtra("TAB","0");
        getActivity().startActivity(intent);
    }


    public void getMonthConSales(){
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date calendarStart = c.getTime();
        String startDate = sdf.format(calendarStart.getTime());

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date calendarEnd = c.getTime();
        String endDate = sdf.format(calendarEnd.getTime());

        Intent intent = new Intent(getActivity(), ConsignSalesActivity.class);
        intent.putExtra("START_DATE",startDate);
        intent.putExtra("END_DATE",endDate);
        intent.putExtra("PAGE","dashboard");
        getActivity().startActivity(intent);
    }

    public void displayDashboard() {
        Calendar c = Calendar.getInstance();
        int curr_year = c.get(Calendar.YEAR);
        int curr_month = c.get(Calendar.MONTH);
        dashTitle.setText("For the Month of "+globalFunctions.stringMonth(curr_month)+" "+curr_year);


        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Retrieving data....");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.adminUrl() + "dashboard.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);

                                    txtExpiry.setText(obj.getString("expiry"));
                                    txtConsignSales.setText("P"+obj.getString("consign_sales"));
                                    txtManagementSales.setText("P"+obj.getString("management_sales"));
                                    txtMedicineSales.setText("P"+obj.getString("medicine_sales"));
                                    txtRestock.setText(obj.getString("restock"));
                                }

                            } catch (JSONException e) {
                                txtExpiry.setText("N/A");
                                txtConsignSales.setText("N/A");
                                txtManagementSales.setText("N/A");
                                txtMedicineSales.setText("N/A");
                                txtRestock.setText("N/A");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "An error occured", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
        }else{
            Toast.makeText(getActivity(), "No Internet Connnection", Toast.LENGTH_SHORT).show();
        }
    }

}
