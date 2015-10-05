package jp.wktk.meijiwirelesslogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
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

    public void onCheck(View v) {
        if (showPass.isChecked()) {
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void onClick(View v) {
        String id, pass;
        id = editId.getText().toString();
        pass = editPass.getText().toString();
        try {
            OutputStream out = openFileOutput("id.txt", MODE_PRIVATE);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.append(id);
            writer.append(pass);
            writer.close();
            Toast.makeText(MainActivity.this, "入力完了", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
