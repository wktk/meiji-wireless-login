package jp.wktk.meijiwirelesslogin;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

import javax.net.SocketFactory;

public class AsyncSocket extends AsyncTask<HashMap, Integer, String> {
    private Callback callback;

    public AsyncSocket(Callback callback){
        this.callback = callback;
    }

    @Override
    public String doInBackground(HashMap... hash) {
        String response = "";
        for (HashMap params : hash) {
            SocketFactory socketFactory = (SocketFactory) params.get("socket");
            String host = (String) params.get("host");
            Integer port = (Integer) params.get("port");
            String request = (String) params.get("request");

            try {
                Socket socket = socketFactory.createSocket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.write(request);
                writer.flush();
                String line;
                while ((line = reader.readLine()) != null) {
                    response += line;
                }

                writer.close();
                reader.close();
                socket.close();
            } catch (Exception e) {
                Log.e("MIND:HTTP", e.toString());
            }
        }

        callback.onSocketComplete(response);
        return null;
    }
}
