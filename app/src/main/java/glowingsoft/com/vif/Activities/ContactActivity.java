package glowingsoft.com.vif.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.support.v7.widget.LinearLayoutManager;
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

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Adapters.BackupAdapter;
import glowingsoft.com.vif.Adapters.ContactAdapter;
import glowingsoft.com.vif.Adapters.FileMenuAdapter;
import glowingsoft.com.vif.Adapters.FileTypeAdapter;
import glowingsoft.com.vif.Adapters.FileTypeModel;
import glowingsoft.com.vif.Adapters.LeftMenuAdapter;
import glowingsoft.com.vif.Adapters.MainActivityFooterAdapter;
import glowingsoft.com.vif.Adapters.RecyclerView_MainAdapter;
import glowingsoft.com.vif.Adapters.SpinnerFolderAdapter;
import glowingsoft.com.vif.Adapters.UserListAdapter;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Interfaces.GetImagePath;
import glowingsoft.com.vif.Models.ContactModel;
import glowingsoft.com.vif.Models.FieldsModel;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.Models.UserListModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class ContactActivity extends AppCompatActivity {
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ContactAdapter mainActivityFooterAdapter;
    RelativeLayout rootLayout;
    public static int a = 0;
    TextView Username, email, changpass, changepin1, pin2, changepin2;
    List<FieldsModel> fieldsModels = new ArrayList<>();
    Boolean chooseFile = false;
    FloatingActionButton floatingActionButton;
    //    LayoutInflater layoutInflater;
    ContentValues values;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Uri imageUri;
    ProgressBar progressBar;
    RecyclerView_MainAdapter mainAdapter;
    List<LeftMenuModel> leftMenuModels;
    LeftMenuAdapter leftMenuAdapter;
    ListView leftMenuListview;
    TextView userNameTv;
    int REQUEST_CODE_SCAN = 110;
    List<UserListModel> userListModels= new ArrayList<>();
    UserListAdapter userListAdapter;
    int PICKFILE_RESULT_CODE = 100;
    List<LeftMenuModel> createNewList;
    List<EditText> editTextsList = new ArrayList<>();
    List<Button> buttonList = new ArrayList<>();
    List<Spinner> spinnersList= new ArrayList<>();
    DrawerLayout drawer;
    List<View> viewList = new ArrayList<>();
    int loc;
    Boolean CameraFlag = false;
    int itemselectedcheck = 0;
    public List<String> imageEncodedUri;
    List<FileTypeModel> fileTypeModels = new ArrayList<>();
    String cameraFile = null, cameraFile1 = null;
    int cameraFileFlag = 0;
    Boolean ScanFlag = false;


    //    int myLastVisiblePos;// global variable of activity
    Boolean flag = false;
    String fileApi = null;
    private static final int REQUEST_IMAGE = 120;
    File destination;
    String imagePath;
    String imagePathKey;
    int PERMISSION_CALLBACK_CONSTANT = 402;
    int requestCode = 0;
    String filePath;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    int REQUEST_PERMISSION_SETTING = 403;
    int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 404;
    int REQUEST_SCAN_CODE = 406;
    int Read_Permission = 407;
    int Record_audio = 408;
    String imageEncoded;
    Boolean fileUploadflag = false;
    HashMap<String, String> spinnerKeyValue= new HashMap<>();
    List<FoldersModel> spinnerFolderModelList;
    SpinnerFolderAdapter spinnerFolderAdapter;
    boolean cameraFlag = false, camerafileclick = false, scanfileClick = false;
    List<String> cameraTakenPhoto;
    ImageView imageView;
    GetImagePath getImagePath;
    int check = 0;
    String scanFile = null, scanFile1 = null;
    int scanfileFlag = 0;
    Boolean recordingFlag = false;
    int recordCode = 502;
    Boolean recordFlag = false;
    SharedPreferences sharedPreferences;
    String publicKey, titles;
    Switch aSwitch;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        rootLayout = findViewById(R.id.rootLayout);
        floatingActionButton = findViewById(R.id.fab);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonList.clear();
                editTextsList.clear();
                viewList.clear();
                fieldsModels.clear();
                publicKey = "/api/user/create/contact";
                titles = "Create Contact";
                callApi("/api/user/create/contact", "Create Contact");
//                progressBar.setVisibility(View.VISIBLE);
            }
        });
        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this, LinearLayoutManager.VERTICAL, false));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                CallingApi();
            }
        });
        CallingApi();
    }

    private void CallingApi() {
        if (GlobalClass.getInstance().isOnline(ContactActivity.this)) {
            WebReq.get(ContactActivity.this, "/api/user/get/contacts", null, new CallingRestApi(),
                    sharedPreferences.getString(preferenceToken, ""));
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
        }
    }

    @Override
    public void onBackPressed() {
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

    public class CallingRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {

                if (response.getString("status").equals("true")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    Log.d("response_recyclebin", response.toString());
//                    JSONArray jsonArrayFiles = response.getJSONObject("data").getJSONArray("files");
//                    JSONArray jsonArrayFolders = response.getJSONObject("data").getJSONArray("contacts");
                    JSONArray menuData = response.getJSONObject("data").getJSONArray("contacts");
                    List<ContactModel> subList = new ArrayList<>();
                    for (int j = 0; j < menuData.length(); j++) {
                        JSONObject jsonObject1 = menuData.getJSONObject(j);
                        ContactModel leftMenuModel = new ContactModel();
                        leftMenuModel.setId("" + jsonObject1.getString("id"));
                        leftMenuModel.setUser_id("" + jsonObject1.getString("user_id"));
                        leftMenuModel.setName("" + jsonObject1.getString("name"));
                        leftMenuModel.setCreated_at("" + jsonObject1.getString("created_at"));
                        subList.add(leftMenuModel);
                    }

                    ;
                    mainActivityFooterAdapter = new ContactAdapter(ContactActivity.this, subList, null, null, shimmerFrameLayout, recyclerView);
                    recyclerView.setAdapter(mainActivityFooterAdapter);

                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, responseString);

        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    private void callApi(String key, String title) {

        if (GlobalClass.getInstance().isOnline(ContactActivity.this)) {
            Log.e("response TGED", "HERE 1");
//            leftMenuModels.clear();
//            createNewList.clear();
            WebReq.get(ContactActivity.this,key, null, new  CreateNewMenuApi("Create Name", title, null, null,
                    null),sharedPreferences.getString(preferenceToken, ""));


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
            leftMenuModels = new ArrayList<>();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
            recyclerView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_file_type", response.toString());
            try {
                Log.d("response_value", response.getJSONObject("data").has("form") + "");
                if (response.getString("status").equals("true")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    if (jsonObject.has("file_type")) {
                        JSONArray fileTypeArray = jsonObject.getJSONArray("file_type");
                        for (int i = 0; i < fileTypeArray.length(); i++) {
                            JSONObject jsonObjectIndex = fileTypeArray.getJSONObject(i);
                            FileTypeModel fileTypeModel = new FileTypeModel();
                            fileTypeModel.setName("" + jsonObjectIndex.getString("name"));
                            fileTypeModel.setType("" + jsonObjectIndex.getString("type"));
                            fileTypeModels.add(fileTypeModel);
                        }

                    }
                    if (jsonObject.has("form")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ContactActivity.this);
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
                    } else {
                        JSONArray formArray = jsonObject.getJSONArray("menu");
                        for (int i = 0; i < formArray.length(); i++) {
                            JSONObject menuObject = formArray.getJSONObject(i);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + menuObject.getString("id"));
                            leftMenuModel.setName("" + menuObject.getString("name"));
                            leftMenuModel.setIcon("" + menuObject.getString("icon"));
                            leftMenuModel.setApi("" + menuObject.getString("api"));
                            leftMenuModel.setUrl("" + menuObject.getJSONObject("icondata").getString("url"));
                            leftMenuModel.setNameIcon("" + menuObject.getJSONObject("icondata").getString("name"));
                            leftMenuModels.add(leftMenuModel);
                        }
                        Log.d("response_file_size", leftMenuModels.size() + "");
                        final AlertDialog.Builder alertFile = new AlertDialog.Builder(ContactActivity.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.file_menu_layout, null);
                        GridView gridView = view1.findViewById(R.id.gridviewfiles);
                        TextView textView = view1.findViewById(R.id.titleTv);
                        textView.setText("Create " + title);
                        FileMenuAdapter fileMenuAdapter = new FileMenuAdapter(leftMenuModels, ContactActivity.this);
                        gridView.setAdapter(fileMenuAdapter);
                        alertFile.setView(view1);
                        final AlertDialog alertDialog = alertFile.create();
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                if (leftMenuModels.get(i).getIcon().equals("CAMERA")) {
                                    alertDialog.dismiss();
                                    cameraFileFlag = 0;
                                    fileTypeModels.clear();
                                    fileApi = leftMenuModels.get(i).getApi();
                                    if (GlobalClass.getInstance().isOnline(ContactActivity.this)) {
                                        WebReq.get(ContactActivity.this, fileApi, null,
                                                new CreateNewMenuApi("Photo Form ", fileApi, null, "file", "CAMERA"),sharedPreferences.getString(preferenceToken, ""));
                                    } else {
                                        GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                    }

                                }
                                if (leftMenuModels.get(i).getIcon().equals("SCANDOC")) {
                                    alertDialog.dismiss();
                                    check = 0;
                                    fileApi = leftMenuModels.get(i).getApi();
                                    WebReq.get(ContactActivity.this, fileApi, null, new CreateNewMenuApi("Photo Form ", fileApi, imagePath, "file", "SCANDOC"),sharedPreferences.getString(preferenceToken, ""));


                                }
                                if (leftMenuModels.get(i).getIcon().equals("UPLOAD")) {
                                    alertDialog.dismiss();
                                    fileTypeModels.clear();
                                    fileApi = leftMenuModels.get(i).getApi();
                                    check = 0;
                                    WebReq.get(ContactActivity.this, fileApi, null, new CreateNewMenuApi("Photo Form ", fileApi, null, "file", "UPLOAD"),sharedPreferences.getString(preferenceToken, ""));

                                }
                                if (leftMenuModels.get(i).getIcon().equals("MICROPHONE")) {
                                    fileApi = leftMenuModels.get(i).getApi();
                                    check = 0;
                                    alertDialog.dismiss();

                                    WebReq.get(ContactActivity.this, fileApi, null, new CreateNewMenuApi("Recording Form ", fileApi, null, "file", "MICROPHONE"),sharedPreferences.getString(preferenceToken, ""));


                                }


                            }
                        });
                        alertDialog.show();


                    }
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "" + response.getString("message"));
                }
            } catch (
                    JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);

            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "" + errorResponse.toString());
        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.GONE);
        }
    }
    private void setLayout(final List<FieldsModel> fieldsModels, LinearLayout
            layout, AlertDialog.Builder alert, final String api, final String path,
                           final String pathKey, final String type) {
        LayoutInflater layoutInflater = getLayoutInflater();
        final RequestParams requestParams = new RequestParams();

        final AlertDialog alertDialog = alert.create();
        for (int i = 0; i < fieldsModels.size(); i++) {
            loc = i;
            switch (fieldsModels.get(i).getType()) {
                case "text":
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
                     if (fieldsModels.get(i).getName().equals("sharing") || fieldsModels.get(i).getName().equals("user_id")) {
                        if (GlobalClass.getInstance().
                                isOnline(ContactActivity.this)) {
                            WebReq.get(ContactActivity.this, Urls.GetList, null, new GetUserList(spinner, loc, requestParams, fieldsModels.get(loc).getName()),sharedPreferences.getString(preferenceToken, ""));
                        } else {
                            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                        }
                    } else if (fieldsModels.get(i).getName().equals("file_type")) {
                        FileTypeAdapter fileTypeAdapter = new FileTypeAdapter(this, fileTypeModels);
                        spinner.setAdapter(fileTypeAdapter);

                    }

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                            Log.d("response_pos", fieldsModels.get(loc).getName());
                            if (++check > 1) {
                                if (fileTypeModels.get(pos).getType().equals("OTHERS")) {
                                    final AlertDialog dialog = new AlertDialog.Builder(ContactActivity.this).create();
                                    dialog.setTitle("Warning");
                                    dialog.setMessage("Please Select Single File");
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "0k", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    final AlertDialog dialog = new AlertDialog.Builder(ContactActivity.this).create();
                                    dialog.setTitle("Warning");
                                    dialog.setMessage("Please Select Multiple File");
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "0k", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }

                            if (fieldsModels.get(loc).getName().equals("file_type")) {
                                requestParams.put(fieldsModels.get(loc).getName(), fileTypeModels.get(pos).getType());
                            }

                            Log.d("response_map_size", spinnerKeyValue.size() + "");

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    layout.addView(spinner);

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
                                        }
                                    }
                                }
                                for (Map.Entry<String, String> entry : spinnerKeyValue.entrySet()) {
                                    String key = entry.getKey();
                                    String value = entry.getValue();
                                    requestParams.put(key, value);
                                }


                                    if (GlobalClass.getInstance().isOnline(ContactActivity.this)) {
                                        alertDialog.dismiss();
                                        WebReq.post(ContactActivity.this, "/api/user/create/contact", requestParams, new CreatefolderEtcApi(),sharedPreferences.getString(preferenceToken, ""));
                                    } else {
                                        GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                    }
                                }
                                Log.d("response_param", requestParams.toString());




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
    public class GetUserList extends JsonHttpResponseHandler {
        Spinner spinner;
        int loc;
        RequestParams requestParams;
        String key;

        public GetUserList(Spinner spinner, int loc, RequestParams requestParams, String key) {
            this.spinner = spinner;
            this.loc = loc;
            this.requestParams = requestParams;
            this.key = key;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_getUser", response.toString());
            try {
                if (response.getString("status").equals("true")) {
                    JSONArray listJsonObject = response.getJSONObject("data").getJSONArray("list");
                    for (int i = 0; i < listJsonObject.length(); i++) {
                        JSONObject jsonObject = listJsonObject.getJSONObject(i);
                        UserListModel userListModel = new UserListModel();
                        userListModel.setId("" + jsonObject.getString("id"));
                        userListModel.setName("" + jsonObject.getString("name"));
                        userListModel.setEmail("" + jsonObject.getString("email"));
                        userListModels.add(userListModel);
                    }
                    userListAdapter = new UserListAdapter(userListModels, ContactActivity.this);
                    spinner.setAdapter(userListAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            requestParams.put(key, userListModels.get(i).getId());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("response_failer", errorResponse.toString());
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
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
                    Toast.makeText(ContactActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                    callApi(publicKey, titles);

                } else {
                    Toast.makeText(ContactActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
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

}
