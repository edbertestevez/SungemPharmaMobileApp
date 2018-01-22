package com.example.sungem.sungempharma.Admin.Dashboard;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sungem.sungempharma.Admin.Sales.ManagementFragment;
import com.example.sungem.sungempharma.Admin.Sales.MedicineFragment;
import com.example.sungem.sungempharma.Others.BottomNavigationViewEx;
import com.example.sungem.sungempharma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminExpiryFragment extends Fragment {
    View view;
    private ViewPager mViewPager;
    private BottomNavigationViewEx.TabAdapter mTabAdapter;

    public AdminExpiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_expiry, container, false);

        mTabAdapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        BottomNavigationViewEx.TabAdapter adapter = new BottomNavigationViewEx.TabAdapter(getFragmentManager());
        adapter.addFragment(new WarehouseExpiryFragment(), "Warehouse Stocks");
        adapter.addFragment(new ConsignExpiryFragment(), "Consigned Stocks");
        viewPager.setAdapter(adapter);
    }
}
