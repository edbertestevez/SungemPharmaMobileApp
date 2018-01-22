package com.example.sungem.sungempharma.Medrep.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryMainFragment;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.Payment.PaymentMainFragment;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockLevelMainFragment;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by trebd on 9/2/2017.
 */

public class SungemService extends Service {
    private Timer mTimer;
    DBController dbController;
    GlobalFunctions globalFunctions;
    public static final String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    //ADDED CODE
    private static final String TAG = "SungemService";
    public static final String BROADCAST_ACTION = "com.example.sungem.sungempharma.BroadcastReceiver";
    private final Handler handler = new Handler();
    Intent intent;
    int counter = 0;

    @Override
    public void onCreate(){
        super.onCreate();

        mTimer = new Timer();
        mTimer.schedule(timerTask ,2000, 2000); //every 2 seconds
        sharedpref = getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        globalFunctions = new GlobalFunctions(getApplicationContext());

        dbController = new DBController(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return START_STICKY;
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(globalFunctions.isNetworkAvailable()){
                checkUpdates();
                checkUnsyncTransaction();
            }

        }
    };


    @Override
    public void onDestroy(){
        //super.onDestroy();
        try{
            mTimer.cancel();
            timerTask.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void checkUpdates() {

        intent = new Intent(BROADCAST_ACTION);
        if (globalFunctions.isNetworkAvailable()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "/checkupdates.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);


                                    //DELIVERY UPDATES CHECK
                                    if (((!obj.getString("delivery_update_count").equals(0))&&(!dbController.getDeliveryUpdateCount(sharedpref.getString("USERID", "")).equals(0)))&&((!obj.getString("delivery_update_count").equals(dbController.getDeliveryUpdateCount(sharedpref.getString("USERID", "")))))) {
                                        if(sharedpref.getBoolean("NOTIFICATION",false)){
                                            syncDelivery(); //METHOD NGA MA CHANGE SA SQLITE DATABASE IF MAY UPDATES
                                            dbController.updateDeliveryCount(sharedpref.getString("USERID", ""), obj.getInt("delivery_update_count"));
                                        }
                                    }

                                    //PAYMENT UPDATES CHECK
                                    if ((!obj.getString("payment_update_count").equals(dbController.getPaymentUpdateCount(sharedpref.getString("USERID", "")))) && (!obj.getString("payment_update_count").equals("0"))) {
                                        syncPayment(); //METHOD NGA MA CHANGE SA SQLITE DATABASE IF MAY UPDATES
                                        syncCurrentSales(); //UPDATE CURRENT SALES
                                        dbController.updatePaymentCount(sharedpref.getString("USERID", ""), obj.getInt("payment_update_count"));

                                    }

                                    //MONITOR UPDATES CHECK
                                    if ((!obj.getString("monitor_update_count").equals(dbController.getMonitorUpdateCount(sharedpref.getString("USERID", "")))) && (!obj.getString("monitor_update_count").equals("0"))) {
                                        syncStockLevel(); //METHOD NGA MA CHANGE SA SQLITE DATABASE IF MAY UPDATES
                                        dbController.updateMonitorCount(sharedpref.getString("USERID", ""), obj.getInt("monitor_update_count"));
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
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("action", "checkupdates");
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getBaseContext()).addToRequestque(stringRequest);
        }
    }

    private void syncCurrentSales() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "current_sales.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    dbController.updateCurrentMedrepSales(sharedpref.getString("USERID",""), obj.getString("total_sales"));
                                }
                                intent.putExtra("SALES_UPDATE", true); //EXTRA SA INTENT PARA I CHECK
                                sendBroadcast(intent); //magsend broadcast salon ka activity para mag update content

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
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getBaseContext()).addToRequestque(stringRequest);
        }
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

                                intent.putExtra("DELIVERY_UPDATE", true); //EXTRA SA INTENT PARA I CHECK
                                sendBroadcast(intent); //magsend broadcast salon ka activity para mag update content

                                //NOTIFICATION SAMPLE
                                PendingIntent resultPendingIntent;
                                Intent resultIntent = new Intent(getApplicationContext(), MedrepMainActivity.class);
                                resultIntent.putExtra("PAGE","delivery");
                                resultPendingIntent =
                                        PendingIntent.getActivity(
                                                getApplicationContext(),
                                                0,
                                                resultIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );

                                NotificationCompat.Builder mBuilder =
                                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                .setSmallIcon(R.drawable.logo_orig)
                                                .setContentTitle("Delivery Update")
                                                .setContentText("There's an update in the delivery record");
                                // Sets an ID for the notification
                                mBuilder.setContentIntent(resultPendingIntent);
                                int mNotificationId = 002;
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());


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

                                intent.putExtra("PAYMENT_UPDATE", true); //EXTRA SA INTENT PARA I CHECK
                                sendBroadcast(intent); //magsend broadcast salon ka activity para mag update content

                                //NOTIFICATION SAMPLE
                                PendingIntent resultPendingIntent;
                                Intent resultIntent = new Intent(getApplicationContext(), MedrepMainActivity.class);
                                resultIntent.putExtra("PAGE","payment");
                                resultPendingIntent =
                                        PendingIntent.getActivity(
                                                getApplicationContext(),
                                                0,
                                                resultIntent,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );

                                NotificationCompat.Builder mBuilder =
                                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                .setSmallIcon(R.drawable.logo_orig)
                                                .setContentTitle("Payment Update")
                                                .setContentText("There's an update in the payment record");
                                // Sets an ID for the notification
                                mBuilder.setContentIntent(resultPendingIntent);
                                int mNotificationId = 001;
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr =
                                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());

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

                            //NOTIFICATION SAMPLE
                            PendingIntent resultPendingIntent;
                            Intent resultIntent = new Intent(getApplicationContext(), MedrepMainActivity.class);
                            resultIntent.putExtra("PAGE","stocklevel");
                            resultPendingIntent =
                                    PendingIntent.getActivity(
                                            getApplicationContext(),
                                            0,
                                            resultIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                            NotificationCompat.Builder mBuilder =
                                    (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                            .setSmallIcon(R.drawable.logo_orig)
                                            .setContentTitle("Stock Level Update")
                                            .setContentText("There's an update in the client stock level");
                            // Sets an ID for the notification
                            mBuilder.setContentIntent(resultPendingIntent);
                            int mNotificationId = 003;
                            // Gets an instance of the NotificationManager service
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            // Builds the notification and issues it.
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());


                            //REPLACE CURRENT DISPLAY :D
                            intent.putExtra("MONITOR_UPDATE", true); //EXTRA SA INTENT PARA I CHECK
                            sendBroadcast(intent); //magsend broadcast salon ka activity para mag update content
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

    private void checkUnsyncTransaction() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        if(dbController.getUnsyncDeliveryCount(sharedpref.getString("USERID", ""))>0){
            //UPLOAD METHOD OF DELIVERY TRANSACTIONS
            uploadDeliveryTransactions();
        }

        if(dbController.getUnsyncPaymentCount(sharedpref.getString("USERID", ""))>0){
            //UPLOAD METHOD OF PAYMENT TRANSACTIONS
            uploadPaymentTransactions();
        }
    }

    //UPLOAD UNSYNCED TRANSACTION TO THE SERVER
    private void uploadDeliveryTransactions() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            //DIALOG BAR MAG LOADING

            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "delivery/uploadtransactions.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //RESPONSE SANG SERVER
                                    JSONObject obj = (JSONObject) jsonArray.get(i);

                                    //UPDATE delivery transaction status
                                    dbController.updateDeliverySyncStatus(obj.getString("delivery_id"), obj.getString("status"));
                                }

                                intent.putExtra("DELIVERY_UPLOAD_UPDATE", true); //EXTRA SA INTENT PARA I CHECK
                                sendBroadcast(intent); //magsend broadcast salon ka activity para mag update content

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

                    params.put("transaction_list", dbController.getUnsyncedDeliveryTransactions(sharedpref.getString("USERID", "")));
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
        }
    }

    //UPLOAD UNSYNCED TRANSACTION TO THE SERVER
    private void uploadPaymentTransactions() {
        sharedpref = this.getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {


            StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "payment/uploadtransactions.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //response sang request
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                //Log.i("RESPONSE: ",jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    //RESPONSE SANG SERVER
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    //UPDATE payment transaction status
                                    dbController.updatePaymentSyncStatus(obj.getString("primary_id"), obj.getString("invoice_id"), obj.getString("status"));
                                }

                                intent.putExtra("PAYMENT_UPLOAD_UPDATE", true); //EXTRA SA INTENT PARA I CHECK
                                sendBroadcast(intent); //magsend broadcast salon ka activity para mag update content

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

                    params.put("transaction_list", dbController.getUnsyncedPaymentTransactions(sharedpref.getString("USERID", "")));
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
        }
    }
}
