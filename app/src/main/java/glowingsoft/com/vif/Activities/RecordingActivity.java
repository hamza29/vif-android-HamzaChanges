package glowingsoft.com.vif.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

public class RecordingActivity extends AppCompatActivity {
    int requestCode = 0;
    String filePath;
    RelativeLayout parentLayout;
    File destination = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        parentLayout = findViewById(R.id.rootLayout);
        String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";

//        destination = new File(Environment.getExternalStorageDirectory(), name + ".wav");
        int color = getResources().getColor(R.color.colorPrimaryDark);
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    public String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Great! User has recorded and saved the audio file

//                if (GlobalClass.getInstance().isOnline(RecordingActivity.this)) {
//                    WebReq.get(RecordingActivity.this, getIntent().getExtras().getString("api"), null, new CallingRestapi());
//                } else {
//                    GlobalClass.getInstance().SnackBar(parentLayout, -1, -1, Urls.internetString);
//                }
                Intent intent = new Intent();
                intent.putExtra("status", true);
                intent.putExtra("path", filePath);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                // Oops! User has canceled the recording
                Toast.makeText(this, "User has canceled the recording", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CallingRestapi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_audio", response.toString());
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("response_failer", throwable.toString());
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }

}
