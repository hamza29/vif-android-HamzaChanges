package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Adapters.CreateNewFabAdapter;
import glowingsoft.com.vif.Adapters.FilesRecyclerViewAdapter;
import glowingsoft.com.vif.Adapters.MainActivityFooterAdapter;
import glowingsoft.com.vif.Adapters.SpinnerFolderAdapter;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Models.FilesModels;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class FoldersActivtiy extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    List<FoldersModel> foldersModels = new ArrayList<>();
    List<FilesModels> filesModelsArrayList = new ArrayList<>();
    RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders_activtiy);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        Intent intent = getIntent();
        //Toast.makeText(this, "" + intent.getStringExtra("ID"), Toast.LENGTH_SHORT).show();
        recyclerView = findViewById(R.id.recylerview);
        rootLayout = findViewById(R.id.rootLayout);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Folder and Files Ali");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton = findViewById(R.id.fabBtn);
        floatingActionButton.setOnClickListener(this);
        if (GlobalClass.getInstance().isOnline(FoldersActivtiy.this)) {
            filesModelsArrayList.clear();
            WebReq.get(FoldersActivtiy.this, Urls.filebyfolder + intent.getStringExtra("ID"), null, new GetFolderListApi(), sharedPreferences.getString(preferenceToken, ""));
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "" + Urls.internetString);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabBtn:

                break;
        }
    }

    public class GetFolderListApi extends JsonHttpResponseHandler {


        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response 109",response.toString()+" ");
            try {
                if (response.getString("status").equals("true")) {
                    JSONArray jsonArrayFolders=new JSONArray();
                    JSONArray jsonArrayfiles =new JSONArray();
                    try {
                         jsonArrayFolders = response.getJSONObject("data").getJSONArray("folders");
                         jsonArrayfiles = response.getJSONObject("data").getJSONArray("files");

                        recyclerView.setLayoutManager(new LinearLayoutManager(FoldersActivtiy.this, LinearLayoutManager.VERTICAL, false));
                        MainActivityFooterAdapter recyclerViewAdapte = new MainActivityFooterAdapter(FoldersActivtiy.this, jsonArrayfiles, jsonArrayFolders, null, null, null, recyclerView);
                        recyclerView.setAdapter(recyclerViewAdapte);
                        Log.e("response 163", "Folder size " + jsonArrayFolders.length() + "");
                        Log.e("response", jsonArrayfiles.length() + "");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (jsonArrayfiles.length()==0&&jsonArrayFolders.length()==0){
                        Toast.makeText(getApplicationContext(),"Folder is Empty",Toast.LENGTH_SHORT).show();
                    }


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

}
