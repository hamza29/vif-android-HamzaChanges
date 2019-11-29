package glowingsoft.com.vif;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import glowingsoft.com.vif.Adapters.UserListAdapter;
import glowingsoft.com.vif.Models.FieldsModel;
import glowingsoft.com.vif.Models.UserListModel;

public class GlobalClass extends Application {

    private static GlobalClass singleton = null;
    SharedPreferences mPref;
    SharedPreferences.Editor editor;
    public String sPreferenceName = "VIF";
    public static Context context;
    List<FieldsModel> fieldsModels;
    List<UserListModel> userListModels;
    List<Button> buttonList;
    List<Spinner> spinnersList;
    List<EditText> editTextsList;
    List<View> viewList;
    String fileApi = null;
    private static final int REQUEST_IMAGE = 120;
    int PICKFILE_RESULT_CODE = 100;
    String filePath;
    File destination;
    int requestCode = 0;
    UserListAdapter userListAdapter;
    String imagePathKey;


    @Override
    public void onCreate() {
        super.onCreate();
        // the following line is important
        Fresco.initialize(getApplicationContext());

        singleton = this;
        mPref = singleton.getSharedPreferences(sPreferenceName, MODE_PRIVATE);
        editor = mPref.edit();
        fieldsModels = new ArrayList<>();
        userListModels = new ArrayList<>();
        buttonList = new ArrayList<>();
        spinnersList = new ArrayList<>();
        editTextsList = new ArrayList<>();
        viewList = new ArrayList<>();
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");

        destination = new File(Environment.getExternalStorageDirectory(), name + ".jpg");


    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static GlobalClass getInstance() {

        return singleton;

    }

    public void storeUserRecord(String name, String email, String create_At) {
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("created_at", create_At);
        editor.commit();
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void SnackBar(View view, int bgColor, int textColor, String message) {
        Snackbar snackbar = Snackbar.make(view, "" + message, Snackbar.LENGTH_LONG);
        View view1 = snackbar.getView();
        if (textColor != -1) {
            view1.setBackgroundColor(bgColor);
        }
        if (bgColor != -1) {
            snackbar.setActionTextColor(textColor);
        }
        snackbar.show();
    }

    public String getEdittextValue(TextInputLayout inputLayout) {
        return inputLayout.getEditText().getText().toString();
    }

    public Boolean addCredientialDetail(String userKey, String userName, String idKey, String userId, String emailKey, String userEmail) {
        editor.putString(idKey, userId);
        editor.putString(userKey, userName);
        editor.putString(emailKey, userEmail);
        return editor.commit();
    }

    public Boolean isLoggedIn(String key) {
        return mPref.contains(key);
    }

    public TextInputLayout creatTextInputLayout(String hint, int id, int bgLayout, int textColor, int width, int height) {
        TextInputLayout textInputLayout = new TextInputLayout(singleton);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        textInputLayout.setId(id);
        textInputLayout.setLayoutParams(params);
        if (bgLayout != 0) {
            textInputLayout.setBackgroundResource(bgLayout);
        }
        TextInputEditText editText = new TextInputEditText(singleton);
        editText.setHint(hint);
        LinearLayout.LayoutParams eParam = new LinearLayout.LayoutParams(width, height);
        editText.setLayoutParams(eParam);
        if (textColor != 0) {
            editText.setBackgroundColor(textColor);
        }

        textInputLayout.addView(editText);
        return textInputLayout;
    }

    public Button createDynamicButton(String text, int id, int textColor, int width, int height, int bgLayout) {
        Button button = new Button(singleton);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        button.setLayoutParams(params);
        button.setText(text);
        button.setId(id);
        button.setTextColor(textColor);
        if (bgLayout != 0) {
            button.setBackgroundResource(bgLayout);
        }
        return button;


    }

    public Spinner createDynamicSpinner(int id, int width, int height) {
        Spinner spinner = new Spinner(singleton);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        spinner.setLayoutParams(params);
        spinner.setId(id);
        return spinner;
    }

    public ImageView createDynamicImageView(int id, int width, int height, int drawable, int gravity) {
        ImageView imageView = new ImageView(singleton);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        imageView.setLayoutParams(params);
        imageView.setId(id);
        imageView.setImageResource(drawable);

        return imageView;
    }

    public void loadImagePicasso(String url, ImageView imageView, final ProgressBar progressBar, int placeHolder) {
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(url).fit().centerCrop().placeholder(placeHolder).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public String getUserId(String key) {
        String value = mPref.getString(key, "");
        return value;
    }

    public String getUserEmail(String key) {
        String email = mPref.getString(key, "");
        return email;
    }

    public String getUserName() {
        String name = mPref.getString("name", "");
        return name;
    }

    public TextView createTextView(int id, String text, int textColor, int bgColor, int bgLayout, int width, int height, Typeface style, int gravity) {
        TextView textView = new TextView(singleton);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        textView.setLayoutParams(params);
        textView.setId(id);
        textView.setTextColor(textColor);
        if (bgColor != 0) {
            textView.setBackgroundColor(bgColor);
        }
        if (bgLayout != 0) {
            textView.setBackgroundResource(bgLayout);
        }
        if (style != null) {
            textView.setTypeface(style);
        }
        if (gravity != -1) {
            textView.setGravity(gravity);
        }

        textView.setText(text);
        return textView;

    }

    public LinearLayout createLinearLayout(int id, int orientation, int width, int height) {
        LinearLayout linearLayout = new LinearLayout(singleton);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        linearLayout.setId(id);
        linearLayout.setOrientation(orientation);
        return linearLayout;
    }

    public void logOut() {
        editor.clear();
        editor.commit();
    }


}
