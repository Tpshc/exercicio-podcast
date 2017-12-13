package br.ufpe.cin.if710.podcast.ui;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Arthur on 13/12/2017.
 */

public class Util {
    public static String MOBILEDATA = "mobileData";
    public static String WIFI = "wifi";
    public static String NONETWORK = "noNetwork";

    public static String checkNetworkStatus(Context context) {
        String networkStatus ="";
        final ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo == null){
            return NONETWORK;
        }else if( networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            networkStatus = WIFI;
        }else if(networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
            networkStatus = MOBILEDATA;
        }
        return networkStatus;
    }
}
