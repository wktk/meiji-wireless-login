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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WiFiReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;
    private URL mURL;
    private boolean triedLogin = false;

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
            selectWifiNetwork(cManager);
        }
        sharedPreferences = context.getSharedPreferences(AuthActivity.PREF_NAME, Context.MODE_PRIVATE);

        try {
            mURL = new URL("http://client3.google.com/generate_204");
        } catch (MalformedURLException e) {
            return;
        }

        checkConnectivity();
    }

    public void login(String response) {
        if (triedLogin) {
            done(false);
            return;
        }
        String url;
        Pattern pattern = Pattern.compile("<LoginURL>(.*)</LoginURL>");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            url = matcher.group(1);
        } else {
            return;
        }
        try {
            mURL = new URL(url);
        } catch (MalformedURLException e) {
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection urlConnection = null;
                String params;
                try {
                    urlConnection = (HttpURLConnection) mURL.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setInstanceFollowRedirects(false);
                    urlConnection.setUseCaches(false);
                    params = "UserName=" + URLEncoder.encode(sharedPreferences.getString(AuthActivity.PREF_ID, ""), "UTF-8");
                    params += "&Password=" + URLEncoder.encode(sharedPreferences.getString(AuthActivity.PREF_PASSWORD, ""), "UTF-8");
                    PrintStream printStream = new PrintStream(urlConnection.getOutputStream());
                    printStream.print(params);
                    printStream.close();
                    urlConnection.getInputStream();
                } catch (IOException e) {
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
            }
        }).start();

        triedLogin = true;
    }

    @TargetApi(21)
    private void selectWifiNetwork(ConnectivityManager cManager) {
        Network[] networks = cManager.getAllNetworks();
        for (Network network : networks) {
            NetworkCapabilities networkCapabilities = cManager.getNetworkCapabilities(network);
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                ConnectivityManager.setProcessDefaultNetwork(network);
            }
        }
    }

    private void checkConnectivity() {
        new Thread(new Runnable() {
            public void run() {
                HttpURLConnection urlConnection = null;
                String body = null;
                int httpResponseCode = 500;
                try {
                    urlConnection = (HttpURLConnection) mURL.openConnection();
                    urlConnection.setInstanceFollowRedirects(false);
                    urlConnection.setUseCaches(false);
                    urlConnection.getInputStream();
                    httpResponseCode = urlConnection.getResponseCode();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder stringBuilder = new StringBuilder();
                    while (true) {
                        byte[] line = new byte[1024];
                        int size = inputStream.read(line);
                        if (size <= 0)
                            break;
                        stringBuilder.append(new String(line, "UTF-8"));
                    }
                    body = stringBuilder.toString();
                } catch (IOException e) {
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();
                }
                if (httpResponseCode == 204) {
                    done(true);
                } else {
                    login(body);
                }
            }
        }).start();
    }

    private void done(boolean isSucceeded) {
        // Done logging in
    }


}
