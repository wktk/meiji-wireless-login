package jp.wktk.meijiwirelesslogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {
    EditText editId, editPass;
    CheckBox showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editId = (EditText)findViewById(R.id.inputId);
        editPass = (EditText)findViewById(R.id.inputPass);
        showPass = (CheckBox)findViewById(R.id.showPass);
    }

    public void onCheck(View v){
        if(showPass.isChecked()){
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void onClick(View v){
        String id, pass;
        id = editId.getText().toString();
        pass = editPass.getText().toString();
        try{
            OutputStream os1 = openFileOutput("id.txt",MODE_PRIVATE);
            PrintWriter idWriter = new PrintWriter(new OutputStreamWriter(os1,"UTF-8"));
            idWriter.append(id);
            idWriter.close();
            OutputStream os2 = openFileOutput("pass.txt",MODE_PRIVATE);
            PrintWriter passWriter = new PrintWriter(new OutputStreamWriter(os2,"UTF-8"));
            passWriter.append(pass);
            passWriter.close();
            Toast.makeText(MainActivity.this, "入力完了", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

/*
id,password読み込み用
 try{
            InputStream is1 = openFileInput("id.txt");
            BufferedReader idReader = new BufferedReader(new InputStreamReader(is1,"UTF-8"));
            Log.e("id", idReader.readLine());
            InputStream is2 = openFileInput("pass.txt");
            BufferedReader passReader = new BufferedReader(new InputStreamReader(is2,"UTF-8"));
            Log.e("pass", passReader.readLine());
    }catch(IOException e){
            e.printStackTrace();
        }
 */