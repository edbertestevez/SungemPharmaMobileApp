package com.example.sungem.sungempharma.Others;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Admin.AdminMainActivity;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_username, et_password;
    Button btnLogin;
    AlertDialog.Builder builder;
    String login_url;
    GlobalFunctions globalFunctions;
    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;
    TextView txtAccess;
    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        globalFunctions = new GlobalFunctions(getApplicationContext());
        dbController = new DBController(getApplicationContext());

        //URL either medrep or admin
        login_url = globalFunctions.internalUrl() + "login.php";

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);

        txtAccess = (TextView) findViewById(R.id.txtAccess);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        builder = new AlertDialog.Builder(LoginActivity.this);

        //GO TO NEXT IF LOGGED IN
        if (sharedpref.getBoolean("LOGGED", false)==true) {
            if (sharedpref.getString("ACCESS", "").equals("admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                startActivity(intent);
            } else if (sharedpref.getString("ACCESS", "").equals("medrep")) {
                Intent intent = new Intent(LoginActivity.this, MedrepMainActivity.class);
                startActivity(intent);
            }
        }

        //CHANGE TEXT OF ACCESS
        if (sharedpref.getString("ACCESS", "").equals("admin")) {
            txtAccess.setText("Admin/Staff");
        } else if (sharedpref.getString("ACCESS", "").equals("medrep")) {
            txtAccess.setText("Medical Representative");
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnLogin:
                final String username, password, access;
                access = sharedpref.getString("ACCESS","");
                username = et_username.getText().toString();
                password = et_password.getText().toString();
                //CHECK CONNECTION
                if(globalFunctions.isNetworkAvailable()==true) {
                    if (username.equals("") || password.equals("")) {
                        //builder.setTitle("Error Found!");
                        builder.setMessage("Please fill up all fields");
                        displayAlert("input_error");
                    } else {
                        final ProgressDialog progressDialog;
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Verifying Login Details");
                        progressDialog.show();

                        //Connect to HTTPRequestToSendData and get result pfre
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        //response sang request
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            //Base sa php file mo sa login
                                            String code = jsonObject.getString("code");
                                            String message = jsonObject.getString("message");
                                            String response_name = jsonObject.getString("name");
                                            String response_userid = jsonObject.getString("userid");
                                            String response_img = jsonObject.getString("acc_photo");
                                            String firstname = jsonObject.getString("firstname");
                                            String middlename = jsonObject.getString("middlename");
                                            String lastname = jsonObject.getString("lastname");
                                            String password = jsonObject.getString("password");


                                            if (code.equals("login_success")) {
                                                //INSERT SHARED PREF INFO
                                                SharedPreferences.Editor editor = getSharedPreferences(SHAREDPREF, MODE_PRIVATE).edit();
                                                editor.putBoolean("LOGGED", true);
                                                editor.putString("USERNAME", response_name);
                                                editor.putString("USERID", response_userid);
                                                editor.putString("USERIMAGE", response_img);
                                                editor.putString("FIRSTNAME", firstname);
                                                editor.putString("MIDDLENAME", middlename);
                                                editor.putString("LASTNAME", lastname);
                                                editor.putString("PASSWORD", password);
                                                editor.putBoolean("NOTIFICATION", true);
                                                editor.commit();

                                                //GO TO NEXT PAGE
                                                sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
                                                if(sharedpref.getString("ACCESS","").equals("admin")){

                                                    Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                                    //para d na mag balik after login ni pfre
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }else if(sharedpref.getString("ACCESS","").equals("medrep")){
                                                    String response_delivery = jsonObject.getString("delivery_update");
                                                    String response_payment = jsonObject.getString("payment_update");
                                                    String response_monitor = jsonObject.getString("monitor_update");
                                                    String response_sales = jsonObject.getString("current_sales");

                                                    //CHECK IF MAY EXISTING NA AMO NI NA MEDREP REOCRD
                                                    if(dbController.medrepCheckExist(sharedpref.getString("USERID",""))){
                                                        dbController.updateDeliveryCount(sharedpref.getString("USERID", ""), Integer.parseInt(response_delivery));
                                                        dbController.updatePaymentCount(sharedpref.getString("USERID", ""), Integer.parseInt(response_payment));
                                                        dbController.updateMonitorCount(sharedpref.getString("USERID", ""), Integer.parseInt(response_monitor));
                                                        dbController.updateMedrepSales(sharedpref.getString("USERID", ""), response_sales);
                                                    }else{
                                                        dbController.insertUpdateTableInfo(response_userid, response_delivery, response_payment, response_monitor, response_sales);
                                                    }
                                                    syncDelivery();
                                                    syncPayment();
                                                    syncStockLevel();

                                                    Intent intent = new Intent(LoginActivity.this, MedrepMainActivity.class);
                                                    //para d na mag balik after login ni pfre
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                Toast.makeText(LoginActivity.this, "Welcome " + response_name, Toast.LENGTH_SHORT).show();
                                            } else {
                                                //display response if may error
                                                builder.setTitle("Server Response. . . ");
                                                builder.setMessage(message);
                                                displayAlert("");
                                                progressDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            builder.setTitle("Error . . ");
                                            builder.setMessage("Incorrect username/password");
                                            displayAlert("");
                                            progressDialog.dismiss();
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //display response if may error
                                builder.setTitle("Error . . ");
                                builder.setMessage("Can't connect to the server");
                                displayAlert("");
                                progressDialog.dismiss();
                            }
                        }) {
                            //PARAMETERS SA POST ACTION NA DI PRE
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("access", access);
                                params.put("username", username);
                                params.put("password", password);
                                return params;
                            }
                        };
                        //Send request sa singleton
                        MySingleton.getInstance(LoginActivity.this).addToRequestque(stringRequest);
                    }
                    break;
                }
                else{
                    builder.setTitle("No internet connection");
                    builder.setMessage("Please connect to a network");

                    displayAlert("no connection");
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
                    case "input_error":{
                        //ACTIONS KUNG BLANGKO ANG FIELDS
                        break;
                    }
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void syncDelivery() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "delivery/display.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            try {
                                //CLEAR SQLiteDelivery First
                                dbController.deleteOldDeliverySync(sharedpref.getString("USERID", ""));
                                dbController.deleteDeliveryItems(sharedpref.getString("USERID", ""));

                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                HashMap<String, String> deliveryValues = new HashMap<String, String>(); // delivery general
                                HashMap<String, String> itemValues = new HashMap<String, String>(); // delivery items
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);

                                    deliveryValues.put("delivery_id", obj.getString("delivery_id"));
                                    deliveryValues.put("order_id", obj.getString("order_id"));
                                    deliveryValues.put("client_name", obj.getString("client_name"));
                                    deliveryValues.put("client_address", obj.getString("client_address"));
                                    deliveryValues.put("medrep_id", sharedpref.getString("USERID", ""));
                                    //LOOP THROUGH ITEMS ARRAY
                                    JSONArray arrayProducts = new JSONArray(obj.getString("items"));
                                    for (int x = 0; x < arrayProducts.length(); x++) {
                                        JSONObject objItem = (JSONObject) arrayProducts.get(x);
                                        itemValues.put("delivery_id", obj.getString("delivery_id"));
                                        itemValues.put("product_id", objItem.getString("pro_id"));
                                        itemValues.put("product_name", objItem.getString("pro_brand") + " " + objItem.getString("pro_generic"));
                                        itemValues.put("lot_number", objItem.getString("lot_number"));
                                        itemValues.put("expiry_date", objItem.getString("expiry_date"));
                                        itemValues.put("quantity", objItem.getString("qty_total"));
                                        itemValues.put("medrep_id", obj.getString("medrep_id"));
                                        dbController.insertDeliveryItem(itemValues);
                                    }
                                    Log.i("RESPONSE: ", arrayProducts.toString());
                                    dbController.insertDeliverySync(deliveryValues);
                                }



                                //REPLACE CURRENT DISPLAY :D
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("action", "getlist");
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(this.getApplicationContext()).addToRequestque(stringRequest);
        }else{
        }
    }

    private void syncPayment() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "payment/display.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //CLEAR SQLiteDelivery First
                                dbController.deleteOldPaymentSync(sharedpref.getString("USERID", ""));
                                dbController.deleteInvoicePayment(sharedpref.getString("USERID", ""));


                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                HashMap<String, String> invoiceValues = new HashMap<String, String>(); // invoice general
                                HashMap<String, String> paymentValues = new HashMap<String, String>(); // invoicepayments
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Log.i("STRING REQUEST: ", obj.toString());
                                    invoiceValues.put("invoice_id", obj.getString("invoice_id"));
                                    invoiceValues.put("client_name", obj.getString("client_name"));
                                    invoiceValues.put("amount_total", obj.getString("amount_total"));
                                    invoiceValues.put("amount_paid", obj.getString("amount_paid"));
                                    invoiceValues.put("amount_remaining", obj.getString("amount_remaining"));
                                    invoiceValues.put("date_due", obj.getString("date_due"));
                                    invoiceValues.put("medrep_id", sharedpref.getString("USERID", ""));

                                    JSONArray arrayPayments = obj.getJSONArray("payments");
                                    for (int x = 0; x < arrayPayments.length(); x++) {
                                        JSONObject objItem = (JSONObject) arrayPayments.get(x);
                                        paymentValues.put("invoice_id", obj.getString("invoice_id"));
                                        paymentValues.put("paymode_name", objItem.getString("paymode_name"));
                                        paymentValues.put("payment_amount", objItem.getString("payment_amount"));
                                        paymentValues.put("payment_date", objItem.getString("payment_date"));
                                        paymentValues.put("medrep_id", sharedpref.getString("USERID", ""));
                                        dbController.insertInvoicePayment(paymentValues);
                                    }
                                    dbController.insertInvoiceSync(invoiceValues);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", "getlist");
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getBaseContext()).addToRequestque(stringRequest);
        }
    }

    private void syncStockLevel() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "stocklevel/display.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //CLEAR SQLiteDelivery First
                            dbController.deleteOldClientStock(sharedpref.getString("USERID",""));
                            dbController.deleteOldStockItems(sharedpref.getString("USERID",""));

                            JSONArray jsonArray = new JSONArray(response);
                            //Log.i("RESPONSE: ",jsonArray.toString());
                            HashMap<String, String> stockLevelGeneral = new HashMap<String, String>(); // stocklevel general
                            HashMap<String, String> itemValues = new HashMap<String, String>(); // stocklevel items
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = (JSONObject) jsonArray.get(i);

                                stockLevelGeneral.put("client_id", obj.getString("client_id"));
                                stockLevelGeneral.put("client_name", obj.getString("client_name"));
                                stockLevelGeneral.put("client_address", obj.getString("client_address"));
                                stockLevelGeneral.put("consign_qty", obj.getString("consign_qty"));
                                stockLevelGeneral.put("medrep_id", sharedpref.getString("USERID",""));
                                //LOOP THROUGH PRODUCTS ARRAY
                                JSONArray arrayProducts = new JSONArray(obj.getString("products"));

                                for (int x = 0; x < arrayProducts.length(); x++){
                                    JSONObject objItem = (JSONObject) arrayProducts.get(x);
                                    itemValues.put("client_id", obj.getString("client_id")); //halin sa value sa babaw
                                    itemValues.put("pro_id",objItem.getString("pro_id"));
                                    itemValues.put("pro_brand",objItem.getString("pro_brand"));
                                    itemValues.put("pro_generic",objItem.getString("pro_generic"));
                                    itemValues.put("pro_formulation",objItem.getString("pro_formulation"));
                                    itemValues.put("lot_number",objItem.getString("lot_number"));
                                    itemValues.put("expiry_date",objItem.getString("expiry_date"));
                                    itemValues.put("quantity", objItem.getString("quantity"));
                                    itemValues.put("medrep_id", sharedpref.getString("USERID",""));
                                    dbController.insertStockItem(itemValues);
                                }

                                dbController.insertClientStock(stockLevelGeneral);
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action", "getlist");
                params.put("medrep_id", sharedpref.getString("USERID", ""));
                return params;
            }
        };
        //Send request
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }
}
