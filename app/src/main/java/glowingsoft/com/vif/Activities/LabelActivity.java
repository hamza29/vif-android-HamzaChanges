package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
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
import glowingsoft.com.vif.Adapters.LabelAdapter;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class LabelActivity extends AppCompatActivity {
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    GridView gridView;
    List<LeftMenuModel> models;
    RelativeLayout rootLayout;
    LabelAdapter adapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootLayout = findViewById(R.id.rootLayout);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.backarrow));
        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        gridView = findViewById(R.id.gridview);
        models = new ArrayList<>();
        Log.d("response_url", getIntent().getExtras().getString("api"));
        adapter = new LabelAdapter(LabelActivity.this, models);
        gridView.setAdapter(adapter);
        CallingApi();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                CallingApi();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void CallingApi() {
        models.clear();
        if (GlobalClass.getInstance().isOnline(LabelActivity.this)) {
            Log.d("response_endPoint", getIntent().getExtras().getString("api"));
            WebReq.get(LabelActivity.this, getIntent().getExtras().getString("api"), null,
                    new CallingApi(), sharedPreferences.getString(preferenceToken, ""))
            ;
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
        }
    }

    public class CallingApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            gridView.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                Log.d("response_alerts", response + "");
                if (response.getString("status").equals("true")) {
                    gridView.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("labels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LeftMenuModel leftMenuModel = new LeftMenuModel();
                        leftMenuModel.setId("" + jsonObject.getString("id"));
                        leftMenuModel.setName("" + jsonObject.getString("name"));
                        leftMenuModel.setDescription("" + jsonObject.getString("description"));
                        leftMenuModel.setIcon("" + jsonObject.getString("icon"));
                        leftMenuModel.setUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                        leftMenuModel.setNameIcon("" + jsonObject.getJSONObject("icondata").getString("name"));
                        models.add(leftMenuModel);
                    }
                    adapter.notifyDataSetChanged();

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
}
