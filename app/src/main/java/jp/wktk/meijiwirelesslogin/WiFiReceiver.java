package jp.wktk.meijiwirelesslogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiFiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    }

}
