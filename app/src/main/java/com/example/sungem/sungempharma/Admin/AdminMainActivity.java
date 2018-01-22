package com.example.sungem.sungempharma.Admin;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.sungem.sungempharma.Admin.Dashboard.DashboardFragment;
import com.example.sungem.sungempharma.Admin.Profile.ProfileFragment;
import com.example.sungem.sungempharma.Admin.Sales.SalesFragment;
import com.example.sungem.sungempharma.Admin.Stocks.Product;
import com.example.sungem.sungempharma.Admin.Stocks.ProductListAdapter;
import com.example.sungem.sungempharma.Admin.Stocks.StocksFragment;
import com.example.sungem.sungempharma.Others.BottomNavigationViewEx;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

public class AdminMainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    String retrieve_url,search_URL,scrollCheck = "all";
    GlobalFunctions globalFunctions;
    AlertDialog.Builder builder;
    public static final String SHAREDPREF = "medrepInfo";
    private ListView lvProduct;
    private ProductListAdapter adapter;
    private ArrayList<Product> mProductList;
    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false, isSearch = false;
    public int currentId=10;
    public int pageCounter = 1;
    SearchView searchView;

    private BottomNavigationViewEx.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationViewEx.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new DashboardFragment()).commit();
                    return true;
                case R.id.navigation_stocks:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new StocksFragment()).commit();
                    return true;
                case R.id.navigation_sales:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new SalesFragment()).commit();
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new ProfileFragment()).commit();
                    return true;

            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        //Open dashboard fragment upon startup
        //NOTE:  Open fragment1 initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new DashboardFragment());
        ft.commit();

        (AdminMainActivity.this).getSupportActionBar().setTitle(GlobalFunctions.adminActivity());
        BottomNavigationViewEx navigation = (BottomNavigationViewEx) findViewById(R.id.navigation);
        //disable bottom navi animation
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


}
