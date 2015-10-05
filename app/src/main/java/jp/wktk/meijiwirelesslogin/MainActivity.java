package jp.wktk.meijiwirelesslogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editId, editPass;
    CheckBox showPass;
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editId = (EditText)findViewById(R.id.inputId);
        editPass = (EditText)findViewById(R.id.inputPass);
        showPass = (CheckBox)findViewById(R.id.showPass);

        preferencesManager = new PreferencesManager(this);
        editId.setText(preferencesManager.getId());
        editPass.setText(preferencesManager.getPassword());
    }

    public void onCheck(View v) {
        if (showPass.isChecked()) {
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void onClick(View v) {
        String id = editId.getText().toString();
        String password = editPass.getText().toString();
        preferencesManager.setCredential(id, password);
        Toast.makeText(MainActivity.this, "入力完了", Toast.LENGTH_SHORT).show();
    }
}
