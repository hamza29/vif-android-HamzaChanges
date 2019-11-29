package glowingsoft.com.vif.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Adapters.CreateNewFabAdapter;
import glowingsoft.com.vif.Adapters.FileMenuAdapter;
import glowingsoft.com.vif.Adapters.FileTypeAdapter;
import glowingsoft.com.vif.Adapters.FileTypeModel;
import glowingsoft.com.vif.Adapters.LeftMenuAdapter;
import glowingsoft.com.vif.Adapters.MainActivityFooterAdapter;
import glowingsoft.com.vif.Adapters.RecyclerView_MainAdapter;
import glowingsoft.com.vif.Adapters.SpinnerFolderAdapter;
import glowingsoft.com.vif.Adapters.UserListAdapter;
import glowingsoft.com.vif.BaseActivity;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Interfaces.GetImagePath;
import glowingsoft.com.vif.Models.FieldsModel;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.Models.UserListModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class SecurityActivity extends AppCompatActivity {
    Toolbar toolbar;
     RelativeLayout rootLayout;
    public static int a = 0;
    TextView Username, email, changpass, changepin1, pin2, changepin2;
    List<FieldsModel> fieldsModels;
    Boolean chooseFile = false;
     AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
      ProgressBar progressBar;
      List<EditText> editTextsList = new ArrayList<>();
    List<Button> buttonList = new ArrayList<>();
    List<Spinner> spinnersList;
     List<View> viewList = new ArrayList<>();
    int loc;
     Boolean ScanFlag = false;


    //    int myLastVisiblePos;// global variable of activity
    Boolean flag = false;
     boolean cameraFlag = false ;
       Boolean recordingFlag = false;
     SharedPreferences sharedPreferences;
    String publicKey, titles;
    Switch aSwitch;

    protected static final String TAG = BaseActivity.class.getName();
    public static boolean isAppWentToBg = false;

    public static boolean isWindowFocused = false;

    public static boolean isMenuOpened = false;

    public static boolean isBackPressed = false;

    public static final String PREF_SKIP_LOGIN = "skip_login";

    ///
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_security);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        progressBar = findViewById(R.id.progress);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.backarrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Username = findViewById(R.id.name);
        email = findViewById(R.id.email);
        changpass = findViewById(R.id.change_pass);
        changepin1 = findViewById(R.id.change_pin1);
        changepin2 = findViewById(R.id.change_pin2);
        aSwitch = findViewById(R.id.switch_pin2);
        final RequestParams requestParams = new RequestParams();
        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String statusSwitch1, statusSwitch2;
                if (aSwitch.isChecked()) {
                    statusSwitch1 = aSwitch.getTextOn().toString();
                    requestParams.put("is_active", "1");
                    Toast.makeText(SecurityActivity.this, "" + statusSwitch1, Toast.LENGTH_SHORT).show();

                    WebReq.post(SecurityActivity.this, "/api/user/activate/pin2", requestParams, new CreatefolderEtcApi(),sharedPreferences.getString(preferenceToken, ""));
                } else {
                    statusSwitch1 = aSwitch.getTextOff().toString();
                    requestParams.put("is_active", "0");
                    WebReq.post(SecurityActivity.this, "/api/user/activate/pin2", requestParams, new CreatefolderEtcApi(),sharedPreferences.getString(preferenceToken, ""));

                    Toast.makeText(SecurityActivity.this, "" + statusSwitch1, Toast.LENGTH_SHORT).show();
                }
            }
        });
        pin2 = findViewById(R.id.pin2);
        fieldsModels = new ArrayList<>();

        Username.setText("" + GlobalClass.getInstance().getUserName());
        email.setText("" + GlobalClass.getInstance().getUserEmail("email"));
        changpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonList.clear();
                editTextsList.clear();
                viewList.clear();
                fieldsModels.clear();
                publicKey = "/api/user/change/password";
                titles = "Change Password";
                callApi("/api/user/change/password", "Change Password");
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        changepin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonList.clear();
                editTextsList.clear();
                viewList.clear();
                fieldsModels.clear();
                publicKey = "/api/user/change/pin1";
                titles = "Change Pin 1";

                callApi("/api/user/change/pin1", "Change Pin 1");
                progressBar.setVisibility(View.VISIBLE);

            }
        });
        changepin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonList.clear();
                editTextsList.clear();
                viewList.clear();
                fieldsModels.clear();
                publicKey = "/api/user/change/pin2";
                titles = "Change Pin 2";

                callApi("/api/user/change/pin2", " Change Pin 2");
                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this instanceof SecurityActivity) {

        } else {
            isBackPressed = true;
        }

        Log.d(TAG,
                "onBackPressed " + isBackPressed + ""
                        + this.getLocalClassName());
        super.onBackPressed();
        finish();
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

    private void callApi(String key, String title) {

        if (GlobalClass.getInstance().isOnline(SecurityActivity.this)) {
            Log.e("response TGED", "HERE 1");
//            leftMenuModels.clear();
//            createNewList.clear();
            WebReq.get(SecurityActivity.this, key, null, new CreateNewMenuApi(title, key, null, " ", " "),sharedPreferences.getString(preferenceToken, ""));


        } else {
            Log.e("response TGED", "HERE 2");

            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
        }
    }

    public class CreateNewMenuApi extends JsonHttpResponseHandler {
        List<LeftMenuModel> leftMenuModels;
        String title;
        String api;
        String path, pathKey;
        String type;

        CreateNewMenuApi(String title, String apifile, String path, String pathKey, String type) {
            this.title = title;
            this.api = apifile;
            this.path = path;
            this.pathKey = pathKey;
            this.type = type;
        }

        @Override
        public void onStart() {
            super.onStart();
            Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);


            super.onStart();


            leftMenuModels = new ArrayList<>();
//            shimmerFrameLayout.setVisibility(View.VISIBLE);
//            shimmerFrameLayout.startShimmerAnimation();
//            recyclerView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_file_type", response.toString());
            Log.e("response TGED", "HERE RES 1");

            try {
                Log.d("response_value", response.getJSONObject("data").has("form") + "");
                if (response.getString("status").equals("true")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    Log.e("response TGED", "HERE RES 2");

                    if (jsonObject.has("form")) {
                        Log.e("response TGED", "HERE RESFORM ");
                        progressBar.setVisibility(View.GONE);

                        AlertDialog.Builder alert = new AlertDialog.Builder(SecurityActivity.this);
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view1 = layoutInflater.inflate(R.layout.field_layout, null);
                        LinearLayout linearLayout = view1.findViewById(R.id.layout);
                        TextView textView = view1.findViewById(R.id.titleTv);
                        textView.setText("Create " + title);
                        JSONArray jsonArray = jsonObject.getJSONArray("form");
                        Log.d("response_form", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject fieldObject = jsonArray.getJSONObject(i);
                            FieldsModel fieldsModel = new FieldsModel();
                            fieldsModel.setType("" + fieldObject.getString("type"));
                            fieldsModel.setLabel("" + fieldObject.getString("label"));
                            fieldsModel.setName("" + fieldObject.getString("name"));
                            fieldsModel.setNeedsValidation("" + fieldObject.getString("needs_validation"));
                            fieldsModels.add(fieldsModel);
                        }
                        Log.d("response_Field", fieldsModels.size() + "");
                        alert.setView(view1);
                        Log.d("response_fileType_size", fieldsModels.size() + "");
                        setLayout(fieldsModels, linearLayout, alert, api, path, pathKey, type);
                    }
                    Log.d("response_file_size", leftMenuModels.size() + "");


                }

            } catch (
                    JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
//            shimmerFrameLayout.stopShimmerAnimation();
//            shimmerFrameLayout.setVisibility(View.GONE);
            Log.e("response TGED", "HERE RES FAIL");
            progressBar.setVisibility(View.GONE);

            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "" + errorResponse.toString());
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressBar.setVisibility(View.GONE);

//            shimmerFrameLayout.stopShimmerAnimation();
//            shimmerFrameLayout.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setLayout(final List<FieldsModel> fieldsModels, LinearLayout
            layout, AlertDialog.Builder alert, final String api, final String path,
                           final String pathKey, final String type) {
        LayoutInflater layoutInflater = getLayoutInflater();
        final RequestParams requestParams = new RequestParams();

        final AlertDialog alertDialog = alert.create();
        for (int i = 0; i < fieldsModels.size(); i++) {
            loc = i;
            switch (fieldsModels.get(i).getType()) {
                case "password":
                    TextView labelView = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                    labelView.setText("" + fieldsModels.get(i).getName().toUpperCase());
                    LinearLayout.LayoutParams labelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    labelParam.setMargins(25, 0, 25, 0);
                    labelView.setLayoutParams(labelParam);
                    labelView.setId(i + 1);
                    layout.addView(labelView);
                    TextInputLayout textView = (TextInputLayout) layoutInflater.inflate(R.layout.textview, null);
                    EditText editTextN = textView.findViewById(R.id.edittext);
                    Log.d("respose_text", fieldsModels.get(i).getName());
                    editTextN.setHint("" + fieldsModels.get(i).getLabel());
                    editTextN.setId(i);
                    viewList.add(editTextN);
                    Log.d("response_list_size", viewList.size() + "");
                    LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    buttonLayoutParams.setMargins(25, 0, 25, 0);
                    textView.setLayoutParams(buttonLayoutParams);
                    editTextsList.add(editTextN);
                    layout.addView(textView);
                    break;
                case "textarea":
                    TextView textareaLabel = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                    textareaLabel.setText("" + fieldsModels.get(i).getName().toUpperCase());
                    LinearLayout.LayoutParams textareaParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textareaParam.setMargins(25, 0, 25, 0);
                    textareaLabel.setLayoutParams(textareaParam);
                    textareaLabel.setId(i + 1);
                    layout.addView(textareaLabel);
                    TextInputLayout textInputLayout = (TextInputLayout) layoutInflater.inflate(R.layout.textarea, null);
                    final EditText editText = textInputLayout.findViewById(R.id.edittext);
                    editText.setId(i);
                    editText.setHint("" + fieldsModels.get(i).getLabel());
                    LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParam.setMargins(25, 0, 25, 0);
                    textInputLayout.setLayoutParams(layoutParam);
                    editTextsList.add(editText);
                    viewList.add(editText);
                    Log.d("response_list_size", viewList.size() + "");

                    layout.addView(textInputLayout);
//                    EditText editText = (EditText) layoutInflater.inflate(R.layout.texta, null);
                    break;
                case "select":
                    TextView spinnerLabel = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                    spinnerLabel.setText("" + (fieldsModels.get(i).getName().contains("_") ? fieldsModels.get(i).getName().toUpperCase().replace("_", " ") : fieldsModels.get(i).getName().toUpperCase()));
                    LinearLayout.LayoutParams spinnerLabelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    spinnerLabelParam.setMargins(25, 0, 25, 0);
                    spinnerLabel.setLayoutParams(spinnerLabelParam);
                    spinnerLabel.setId(i + 1);
                    layout.addView(spinnerLabel);
                    final Spinner spinner = (Spinner) layoutInflater.inflate(R.layout.spinner, null);
                    spinner.setId(i);
                    LinearLayout.LayoutParams spinnerParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    spinnerParam.setMargins(25, 0, 25, 0);
                    spinner.setPadding(0, 5, 0, 5);
                    spinner.setLayoutParams(spinnerParam);
                    spinnersList.add(spinner);
                    viewList.add(spinner);
                    Log.d("response_list_size", viewList.size() + "");
                    final int loc = i;
                    break;
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
                            if (fieldsModels.get(finalI).getType().equals("submit")) {
                                for (int i = 0; i < viewList.size(); i++) {
                                    if (viewList.get(i) instanceof EditText) {
                                        EditText editText1 = (EditText) viewList.get(i);
                                        if (editText1.getText().toString().length() == 0) {
                                            editText1.setError("Field is required");
                                            flag = true;
                                        } else {
                                            flag = false;
                                            requestParams.put(fieldsModels.get(i).getName(), editText1.getText().toString());
                                            if (fieldsModels.get(i).getName().equals("pin1")) {
                                                sharedPreferences.edit().putString(PREF_SKIP_LOGIN, "" + editText1.getText().toString()).commit();
                                                a = 1;
                                                Log.e("response TGED", "PIN IS " + sharedPreferences.getString(PREF_SKIP_LOGIN, ""));
                                            }

                                        }
                                    }
                                }

                                if (GlobalClass.getInstance().isOnline(SecurityActivity.this)) {
                                    alertDialog.dismiss();
                                    WebReq.post(SecurityActivity.this, api, requestParams, new CreatefolderEtcApi(),sharedPreferences.getString(preferenceToken, ""));
                                } else {
                                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
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
                    final Button cancel = (Button) layoutInflater.inflate(R.layout.button, null);
                    cancel.setId(i);
                    cancel.setText("" + fieldsModels.get(i).getLabel());

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    LinearLayout.LayoutParams cancelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cancelParam.setMargins(25, 0, 25, 0);
                    cancel.setLayoutParams(cancelParam);
                    layout.addView(cancel);
                    break;


            }
        }

        alertDialog.show();
    }

    public class CreatefolderEtcApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_folder", response.toString());
            try {

                if (response.getString("status").equals("true")) {
                    cameraFlag = false;
                    ScanFlag = false;
                    recordingFlag = false;
                    chooseFile = false;
                    Toast.makeText(SecurityActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
//                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
//                    callApi(publicKey, titles);
                } else {
                    Toast.makeText(SecurityActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();

//                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable
                throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, throwable.getMessage());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
                errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, throwable.getMessage());

        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        a = 1;
        Log.d(TAG, "onStop ");
        applicationdidenterbackground();
    }

    public void applicationdidenterbackground() {
        if (!isWindowFocused) {
            isAppWentToBg = true;
//            Toast.makeText(getApplicationContext(),
//                    "App is Going to Background", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        applicationWillEnterForeground();
        if (a == 1) {
//            initiatePopupWindow();
        }
    }

    private void initiatePopupWindow() {

        dialogBuilder = new AlertDialog.Builder(SecurityActivity.this);

        LayoutInflater inflater = this.getLayoutInflater();

        @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.custom_dialog, null);

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();

        final Button subscribe = (Button) dialogView.findViewById(R.id.popuppin);
        final EditText edite = dialogView.findViewById(R.id.pin);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edite.getText().toString().equalsIgnoreCase("" + sharedPreferences.getString(PREF_SKIP_LOGIN, ""))) {
                    Toast.makeText(SecurityActivity.this, "PIN VERIFIED", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(SecurityActivity.this, "NOT VERIFIED", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.setCancelable(false);

    }

    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
//            Toast.makeText(getApplicationContext(), "App is in foreground",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        isWindowFocused = hasFocus;

        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }

        super.onWindowFocusChanged(hasFocus);
    }
}
