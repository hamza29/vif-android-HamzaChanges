package glowingsoft.com.vif.webRequest;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class WebReq {
    public static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient(true,80,443);
        client.addHeader("Accept", "application/json");
        client.addHeader("consumerkey", "da8a96d4-221b-4c9d-9182-725a149685c6");

    }

    public static void get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler,String accessToken) {
        client.addHeader("accesstoken", accessToken);

        client.get(context, getAbsoluteUrl(url), params, responseHandler);
        Log.d("urlhit", getAbsoluteUrl(url));
    }

    public static void post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler,String accessToken) {
        client.addHeader("accesstoken", accessToken);
        client.post(context, getAbsoluteUrl(url), params, responseHandler);

        Log.d("urlhit", getAbsoluteUrl(url));
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return Urls.BASEURL + relativeUrl;
        // return relativeUrl;
    }


}
