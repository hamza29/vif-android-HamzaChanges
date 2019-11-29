package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Adapters.BackupAdapter;
import glowingsoft.com.vif.Adapters.MainActivityFooterAdapter;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class BackupActivity extends AppCompatActivity {

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    BackupAdapter mainActivityFooterAdapter;
    RelativeLayout rootLayout;
    ShimmerFrameLayout shimmerFrameLayout;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        rootLayout = findViewById(R.id.rootLayout);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);

        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(BackupActivity.this, LinearLayoutManager.VERTICAL, false));
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
        if (GlobalClass.getInstance().isOnline(BackupActivity.this)) {
            WebReq.get(BackupActivity.this, "/api/user/backup/folders", null, new CallingRestApi(),
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
                    JSONArray jsonArrayFolders = response.getJSONObject("data").getJSONArray("folders");
                    mainActivityFooterAdapter = new BackupAdapter(BackupActivity.this,  jsonArrayFolders, null, null, shimmerFrameLayout, recyclerView);
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
    }}