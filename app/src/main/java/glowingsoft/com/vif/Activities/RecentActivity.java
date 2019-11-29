package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.Adapters.FilesRecyclerViewAdapter;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Models.FilesModels;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class RecentActivity extends AppCompatActivity {
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    FilesRecyclerViewAdapter filesRecyclerViewAdapter;
    List<FilesModels> filesModelsList;
    RelativeLayout rootLayout;
    ShimmerFrameLayout shimmerFrameLayout;

SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        filesModelsList = new ArrayList<>();
        rootLayout = findViewById(R.id.rootLayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                CallingApi();
            }
        });
        recyclerView = findViewById(R.id.recylerview);
        CallingApi();


    }

    private void CallingApi() {
        if (GlobalClass.getInstance().isOnline(RecentActivity.this)) {
            filesModelsList.clear();
            WebReq.get(RecentActivity.this, getIntent().getExtras().getString("api"), null,
                    new CallingRestApi(),sharedPreferences.getString(preferenceToken, ""));
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, "" + Urls.internetString);
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
                startActivity(new Intent(RecentActivity.this, MainActivity.class));
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
            Log.d("response_recent", response.toString());
            recyclerView.setVisibility(View.VISIBLE);

            try {
                if (response.getString("status").equals("true")) {
                    JSONArray files = response.getJSONObject("data").getJSONArray("files");
                    for (int i = 0; i < files.length(); i++) {
                        JSONObject fileJsonobject = files.getJSONObject(i);
                        FilesModels filesModels = new FilesModels();
                        filesModels.setId("" + fileJsonobject.getString("id"));
                        filesModels.setName("" + fileJsonobject.getString("name"));
                        filesModels.setDescription("" + fileJsonobject.getString("description"));
                        filesModels.setPath("" + fileJsonobject.getString("path"));
                        filesModels.setOwner("" + fileJsonobject.getString("owner"));
                        filesModels.setShares("" + fileJsonobject.getString("shares"));
                        filesModels.setViews("" + fileJsonobject.getString("views"));
                        filesModels.setDownloads("" + fileJsonobject.getString("downloads"));
                        filesModels.setIcon("" + fileJsonobject.getString("icon"));
                        filesModels.setCreated_at("" + fileJsonobject.getString("created_at"));
                        List<LeftMenuModel> menuModels = new ArrayList<>();
                        JSONArray menuArray = fileJsonobject.getJSONArray("menudata");
                        Log.d("response_menuarray", menuArray.toString());
                        for (int j = 0; j < menuArray.length(); j++) {
                            JSONObject jsonObject = menuArray.getJSONObject(j);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + jsonObject.getString("id"));
                            leftMenuModel.setName("" + jsonObject.getString("name"));
                            leftMenuModel.setIcon("" + jsonObject.getString("icon"));
                            leftMenuModel.setApi("" + jsonObject.getString("api"));
                            leftMenuModel.setNameIcon("" + jsonObject.getJSONObject("icondata").getString("name"));
                            leftMenuModel.setUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                            menuModels.add(leftMenuModel);
                        }
                        Log.d("response_size", menuModels.size() + "");
                        filesModels.setMenudata(menuModels);
                        filesModels.setIconname("" + fileJsonobject.getJSONObject("icondata").getString("name"));
                        filesModels.setIconUrl("" + fileJsonobject.getJSONObject("icondata").getString("url"));
                        filesModelsList.add(filesModels);
                    }
                    recyclerView.setLayoutManager(new GridLayoutManager(RecentActivity.this, 2));
                    filesRecyclerViewAdapter = new FilesRecyclerViewAdapter(RecentActivity.this,
                            filesModelsList, null, null, shimmerFrameLayout, recyclerView);
                    recyclerView.setAdapter(filesRecyclerViewAdapter);
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
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
        }
    }

}
