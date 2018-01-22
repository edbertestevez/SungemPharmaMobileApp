package com.example.sungem.sungempharma.Others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryData;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryItemData;
import com.example.sungem.sungempharma.Medrep.Delivery.DeliveryTransactionData;
import com.example.sungem.sungempharma.Medrep.Payment.InvoicePaymentData;
import com.example.sungem.sungempharma.Medrep.Payment.PaymentData;
import com.example.sungem.sungempharma.Medrep.Payment.PaymentTransactionData;
import com.example.sungem.sungempharma.Medrep.StockLevel.ClientStockData;
import com.example.sungem.sungempharma.Medrep.StockLevel.StockItemData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Created by trebd on 8/20/2017.
 */

public class DBController extends SQLiteOpenHelper{
    public DBController(Context applicationcontext) {
        super(applicationcontext, "sungemsqlite.db", null, 1);
    }

    public static String ID = "_id";
    public static String SYNC_STATUS = "sync_status";

    /*************UPDATES TABLE CHECK SA SERVER**********************/
    public static String CHECK_MEDREP_ID = "medrep_id";
    public static String CHECK_DELIVERY_COUNT = "delivery_update";
    public static String CHECK_PAYMENT_COUNT = "payment_update";
    public static String CHECK_MONITOR_COUNT = "monitor_update";
    public static String CHECK_CURRENT_SALES = "current_sales";
    public static String CHECK_UPDATE_TABLE = "tbl_check_update";

    /**************************PAYMENT TABLES**********************************************/
    public static String INVOICE_ID = "invoice_id";
    public static String CLIENT_NAME = "client_name";
    public static String PAYMODE = "paymode_id";
    public static String PAY_AMOUNT = "payment_amount";
    public static String PDC_NUMBER = "pdc_number";
    public static String PDC_DATE = "pdc_date";
    public static String PDC_BANK = "pdc_bank";
    public static String MEDREP_ID = "medrep_id";
    public static  String PAY_DATE = "payment_date";
    public static String PAY_TIME = "payment_time";
    public static String PAY_TRANSACTION_STATUS = "payment_transaction_status";
    public static String PAY_TRANSACTION_TABLE = "tbl_payment_transactions";

    /************************STOCK LEVEL INVENTORY TABLE**********/
    public static String STOCK_CLIENT_ID = "client_id";
    public static String STOCK_CLIENT_NAME = "client_name";
    public static String STOCK_CLIENT_ADDRESS = "client_address";
    public static String STOCK_CLIENT_QTY = "consign_qty";
    public static String STOCK_MEDREP_ID = "medrep_id";
    public static String CLIENT_STOCK_LEVEL_TABLE = "tbl_client_stock";

    //STOCK ITEM
    public static String STOCK_ITEM_CLIENT = "client_id";
    public static String STOCK_ITEM_PRO_ID = "pro_id";
    public static String STOCK_ITEM_PRO_BRAND = "pro_brand";
    public static String STOCK_ITEM_PRO_GENERIC = "pro_generic";
    public static String STOCK_ITEM_PRO_FORMULATION = "pro_formulation";
    public static String STOCK_ITEM_LOT = "lot_number";
    public static String STOCK_ITEM_EXPIRY = "lot_expiry";
    public static String STOCK_ITEM_QTY = "qty";
    public static String STOCK_ITEM_MEDREP = "medrep_id";
    public static String STOCK_LEVEL_ITEM_TABLE = "tbl_stock_items";

    //MONITOR MAIN
    public static String MONITOR_ID = "monitor_id";
    public static String MONITOR_CLIENT = "client_id";
    public static String MONITOR_CLIENT_NAME = "client_name";
    public static String MONITOR_DATE = "monitor_date";
    public static String MONITOR_TIME = "monitor_time";
    public static String MONITOR_MEDREP = "medrep_id";
    public static String MONITOR_MAIN_TABLE = "tbl_monitor_main";

    //MONITOR ITEMS;
    public static String MONITOR_ITEM_PRODUCT = "pro_id";
    public static String MONITOR_ITEM_LOT = "lot_number";
    public static String MONITOR_ITEM_SOLD = "qty_sold";
    public static String MONITOR_ITEM_WITHDRAWN = "qty_withdrawn";
    public static String MONITOR_ITEM_TABLE = "tbl_monitor_items";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //STARTING SA PAYMENT MA CONSTANT VARIABLES NKO AH

        String queryUpdateTable = "CREATE TABLE "+CHECK_UPDATE_TABLE+" ( "+ID+" INTEGER PRIMARY KEY, "+CHECK_MEDREP_ID+" TEXT, "+CHECK_DELIVERY_COUNT+" INTEGER DEFAULT 0, "+CHECK_PAYMENT_COUNT+" INTEGER DEFAULT 0, "+CHECK_MONITOR_COUNT+" INTEGER DEFAULT 0, "+CHECK_CURRENT_SALES+" TEXT DEFAULT 0)";
        sqLiteDatabase.execSQL(queryUpdateTable);

        /************************STOCK LEVEL INVENTORY TABLES**********************************/
        String queryStock = "CREATE TABLE "+CLIENT_STOCK_LEVEL_TABLE+" ("+ID+" INTEGER PRIMARY KEY, "+STOCK_CLIENT_ID+" TEXT, "+STOCK_CLIENT_NAME+" TEXT, "+STOCK_CLIENT_ADDRESS+" TEXT, "+STOCK_CLIENT_QTY+" TEXT, "+STOCK_MEDREP_ID+" TEXT)";
        sqLiteDatabase.execSQL(queryStock);

        String queryStockItems = "CREATE TABLE "+STOCK_LEVEL_ITEM_TABLE+" ("+ID+" INTEGER PRIMARY KEY, "+STOCK_ITEM_CLIENT+" TEXT, "+STOCK_ITEM_PRO_ID+" TEXT, "+STOCK_ITEM_PRO_BRAND+" TEXT, "+STOCK_ITEM_PRO_GENERIC+" TEXT,"+STOCK_ITEM_PRO_FORMULATION+" TEXT,"+STOCK_ITEM_LOT+" TEXT, "+STOCK_ITEM_EXPIRY+" TEXT, "+STOCK_ITEM_QTY+" TEXT, "+STOCK_ITEM_MEDREP+" TEXT)";
        sqLiteDatabase.execSQL(queryStockItems);

        String queryMonitorStock = "CREATE TABLE "+MONITOR_MAIN_TABLE+" ("+ID+" INTEGER PRIMARY KEY, "+MONITOR_ID+" INTEGER, "+MONITOR_CLIENT+" TEXT, "+MONITOR_CLIENT_NAME+" TEXT, "+MONITOR_DATE+" TEXT,"+MONITOR_TIME+" TEXT,"+MONITOR_MEDREP+" TEXT, "+SYNC_STATUS+" TEXT DEFAULT '0')";
        sqLiteDatabase.execSQL(queryMonitorStock);

        String queryMonitorItems = "CREATE TABLE "+MONITOR_ITEM_TABLE+" ("+ID+" INTEGER PRIMARY KEY, "+MONITOR_ID+" INTEGER, "+MONITOR_ITEM_PRODUCT+" TEXT, "+MONITOR_ITEM_LOT+" TEXT, "+MONITOR_ITEM_SOLD+" TEXT,"+MONITOR_ITEM_WITHDRAWN+" TEXT,"+MONITOR_MEDREP+" TEXT, "+MONITOR_CLIENT+" TEXT,  "+MONITOR_DATE+" TEXT, "+MONITOR_TIME+" TEXT,"+SYNC_STATUS+" TEXT DEFAULT '0', pro_name)";
        sqLiteDatabase.execSQL(queryMonitorItems);

        /**************************DELIVERY TABLES**********************************************/
        //CREATE DELIVERY TABLE (SQLITE) -> Diri masulod ang synced data
        String queryDelivery = "CREATE TABLE tbl_delivery ( _id INTEGER PRIMARY KEY, delivery_id TEXT, order_id TEXT, client_name TEXT, client_address TEXT, medrep_id TEXT)";
        sqLiteDatabase.execSQL(queryDelivery);

        //CREATE DELIVERY TRANSACTION TABLE (Contains all transactions made nga na deliver)
        String queryDeliveryTransactionData = "CREATE TABLE tbl_delivery_transactions ( _id INTEGER PRIMARY KEY, delivery_id TEXT, delivery_status TEXT, delivery_date TEXT, delivery_time TEXT, medrep_id TEXT, or_number TEXT, "+SYNC_STATUS+" TEXT DEFAULT '0', client_name TEXT, address TEXT)";
        sqLiteDatabase.execSQL(queryDeliveryTransactionData);

        //CREATE DELIVERY ITEMS TABLE (Contains all delivery items)
        String queryDeliveryItems = "CREATE TABLE tbl_delivery_items ( _id INTEGER PRIMARY KEY, delivery_id TEXT, product_id TEXT, product_name TEXT, lot_number TEXT, expiry_date TEXT, quantity TEXT, medrep_id TEXT)";
        sqLiteDatabase.execSQL(queryDeliveryItems);

        /**************************PAYMENT TABLES**********************************************/
        String queryInvoice = "CREATE TABLE tbl_invoice ( _id INTEGER PRIMARY KEY, invoice_id TEXT, client_name TEXT, amount_total TEXT, amount_paid TEXT, amount_remaining TEXT, date_due TEXT, medrep_id TEXT)";
        sqLiteDatabase.execSQL(queryInvoice);

        String queryInvoicePayments = "CREATE TABLE tbl_invoice_payments ( _id INTEGER PRIMARY KEY, invoice_id TEXT, paymode_name TEXT, payment_amount TEXT, payment_date TEXT, medrep_id TEXT)";
        sqLiteDatabase.execSQL(queryInvoicePayments);

        String queryPayTransaction = "CREATE TABLE "+PAY_TRANSACTION_TABLE+" ( "+ID+" INTEGER PRIMARY KEY, "+INVOICE_ID+" TEXT, "+CLIENT_NAME+" TEXT, "+PAYMODE+" TEXT, "+PAY_AMOUNT+" TEXT, "+PDC_NUMBER+" TEXT, "+PDC_DATE+" TEXT, "+PDC_BANK+" TEXT, "+PAY_DATE+" TEXT, "+PAY_TIME+" TEXT, "+PAY_TRANSACTION_STATUS+" TEXT, "+MEDREP_ID+" TEXT, "+SYNC_STATUS+" TEXT DEFAULT '0')";
        sqLiteDatabase.execSQL(queryPayTransaction);
        //Create 1 sample record
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    //DASHBOARD CONTENT
    public void updateCurrentMedrepSales(String medrep_id, String current_sales) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+CHECK_UPDATE_TABLE+" SET "+CHECK_CURRENT_SALES+" = "+current_sales+" WHERE medrep_id = '"+medrep_id+"'");
        database.close();
    }

    public int getPendingDeliveryCountNative(String medrep_id) {
        int pending_count=0;
        int transaction_count=0;

        //TRANSACTION COUNT
        String selectQuery = "SELECT COUNT(*) FROM tbl_delivery_transactions WHERE medrep_id = '"+medrep_id+"' AND sync_status='0'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                transaction_count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        //PENDING COUNT
        String pendingQuery = "SELECT COUNT(*) FROM tbl_delivery WHERE medrep_id = '"+medrep_id+"'";
        Cursor cursorPending = database.rawQuery(pendingQuery, null);
        if (cursorPending.moveToFirst()) {
            do {
                pending_count = cursorPending.getInt(0);
            } while (cursorPending.moveToNext());
        }
        database.close();
        return pending_count-transaction_count;

    }

    public int getClientCount(String medrep_id) {
        String selectQuery = "SELECT COUNT(*) FROM tbl_client_stock WHERE medrep_id = '"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int check_count=0;

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return check_count;
    }

    public int getInvoiceCount(String medrep_id) {
        String selectQuery = "SELECT COUNT(*) FROM tbl_invoice WHERE medrep_id = '"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int check_count=0;

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return check_count;
    }

    public int getNearExpiryCount(String medrep_id) {

        int sumExpiry=0;
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String selectQuery = "SELECT * FROM tbl_stock_items WHERE medrep_id = '"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Date expiryDate = null;
                String strExpiry = cursor.getString(7);
                try {
                    expiryDate = dateFormat.parse(strExpiry);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date currentDate = new Date();

                Calendar c = Calendar.getInstance();
                c.setTime(currentDate);

                //ADD 3 months for expiry checking
                //c.add(Calendar.MONTH, 3);
                c.add(Calendar.MONTH, 3);

                Date currAddedDate = c.getTime();

                if (currAddedDate.getTime() >= expiryDate.getTime()) {
                    sumExpiry += cursor.getInt(8);
                }
            } while (cursor.moveToNext());
        }
        database.close();
        return sumExpiry;
    }
    //DATE FUNCTIONS
    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);

        return cal.getTime();
    }

    //END OF DASHBOARD
    public int getUnsyncDeliveryCount(String medrep_id) {
        String selectQuery = "SELECT COUNT(*) FROM tbl_delivery_transactions WHERE medrep_id = '"+medrep_id+"' AND sync_status='0'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int check_count=0;

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return check_count;
    }

    public boolean medrepCheckExist(String medrep_id) {
        String selectQuery = "SELECT * FROM "+CHECK_UPDATE_TABLE+" WHERE medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            database.close();
            return true;
        }else{
            database.close();
            return false;
        }
    }

    public String getCurrentSalesCount(String medrep_id) {
        String selectQuery = "SELECT current_sales FROM "+CHECK_UPDATE_TABLE+" WHERE medrep_id = '"+medrep_id+"' ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="0";

        if (cursor.moveToFirst()) {
                check_count = cursor.getString(0);
        }
        database.close();
        return check_count;
    }

    public int getUnsyncPaymentCount(String medrep_id) {
        String selectQuery = "SELECT COUNT(*) FROM "+PAY_TRANSACTION_TABLE+" WHERE medrep_id = '"+medrep_id+"' AND sync_status='0'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int check_count=0;

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return check_count;
    }


    public void insertUpdateTableInfo(String medrep_id, String delivery, String payment, String monitor, String sales){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //0 lang danay ang values ah. Para i check nya danay sa monitor tapos ma update danay at first value
        values.put(CHECK_MEDREP_ID, medrep_id);
        values.put(CHECK_DELIVERY_COUNT, delivery);
        values.put(CHECK_PAYMENT_COUNT, payment);
        values.put(CHECK_MONITOR_COUNT, monitor);
        values.put(CHECK_CURRENT_SALES, sales);
        database.insert(CHECK_UPDATE_TABLE, null, values);
        database.close();
    }


    public void deleteUpdateTableInfo() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+CHECK_UPDATE_TABLE);
      database.close();
    }

    public String getDeliveryUpdateCount(String medrep_id) {
        String selectQuery = "SELECT "+CHECK_DELIVERY_COUNT+" FROM "+CHECK_UPDATE_TABLE+" WHERE medrep_id = '"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = String.valueOf(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return check_count;
    }


    public String getPaymentUpdateCount(String medrep_id) {
        String selectQuery = "SELECT "+CHECK_PAYMENT_COUNT+" FROM "+CHECK_UPDATE_TABLE+" WHERE medrep_id = '"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = String.valueOf(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return check_count;
    }

    public String getMonitorUpdateCount(String medrep_id) {
        String selectQuery = "SELECT "+CHECK_MONITOR_COUNT+" FROM "+CHECK_UPDATE_TABLE+" WHERE medrep_id = '"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = String.valueOf(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return check_count;
    }

    public String updateDeliveryCount(String medrep_id, int update_count) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+CHECK_UPDATE_TABLE+" SET "+CHECK_DELIVERY_COUNT+" = "+update_count+" WHERE medrep_id = '"+medrep_id+"'");
        database.close();
        return "done";
    }

    public void updatePaymentCount(String medrep_id, int update_count) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+CHECK_UPDATE_TABLE+" SET "+CHECK_PAYMENT_COUNT+" = "+update_count+" WHERE medrep_id = '"+medrep_id+"'");
        database.close();
    }

    public void updateMonitorCount(String medrep_id, int update_count) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+CHECK_UPDATE_TABLE+" SET "+CHECK_MONITOR_COUNT+" = "+update_count+" WHERE medrep_id = '"+medrep_id+"'");
        database.close();
    }

    public void updateMedrepSales(String medrep_id, String sales) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+CHECK_UPDATE_TABLE+" SET "+CHECK_CURRENT_SALES+" = '"+sales+"' WHERE medrep_id = '"+medrep_id+"'");
        database.close();
    }

    /******* UPLOADING UNSYNCED TRANSACTIONS RETRIEVAL OF RECORDS*****************/
    //MONITORING TRANSACTIONS
    public String getUnsyncedMonitorTransactions(String medrep_id){
        ArrayList<HashMap<String, String>> transactionList;
        String jsonList;
        transactionList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM tbl_monitor_items where sync_status = '0' AND medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("monitor_id", cursor.getString(1));
                map.put("pro_id", cursor.getString(2));
                map.put("lot_number", cursor.getString(3));
                map.put("qty_sold", cursor.getString(4));
                map.put("qty_withdrawn", cursor.getString(5));
                map.put("medrep_id", cursor.getString(6));
                map.put("client_id", cursor.getString(7));
                map.put("monitor_date", cursor.getString(8));
                map.put("monitor_time", cursor.getString(9));
               transactionList.add(map);
            } while (cursor.moveToNext());
            jsonList = new Gson().toJson(transactionList);
        }else{
            jsonList = "";
        }
        database.close();
        //Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON

        return jsonList;
    }

    public void deleteSelectedMonitorTransaction(String monitor_id, String client_id, String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+MONITOR_MAIN_TABLE+" WHERE medrep_id='"+medrep_id+"' AND client_id='"+client_id+"' AND monitor_id='"+monitor_id+"'");
        database.execSQL("DELETE FROM "+MONITOR_ITEM_TABLE+" WHERE medrep_id='"+medrep_id+"' AND client_id='"+client_id+"' AND monitor_id='"+monitor_id+"'");
        database.close();
    }

    public void updateMonitoringSyncStatus(String monitor_id, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+MONITOR_MAIN_TABLE+" SET sync_status='"+status+"' WHERE "+MONITOR_ID+" = '"+monitor_id+"'");
        database.execSQL("UPDATE "+MONITOR_ITEM_TABLE+" SET sync_status='"+status+"' WHERE "+MONITOR_ID+" = '"+monitor_id+"'");
        database.close();
    }

    //PAYMENT TRANSACTIONS
    public String getUnsyncedPaymentTransactions(String medrep_id){
        ArrayList<HashMap<String, String>> transactionList;
        transactionList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM "+PAY_TRANSACTION_TABLE+" where "+SYNC_STATUS+"= '0' AND medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("primary_id", cursor.getString(0));
                map.put("invoice_id", cursor.getString(1));
                map.put("paymode_id", cursor.getString(3));
                map.put("payment_amount", cursor.getString(4));
                map.put("pdc_number", cursor.getString(5));
                map.put("pdc_date", cursor.getString(6));
                map.put("pdc_bank", cursor.getString(7));
                map.put("payment_date", cursor.getString(8));
                map.put("payment_time", cursor.getString(9));
                transactionList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        //Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        String jsonList = new Gson().toJson(transactionList);
        return jsonList;
    }

    public void updatePaymentSyncStatus(String primary_id, String invoice_id, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE "+PAY_TRANSACTION_TABLE+" SET payment_transaction_status='"+status+"', sync_status='"+status+"' WHERE "+ID+" = '"+primary_id+"' AND invoice_id='"+invoice_id+"'");
        database.close();
    }


    //DELIVERY TRANSACTIONS
    public String getUnsyncedDeliveryTransactions(String medrep_id){
        ArrayList<HashMap<String, String>> transactionList;
        transactionList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM tbl_delivery_transactions where sync_status = '0' AND medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("delivery_id", cursor.getString(1));
                map.put("delivery_date", cursor.getString(3));
                map.put("delivery_time", cursor.getString(4));
                map.put("or_number", cursor.getString(6));
                transactionList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        //Gson gson = new GsonBuilder().create();
        //Use GSON to serialize Array List to JSON
        String jsonList = new Gson().toJson(transactionList);
        return jsonList;
    }

    public void updateDeliverySyncStatus(String delivery_id, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("UPDATE tbl_delivery_transactions SET delivery_status='"+status+"', sync_status='"+status+"' WHERE delivery_id = '"+delivery_id+"'");
        database.close();
    }





    /********************SQLITE for STOCK LEVEL INVENTORY RECORDS****************/

    //GET PRODUCT BRAND THROUGH PRODUCT ID
    public String getProductBrand(String pro_id) {
        String selectQuery = "SELECT pro_brand FROM "+STOCK_LEVEL_ITEM_TABLE+" WHERE pro_id = "+pro_id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String pro_brand="";

        if (cursor.moveToFirst()) {
            do {
                pro_brand = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return pro_brand;
    }


    //GET ALL SELECTED TRANSACTION ITEMS DATA
    public Cursor getSelectedTransactionItemsCursor(String monitor_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + MONITOR_ITEM_TABLE + " where "+MONITOR_ID+"='" + monitor_id + "'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //GET ALL MAIN TRANSACTION DATA
    public Cursor getMonitorTransactionDataCursor(String medrep_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + MONITOR_MAIN_TABLE + " where medrep_id='" + medrep_id + "'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    //GET CLIENT NAME THROUGH CLIENT ID
    public String getClientName(String client_id) {
        String selectQuery = "SELECT client_name FROM "+CLIENT_STOCK_LEVEL_TABLE+" WHERE client_id = "+client_id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String client_name="";

        if (cursor.moveToFirst()) {
            do {
                client_name = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return client_name;
    }

    //OVERALL LOT TRANSACTION NA I DEDUCT SA PRODUCT LIST PER LOT KA CLIENT
    public int getClientLotDeduct(String lot_number, String client_id) {
        String selectQuery = "SELECT SUM("+MONITOR_ITEM_WITHDRAWN+" + "+MONITOR_ITEM_SOLD+") FROM "+MONITOR_ITEM_TABLE+" WHERE sync_status ='0' AND "+MONITOR_CLIENT+" = '"+client_id+"' AND "+MONITOR_ITEM_LOT+" = '"+lot_number+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int sumTotal=0;

        if (cursor.moveToFirst()) {
            do {
                sumTotal = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return sumTotal;
    }

    //OVERALL PRODUCT TRANSACTION NA I DEDUCT SA PRODUCT LIST KA CLIENT
    public int getTransactionProductDeduct(String pro_id, String client_id) {
        String selectQuery = "SELECT SUM("+MONITOR_ITEM_WITHDRAWN+" + "+MONITOR_ITEM_SOLD+") FROM "+MONITOR_ITEM_TABLE+" WHERE sync_status ='0' AND "+MONITOR_CLIENT+" = '"+client_id+"' AND "+MONITOR_ITEM_PRODUCT+" = '"+pro_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int sumTotal=0;

        if (cursor.moveToFirst()) {
            do {
                sumTotal = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return sumTotal;
    }

    //OVERALL TOTAL TRANSACTION NA I DEDUCT SA CLIENT LIST
    public int getTransactionTotalDeduct(String client_id) {
        String selectQuery = "SELECT SUM("+MONITOR_ITEM_WITHDRAWN+" + "+MONITOR_ITEM_SOLD+") FROM "+MONITOR_ITEM_TABLE+" WHERE sync_status ='0' AND "+MONITOR_CLIENT+" = "+client_id;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int sumTotal=0;

        if (cursor.moveToFirst()) {
            do {
                sumTotal = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return sumTotal;
    }

    //INSERT MONITOR MAIN TRANSACTION
    public void insertMonitorMain(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONITOR_ID, queryValues.get("monitor_id"));
        values.put(MONITOR_CLIENT, queryValues.get("client_id"));
        values.put(MONITOR_CLIENT_NAME, queryValues.get("client_name"));
        values.put(MONITOR_DATE, queryValues.get("monitor_date"));
        values.put(MONITOR_TIME, queryValues.get("monitor_time"));
        values.put(MONITOR_MEDREP, queryValues.get("medrep_id"));
        values.put(SYNC_STATUS, "0");
        database.insert(MONITOR_MAIN_TABLE, null, values);
        database.close();
    }

    public String getNewMonitorId() {
        String new_monitor_id="";
        String selectQuery = "SELECT MAX("+MONITOR_ID+")+1 FROM "+MONITOR_MAIN_TABLE;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                new_monitor_id = String.valueOf(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        database.close();
        return new_monitor_id;
    }

    //INSERT MONITOR MAIN TRANSACTION
    public void insertMonitorItems(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONITOR_ID, queryValues.get("monitor_id"));
        values.put(MONITOR_ITEM_PRODUCT, queryValues.get("pro_id"));
        values.put(MONITOR_ITEM_LOT, queryValues.get("lot_number"));
        values.put(MONITOR_ITEM_SOLD, queryValues.get("qty_sold"));
        values.put(MONITOR_ITEM_WITHDRAWN, queryValues.get("qty_withdrawn"));
        values.put(MONITOR_CLIENT, queryValues.get("client_id"));
        values.put(MONITOR_MEDREP, queryValues.get("medrep_id"));
        values.put(MONITOR_DATE, queryValues.get("monitor_date"));
        values.put(MONITOR_TIME, queryValues.get("monitor_time"));
        values.put("pro_name", queryValues.get("pro_name"));
        values.put(SYNC_STATUS, "0");
        database.insert(MONITOR_ITEM_TABLE, null, values);
        database.close();
    }


    //INSERT STOCK ITEM INFORMATION
    public void insertStockItem(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STOCK_ITEM_CLIENT, queryValues.get("client_id"));
        values.put(STOCK_ITEM_PRO_ID, queryValues.get("pro_id"));
        values.put(STOCK_ITEM_PRO_BRAND, queryValues.get("pro_brand"));
        values.put(STOCK_ITEM_PRO_GENERIC, queryValues.get("pro_generic"));
        values.put(STOCK_ITEM_PRO_FORMULATION, queryValues.get("pro_formulation"));
        values.put(STOCK_ITEM_LOT, queryValues.get("lot_number"));
        values.put(STOCK_ITEM_EXPIRY, queryValues.get("expiry_date"));
        values.put(STOCK_ITEM_QTY, queryValues.get("quantity"));
        values.put(STOCK_ITEM_MEDREP, queryValues.get("medrep_id"));
        database.insert(STOCK_LEVEL_ITEM_TABLE, null, values);
        database.close();
    }

    //DELETE OLD STOCK ITEM WHEN SYNCED
    public void deleteOldStockItems(String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+STOCK_LEVEL_ITEM_TABLE+" WHERE medrep_id='"+medrep_id+"'");
        database.close();
    }

    //INSERT CLIENT STOCK GENERAL INFO
    public void insertClientStock(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STOCK_CLIENT_ID, queryValues.get("client_id"));
        values.put(STOCK_CLIENT_NAME, queryValues.get("client_name"));
        values.put(STOCK_CLIENT_ADDRESS, queryValues.get("client_address"));
        values.put(STOCK_CLIENT_QTY, queryValues.get("consign_qty"));
        values.put(STOCK_MEDREP_ID, queryValues.get("medrep_id"));
        database.insert(CLIENT_STOCK_LEVEL_TABLE, null, values);
        database.close();
    }

    public void deleteOldClientStock(String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+CLIENT_STOCK_LEVEL_TABLE+" WHERE medrep_id='"+medrep_id+"'");
        database.close();
    }

    public ArrayList<ClientStockData> getClientStock(String medrep_id) {
        ArrayList<ClientStockData> clientList;
        clientList = new ArrayList<ClientStockData>();
        String selectQuery = "SELECT * FROM "+CLIENT_STOCK_LEVEL_TABLE+" where medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String client_id = cursor.getString(1);
                String client_name = cursor.getString(2);
                String client_address = cursor.getString(3);
                String consign_qty = cursor.getString(4);
                clientList.add(new ClientStockData(client_id, client_name, client_address,consign_qty,medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return clientList;
    }

    //PARA SA LISTVIEW NA RECORD
    public Cursor getClientStockCursor(String medrep_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + CLIENT_STOCK_LEVEL_TABLE + " where medrep_id='" + medrep_id + "'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public ArrayList<StockItemData> getStockItemList(String client_id) {
        ArrayList<StockItemData> itemList;
        itemList = new ArrayList<StockItemData>();
        String selectQuery = "SELECT * FROM "+STOCK_LEVEL_ITEM_TABLE+" where client_id='"+client_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String pro_id = cursor.getString(2);
                String pro_brand = cursor.getString(3);
                String pro_generic = cursor.getString(4);
                String pro_formulation = cursor.getString(5);
                String lot_number = cursor.getString(6);
                String expiry_date = cursor.getString(7);
                String qty = cursor.getString(8);
                String medrep_id = cursor.getString(9);
                itemList.add(new StockItemData(client_id, pro_id, pro_brand,pro_generic,pro_formulation, lot_number,expiry_date,qty,medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return itemList;
    }


    //PARA SA LISTVIEW NA RECORD NGA GROUPED GENEREAL
    public Cursor getStockItemListCursor(String client_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + STOCK_LEVEL_ITEM_TABLE + " where client_id='" + client_id + "' GROUP BY pro_id", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //PARA SA LISTVIEW NA RECORD NGA GROUPED GENEREAL
    public Cursor getExpiryStockItemListCursor(String medrep_id) {
        SQLiteDatabase db = getReadableDatabase();
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        //ADD 3 months for expiry checking
        //c.add(Calendar.MONTH, 3);
        c.add(Calendar.MONTH, 3);
        Date currAddedDate = c.getTime();
        String dateCompare = String.valueOf(dateFormat.format(currAddedDate));

        Cursor mCursor = db.rawQuery("SELECT * FROM " + STOCK_LEVEL_ITEM_TABLE + " where medrep_id='" + medrep_id + "' AND lot_expiry<='"+dateCompare+"'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    //PARA SA LISTVIEW NA  SPECIFIC
    public Cursor getMonitorItemListCursor(String client_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + STOCK_LEVEL_ITEM_TABLE + " where client_id='" + client_id + "'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getSelectedProductLotListCursor(String pro_id, String client_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + STOCK_LEVEL_ITEM_TABLE + " where pro_id='"+pro_id+"' AND client_id='" + client_id + "'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int getProductTotal(String pro_id, String client_id) {
        SQLiteDatabase db = getReadableDatabase();
        int sumValue=0;
        Cursor mCursor = db.rawQuery("SELECT SUM("+STOCK_ITEM_QTY+") AS total FROM " + STOCK_LEVEL_ITEM_TABLE + " where pro_id='"+pro_id+"' AND client_id='" + client_id + "'", null);
        if (mCursor.moveToFirst()) {
            do {
                sumValue = mCursor.getInt(0);
            } while (mCursor.moveToNext());
        }
        db.close();
        return sumValue;
    }

    /********************SQLITE for PAYMENT RECORDS****************/
    //DELETE SQLite Delivery Records Sync
    public void deleteOldPaymentSync(String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM tbl_invoice WHERE medrep_id='"+medrep_id+"'");
        database.close();
    }

    //INSERT PAYMENT TRANSACTION
    public void insertPaymentTransaction(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INVOICE_ID, queryValues.get("invoice_id"));
        values.put(CLIENT_NAME, queryValues.get("client_name"));
        values.put(PAYMODE, queryValues.get("paymode_id"));
        values.put(PAY_AMOUNT, queryValues.get("pay_amount"));
        values.put(PDC_NUMBER, queryValues.get("pdc_number"));
        values.put(PDC_DATE, queryValues.get("pdc_date"));
        values.put(PDC_BANK, queryValues.get("pdc_bank"));
        values.put(PAY_DATE, queryValues.get("pay_date"));
        values.put(PAY_TIME, queryValues.get("pay_time"));
        values.put(PAY_TRANSACTION_STATUS, queryValues.get("payment_transaction_status"));
        values.put(MEDREP_ID, queryValues.get("medrep_id"));
        database.insert(PAY_TRANSACTION_TABLE, null, values);
        database.close();
    }

    //GET SUM OF ALL PAYMENT TRANSACTION SANG ISA KA INVOICE NA OFFLINE PARA MA MINUS SA DISPLAY
    public Double getPayTransactionTotal(String invoice_id) {
        Double sumValue=0.00;
        String selectQuery = "SELECT SUM(payment_amount) FROM "+PAY_TRANSACTION_TABLE+" where "+SYNC_STATUS+" = '0' AND invoice_id='"+invoice_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
               sumValue = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return sumValue;
    }

    public void deleteSelectedPaymentTransaction(String payment_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM "+PAY_TRANSACTION_TABLE+" WHERE "+ID+"='"+payment_id+"'");
        database.close();
    }

    //RETRIEVE All SQLite Payment Transaction Records
    public ArrayList<PaymentTransactionData> getAllPaymentTransactions(String medrep_id) {
        ArrayList<PaymentTransactionData> paymentList;
        paymentList = new ArrayList<PaymentTransactionData>();
        String selectQuery = "SELECT * FROM "+PAY_TRANSACTION_TABLE+" where medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String payment_id = cursor.getString(0);
                String invoice_id = cursor.getString(1);
                String client_name = cursor.getString(2);
                String paymode_name = cursor.getString(3);
                String pay_amount = cursor.getString(4);
                String pdc_number = cursor.getString(5);
                String pdc_date = cursor.getString(6);
                String pdc_bank = cursor.getString(7);
                String pay_date = cursor.getString(8);
                String pay_time = cursor.getString(9);
                String pay_status = cursor.getString(10);
                paymentList.add(new PaymentTransactionData(payment_id,invoice_id, client_name, pay_amount,paymode_name,pdc_number,pdc_date, pdc_bank,pay_date,pay_time,pay_status,medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return paymentList;
    }

    //RETRIEVE SELECTED SQLite Payment Transaction Records
    public ArrayList<PaymentTransactionData> getSelectedPaymentTransaction(String payment_id) {
        ArrayList<PaymentTransactionData> paymentList;
        paymentList = new ArrayList<PaymentTransactionData>();
        String selectQuery = "SELECT * FROM "+PAY_TRANSACTION_TABLE+" where "+ID+"= '"+payment_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String invoice_id = cursor.getString(1);
                String client_name = cursor.getString(2);
                String paymode_name = cursor.getString(3);
                String pay_amount = cursor.getString(4);
                String pdc_number = cursor.getString(5);
                String pdc_date = cursor.getString(6);
                String pdc_bank = cursor.getString(7);
                String pay_date = cursor.getString(8);
                String pay_time = cursor.getString(9);
                String pay_status = cursor.getString(10);
                String medrep_id = cursor.getString(11);
                paymentList.add(new PaymentTransactionData(payment_id,invoice_id, client_name, pay_amount,paymode_name,pdc_number,pdc_date, pdc_bank,pay_date,pay_time,pay_status,medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return paymentList;
    }

    //INSERT SQLite INVOICE Records Sync
    public void insertInvoiceSync(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("invoice_id", queryValues.get("invoice_id"));
        values.put("client_name", queryValues.get("client_name"));
        values.put("amount_total", queryValues.get("amount_total"));
        values.put("amount_paid", queryValues.get("amount_paid"));
        values.put("amount_remaining", queryValues.get("amount_remaining"));
        values.put("date_due", queryValues.get("date_due"));
        values.put("medrep_id", queryValues.get("medrep_id"));
        database.insert("tbl_invoice", null, values);
        database.close();
    }

    //RETRIEVE All SQLite Delivery Records
    public ArrayList<PaymentData> getAllInvoice(String medrep_id) {
        ArrayList<PaymentData> invoiceList;
        invoiceList = new ArrayList<PaymentData>();
        //String selectQuery = "SELECT  * FROM tbl_delivery WHERE medrep_id='"+medrep_id+"'";
        String selectQuery = "SELECT * FROM tbl_invoice where medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String invoice_id = cursor.getString(1);
                String client_name = cursor.getString(2);
                String amount_total = cursor.getString(3);
                String amount_paid = cursor.getString(4);
                //MINUS TANAN NA PAYMENT TRANSACTIONS SA PHONE NA NAHIMO NGA LA PA NA SEND SA ONLINE
                String amount_remaining = String.valueOf(Double.parseDouble(cursor.getString(5))-this.getPayTransactionTotal(invoice_id));
                String date_due = cursor.getString(6);
                invoiceList.add(new PaymentData(invoice_id, client_name, amount_total, amount_paid,amount_remaining,date_due, medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return invoiceList;
    }

    //INSERT SQLite INVOICE Records Sync
    public void insertInvoicePayment(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("invoice_id", queryValues.get("invoice_id"));
        values.put("paymode_name", queryValues.get("paymode_name"));
        values.put("payment_amount", queryValues.get("payment_amount"));
        values.put("payment_date", queryValues.get("payment_date"));
        values.put("medrep_id", queryValues.get("medrep_id"));
        database.insert("tbl_invoice_payments", null, values);
        database.close();
    }

    public void deleteInvoicePayment(String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM tbl_invoice_payments WHERE medrep_id='"+medrep_id+"'");
        database.close();
    }

    public ArrayList<InvoicePaymentData> getInvoicePayments(String invoice_id) {
        ArrayList<InvoicePaymentData> paymentList;
        paymentList = new ArrayList<InvoicePaymentData>();
        String selectQuery = "SELECT * FROM tbl_invoice_payments where paymode_name!='null' AND invoice_id='"+invoice_id+"'";
        String selectTransactionQuery = "SELECT * FROM "+PAY_TRANSACTION_TABLE+" where invoice_id='"+invoice_id+"' AND sync_status='0'";
        SQLiteDatabase database = this.getWritableDatabase();

        //PAYMENTS SA SERVER
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String paymode_name = cursor.getString(2);
                String payment_amount = cursor.getString(3);
                String payment_date = cursor.getString(4);
                String medrep_id = cursor.getString(5);
                paymentList.add(new InvoicePaymentData(invoice_id, paymode_name, payment_amount, payment_date,medrep_id));
            } while (cursor.moveToNext());
        }
        //TRANSACTIONS SA PHONE NA PAYMENTS
        Cursor cursorTransaction = database.rawQuery(selectTransactionQuery, null);
        if (cursorTransaction.moveToFirst()) {
            do {
                String t_paymode_name = cursorTransaction.getString(3);
                String t_payment_amount = cursorTransaction.getString(4);
                String t_payment_date = cursorTransaction.getString(8);
                String t_medrep_id = cursorTransaction.getString(11);
                paymentList.add(new InvoicePaymentData(invoice_id, t_paymode_name, t_payment_amount, t_payment_date, t_medrep_id));
            } while (cursorTransaction.moveToNext());
        }
        database.close();
        return paymentList;
    }

    public int getInvoicePaymentsLoaded(String invoice_id) {
        String selectQuery = "SELECT SUM(payment_amount) FROM tbl_invoice_payments where paymode_name!='null' AND invoice_id='"+invoice_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int check_total=0;
        if (cursor.moveToFirst()) {
            do {
                check_total = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return check_total;
    }



    /*****************SQLITE for DELIVERY RECORDS**************/
    //INSERT SQLite Delivery TRANSACTION Records Sync
    public void insertDeliveryTransactionData(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("delivery_id", queryValues.get("delivery_id"));
        values.put("delivery_status", queryValues.get("delivery_status"));
        values.put("delivery_date", queryValues.get("delivery_date"));
        values.put("delivery_time", queryValues.get("delivery_time"));
        values.put("medrep_id", queryValues.get("medrep_id"));
        values.put("or_number", queryValues.get("or_number"));
        values.put("client_name", queryValues.get("client_name"));
        values.put("address", queryValues.get("address"));
        database.insert("tbl_delivery_transactions", null, values);
        database.close();
    }

    //RETRIEVE All SQLite Delivery TRANSACTIONS Records
    public ArrayList<DeliveryTransactionData> getDeliveryTransaction(String medrep_id) {
        ArrayList<DeliveryTransactionData> deliveryTransactionList;
        deliveryTransactionList = new ArrayList<DeliveryTransactionData>();
        //String selectQuery = "SELECT  * FROM tbl_delivery_transactions WHERE medrep_id='"+medrep_id+"' AND delivery_status=0";
        String selectQuery = "SELECT  * FROM tbl_delivery_transactions WHERE medrep_id='"+medrep_id+"' ORDER BY sync_status, delivery_date";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String delivery_id = cursor.getString(1);
                String status = cursor.getString(7);
                String date =cursor.getString(3);
                String time =cursor.getString(4);
                String medrep =cursor.getString(5);
                String or_number =cursor.getString(6);
                deliveryTransactionList.add(new DeliveryTransactionData(delivery_id, status, date, time, medrep, or_number));
            } while (cursor.moveToNext());
        }
        database.close();
        return deliveryTransactionList;
    }

    //DELETE SQLite Delivery Records Sync
    public void deleteSelectedDeliveryTransaction(String delivery_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM tbl_delivery_transactions WHERE delivery_id='"+delivery_id+"'");
        database.close();
    }


    //INSERT SQLite Delivery Records Sync
    public void insertDeliverySync(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("delivery_id", queryValues.get("delivery_id"));
        values.put("order_id", queryValues.get("order_id"));
        values.put("client_name", queryValues.get("client_name"));
        values.put("client_address", queryValues.get("client_address"));
        values.put("medrep_id", queryValues.get("medrep_id"));
        database.insert("tbl_delivery", null, values);
        database.close();
    }

    //DELETE SQLite Delivery Records Sync
    public void deleteOldDeliverySync(String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM tbl_delivery WHERE medrep_id='"+medrep_id+"'");
        database.close();
    }

    //RETRIEVE All SQLite Delivery Records
    public ArrayList<DeliveryData> getAllDelivery(String medrep_id) {
        ArrayList<DeliveryData> deliveryList;
        deliveryList = new ArrayList<DeliveryData>();
        //String selectQuery = "SELECT  * FROM tbl_delivery WHERE medrep_id='"+medrep_id+"'";
        String selectQuery = "SELECT * FROM tbl_delivery where delivery_id NOT IN(SELECT delivery_id FROM tbl_delivery_transactions) AND tbl_delivery.medrep_id='"+medrep_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String delivery_id = cursor.getString(1);
                String order_id = cursor.getString(2);
                String client_name = cursor.getString(3);
                String client_address = cursor.getString(4);
                deliveryList.add(new DeliveryData(delivery_id, order_id, client_name, client_address, medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return deliveryList;
    }

    //DELIVERY ITEMS NA DB NI PRPE

    //DELETE DELIVERY ITEMS
    public void deleteDeliveryItems(String medrep_id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM tbl_delivery_items WHERE medrep_id='"+medrep_id+"'");
        database.close();
    }
    //INSERT DELIVERY ITEM
    public void insertDeliveryItem(HashMap<String, String> queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("delivery_id", queryValues.get("delivery_id"));
        values.put("product_id", queryValues.get("product_id"));
        values.put("product_name", queryValues.get("product_name"));
        values.put("lot_number", queryValues.get("lot_number"));
        values.put("expiry_date", queryValues.get("expiry_date"));
        values.put("quantity", queryValues.get("quantity"));
        values.put("medrep_id", queryValues.get("medrep_id"));
        database.insert("tbl_delivery_items", null, values);
        database.close();
    }

    //RETRIEVE All SQLite Delivery ITEM Records
    public ArrayList<DeliveryItemData> getDeliveryItems(String delivery_id) {
        ArrayList<DeliveryItemData> itemList;
        itemList = new ArrayList<DeliveryItemData>();
        String selectQuery = "SELECT  * FROM tbl_delivery_items WHERE delivery_id='"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String d_id = cursor.getString(1);
                String product_id = cursor.getString(2);
                String product_name = cursor.getString(3);
                String lot_number = cursor.getString(4);
                String expiry = cursor.getString(5);
                String quantity = cursor.getString(6);
                String medrep_id = cursor.getString(7);
                itemList.add(new DeliveryItemData(d_id,product_id,product_name, lot_number, expiry, quantity, medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return itemList;
    }

    //RETRIEVE All SQLite Delivery ITEM Records
    public ArrayList<DeliveryItemData> getDeliveryTransactionItems(String delivery_id) {
        ArrayList<DeliveryItemData> itemList;
        itemList = new ArrayList<DeliveryItemData>();
        String selectQuery = "SELECT  * FROM tbl_delivery_transactions WHERE delivery_id='"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                // cursor 0 is the list id
                String d_id = cursor.getString(1);
                String product_id = cursor.getString(2);
                String product_name = cursor.getString(3);
                String lot_number = cursor.getString(4);
                String expiry = cursor.getString(5);
                String quantity = cursor.getString(6);
                String medrep_id = cursor.getString(7);
                itemList.add(new DeliveryItemData(d_id,product_id,product_name, lot_number, expiry, quantity, medrep_id));
            } while (cursor.moveToNext());
        }
        database.close();
        return itemList;
    }

    public String getDeliveryClient(String delivery_id) {
        String selectQuery = "SELECT client_name FROM tbl_delivery_transactions WHERE delivery_id = '"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return check_count;
    }

    public String getDeliveryDate(String delivery_id) {
        String selectQuery = "SELECT delivery_date FROM tbl_delivery_transactions WHERE delivery_id = '"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return check_count;
    }

    public String getDeliveryTime(String delivery_id) {
        String selectQuery = "SELECT delivery_time FROM tbl_delivery_transactions WHERE delivery_id = '"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return check_count;
    }

    public String getDeliveryOr(String delivery_id) {
        String selectQuery = "SELECT or_number FROM tbl_delivery_transactions WHERE delivery_id = '"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return check_count;
    }

    public String getDeliveryAddress(String delivery_id) {
        String selectQuery = "SELECT address FROM tbl_delivery_transactions WHERE delivery_id = '"+delivery_id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        String check_count="";

        if (cursor.moveToFirst()) {
            do {
                check_count = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return check_count;
    }
}
