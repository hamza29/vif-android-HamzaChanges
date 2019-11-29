package glowingsoft.com.vif.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.scanlibrary.ScanActivity;
import com.squareup.picasso.Picasso;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Adapters.CreateNewFabAdapter;
import glowingsoft.com.vif.Adapters.FileMenuAdapter;
import glowingsoft.com.vif.Adapters.FileTypeAdapter;
import glowingsoft.com.vif.Adapters.FileTypeModel;
import glowingsoft.com.vif.Adapters.LeftMenuAdapter;
import glowingsoft.com.vif.Adapters.RecyclerView_MainAdapter;
import glowingsoft.com.vif.Adapters.SpinnerFolderAdapter;
import glowingsoft.com.vif.Adapters.UserListAdapter;
import glowingsoft.com.vif.BaseActivity;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Interfaces.GetImagePath;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.FieldsModel;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.Models.UserListModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.RealPathUtil;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.SecurityActivity.a;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RefreshInterface, GetImagePath {
    Boolean chooseFile = false;
    RelativeLayout rootLayout;
    FloatingActionButton floatingActionButton;
    //    LayoutInflater layoutInflater;
    ContentValues values;
    Uri imageUri;
    protected static final String TAG = BaseActivity.class.getName();
    public static boolean isAppWentToBg = false;

    public static boolean isWindowFocused = false;

    public static boolean isMenuOpened = false;

    public static boolean isBackPressed = false;

    public static final String PREFERENCE = "preference";
    public static final String PREF_SKIP_LOGIN = "skip_login";
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;


    DualProgressView progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView_MainAdapter mainAdapter;
    List<LeftMenuModel> leftMenuModels;
    LeftMenuAdapter leftMenuAdapter;
    ListView leftMenuListview;
    TextView userNameTv;
    RecyclerView recyclerView;
    int REQUEST_CODE_SCAN = 110;
    List<UserListModel> userListModels;
    UserListAdapter userListAdapter;
    int PICKFILE_RESULT_CODE = 100;
    List<LeftMenuModel> createNewList;
    List<FieldsModel> fieldsModels;
    List<EditText> editTextsList;
    List<Button> buttonList;
    List<Spinner> spinnersList;
    DrawerLayout drawer;
    List<View> viewList;
    int loc;
    Boolean CameraFlag = false;
    int itemselectedcheck = 0;
    public List<String> imageEncodedUri = new ArrayList<>();
    List<FileTypeModel> fileTypeModels = new ArrayList<>();
    String cameraFile = null, cameraFile1 = null;
    int cameraFileFlag = 0;
    Boolean ScanFlag = false;
    SharedPreferences sharedPreferences;


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
    ShimmerFrameLayout shimmerFrameLayout, menuShimmer;
    String imageEncoded;
    Boolean fileUploadflag = false;
    HashMap<String, String> spinnerKeyValue;
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
    ///new
    private static final int REQUEST_CODE = 123;
    private ArrayList<String> mResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        if (a == 0) {
            sharedPreferences.edit().putString(PREF_SKIP_LOGIN, "123456").commit();
            a = 1;
        }
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.RECORD_AUDIO

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (report.areAllPermissionsGranted()) {

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(Manifest.permission.CAMERA, true);
                    editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
                    editor.putBoolean(Manifest.permission.READ_EXTERNAL_STORAGE, true);
                    editor.putBoolean(Manifest.permission.RECORD_AUDIO, true);
                    editor.commit();
//                    loadHomeFragment();
                } else {
//                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        bindViews();
    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {

        } else {
            isBackPressed = true;
        }

        Log.d(TAG,
                "onBackPressed " + isBackPressed + ""
                        + this.getLocalClassName());
       // super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Warning");
            alertDialog.setMessage("Are you sure to close this application");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    MainActivity.super.onBackPressed();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void bindViews() {
        spinnerKeyValue = new HashMap<>();
        cameraTakenPhoto = new ArrayList<>();
        getImagePath = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recylerview);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        menuShimmer = findViewById(R.id.menuShimmer);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        userListModels = new ArrayList<>();
        buttonList = new ArrayList<>();
        spinnersList = new ArrayList<>();
        editTextsList = new ArrayList<>();
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");
        fieldsModels = new ArrayList<>();
        userNameTv = findViewById(R.id.userNameTv);
        userNameTv.setText("" + GlobalClass.getInstance().getUserName());
//        filesModelList = new ArrayList<>();
        rootLayout = findViewById(R.id.rootLayout);
        leftMenuListview = findViewById(R.id.leftMenuListView);
        leftMenuModels = new ArrayList<>();
        leftMenuAdapter = new LeftMenuAdapter(leftMenuModels, MainActivity.this, drawer);
        leftMenuListview.setAdapter(leftMenuAdapter);
//        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.bringToFront();
                callApi();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");
        createNewList = new ArrayList<>();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //home icon layout work
        callApi();


    }

    private void callApi() {

        if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
            leftMenuModels.clear();
            createNewList.clear();
            WebReq.get(MainActivity.this, Urls.Home, null, new HomeApi(), sharedPreferences.getString(preferenceToken, ""));
        } else {

            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list_view:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void floatinActionbuttonAction() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialog = layoutInflater.inflate(R.layout.create_new_menu, null);
        ListView listView = dialog.findViewById(R.id.listviewmenu);
        TextView oktv = dialog.findViewById(R.id.tvOk);
        TextView canceltv = dialog.findViewById(R.id.tvCancel);
        Log.d("response_size 389", createNewList.size() + "");
        CreateNewFabAdapter adapter = new CreateNewFabAdapter(createNewList, MainActivity.this);
        listView.setAdapter(adapter);
        alert.setView(dialog);
        final AlertDialog alertDialog = alert.create();
        oktv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        canceltv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                fieldsModels.clear();
                Log.d("response_api", createNewList.get(i).getApi());
                alertDialog.dismiss();
                Log.e("response response TGED", "NEW IS : " + createNewList.get(i).getApi());
                if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                    userListModels.clear();
                    buttonList.clear();
                    spinnersList.clear();
                    editTextsList.clear();
                    viewList = new ArrayList<>();
                    WebReq.get(MainActivity.this, createNewList.get(i).getApi(),
                            null, new CreateNewMenuApi(createNewList.get(i).getName(),
                                    createNewList.get(i).getApi(),
                                    null, null, null), sharedPreferences.getString(preferenceToken, ""));
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "" + Urls.internetString);
                }

            }
        });
        alertDialog.show();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                floatinActionbuttonAction();
                break;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == recordCode) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file
                Log.d("response_audio", filePath);
                Picasso.get().load(new File(filePath)).fit().centerCrop().placeholder(R.drawable.audioplaceholder).into(imageView);

            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
            }
        }

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                TakePicturefun();
            }
        }
        if (requestCode == REQUEST_SCAN_CODE) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                ScanDocument();
            }
        }

        if (requestCode == 13 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 23) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri selectedImage = getImageUri(this, photo);
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(filePath));
//                                image_check = 1;

                imageView.setImageBitmap(thumbnail);
////

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                imagePath = filePath;
                Log.d("response_path", imagePath);
                File imgFile = new File(imagePath);
                getImagePath.AddToList(imagePath);
                if (cameraFileFlag == 0) {
                    cameraFile = imagePath;
                    cameraFileFlag++;

                } else {
                    cameraFile1 = imagePath;
                    cameraFileFlag++;
                }
                Log.d("response_images", cameraTakenPhoto.get(0));

                Log.d("response_images_size", cameraTakenPhoto.size() + "");

                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    Picasso.get().load(new File(imagePath)).fit().centerCrop().into(imageView);
                    //////////
                } else {
                    Uri selectedImage1 = data.getData();
                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                    Cursor cursor1 = getContentResolver().query(selectedImage1, filePathColumn1, null, null, null);
                    cursor1.moveToFirst();
                    int columnIndex1 = cursor1.getColumnIndex(filePathColumn1[0]);
                    filePath = cursor1.getString(columnIndex1);
                    cursor1.close();
                    Bitmap thumbnail1 = (BitmapFactory.decodeFile(filePath));

                    imageView.setImageBitmap(thumbnail1);
                    ////////

                    imagePath = filePath;
                    Log.d("response_path", imagePath);
                    File imgFile1 = new File(imagePath);
                    getImagePath.AddToList(imagePath);
                    if (cameraFileFlag == 0) {
                        cameraFile = imagePath;
                        cameraFileFlag++;

                    } else {
                        cameraFile1 = imagePath;
                        cameraFileFlag++;
                    }
                    Log.d("response_images", cameraTakenPhoto.get(0));

                    Log.d("response_images_size", cameraTakenPhoto.size() + "");

                    if (imgFile1.exists()) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Picasso.get().load(new File(imagePath)).fit().centerCrop().into(imageView);

                        ///////
                    }
                }
            }

        }
        if (requestCode == REQUEST_CODE_SCAN && resultCode == Activity.RESULT_OK) {
            imagePath = data.getStringExtra(ScanActivity.RESULT_IMAGE_PATH);

            try {
                FileInputStream in = new FileInputStream(destination);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            Log.d("response_path", imagePath);
            if (scanfileFlag == 0) {
                scanFile = imagePath;
                scanfileFlag++;

            } else {
                scanFile1 = imagePath;
                scanfileFlag++;
            }
            Picasso.get().load(new File(imagePath)).fit().centerCrop().into(imageView);
//                        Picasso.get().load(new File(RealPathUtil.getRealPath(MainActivity.this, uri))).fit().centerCrop().into(imageView);


        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                fileUploadflag = true;
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
//////                Log.e("response TGED", "herer" + imageEncodedUri.get(0).toString());
////
                Picasso.get().load(new File(mResults.get(0))).fit().centerCrop().into(imageView);
////
                // show results in textview
//                StringBuffer sb = new StringBuffer();
//                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
//                for(String result : mResults) {
//                    sb.append(result).append("\n");
//                }
//                tvResults.setText(sb.toString());
            }
        }
//        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
//
////            //////////////
////            fileUploadflag = true;
////            String[] filePathColumn = {MediaStore.Images.Media.DATA};
////            imageEncodedUri = new ArrayList<String>();
//////
////            Log.e("response TGED", "herer");
////            if (data.getData() != null) {
////
////                Uri mImageUri = data.getData();
////
////                // Get the cursor
////                Cursor cursor = getContentResolver().query(mImageUri,
////                        filePathColumn, null, null, null);
////                // Move to first row
////                cursor.moveToFirst();
////
////                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                imagePath = cursor.getString(columnIndex);
////
////                cursor.close();
////                imageEncodedUri.add(imagePath);
//////                Log.e("response TGED", "herer" + imageEncodedUri.get(0).toString());
////
////                Picasso.get().load(new File(imageEncodedUri.get(0))).fit().centerCrop().into(imageView);
////
////
////            } else {
////                Log.e("response TGED", "no herer");
////
////                if (data.getClipData() != null) {
////                    ClipData mClipData = data.getClipData();
////                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
////                    for (int i = 0; i < mClipData.getItemCount(); i++) {
////
////                        ClipData.Item item = mClipData.getItemAt(i);
////                        Uri uri = item.getUri();
////                        mArrayUri.add(uri);
////                        // Get the cursor
////                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
////                        // Move to first row
////                        cursor.moveToFirst();
////
////                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                        imagePath = cursor.getString(columnIndex);
////
////                        cursor.close();
////                        imageEncodedUri.add(RealPathUtil.getRealPath(MainActivity.this, uri));
////
//////                        Picasso.get().load(new File(imageEncodedUri.get(0))).fit().centerCrop().into(imageView);
////
////                        Picasso.get().load(new File(RealPathUtil.getRealPath(MainActivity.this, uri))).fit().centerCrop().into(imageView);
////                        Log.e("response TGED", "no herer" + imageEncodedUri.get(0).toString());
////
////                    }
////                    Log.e("LOG_TAG", "Selected Images" + imageEncodedUri.get(0));
////                }
////            }
////        }
//
//
//            ///////////////
//            Log.d("response_file", "response_file");
//            imagePath = data.getData().getPath();
//            fileUploadflag = true;
//            imageEncodedUri = new ArrayList<>();
//
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            if (data.getData() != null) {
//
//                Uri mImageUri = data.getData();
//
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(mImageUri,
//                        filePathColumn, null, null, null);
//                // Move to first row
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imageEncoded = cursor.getString(columnIndex);
//                imageEncodedUri.add(RealPathUtil.getRealPath(MainActivity.this, data.getData()));
//                cursor.close();
//
//                Picasso.get().load(new File(RealPathUtil.getRealPath(MainActivity.this, data.getData()))).fit().centerCrop().into(imageView);
//
//
//            } else {
//                if (data.getClipData() != null) {
//                    ClipData mClipData = data.getClipData();
//                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                    for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                        ClipData.Item item = mClipData.getItemAt(i);
//                        Uri uri = item.getUri();
//                        mArrayUri.add(uri);
//                        // Get the cursor
//                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
//                        // Move to first row
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        imageEncoded = cursor.getString(columnIndex);
//                        imageEncodedUri.add(RealPathUtil.getRealPath(MainActivity.this, uri));
//                        cursor.close();
//                        Picasso.get().load(new File(RealPathUtil.getRealPath(MainActivity.this, uri))).fit().centerCrop().into(imageView);
//
//                    }
//                    Log.d("response_file_size", imageEncodedUri.size() + "");
//
//
//                }
//            }
//        }
    }

    @Override
    public void refreshLayout() {
        callApi();
    }

    @Override
    public void AddToList(String path) {
        cameraTakenPhoto.add(path);
    }

    public class HomeApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
//            recyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
            menuShimmer.setVisibility(View.VISIBLE);
            menuShimmer.startShimmerAnimation();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response", response.toString());
            mainAdapter = new RecyclerView_MainAdapter(MainActivity.this, response, progressBar, MainActivity.this, shimmerFrameLayout, recyclerView);
            recyclerView.setAdapter(mainAdapter);
            try {

                JSONArray leftMenuArray = response.getJSONObject("data").getJSONArray("leftmenu");
                JSONArray createNewArray = response.getJSONObject("data").getJSONArray("createnew");

                for (int k = 0; k < createNewArray.length(); k++) {
                    JSONObject createObject = createNewArray.getJSONObject(k);
                    LeftMenuModel leftMenuModel = new LeftMenuModel();
                    leftMenuModel.setId("" + createObject.getString("id"));
                    leftMenuModel.setName("" + createObject.getString("name"));
                    leftMenuModel.setIcon("" + createObject.getString("icon"));
                    leftMenuModel.setApi("" + createObject.getString("api"));
                    leftMenuModel.setUrl("" + createObject.getJSONObject("icondata").getString("url"));
                    leftMenuModel.setNameIcon("" + createObject.getJSONObject("icondata").getString("name"));
                    createNewList.add(leftMenuModel);
                }

                for (int i = 0; i < leftMenuArray.length(); i++) {
                    JSONObject jsonObject = leftMenuArray.getJSONObject(i);
                    LeftMenuModel leftMenuModel = new LeftMenuModel();
                    leftMenuModel.setId("" + jsonObject.getString("id"));
                    leftMenuModel.setName("" + jsonObject.getString("name"));
                    leftMenuModel.setApi("" + jsonObject.getString("api"));
                    leftMenuModel.setIcon("" + jsonObject.getString("icon"));
                    leftMenuModel.setUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                    leftMenuModel.setNameIcon("" + jsonObject.getJSONObject("icondata").getString("name"));
                    leftMenuModels.add(leftMenuModel);
                }
                Log.d("response_leftMenu", leftMenuModels.size() + "");
                leftMenuAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String
                responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, responseString);
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
            menuShimmer.setVisibility(View.GONE);
            menuShimmer.stopShimmerAnimation();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
            menuShimmer.setVisibility(View.GONE);
            menuShimmer.stopShimmerAnimation();
            recyclerView.setVisibility(View.VISIBLE);

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
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View view1 = layoutInflater.inflate(R.layout.field_layout, null);
                        LinearLayout linearLayout = view1.findViewById(R.id.layout);
                        TextView textView = view1.findViewById(R.id.titleTv);
                        textView.setText("Create " + title);
                        JSONArray jsonArray = jsonObject.getJSONArray("form");
                        Log.d("response_form", jsonArray.toString());
                        //tausif code
                        fieldsModels.clear();
                        //tausif code end
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
                        final AlertDialog.Builder alertFile = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view1 = layoutInflater.inflate(R.layout.file_menu_layout, null);
                        GridView gridView = view1.findViewById(R.id.gridviewfiles);
                        TextView textView = view1.findViewById(R.id.titleTv);
                        textView.setText("Create " + title);
                        FileMenuAdapter fileMenuAdapter = new FileMenuAdapter(leftMenuModels, MainActivity.this);
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
                                    if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                                        WebReq.get(MainActivity.this, fileApi, null,
                                                new CreateNewMenuApi("Photo Form ", fileApi, null,
                                                        "file", "CAMERA"), sharedPreferences.getString(preferenceToken, ""));
                                    } else {
                                        GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                    }

                                }
                                if (leftMenuModels.get(i).getIcon().equals("SCANDOC")) {
                                    alertDialog.dismiss();
                                    check = 0;
                                    fileApi = leftMenuModels.get(i).getApi();
                                    WebReq.get(MainActivity.this, fileApi, null, new CreateNewMenuApi("Photo Form ", fileApi, imagePath, "file", "SCANDOC"), sharedPreferences.getString(preferenceToken, ""));


                                }
                                if (leftMenuModels.get(i).getIcon().equals("UPLOAD")) {
                                    alertDialog.dismiss();
                                    fileTypeModels.clear();
                                    fileApi = leftMenuModels.get(i).getApi();
                                    check = 0;
                                    WebReq.get(MainActivity.this, fileApi, null, new CreateNewMenuApi("Photo Form ", fileApi, null, "file", "UPLOAD"), sharedPreferences.getString(preferenceToken, ""));

                                }
                                if (leftMenuModels.get(i).getIcon().equals("MICROPHONE")) {
                                    fileApi = leftMenuModels.get(i).getApi();
                                    check = 0;
                                    alertDialog.dismiss();

                                    WebReq.get(MainActivity.this, fileApi, null, new CreateNewMenuApi("Recording Form ", fileApi, null, "file", "MICROPHONE"), sharedPreferences.getString(preferenceToken, ""));


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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void UploadFile() {
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.CAMERA, true);
        editor.commit();
        sentToSettings = false;
        Intent intent = new Intent(MainActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 2);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);
//        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//        chooseFile.setType("*/*");
//        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

    private void TakePicturefun() {
        camerafileclick = true;
        check = 0;
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.CAMERA, true);
        editor.commit();
        if (Build.VERSION.SDK_INT >= 23) {

            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 13);
//            return true;


        } else { //permission is automatically granted on sdk<23 upon installation
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 13);

            Log.v("TAG", "Permission is granted");
//            return true;
        }
    }

    private void RecordAudio() {
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        recordingFlag = true;
        recordFlag = true;

        filePath = Environment.getExternalStorageDirectory() + "/" + name + ".wav";
        int color = getResources().getColor(R.color.colorPrimaryDark);
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(recordCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    private void ScanDocument() {
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(Manifest.permission.CAMERA, true);
        editor.commit();
        sentToSettings = false;
        scanfileClick = true;

        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        intent.putExtra(ScanActivity.EXTRA_BRAND_IMG_RES, R.drawable.ic_crop_white_24dp); // Set image for title icon - optional
        intent.putExtra(ScanActivity.EXTRA_TITLE, "Crop Document"); // Set title in action Bar - optional
        intent.putExtra(ScanActivity.EXTRA_ACTION_BAR_COLOR, R.color.colorPrimary); // Set title color - optional
        intent.putExtra(ScanActivity.EXTRA_LANGUAGE, "en"); // Set language - optional
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {

                TakePicturefun();


            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)
                    || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Write permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
            if (requestCode == Read_Permission) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //The External Storage Write Permission is granted to you... Continue your left job...
                    UploadFile();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Need Read Memory Permission");
                        builder.setMessage("This app needs Read Memory permission");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();


                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);


                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                    }
                    if (requestCode == Record_audio) {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            //The External Storage Write Permission is granted to you... Continue your left job...
                            UploadFile();
                        } else {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
                                //Show Information about why you need the permission
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Need Record Audio  Permission");
                                builder.setMessage("This Feature needs Record Audio  permission");
                                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();


                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, Record_audio);


                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            } else {
                                Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                }
            }
        }
    }


    private void setLayout(final List<FieldsModel> fieldsModels, final LinearLayout
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
                    String name = fieldsModels.get(i).getName();
                    if (name.length()>1) {
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);;
                    }
                    labelView.setText("" + name);
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
                    String lblname = fieldsModels.get(i).getName();
                    if (lblname.length()>1) {
                        name = lblname.substring(0, 1).toUpperCase() + lblname.substring(1);;
                    }
                    textareaLabel.setText("" + lblname);
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
                    if (fieldsModels.get(i).getName().equals("folder_id")) {
                        if (GlobalClass.getInstance().
                                isOnline(MainActivity.this)) {
                            WebReq.get(MainActivity.this, "/api/user/get/folders",
                                    null, new GetFolderListApi(spinner, loc, requestParams, fieldsModels.get(loc).getName()), sharedPreferences.getString(preferenceToken, ""));
                        } else {
                            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                        }

                    } else if (fieldsModels.get(i).getName().equals("sharing") || fieldsModels.get(i).getName().equals("user_id")) {
                        if (GlobalClass.getInstance().
                                isOnline(MainActivity.this)) {
                            WebReq.get(MainActivity.this, Urls.GetList, null, new GetUserList(spinner, loc, requestParams, fieldsModels.get(loc).getName()), sharedPreferences.getString(preferenceToken, ""));
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
                                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                                    dialog.setTitle("Warning");
                                    dialog.setMessage("Single File Required");
                                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "0k", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    dialog.show();
                                } else {
                                    final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                                    dialog.setTitle("Warning");
                                    dialog.setMessage("2 Files Required");
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
                                            Log.d("response ","Field is required");
                                            flag = true;
                                            //tausif code start
                                            return;
                                            //tausif code end
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
                                Log.d("response type 1358",type+" ");
                                if (type != null) {
                                    switch (type) {
                                        case "CAMERA":
                                            if (camerafileclick) {
                                                try {
                                                    requestParams.put("file", new File(cameraFile));
                                                    if (cameraFileFlag > 1) {
                                                        requestParams.put("file1", new File(cameraFile1));
                                                    }

                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                                                    alertDialog.dismiss();
                                                    WebReq.post(MainActivity.this, api, requestParams, new CreatefolderEtcApi(), sharedPreferences.getString(preferenceToken, ""));
                                                } else {
                                                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                                }

                                            } else {
                                                if (flag == false) {
                                                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                                    alertDialog1.setTitle("Warning");
                                                    alertDialog1.setMessage("Please Upload Images");
                                                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    alertDialog1.show();
                                                }
                                            }
//                                        try {
//                                            requestParams.put("file", new File(cameraTakenPhoto.get(0)));
//                                        } catch (FileNotFoundException e) {
//                                            e.printStackTrace();
//                                        }
                                            break;
                                        case "UPLOAD":
                                            if (fileUploadflag) {
                                                if (mResults.size() > 0) {
                                                    try {
                                                        requestParams.put("file", new File(mResults.get(0)));
                                                        if (mResults.size() > 1) {
                                                            requestParams.put("file1", new File(mResults.get(1)));
                                                        }
                                                        if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                                                            alertDialog.dismiss();
                                                            WebReq.post(MainActivity.this,
                                                                    api, requestParams, new CreatefolderEtcApi(), sharedPreferences.getString(preferenceToken, ""));

                                                        } else {
                                                            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                                        }
                                                    } catch (FileNotFoundException e) {
                                                        e.printStackTrace();
                                                    }

                                                    Log.d("response_param", requestParams.toString());
                                                    Log.d("response_edittext_size", editTextsList.size() + "");
                                                    Log.d("response_spinner_size", spinnersList.size() + "");
                                                } else {
                                                    if (flag == false) {
                                                        AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                                        alertDialog1.setTitle("Warning");
                                                        alertDialog1.setMessage("Please Upload Images");
                                                        alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        });
                                                        alertDialog1.show();
                                                    }
                                                }
                                            } else {
                                                if (flag == false) {
                                                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                                    alertDialog1.setTitle("Warning");
                                                    alertDialog1.setMessage("Please " +
                                                            " Images");
                                                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    alertDialog1.show();
                                                }
                                            }


                                            Log.d("response_api_hint", api);
                                            break;
                                        case "SCANDOC":
                                            if (scanfileClick) {
                                                try {
                                                    Log.d("response_file_Scan", scanFile);
                                                    requestParams.put("file", new File(scanFile));


                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                                                    alertDialog.dismiss();
                                                    WebReq.post(MainActivity.this, api, requestParams, new CreatefolderEtcApi(), sharedPreferences.getString(preferenceToken, ""));
                                                } else {
                                                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                                }
                                            } else {
                                                if (flag == false) {
                                                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                                    alertDialog1.setTitle("Warning");
                                                    alertDialog1.setMessage("Please Scan Images");
                                                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    alertDialog1.show();
                                                }
                                            }
                                            break;
                                        case "MICROPHONE":
                                            if (recordFlag) {
                                                try {
                                                    requestParams.put("file", new File(filePath));
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                                                    alertDialog.dismiss();
                                                    WebReq.post(MainActivity.this, api, requestParams, new CreatefolderEtcApi(), sharedPreferences.getString(preferenceToken, ""));
                                                } else {
                                                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                                }
                                            } else {
                                                if (flag == false) {
                                                    AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
                                                    alertDialog1.setTitle("Warning");
                                                    alertDialog1.setMessage("Please Record Some Audio");
                                                    alertDialog1.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    });
                                                    alertDialog1.show();
                                                }
                                            }
                                            break;
                                    }
                                } else {
                                    Log.d("response_contact", api);
                                    Log.d("response_hit", requestParams + "");

                                    if (GlobalClass.getInstance().isOnline(MainActivity.this)) {
                                        alertDialog.dismiss();
                                        WebReq.post(MainActivity.this, api, requestParams, new CreatefolderEtcApi(), sharedPreferences.getString(preferenceToken, ""));
                                    } else {
                                        GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
                                    }
                                }
                                Log.d("response_param", requestParams.toString());


                            } else {
                                GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "Some Field is missing");
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
                case "file":
                    if (type.equals("CAMERA") && cameraFlag == false) {
                        cameraFlag = true;
                        TextView fileLabel = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                        fileLabel.setText("" + fieldsModels.get(i).getName().toUpperCase());
                        LinearLayout.LayoutParams fileLabelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        fileLabelParam.setMargins(25, 0, 25, 0);
                        fileLabel.setLayoutParams(fileLabelParam);
                        fileLabel.setId(i + 1);
                        layout.addView(fileLabel);
                        imagePathKey = fieldsModels.get(i).getName();
                        imageView = (ImageView) layoutInflater.inflate(R.layout.choose_image_view, null);
                        chooseFile = true;
                        LinearLayout.LayoutParams imageviewParam = new LinearLayout.LayoutParams(190, 190);
                        imageviewParam.gravity = Gravity.CENTER_HORIZONTAL;
                        imageviewParam.setMargins(0, 0, 0, 8);
                        imageView.setLayoutParams(imageviewParam);
                        imageView.setId(i);

                        layout.addView(imageView);
                    } else if (type.equals("SCANDOC") && ScanFlag == false) {
                        ScanFlag = true;
                        TextView fileLabel = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                        fileLabel.setText("" + fieldsModels.get(i).getName().toUpperCase());
                        LinearLayout.LayoutParams fileLabelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        fileLabelParam.setMargins(25, 0, 25, 0);
                        fileLabel.setLayoutParams(fileLabelParam);
                        fileLabel.setId(i + 1);
                        layout.addView(fileLabel);
                        imagePathKey = fieldsModels.get(i).getName();
                        imageView = (ImageView) layoutInflater.inflate(R.layout.choose_image_view, null);
                        chooseFile = true;
                        LinearLayout.LayoutParams imageviewParam = new LinearLayout.LayoutParams(190, 190);
                        imageviewParam.gravity = Gravity.CENTER_HORIZONTAL;
                        imageviewParam.setMargins(0, 0, 0, 8);
                        imageView.setLayoutParams(imageviewParam);
                        imageView.setId(i);
                        layout.addView(imageView);

                    } else if (type.equals("MICROPHONE") && recordingFlag == false) {
                        recordingFlag = true;
                        TextView fileLabel = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                        fileLabel.setText("" + fieldsModels.get(i).getName().toUpperCase());
                        LinearLayout.LayoutParams fileLabelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        fileLabelParam.setMargins(25, 0, 25, 0);
                        fileLabel.setLayoutParams(fileLabelParam);
                        fileLabel.setId(i + 1);
                        layout.addView(fileLabel);
                        imagePathKey = fieldsModels.get(i).getName();
                        imageView = (ImageView) layoutInflater.inflate(R.layout.choose_image_view, null);
                        chooseFile = true;
                        LinearLayout.LayoutParams imageviewParam = new LinearLayout.LayoutParams(190, 190);
                        imageviewParam.gravity = Gravity.CENTER_HORIZONTAL;
                        imageviewParam.setMargins(0, 0, 0, 8);
                        imageView.setLayoutParams(imageviewParam);
                        imageView.setId(i);
                        layout.addView(imageView);
                    } else {
                        if (!chooseFile) {
                            TextView fileLabel = (TextView) layoutInflater.inflate(R.layout.textview_label, null);
                            fileLabel.setText("" + fieldsModels.get(i).getName().toUpperCase());
                            LinearLayout.LayoutParams fileLabelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            fileLabelParam.setMargins(25, 0, 25, 0);
                            fileLabel.setLayoutParams(fileLabelParam);
                            fileLabel.setId(i + 1);
                            layout.addView(fileLabel);
                            imagePathKey = fieldsModels.get(i).getName();
                            imageView = (ImageView) layoutInflater.inflate(R.layout.choose_image_view, null);
                            chooseFile = true;
                            LinearLayout.LayoutParams imageviewParam = new LinearLayout.LayoutParams(190, 190);
                            imageviewParam.gravity = Gravity.CENTER_HORIZONTAL;
                            imageviewParam.setMargins(0, 0, 0, 8);
                            imageView.setLayoutParams(imageviewParam);
                            imageView.setId(i);
                            if (path != null) {
                                File imgFile = new File(path);

                                if (imgFile.exists()) {

                                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    imageView.setImageBitmap(myBitmap);

                                }
                            }
                            layout.addView(imageView);
                        }
                    }


                    imageView.setOnClickListener(new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                        @Override
                        public void onClick(View view) {
                            switch (type) {
                                case "CAMERA":

                                    TakePicturefun();

                                    break;
                                case "UPLOAD":

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                        UploadFile();
                                    }

                                    break;
                                case "SCANDOC":

                                    ScanDocument();

                                    break;
                                case "MICROPHONE":


                                    RecordAudio();

                            }

                        }
                    });


//                    File imgFile = new File(imagePath);
//                    Log.d("response_imagePath", imagePath);

//                    if (imgFile.exists()) {
//                        Log.d("response_exist", "response_exist");
//                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                        if (myBitmap != null) {
//                            int nh = (int) (myBitmap.getHeight() * (512.0 / myBitmap.getWidth()));
//                            Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 512, nh, true);
//                            imageView.setImageBitmap(scaled);
//                            LinearLayout.LayoutParams imageviewParam = new LinearLayout.LayoutParams(190, 190);
//                            imageviewParam.gravity = Gravity.CENTER_HORIZONTAL;
//                            imageviewParam.setMargins(0, 0, 0, 8);
//                            imageView.setLayoutParams(imageviewParam);
//                        }
//                        imageView.setId(i);
//                        layout.addView(imageView);
//                    }
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
                    userListAdapter = new UserListAdapter(userListModels, MainActivity.this);
                    spinner.setAdapter(userListAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            requestParams.put(key, userListModels.get(i).getId() + "");

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
            shimmerFrameLayout.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.startShimmerAnimation();
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
                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
                    callApi();
                } else {
                    GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, response.getString("message"));
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
            recyclerView.setVisibility(View.VISIBLE);
            shimmerFrameLayout.stopShimmerAnimation();
            shimmerFrameLayout.setVisibility(View.GONE);
        }
    }

    public class GetFolderListApi extends JsonHttpResponseHandler {
        Spinner spinner;
        int loc;
        RequestParams requestParams;
        String key;


        public GetFolderListApi(Spinner spinner, int loc, RequestParams requestParams, String key) {
            this.spinner = spinner;
            this.loc = loc;
            this.requestParams = requestParams;
            this.key = key;
            spinnerFolderModelList = new ArrayList<>();

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);

            try {
                if (response.getString("status").equals("true")) {
                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("folders");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FoldersModel foldersModel = new FoldersModel();
                        foldersModel.setId("" + jsonObject.getString("id"));
                        foldersModel.setName("" + jsonObject.getString("name"));
                        foldersModel.setDescription("" + jsonObject.getString("description"));
                        foldersModel.setIcon("" + jsonObject.getString("icon"));
                        foldersModel.setOwner("" + jsonObject.getString("owner"));
                        foldersModel.setCreated_at("" + jsonObject.getString("created_at"));
                        foldersModel.setIconName("" + jsonObject.getJSONObject("icondata").getString("name"));
                        foldersModel.setIconUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                        JSONArray menuData = jsonObject.getJSONArray("menudata");
                        List<LeftMenuModel> subList = new ArrayList<>();
                        for (int j = 0; j < menuData.length(); j++) {
                            JSONObject jsonObject1 = menuData.getJSONObject(j);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + jsonObject1.getString("id"));
                            leftMenuModel.setName("" + jsonObject1.getString("name"));
                            leftMenuModel.setIcon("" + jsonObject1.getString("icon"));
                            leftMenuModel.setApi("" + jsonObject1.getString("api"));
                            leftMenuModel.setUrl("" + jsonObject1.getJSONObject("icondata").getString("url"));
                            leftMenuModel.setNameIcon("" + jsonObject1.getJSONObject("icondata").getString("name"));
                            subList.add(leftMenuModel);
                        }
                        foldersModel.setMenudata(subList);
                        spinnerFolderModelList.add(foldersModel);
                    }
                    spinnerFolderAdapter = new SpinnerFolderAdapter(MainActivity.this, spinnerFolderModelList);
                    spinner.setAdapter(spinnerFolderAdapter);
                    Log.d("response_spinner_size", spinnerFolderModelList.size() + "");
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            requestParams.put(key, spinnerFolderModelList.get(i).getId());

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
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
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

        dialogBuilder = new AlertDialog.Builder(MainActivity.this);

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
                    Toast.makeText(MainActivity.this, "PIN VERIFIED", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "NOT VERIFIED", Toast.LENGTH_SHORT).show();
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
