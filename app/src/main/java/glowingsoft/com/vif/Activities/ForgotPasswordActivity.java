package glowingsoft.com.vif.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import glowingsoft.com.vif.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView tvLogin;
    Button btn_submit;
    EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getViews();
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginSignupActivity.class);
                startActivity(intent);
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().equals("")) {
                    etEmail.setError("Field can't be empty");
                } else {
                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void getViews() {
        tvLogin = findViewById(R.id.tvLogin);
        btn_submit = findViewById(R.id.btn_submit);
        etEmail = findViewById(R.id.etEmail);
    }
}
