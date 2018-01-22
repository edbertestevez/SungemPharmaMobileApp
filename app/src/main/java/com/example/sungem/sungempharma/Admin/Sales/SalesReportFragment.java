package com.example.sungem.sungempharma.Admin.Sales;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sungem.sungempharma.Admin.Stocks.Product;
import com.example.sungem.sungempharma.Admin.Stocks.ProductListAdapter;
import com.example.sungem.sungempharma.Admin.Stocks.StocksFragment;
import com.example.sungem.sungempharma.Medrep.Delivery.PendingDeliveryFragment;
import com.example.sungem.sungempharma.Medrep.Delivery.TransactionDeliveryFragment;
import com.example.sungem.sungempharma.Others.BottomNavigationViewEx;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesReportFragment extends Fragment {

    View view;
    private BottomNavigationViewEx.TabAdapter mTabAdapter;
    private ViewPager mViewPager;
    String startDate;


    public SalesReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sales_report, container, false);

        mTabAdapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if(((SalesReportActivity)getActivity()).tabSelected().equals("1")){
            tabLayout.getTabAt(1).select();
        }else{
            tabLayout.getTabAt(0).select();
        }


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        BottomNavigationViewEx.TabAdapter adapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        adapter.addFragment(new MedicineFragment(), "Medicine Sales");
        adapter.addFragment(new ManagementFragment(), "Management Sales");
        viewPager.setAdapter(adapter);
    }


}
