package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import glowingsoft.com.vif.Adapters.LeftMenuBookMarkAdapter;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Models.BookMarkModel;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class BookMarkActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout rootLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<BookMarkModel> bookMarkModelList;
    LeftMenuBookMarkAdapter leftMenuBookMarkAdapter;
    ShimmerFrameLayout shimmerFrameLayout;
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        swipeRefreshLayout = findViewById(R.id.swipelayoutBookmark);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        shimmerFrameLayout = findViewById(R.id.shimmer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backarrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookMarkModelList = new ArrayList<>();
        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new GridLayoutManager(BookMarkActivity.this, 2));

        rootLayout = findViewById(R.id.rootLayout);


        CallingApi();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                CallingApi();
            }
        });
    }

    private void CallingApi() {
        if (GlobalClass.getInstance().isOnline(BookMarkActivity.this)) {
            bookMarkModelList.clear();
            WebReq.get(BookMarkActivity.this, getIntent().getExtras().getString("api"),
                    null, new CallingRestApi()
            ,   sharedPreferences.getString(preferenceToken, ""));
        } else {
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, Urls.internetString);
        }

    }

    public class CallingRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.startShimmerAnimation();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                recyclerView.setVisibility(View.VISIBLE);
                Log.d("response_bookmark", response.toString());
                if (response.getString("status").equals("true")) {
                    JSONArray jsonArrayBookmark = response.getJSONObject("data").getJSONArray("bookmarks");
                    for (int i = 0; i < jsonArrayBookmark.length(); i++) {
                        JSONObject jsonObject = jsonArrayBookmark.getJSONObject(i);
                        BookMarkModel bookMarkModel = new BookMarkModel();
                        bookMarkModel.setId("" + jsonObject.getString("id"));
                        bookMarkModel.setType("" + jsonObject.getString("type"));
                        if (bookMarkModel.getType().equals("FOLDER")) {
                            JSONObject jsonObjectData = jsonObject.getJSONObject("bookmarkdata");
                            FoldersModel foldersModel = new FoldersModel();
                            foldersModel.setId("" + jsonObjectData.getString("id"));
                            foldersModel.setName("" + jsonObjectData.getString("name"));
                            foldersModel.setDescription("" + jsonObjectData.getString("description"));
                            foldersModel.setIcon("" + jsonObjectData.getString("icon"));
                            foldersModel.setOwner("" + jsonObjectData.getString("owner"));
                            foldersModel.setCreated_at("" + jsonObjectData.getString("created_at"));
                            List<LeftMenuModel> leftMenuModels = new ArrayList<>();
                            JSONArray menuArray = jsonObjectData.getJSONArray("menudata");
                            for (int j = 0; j < menuArray.length(); j++) {
                                JSONObject jsonObject1 = menuArray.getJSONObject(j);
                                LeftMenuModel leftMenuModel = new LeftMenuModel();
                                leftMenuModel.setId("" + jsonObject1.getString("id"));
                                leftMenuModel.setName("" + jsonObject1.getString("name"));
                                leftMenuModel.setIcon("" + jsonObject1.getString("icon"));
                                leftMenuModel.setApi("" + jsonObject1.getString("api"));
                                leftMenuModel.setUrl("" + jsonObject1.getJSONObject("icondata").getString("url"));
                                leftMenuModel.setNameIcon("" + jsonObject1.getJSONObject("icondata").getString("name"));
                                leftMenuModels.add(leftMenuModel);
                            }
                            foldersModel.setMenudata(leftMenuModels);
                            bookMarkModel.setFolderModel(foldersModel);

                        } else {
                            BookMarkModel model = new BookMarkModel();
                            model.setId("" + jsonObject.getString("id"));
                            model.setType("" + jsonObject.getString("type"));
                            JSONObject BookmarkData = jsonObject.getJSONObject("bookmarkdata");
                            FoldersModel foldersModel = new FoldersModel();
                            foldersModel.setId("" + BookmarkData.getString("id"));
                            foldersModel.setName("" + BookmarkData.getString("name"));
                            foldersModel.setDescription("" + BookmarkData.getString("description"));
                            foldersModel.setPath("" + BookmarkData.getString("path"));
                            foldersModel.setOwner("" + BookmarkData.getString("owner"));
                            foldersModel.setShares("" + BookmarkData.getString("shares"));
                            foldersModel.setViews("" + BookmarkData.getString("views"));
                            foldersModel.setDownloads("" + BookmarkData.getString("downloads"));
                            foldersModel.setIcon("" + BookmarkData.getString("icon"));
                            foldersModel.setCreated_at("" + BookmarkData.getString("created_at"));
                            JSONArray menuData = BookmarkData.getJSONArray("menudata");
                            List<LeftMenuModel> leftMenuModels = new ArrayList<>();
                            for (int j = 0; j < menuData.length(); j++) {
                                JSONObject jsonObject1 = menuData.getJSONObject(j);
                                LeftMenuModel leftMenuModel = new LeftMenuModel();
                                leftMenuModel.setId("" + jsonObject1.getString("id"));
                                leftMenuModel.setName("" + jsonObject1.getString("name"));
                                leftMenuModel.setIcon("" + jsonObject1.getString("icon"));
                                leftMenuModel.setApi("" + jsonObject1.getString("api"));
                                leftMenuModel.setUrl("" + jsonObject1.getJSONObject("icondata").getString("url"));
                                leftMenuModel.setNameIcon("" + jsonObject1.getJSONObject("icondata").getString("name"));
                                leftMenuModels.add(leftMenuModel);
                            }
                            foldersModel.setMenudata(leftMenuModels);
                            bookMarkModel.setFolderModel(foldersModel);
                        }
                        bookMarkModelList.add(bookMarkModel);


                        leftMenuBookMarkAdapter = new LeftMenuBookMarkAdapter(BookMarkActivity.this, bookMarkModelList);
                        recyclerView.setAdapter(leftMenuBookMarkAdapter);


                    }
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
            Log.d("response_failer", throwable.toString());
            GlobalClass.getInstance().SnackBar(rootLayout, -1, -1, responseString);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
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
}
