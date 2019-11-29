package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class LoginSignupActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivLoginBg, ivSignupBg, ivLogin, ivSignup, ivName;
    RelativeLayout rlName;
    Button btnLogin, btnSignup;
    TextView tvLogin, tvName, tvForgotPassword;
    EditText etName, etEmail, etPassword;
    String name, email, password;
    RelativeLayout rootLayout;
    String preferenceId = "id";
    String preferenceName = "name";
    String preferenceEmail = "email";
    public static String preferenceToken = "access_token";
    DualProgressView progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (GlobalClass.getInstance().isLoggedIn(preferenceEmail)) {
            Intent mainActivityIntent = new Intent(LoginSignupActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        } else {
            getViews();

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivSignup.setVisibility(View.INVISIBLE);
                    ivSignupBg.setVisibility(View.INVISIBLE);
                    rlName.setVisibility(View.INVISIBLE);
                    ivName.setVisibility(View.INVISIBLE);
                    tvName.setVisibility(View.INVISIBLE);
                    ivLogin.setVisibility(View.VISIBLE);
                    ivLoginBg.setVisibility(View.VISIBLE);
                    tvForgotPassword.setVisibility(View.VISIBLE);
                }
            });
            ivLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalClass.getInstance().isOnline(getApplicationContext()) == true) {
                        email = etEmail.getText().toString();
                        password = etPassword.getText().toString();
                        if (email.equals("")) {
                            etEmail.setError("Enter email");
                        } else if (GlobalClass.getInstance().emailValidator(email) == false) {
                            etEmail.setError("Enter Valid Email");
                        } else if (password.equals("")) {
                            etPassword.setError("Enter Password");
                        } else if (password.length() < 8) {
                            etPassword.setError("Password Length can't be less than 8");
                        } else {
                            LoginMe();
                        }
                    } else {
                        GlobalClass.getInstance().SnackBar(rootLayout, Color.BLACK, Color.WHITE, Urls.internetString);
                    }
                }
            });
            ivSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalClass.getInstance().isOnline(getApplicationContext()) == true) {
                        name = etName.getText().toString();
                        email = etEmail.getText().toString();
                        password = etPassword.getText().toString();
                        if (name.equals("")) {
                            etName.setError("Enter name");

                        } else if (name.length() < 3) {
                            etName.setError("Enter Valid Name");
                        } else if (name.length() < 10) {
                            etName.setError("Name must be of 10 character");
                        } else if (email.equals("")) {
                            etEmail.requestFocus();
                            etEmail.setError("Enter email");

                        } else if (GlobalClass.getInstance().emailValidator(email) == false) {
                            etEmail.setError("Enter Valid Email");
                        } else if (password.equals("")) {
                            etPassword.requestFocus();
                            etPassword.setError("Enter Password");
                        } else if (password.length() < 8) {
                            etPassword.requestFocus();
                            etPassword.setError("Password Length can't be less than 8");
                        } else {
                            RegisterMe();
                        }
                    } else {
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), Urls.internetString);

                    }
                }
            });
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivSignup.setVisibility(View.VISIBLE);
                    ivSignupBg.setVisibility(View.VISIBLE);
                    rlName.setVisibility(View.VISIBLE);
                    rlName.requestFocus();
                    ivName.setVisibility(View.VISIBLE);
                    tvName.setVisibility(View.VISIBLE);
                    tvLogin.setText("Signup");
                    ivLogin.setVisibility(View.INVISIBLE);
                    ivLoginBg.setVisibility(View.INVISIBLE);
                    tvForgotPassword.setVisibility(View.INVISIBLE);

                }
            });
            tvForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginSignupActivity.this, changePassword.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void getViews() {
        ivLoginBg = findViewById(R.id.ivLoginBg);
        ivSignupBg = findViewById(R.id.ivSignupBg);
        ivLogin = findViewById(R.id.ivLogin);
        ivSignup = findViewById(R.id.ivSignup);
        rlName = findViewById(R.id.rlName);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();

        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        ivName = findViewById(R.id.ivName);
        tvName = findViewById(R.id.tvName);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        rootLayout = findViewById(R.id.rootLayout);
    }

    protected void RegisterMe() {

        if (GlobalClass.getInstance().isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.addHeader("consumer-key", "da8a96d4-221b-4c9d-9182-725a149685c6");

            mParams.put("name", name);
            mParams.put("email", email);
            mParams.put("password", password);
            //   WebReq.client.addHeader("content-type", "application/json");
            WebReq.post(getApplicationContext(), Urls.REGISTER, mParams, new LoginSignupActivity.RegisterHttpResponseHandlerSignUP(), "");

        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), Urls.internetString);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    class RegisterHttpResponseHandlerSignUP extends JsonHttpResponseHandler {
        RegisterHttpResponseHandlerSignUP() {
        }

        @Override
        public void onStart() {
            super.onStart();
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d("response_signup", "OnFailure" + e);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("response_signup", responseString + "" + throwable);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            Log.d("response_signup", mResponse.toString() + "Respo");

            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String message = mResponse.getString("message");
                    String status = mResponse.getString("status");
                    if (status.equals("true")) {
                        ivSignup.setVisibility(View.INVISIBLE);
                        ivSignupBg.setVisibility(View.INVISIBLE);
                        rlName.setVisibility(View.INVISIBLE);
                        ivName.setVisibility(View.INVISIBLE);
                        tvName.setVisibility(View.INVISIBLE);
                        ivLogin.setVisibility(View.VISIBLE);
                        ivLoginBg.setVisibility(View.VISIBLE);
                        tvForgotPassword.setVisibility(View.VISIBLE);
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), message);

                    } else {
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), message);
                    }
    } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    protected void LoginMe() {

        if (GlobalClass.getInstance().isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", email);
            mParams.put("password", password);

            WebReq.post(getApplicationContext(), Urls.LOGIN, mParams, new LoginSignupActivity.LoginHttpResponseHandler(), "");


        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), getResources().getString(R.string.incorrentPassword));
        }


    }

    class LoginHttpResponseHandler extends JsonHttpResponseHandler {
        LoginHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            progressBar.setVisibility(View.VISIBLE);


        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d("response_signup", "OnFailure");
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            Log.d("response_login", mResponse.toString() + "Respo");

            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String message = mResponse.getString("message");
                    String status = mResponse.getString("status");
                    if (status.equals("true")) {
                        JSONObject jsonObject = mResponse.getJSONObject("data").getJSONObject("user");
                        GlobalClass.getInstance().
                                addCredientialDetail(preferenceId,
                                        jsonObject.getString("id"),
                                        preferenceName, jsonObject.getString("name"),
                                        preferenceEmail, jsonObject.getString("email"));
                        sharedPreferences.edit().putString(preferenceToken, jsonObject.getString("access_token")).commit();
                        Intent mainActivityIntent = new Intent(LoginSignupActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), message);

                    } else {
                        GlobalClass.getInstance().SnackBar(rootLayout, getResources().getColor(R.color.black), getResources().getColor(R.color.white), message);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }
}
