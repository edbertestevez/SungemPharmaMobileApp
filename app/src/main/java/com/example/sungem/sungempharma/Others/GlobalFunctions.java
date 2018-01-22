package com.example.sungem.sungempharma.Others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by trebd on 8/19/2017.
 */

public class GlobalFunctions {

    Context mContext;
    // constructor
    public GlobalFunctions(Context context){
        this.mContext = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String stringMonth(int intMonth) {
        String month[] = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        String monthString = month[intMonth];
        return monthString;
    }

    //MAIN LINK FOR TRANSACTIONS
    public String mainUrl(){
        return IPAddress()+"mobile/medrep/";
    }
    public String internalUrl() {
        return IPAddress()+"mobile/";
    }
    public String externalUrl(){
        return IPAddress();
    }

    public String adminUrl(){
        return IPAddress()+"mobile/admin/";
    }

    public String IPAddress() {
        return "http://sungempharma.000webhostapp.com/";
    }

    /*0
    //MAIN LINK FOR TRANSACTIONS
    public String mainUrl(){
        return IPAddress()+"SungemPharmaFinalOldVersion/mobile/medrep/";
    }
    public String internalUrl() {
        return IPAddress()+"SungemPharmaFinalOldVersion/mobile/";
    }
    public String externalUrl(){
        return IPAddress()+"SungemPharmaFinalOldVersion/";
    }

    public String adminUrl(){
        return IPAddress()+"SungemPharmaFinalOldVersion/mobile/admin/";
    }

    public String IPAddress(){
        return "http://192.168.1.4/";
    }
*/
    //JACO CODES
    public static String appName(){
        return "SungemPharma";
    }
    public static String adminActivity(){
        return "Admin";
    }
    public static String medrepActivity(){
        return "Medical Representative";
    }

}
