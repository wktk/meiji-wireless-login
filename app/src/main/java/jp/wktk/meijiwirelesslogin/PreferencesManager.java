package jp.wktk.meijiwirelesslogin;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class PreferencesManager {

    private String DATA_FILE = "id.txt";

    private Context context;
    private String id;
    private String password;

    public PreferencesManager(Context context_) {
        this.context = context_;
        try {
            InputStream inputStream = context.openFileInput(DATA_FILE);
            BufferedReader reader= new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            id = reader.readLine();
            password = reader.readLine();
        } catch (IOException e) {
            Log.e("MIND:PreferencesManager", e.toString());
        }
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setCredential(String id, String password) {
        try {
            OutputStream out = context.openFileOutput(DATA_FILE, context.MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.append(id + "\n" + password);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
