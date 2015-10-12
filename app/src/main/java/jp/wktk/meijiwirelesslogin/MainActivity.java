package jp.wktk.meijiwirelesslogin;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.apache.http.auth.AUTH;

public class MainActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference cbp;

    private NotificationManager mManager;

    private void sendNotification() {
        mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new Notification();

        Intent intent = new Intent();
        intent.setClassName("jp.wktk.meijiwirelesslogin", "jp.wktk.meijiwirelesslogin.MainActivity");

        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

        n.icon = R.drawable.ic_launcher;
        n.tickerText = "Wow! Meiji";
        n.number = 1;
        n.flags = Notification.FLAG_ONGOING_EVENT; // 常駐
        n.setLatestEventInfo(getApplicationContext(), "Wow! Meiji", "起動中", pi);

        mManager.notify(0, n);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        //cbp = (CheckBoxPreference)findPreference("checkbox_preference");
        //cbp.setOnPreferenceChangeListener(mListPreferenceListener);


        //PreferenceScreen preference = (PreferenceScreen)getPreferenceScreen().findPreference("preferenceKey2");
        //preference.setOnPreferenceClickListener(new LinkOnPreference(getApplicationContext()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        cbp = (CheckBoxPreference)findPreference("checkbox_preference");
        cbp.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean pNotification = sharedPreferences.getBoolean("checkbox_preference", false);
        if (!(pNotification)) {
            sendNotification();
        } else {
            mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            mManager.cancel(0);
        }
        Log.e("", "ok");
        onResume();
        return true;
    }
}
