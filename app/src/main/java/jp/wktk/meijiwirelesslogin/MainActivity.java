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
import android.preference.PreferenceManager;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference cbp;
    private NotificationManager mManager;

    private void sendNotification() {
        Intent intent = new Intent();
        intent.setClassName("jp.wktk.meijiwirelesslogin", "jp.wktk.meijiwirelesslogin.MainActivity");

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n = new Notification();
        n.icon = R.drawable.ic_launcher;
        n.tickerText = getResources().getString(R.string.app_name);
        n.number = 1;
        n.flags = Notification.FLAG_ONGOING_EVENT; // 常駐
        n.setLatestEventInfo(getApplicationContext(), getResources().getString(R.string.app_name),
                getResources().getString(R.string.working), pi);

        mManager.notify(0, n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        cbp = (CheckBoxPreference)findPreference("checkbox_preference");
        cbp.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean pNotification = sharedPreferences.getBoolean("checkbox_preference", false);
        if (pNotification) {
            mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.cancel(0);
        } else {
            sendNotification();
        }
        return true;
    }

}
