package jp.wktk.meijiwirelesslogin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class MainActivity extends PreferenceActivity {

    private CheckBoxPreference cbp;
    private NotificationManager mManager;
    private SharedPreferences globalSharedPreferences;

    private void updateNotification() {
        mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancel(0);

        if (!globalSharedPreferences.getBoolean("taskbar", false)) {
            return;
        }

        Intent intent = new Intent();
        intent.setClassName("jp.wktk.meijiwirelesslogin", "jp.wktk.meijiwirelesslogin.MainActivity");

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n = new Notification();
        n.icon = getNotificationIcon();
        n.tickerText = getResources().getString(R.string.app_name);
        n.number = 1;
        n.flags = Notification.FLAG_ONGOING_EVENT; // 常駐
        n.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name), getStatus(), pi);

        mManager.notify(0, n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        globalSharedPreferences = getSharedPreferences("system", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        cbp = (CheckBoxPreference)findPreference("taskbar");
        cbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = globalSharedPreferences.edit();
                editor.putBoolean("taskbar", (Boolean) newValue);
                editor.apply();
                updateNotification();
                return true;
            }
        });
        cbp = (CheckBoxPreference)findPreference("enabled");
        cbp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = globalSharedPreferences.edit();
                editor.putBoolean("enabled", (Boolean) newValue);
                editor.apply();
                updateNotification();
                return true;
            }
        });
        updateNotification();
    }

    private int getNotificationIcon() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return R.drawable.ic_silhouette;
        } else {
            return R.drawable.ic_launcher;
        }
    }

    private String getStatus() {
        if (globalSharedPreferences.getBoolean("enabled", true)) {
            return getResources().getString(R.string.working);
        } else {
            return getResources().getString(R.string.not_working);
        }
    }

}
