package jp.wktk.meijiwirelesslogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {
    public static String PREF_ID = "id";
    public static String PREF_PASSWORD = "password";
    public static String PREF_NAME = "credentials";
    private EditText editId;
    private EditText editPass;
    private CheckBox showPass;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        editId = (EditText)findViewById(R.id.inputId);
        editPass = (EditText)findViewById(R.id.inputPass);
        showPass = (CheckBox)findViewById(R.id.showPass);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        editId.setText(sharedPreferences.getString(PREF_ID, ""));
        editPass.setText(sharedPreferences.getString(PREF_PASSWORD, ""));
    }

    public void onCheck(View v) {
        if (showPass.isChecked()) {
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ID, editId.getText().toString());
        editor.putString(PREF_PASSWORD, editPass.getText().toString());
        editor.apply();
        Toast.makeText(AuthActivity.this, R.string.saved, Toast.LENGTH_LONG).show();
    }
}
