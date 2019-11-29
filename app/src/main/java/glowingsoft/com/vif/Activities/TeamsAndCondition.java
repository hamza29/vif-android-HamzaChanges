package glowingsoft.com.vif.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class TeamsAndCondition extends AppCompatActivity {
    Toolbar toolbar;
    TextView textView;
    DualProgressView progressBar;
    SharedPreferences sharedPreferences;
    TextView descriptiontv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_and_condition);
        toolbar = findViewById(R.id.toolbar);
        sharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        textView = findViewById(R.id.titleTv);
        progressBar = findViewById(R.id.progressBar);
        descriptiontv = findViewById(R.id.descriptionTv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.backarrow));
        if (GlobalClass.getInstance().isOnline(TeamsAndCondition.this)) {
            WebReq.get(TeamsAndCondition.this, getIntent().getExtras().getString("api"),
                    null, new CallingRestApi(),sharedPreferences.getString(preferenceToken, ""));
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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                JSONObject jsonObject;
                Log.d("response_term", response.toString());
                if (response.getJSONObject("data").has(getIntent().getExtras().getString("key").toLowerCase())) {
                    jsonObject = response.getJSONObject("data").getJSONObject(getIntent().getExtras().getString("key").toLowerCase());
                } else {
                    jsonObject = response.getJSONObject("data").getJSONObject(getIntent().getExtras().getString("name").replace(" ", "").toLowerCase());
                }
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");
                textView.setText(title);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    descriptiontv.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    descriptiontv.setText(Html.fromHtml(body));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("response_terms", response.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressBar.setVisibility(View.GONE);
        }
    }
}
