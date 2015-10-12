package jp.wktk.meijiwirelesslogin;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.SocketFactory;

public class WiFiReceiver extends BroadcastReceiver implements Callback {

    private SharedPreferences sharedPreferences;
    private SocketFactory socketFactory;

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences("system", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("enabled", true)) {
            return;
        }

        // Return unless connected to the MIND Wireless
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (!ssid.equals("\"MIND-wireless-ap-n\"") && !ssid.equals("\"MIND-wireless-ap-bg\"")) {
            return;
        }

        // Verify that connection is available
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (nInfo == null || !nInfo.isAvailable() || nInfo.isConnected()) {
            return;
        }

        // Select Wi-Fi network on APIs greater than v21
        if (Build.VERSION.SDK_INT >= 21) {
            getWifiSocketFactory(cManager);
        } else {
            socketFactory = SocketFactory.getDefault();
        }
        sharedPreferences = context.getSharedPreferences(AuthActivity.PREF_NAME, Context.MODE_PRIVATE);

        HashMap params = new HashMap();
        params.put("socket", socketFactory);
        params.put("host", "connectivitycheck.android.com");
        params.put("port", 80);
        params.put("request", "GET /generate_204 HTTP/1.1\r\n\r\n");
        AsyncSocket asyncSocket = new AsyncSocket(this);
        asyncSocket.execute(params);
    }

    @Override
    public void onSocketComplete(String response) {
        String url;
        Pattern pattern = Pattern.compile("<LoginURL>(.*)</LoginURL>");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            url = matcher.group(1);
        } else {
            return;
        }
        Uri uri = Uri.parse(url);
        HashMap params = new HashMap();
        String content;
        try {
            content = "UserName=" + URLEncoder.encode(sharedPreferences.getString(AuthActivity.PREF_ID, ""), "UTF-8");
            content += "&Password=" + URLEncoder.encode(sharedPreferences.getString(AuthActivity.PREF_PASSWORD, ""), "UTF-8");
        } catch (IOException e) {
            return;
        }
        params.put("socket", socketFactory);
        params.put("host", uri.getHost());
        params.put("port", 8080);
        params.put("request",
                        "POST " + uri.getPath() + " HTTP/1.1\n" +
                        "Host: " + uri.getHost() +
                        "Content-Type: text/plain\n" +
                        "Content-length: " + content.length() + "\n" +
                        "\n" + content);
        AsyncSocket asyncSocket = new AsyncSocket(new Callback() {
            @Override
            public void onSocketComplete(String response) {
            }
        });
        asyncSocket.execute(params);
    }

    @TargetApi(21)
    private void getWifiSocketFactory(ConnectivityManager cManager) {
        Network[] networks = cManager.getAllNetworks();
        for (Network network : networks) {
            NetworkCapabilities networkCapabilities = cManager.getNetworkCapabilities(network);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                socketFactory = network.getSocketFactory();
            }
        }
    }

}
