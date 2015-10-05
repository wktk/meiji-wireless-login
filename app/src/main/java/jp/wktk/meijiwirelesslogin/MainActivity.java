package jp.wktk.meijiwirelesslogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        //ボタンが押された時
        Toast.makeText(MainActivity.this, "ボタン", Toast.LENGTH_SHORT).show();
    }
}
