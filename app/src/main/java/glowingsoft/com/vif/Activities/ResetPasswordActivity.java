package glowingsoft.com.vif.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.R;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnSubmit;
    EditText etRestCode, etNewPassword, etConfirmPassword;
    LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        getViews();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etRestCode.getText().toString().equals("")) {
                    etRestCode.setError("Enter Code");
                } else if (etNewPassword.getText().toString().equals("")) {
                    etNewPassword.setError("Enter Password");
                } else if (etConfirmPassword.getText().toString().equals("")) {
                    etConfirmPassword.setError("Enter Password");
                } else if (etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), getResources().getString(R.string.incorrentPassword));
                }
            }
        });

    }

    public void getViews() {
        btnSubmit = findViewById(R.id.btnSubmit);
        etRestCode = findViewById(R.id.etRestCode);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rootLayout = findViewById(R.id.rootLayout);
    }
}
