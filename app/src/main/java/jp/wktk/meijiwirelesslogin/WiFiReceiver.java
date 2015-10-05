package jp.wktk.meijiwirelesslogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiFiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Return unless connected to the MIND Wireless
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (!ssid.equals("\"MIND-wireless-ap-n\"") && !ssid.equals("\"MIND-wireless-ap-bg\"")) {
            return;
        }

        // When connected to MIND and available?
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (nInfo != null && nInfo.isAvailable()) {
            // Do the stuff
        }
    }

}
