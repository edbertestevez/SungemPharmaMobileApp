package com.example.sungem.sungempharma.Medrep.Dashboard;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryAdapter;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.Service.ServiceBroadcastReceiver;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    DBController dbController;
    GlobalFunctions globalFunctions;
    View view;

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    TextView dashTitle;
    TextView txtClientCount;
    TextView txtExpiryCount;
    TextView txtDeliveryCount;
    TextView txtInvoiceCount;
    TextView txtSalesCount;

    NavigationView drawer_navigation;
    Intent intentPage;

    Button btnSales, btnExpiry, btnConsignment, btnInvoice, btnDelivery;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        FloatingActionButton fab = ((MedrepMainActivity) getActivity()).getFloatingActionButton();
        fab.hide();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Dashboard");

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        dashTitle = (TextView) view.findViewById(R.id.dashTitle);
        txtClientCount = (TextView) view.findViewById(R.id.txtClientCount);
        txtDeliveryCount = (TextView) view.findViewById(R.id.txtDeliveryCount);
        txtInvoiceCount = (TextView) view.findViewById(R.id.txtInvoiceCount);
        txtSalesCount = (TextView) view.findViewById(R.id.txtSalesCount);
        txtExpiryCount = (TextView) view.findViewById(R.id.txtExpiryCount);

        btnSales = (Button) view.findViewById(R.id.btnSales);
        btnExpiry = (Button) view.findViewById(R.id.btnExpiry);
        btnConsignment = (Button) view.findViewById(R.id.btnConsignment);
        btnInvoice = (Button) view.findViewById(R.id.btnInvoice);
        btnDelivery = (Button) view.findViewById(R.id.btnDelivery);

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentPage = new Intent(getActivity(), MedrepMainActivity.class);
                intentPage.putExtra("PAGE","delivery");
                startActivity(intentPage);
            }
        });

        btnConsignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentPage = new Intent(getActivity(), MedrepMainActivity.class);
                intentPage.putExtra("PAGE","stocklevel");
                startActivity(intentPage);
            }
        });

        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentPage = new Intent(getActivity(), MedrepMainActivity.class);
                intentPage.putExtra("PAGE","payment");
                startActivity(intentPage);
            }
        });

        btnSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SalesMedrepActivity.class);
                intent.putExtra("MEDREP_ID",sharedpref.getString("USERID",""));
                startActivity(intent);
            }
        });

        btnExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MedrepExpiryActivity.class);
                intent.putExtra("MEDREP_ID",sharedpref.getString("USERID",""));
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);
        displayDashboard();


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
                displayDashboard();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**NEEDED FOR SERVICE TO UPDATE CONTENT**/
    @Override
    public void onResume() {
        getActivity().registerReceiver(serviceBroadcastReceiver, new IntentFilter(SungemService.BROADCAST_ACTION));
        displayDashboard();

        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(serviceBroadcastReceiver);
        super.onPause();
    }

    private ServiceBroadcastReceiver serviceBroadcastReceiver = new ServiceBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            //CHECKER IF MAY UPDATE SA DELIVERY
            if(intent.hasExtra("DELIVERY_UPDATE") || intent.hasExtra("PAYMENT_UPDATE") || intent.hasExtra("MONITOR_UPDATE") || intent.hasExtra("SALES_UPDATE")){
                displayDashboard();

            }

        }
    };

    public void displayDashboard() {
        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        Calendar c = Calendar.getInstance();
        int curr_year = c.get(Calendar.YEAR);
        int curr_month = c.get(Calendar.MONTH);
        dashTitle.setText("For the Month of "+globalFunctions.stringMonth(curr_month)+" "+curr_year);
        txtDeliveryCount.setText(""+dbController.getPendingDeliveryCountNative(sharedpref.getString("USERID","")));
        txtClientCount.setText(""+dbController.getClientCount(sharedpref.getString("USERID","")));
        txtInvoiceCount.setText(""+dbController.getInvoiceCount(sharedpref.getString("USERID","")));
        txtExpiryCount.setText(""+dbController.getNearExpiryCount(sharedpref.getString("USERID","")));
        txtSalesCount.setText("P"+dbController.getCurrentSalesCount(sharedpref.getString("USERID","")));
    }
}
