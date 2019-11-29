package glowingsoft.com.vif.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Activities.AlertActivity;
import glowingsoft.com.vif.Activities.BackupActivity;
import glowingsoft.com.vif.Activities.BookMarkActivity;
import glowingsoft.com.vif.Activities.ContactActivity;
import glowingsoft.com.vif.Activities.LabelActivity;
import glowingsoft.com.vif.Activities.LoginSignupActivity;
import glowingsoft.com.vif.Activities.RecentActivity;
import glowingsoft.com.vif.Activities.RecyclerBinActivity;
import glowingsoft.com.vif.Activities.SecurityActivity;
import glowingsoft.com.vif.Activities.SharedActivity;
import glowingsoft.com.vif.Activities.TeamsAndCondition;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Models.LanguageModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class LeftMenuAdapter extends BaseAdapter {
    List<LeftMenuModel> leftMenuModels;
    Context context;
    LayoutInflater layoutInflater;
    LanguageSpinnerAdapter languageSpinnerAdapter;
    List<LanguageModel> languageModels;
    Spinner spinner;
    DrawerLayout drawerLayout;
    SharedPreferences sharedPreferences;

    public LeftMenuAdapter(List<LeftMenuModel> leftMenuModels, Context context, DrawerLayout drawerLayout) {
        this.leftMenuModels = leftMenuModels;
        this.context = context;
        this.drawerLayout = drawerLayout;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        languageModels = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return leftMenuModels.size();
    }

    @Override
    public Object getItem(int i) {
        return leftMenuModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        if (leftMenuModels.get(i).getIcon().equals("SEPARATOR")) {
            return layoutInflater.inflate(R.layout.seperator_view, viewGroup, false);
        } else {
            final View layout = layoutInflater.inflate(R.layout.leftmenu_view, viewGroup, false);
            TextView textView = layout.findViewById(R.id.titleTv);
            ImageView imageView = layout.findViewById(R.id.menuIcon);
            textView.setText("" + leftMenuModels.get(i).getName());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (leftMenuModels.get(i).getIcon().equals("TAG")) {
                        Intent intent = new Intent(context, LabelActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equals("ALERTBLUE")) {
                        Intent intent = new Intent(context, AlertActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equals("BOOKMARK")) {
                        Intent intent = new Intent(context, BookMarkActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equals("LANGUAGE")) {
                        drawerLayout.closeDrawers();
                        final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(context);
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View dialog = layoutInflater.inflate(R.layout.language_spinner_view, null);
                        spinner = dialog.findViewById(R.id.lanSp);
                        Button btnchoose = dialog.findViewById(R.id.choosebtn);
                        alert.setView(dialog);
                        final android.support.v7.app.AlertDialog alertDialog = alert.create();
                        WebReq.get(context, leftMenuModels.get(i).getApi(), null, new CallingRestApi(btnchoose, alertDialog), sharedPreferences.getString(preferenceToken, ""));
                        btnchoose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        alertDialog.show();
                    }
                    if (leftMenuModels.get(i).getIcon().equals("PRIVACY") || leftMenuModels.get(i).getIcon().equals("TERMS") || leftMenuModels.get(i).getIcon().equals("ABOUT") || leftMenuModels.get(i).getIcon().equals("SETTINGS")) {
                        Intent intent = new Intent(context, TeamsAndCondition.class);
                        intent.putExtra("key", leftMenuModels.get(i).getIcon());
                        intent.putExtra("name", leftMenuModels.get(i).getName());
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equals("LOGOUT")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setMessage("Are you sure to logout application");
                        alertDialog.setTitle("Warning");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                GlobalClass.getInstance().logOut();
                                context.startActivity(new Intent(context, LoginSignupActivity.class));
                                ((Activity) context).finish();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    if (leftMenuModels.get(i).getIcon().equals("RECYCLEBIN")) {
                        Intent intent = new Intent(context, RecyclerBinActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equalsIgnoreCase("ABOUT")) {
                        Intent intent = new Intent(context, RecentActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equalsIgnoreCase("SECURITY")) {
                        Intent intent = new Intent(context, SecurityActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
//                        intent.putExtra("name", leftMenuModels.get(i).get());
//                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equalsIgnoreCase("BACKUP")) {
                        Intent intent = new Intent(context, BackupActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equalsIgnoreCase("CONTACTS")) {
                        Intent intent = new Intent(context, ContactActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equals("RECENT")) {
                        Intent intent = new Intent(context, RecentActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                    if (leftMenuModels.get(i).getIcon().equals("SHAREDWITHME")) {
                        Intent intent = new Intent(context, SharedActivity.class);
                        intent.putExtra("api", leftMenuModels.get(i).getApi());
                        context.startActivity(intent);
                    }
                }
            });
            Picasso.get().load(leftMenuModels.get(i).getUrl()).resize(30, 30).centerInside().placeholder(R.mipmap.ic_launcher).into(imageView);
            return layout;

        }
    }

    public class CallingRestApi extends JsonHttpResponseHandler {
        android.support.v7.app.AlertDialog alertDialog;
        Button btnChoose;
        RequestParams requestParams;


        public CallingRestApi(Button btnChoose, android.support.v7.app.AlertDialog alertDialog) {
            this.btnChoose = btnChoose;
            this.alertDialog = alertDialog;
            requestParams = new RequestParams();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getString("status").equals("true")) {
                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("languages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LanguageModel languageModel = new LanguageModel();
                        languageModel.setLanguage("" + jsonObject.getString("language"));
                        languageModel.setIdentifier("" + jsonObject.getString("identifier"));
                        languageModels.add(languageModel);
                    }
                    languageSpinnerAdapter = new LanguageSpinnerAdapter(context, languageModels);
                    spinner.setAdapter(languageSpinnerAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            requestParams.put("language", languageModels.get(i).getIdentifier());
                            Log.d("response_value", languageModels.get(i).getIdentifier());

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    btnChoose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            if (GlobalClass.getInstance().isOnline(context)) {
                                WebReq.post(context, Urls.changeLanguage, requestParams, new SelectLanguageApi(), sharedPreferences.getString(preferenceToken, ""));

                            } else {
                                Toast.makeText(context, "" + Urls.internetString, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    }

    public class SelectLanguageApi extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                if (response.getString("status").equals("true")) {
                    Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    }
}
