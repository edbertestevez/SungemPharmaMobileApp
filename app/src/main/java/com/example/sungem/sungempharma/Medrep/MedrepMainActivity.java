package com.example.sungem.sungempharma.Medrep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sungem.sungempharma.Medrep.Dashboard.DashboardFragment;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryMainFragment;
import com.example.sungem.sungempharma.Medrep.Delivery.PendingDeliveryFragment;
import com.example.sungem.sungempharma.Medrep.Payment.PaymentMainFragment;
import com.example.sungem.sungempharma.Medrep.Performance.PerformanceMainFragment;
import com.example.sungem.sungempharma.Medrep.Profile.ProfileMainFragment;
import com.example.sungem.sungempharma.Medrep.Service.SungemService;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockLevelMainFragment;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MainSelectionActivity;
import com.example.sungem.sungempharma.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MedrepMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    GlobalFunctions globalFunctions;
    String userid, username, access, userimg;
    TextView drawer_username, drawer_access;
    ImageView drawer_image;
    FloatingActionButton fab;
    Intent serviceIntent;
    DBController dbController;
    PendingDeliveryFragment deliveryFragment;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medrep_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DashboardFragment()).commit();
        }

        //START SERVICE
        startService(new Intent(getBaseContext(), SungemService.class));
        serviceIntent = new Intent(this, SungemService.class);

        /*
        //DELIVERY FRAGMENT ACCESS
        deliveryFragment = (PendingDeliveryFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        */
        // get the SharedPreferences object and global functions
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        globalFunctions = new GlobalFunctions(getApplicationContext());
        dbController = new DBController(getApplicationContext());
        // Retrieve the saved values
        userimg = sharedpref.getString("USERIMAGE","");
        userid = sharedpref.getString("USERID","");
        username = sharedpref.getString("USERNAME","");
        access = sharedpref.getString("ACCESS","");

        fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //EDBERT CODES
        //HEADER NAVIGATION
        View headerView = navigationView.getHeaderView(0);

        drawer_image = headerView.findViewById(R.id.drawer_image);
        drawer_username = headerView.findViewById(R.id.drawer_username);
        drawer_access = headerView.findViewById(R.id.drawer_access);

        //Picasso.with(getApplicationContext()).load(userimg).into(drawer_image);

        if(!userimg.equals("")) {
            Picasso.with(getApplicationContext()).load(globalFunctions.externalUrl()+userimg).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(drawer_image);
            //Picasso.with(getApplicationContext()).load(userimg).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(drawer_image);
        }else{
            Picasso.with(getApplicationContext()).load(R.drawable.logo_orig).into(drawer_image);
        }
        drawer_username.setText(username);
        drawer_access.setText("Medical Representative");

        //CHECK IF MAY EXTRAS SA INTENT NA GN HATAG FOR REDIRECTION
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("PAGE")!=null){
                switch (bundle.getString("PAGE")){
                    case "delivery":{
                        loadDelivery();
                        navigationView.getMenu().getItem(2).setChecked(true);
                        break;
                    }
                    case "stocklevel":{
                        loadStocklevel();
                        navigationView.getMenu().getItem(1).setChecked(true);
                        break;
                    }
                    case "profile":{
                        loadProfile();
                        navigationView.getMenu().getItem(5).setChecked(true);
                        break;
                    }
                    case "payment":{
                        loadPayment();
                        navigationView.getMenu().getItem(3).setChecked(true);
                        break;
                    }
                    case "performance":{
                        loadPerformance();
                        navigationView.getMenu().getItem(4).setChecked(true);
                        break;
                    }
                }
            }
        }

    }



    public NavigationView getNavigationView(){
        return navigationView;
    }

    public FloatingActionButton getFloatingActionButton(){
        return fab;
    }

    public ImageView getUserImage(){return drawer_image;}

    public void updateHeaderName(String name) {
        drawer_username.setText(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.medrep_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_dashboard: loadDashboard();
                break;
            case R.id.nav_stocklevel:loadStocklevel();
                break;
            case R.id.nav_delivery: loadDelivery();
                break;
            case R.id.nav_payment: loadPayment();
                break;
            case R.id.nav_performance: loadPerformance();
                break;
            case R.id.nav_profile: loadProfile();
                break;
            case R.id.nav_logout:{
                //dbController.deleteUpdateTableInfo();
                sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("USERNAME", null);
                editor.putString("USERID", null);
                editor.putBoolean("LOGGED", false);
                editor.putBoolean("NOTIFICATION", false);
                editor.putString("USERIMAGE", null);
                editor.putString("ACCESS", null);
                editor.commit();
                Intent intent = new Intent(MedrepMainActivity.this, MainSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                //STOP SERVICE
                stopService(new Intent(getBaseContext(), SungemService.class));
            }
            break;
            default: getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DashboardFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadDashboard(){getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.content_frame, new DashboardFragment()).commit();}
    public void loadStocklevel(){getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.content_frame, new StockLevelMainFragment()).commit();}
    public void loadDelivery(){getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.content_frame, new DeliveryMainFragment(), "DELIVERY_FRAGMENT").commit();}
    public void loadPayment(){getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.content_frame, new PaymentMainFragment()).commit();}
    public void loadPerformance(){getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.content_frame, new PerformanceMainFragment()).commit();}
    public void loadProfile(){getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.content_frame, new ProfileMainFragment()).commit();}
}
