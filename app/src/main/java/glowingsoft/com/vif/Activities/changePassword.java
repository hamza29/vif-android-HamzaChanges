package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Models.FieldsModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class changePassword extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout linearLayout;
    DualProgressView progressBar;
    List<FieldsModel> fieldsModels;
    RelativeLayout rootLayout;
    List<View> viewList;
    Boolean flag = false;

SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        rootLayout = findViewById(R.id.rootLayout);
        fieldsModels = new ArrayList<>();
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.backarrow));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.progressBar);
        viewList = new ArrayList<>();
        if (GlobalClass.getInstance().isOnline(changePassword.this)) {
            fieldsModels.clear();
            viewList.clear();
            WebReq.get(changePassword.this, Urls.changePassword, null, new CallingRestapi(), sharedPreferences.getString(preferenceToken, ""));
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CallingRestapi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getString("status").equals("true")) {
                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("form");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FieldsModel fieldsModel = new FieldsModel();
                        fieldsModel.setName(jsonObject.getString("name"));
                        fieldsModel.setLabel(jsonObject.getString("label"));
                        fieldsModel.setType(jsonObject.getString("type"));
                        fieldsModel.setNeedsValidation(jsonObject.getString("needs_validation"));
                        fieldsModels.add(fieldsModel);
                    }
                    setLayout(fieldsModels, linearLayout);

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
                }
                Log.d("response_password", response.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setLayout(final List<FieldsModel> fieldsModels, LinearLayout layout) {
        LayoutInflater layoutInflater = getLayoutInflater();
        for (int i = 0; i < fieldsModels.size(); i++) {
            switch (fieldsModels.get(i).getType()) {
                case "password":
                    TextInputLayout textView = (TextInputLayout) layoutInflater.inflate(R.layout.textview, null);
                    EditText editTextN = textView.findViewById(R.id.edittext);
                    editTextN.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Log.d("respose_text", fieldsModels.get(i).getName());
                    editTextN.setHint("" + fieldsModels.get(i).getLabel());
                    editTextN.setId(i);
                    viewList.add(editTextN);
                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    buttonLayoutParams.setMargins(25, 0, 25, 0);
                    textView.setLayoutParams(buttonLayoutParams);
                    layout.addView(textView);
                    break;
                case "textarea":
                    TextInputLayout textInputLayout = (TextInputLayout) layoutInflater.inflate(R.layout.textarea, null);
                    final EditText editText = textInputLayout.findViewById(R.id.edittext);
                    editText.setId(i);
                    editText.setHint("" + fieldsModels.get(i).getLabel());
                    LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParam.setMargins(25, 0, 25, 0);
                    textInputLayout.setLayoutParams(layoutParam);
                    viewList.add(editText);
                    layout.addView(textInputLayout);
//                    EditText editText = (EditText) layoutInflater.inflate(R.layout.texta, null);
                    break;
//                case "select":
//                    final Spinner spinner = (Spinner) layoutInflater.inflate(R.layout.spinner, null);
//                    spinner.setId(i);
//                    LinearLayout.LayoutParams spinnerParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    spinnerParam.setMargins(25, 0, 25, 0);
//                    spinner.setLayoutParams(spinnerParam);
//                    spinner.setPrompt("Select Value");
//                    viewList.add(spinner);
//                    userListAdapter = new UserListAdapter(userListModels, MainActivity.this);
//                    spinner.setAdapter(userListAdapter);
//
//                    if (GlobalClass.getInstance().
//
//                            isOnline(MainActivity.this)) {
//                        Log.d("click", "click");
//                        WebReq.get(MainActivity.this, Urls.GetList, null, new GetUserList());
//                    } else {
//                        GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
//                    }
//
//                    layout.addView(spinner);
//
//                    break;
                case "submit":
                    LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.horizontallayout, null);

                    final Button button = (Button) layoutInflater.inflate(R.layout.button, null);
                    button.setId(i);
                    button.setGravity(Gravity.CENTER);
                    button.setText("" + fieldsModels.get(i).getLabel());
                    LinearLayout.LayoutParams buttonparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    buttonparam.setMargins(25, 0, 25, 0);
                    buttonparam.weight = 1;
                    button.setLayoutParams(buttonparam);
                    final int finalI = i;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RequestParams requestParams = new RequestParams();
                            if (fieldsModels.get(finalI).getType().equals("submit")) {
                                for (int i = 0; i < viewList.size(); i++) {
                                    if (viewList.get(i) instanceof EditText) {
                                        EditText editText1 = (EditText) viewList.get(i);
                                        if (editText1.getText().toString().length() == 0) {
                                            editText1.requestFocus();
                                            editText1.setError("Field is required");
                                            flag = true;
                                            break;
                                        } else {
                                            flag = false;
                                            requestParams.put(fieldsModels.get(i).getName(), editText1.getText().toString());
                                        }
                                    }


                                }
                                if (!flag) {
                                    if (GlobalClass.getInstance().isOnline(changePassword.this)) {
                                        WebReq.post(changePassword.this, Urls.changePassword, requestParams,
                                                new CallingPostapi(),  sharedPreferences.getString(preferenceToken, ""));
                                    } else {
                                        GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                    }
                                }

                            }
                        }
                    });
                    linearLayout.addView(button);
                    layout.addView(linearLayout);
                    break;
                case "label":
                    TextView label = (TextView) layoutInflater.inflate(R.layout.textviewlable, null);
                    label.setId(i);
                    layout.addView(label);
                    break;
                case "cancel":
                    Button cancel = (Button) layoutInflater.inflate(R.layout.button, null);
                    cancel.setId(i);
                    cancel.setText("" + fieldsModels.get(i).

                            getLabel());
                    cancel.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams cancelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cancelParam.setMargins(25, 0, 25, 0);
                    cancel.setLayoutParams(cancelParam);
                    layout.addView(cancel);
                    break;


            }
        }

    }

    public class CallingPostapi extends JsonHttpResponseHandler {
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
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_post_api", response.toString());
            try {
                GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            progressBar.setVisibility(View.GONE);
        }
    }

}
