package com.hlgranite;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yeang-shing.then on 10/10/13.
 */
public class Utils {

    /**
     * Check internet access.
     * See http://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
     * @param context
     * @return
     */
    public static boolean isInternetConnected(Context context) {
        boolean connected = false;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        Log.d("INFO", "Network: "+connected);
        if(!connected) {
            Toast.makeText(context.getApplicationContext(), "No internet connection!", Toast.LENGTH_LONG).show();
        }
        return connected;
    }

}
