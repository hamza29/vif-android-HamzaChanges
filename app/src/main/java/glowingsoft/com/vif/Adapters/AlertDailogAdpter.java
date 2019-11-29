package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.Fragments.BottomSheetFragment;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.FilesModels;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

import static glowingsoft.com.vif.Activities.LoginSignupActivity.preferenceToken;
import static glowingsoft.com.vif.Activities.MainActivity.PREFERENCE;

public class AlertDailogAdpter extends BaseAdapter {
    List<LeftMenuModel> list;
    Context context;
    LayoutInflater layoutInflater;
    List<LeftMenuModel> DottedMenu;
    AlertDialog alertDialog;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    String sourceId;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    SharedPreferences sharedPreferences;

    public AlertDailogAdpter(List<LeftMenuModel> list, Context context, DualProgressView progressBar, RefreshInterface refreshInterface, String sourceId, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.list = list;
        this.context = context;
        DottedMenu = new ArrayList<>();
        this.sourceId = sourceId;
        this.refreshInterface = refreshInterface;
        this.progressBar = progressBar;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.recyclerView = (RecyclerView) view;
    }

    public void setReference(AlertDialog alertDialog) {
        this.alertDialog = alertDialog;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.gridview_alert_dialog, viewGroup, false);
        TextView titleTv = layout.findViewById(R.id.titleTv);
        ImageView imageView = layout.findViewById(R.id.iconIv);
        titleTv.setText("" + list.get(i).getName());
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (list.get(i).getIcon().equals("RESIZE")) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    View view1 = layoutInflater.inflate(R.layout.move_files_views, viewGroup, false);
                    final Spinner spinner = view1.findViewById(R.id.spinner);
                    Button confirmBtn = view1.findViewById(R.id.confirmBtn);
                    alertDialog.setView(view1);
                    Button cancelBtn = view1.findViewById(R.id.cancelBtn);
                    final AlertDialog alertDialog1 = alertDialog.create();
                    if (GlobalClass.getInstance().isOnline(context)) {
                        WebReq.get(context, "/api/user/get/folders", null, new GetAllFoldersApi(spinner, confirmBtn, sourceId, progressBar, alertDialog1, shimmerFrameLayout, recyclerView), sharedPreferences.getString(preferenceToken, ""));
                    }
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog1.dismiss();
                        }
                    });


                    alertDialog1.show();

                }
                if (list.get(i).getIcon().equals("PRIVACY")) {
                    if (GlobalClass.getInstance().isOnline(context)) {
                        WebReq.get(context, list.get(i).getApi(), null, new CallRestApi(), sharedPreferences.getString(preferenceToken, ""));
                    } else {
                        Toast.makeText(context, "" + Urls.internetString, Toast.LENGTH_SHORT).show();
                    }

                }

                if (list.get(i).getIcon().equals("BOOKMARK")) {
                    if (GlobalClass.getInstance().isOnline(context)) {
                        WebReq.get(context, list.get(i).getApi(),
                                null, new BookMarkApi(progressBar, i, shimmerFrameLayout, recyclerView), sharedPreferences.getString(preferenceToken, ""));
                    } else {
                        Toast.makeText(context, "" + Urls.internetString, Toast.LENGTH_SHORT).show();
                    }
                }

                if (list.get(i).getNameIcon().equals("3DOTS")) {
                    Log.d("response_api", list.get(i).getApi());
                    if (GlobalClass.getInstance().isOnline(context)) {
                        DottedMenu.clear();
                        WebReq.client.cancelAllRequests(true);
                        String url = list.get(i).getApi();
                        Log.d("response_url", url);
                        WebReq.get(context, url, null, new DottedMenuApi(progressBar, shimmerFrameLayout, recyclerView), sharedPreferences.getString(preferenceToken, ""));
                    }
                }
                if (list.get(i).getIcon().equals("TRASH")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Warning");
                    final int pos = i;
                    alertDialog.setMessage("Are you sure to delete this folder");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if (GlobalClass.getInstance().isOnline(context)) {
                                WebReq.get(context, list.get(pos).getApi(), null, new BookMarkApi(progressBar, i, shimmerFrameLayout, recyclerView), sharedPreferences.getString(preferenceToken, ""));

                            } else {
                                Toast.makeText(context, "" + Urls.internetString, Toast.LENGTH_SHORT).show();
                            }
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
            }
        });

        Picasso.get().load(list.get(i).getUrl()).resize(70, 70).centerInside().placeholder(R.mipmap.ic_launcher).into(imageView);
        return layout;
    }

    public class DottedMenuApi extends JsonHttpResponseHandler {
        DualProgressView progressBar;
        ShimmerFrameLayout shimmerFrameLayout;
        RecyclerView recyclerView;

        public DottedMenuApi(DualProgressView progressBar, ShimmerFrameLayout shimmerFrameLayout, View view) {
            this.progressBar = progressBar;
            this.shimmerFrameLayout = shimmerFrameLayout;
            this.recyclerView = (RecyclerView) view;
        }

        @Override
        public void onStart() {
            super.onStart();
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_menu", response.toString());
            try {
                if (response.getString("status").equals("true")) {
                    JSONArray menuJsonArray = response.getJSONObject("data").getJSONArray("menu");
                    for (int i = 0; i < menuJsonArray.length(); i++) {
                        JSONObject menuObject = menuJsonArray.getJSONObject(i);
                        LeftMenuModel leftMenuModel = new LeftMenuModel();
                        leftMenuModel.setId("" + menuObject.getString("id"));
                        leftMenuModel.setName("" + menuObject.getString("name"));
                        leftMenuModel.setIcon("" + menuObject.getString("icon"));
                        leftMenuModel.setApi("" + menuObject.getString("api"));
                        leftMenuModel.setUrl("" + menuObject.getJSONObject("icondata").getString("url"));
                        leftMenuModel.setNameIcon("" + menuObject.getJSONObject("icondata").getString("name"));
                        DottedMenu.add(leftMenuModel);
                    }
                    Log.d("response_length", DottedMenu.size() + "");
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.setdata(DottedMenu);
                    bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("response_failer", responseString);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("response_failer", errorResponse.toString());

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d("response_failer", errorResponse.toString());
            progressBar.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            progressBar.setVisibility(View.GONE);
        }
    }

    public class CallRestApi extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            try {
                Log.d("response_verify", response.toString());
                if (response.getString("status").equals("true")) {
                    JSONObject jsonObject = response.getJSONObject("data").getJSONObject("stampdata");
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialog = layoutInflater.inflate(R.layout.verify_alert_dialog, null);
                    Button cancel = dialog.findViewById(R.id.cancel);
                    TextView time = dialog.findViewById(R.id.timeTv);
                    TextView token = dialog.findViewById(R.id.tokenTv);
                    TextView stamp = dialog.findViewById(R.id.stampTv);
                    TextView hash = dialog.findViewById(R.id.hashTv);
                    time.setText("" + jsonObject.getString("time"));
                    token.setText("" + jsonObject.getString("token"));
                    stamp.setText("" + jsonObject.getString("stamp_id"));
                    hash.setText("" + jsonObject.getString("hash"));


                    alert.setView(dialog);
                    final AlertDialog alertDialog = alert.create();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFinish() {
            super.onFinish();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }
    }

    public class BookMarkApi extends JsonHttpResponseHandler {
        DualProgressView progressBar;
        int position;
        ShimmerFrameLayout shimmerFrameLayout;
        RecyclerView recyclerView;

        public BookMarkApi(DualProgressView progressBar, int position, ShimmerFrameLayout shimmerFrameLayout, View view) {
            this.progressBar = progressBar;
            this.position = position;
            this.shimmerFrameLayout = shimmerFrameLayout;
            this.recyclerView = (RecyclerView) view;
        }

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
                notifyDataSetChanged();
                Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                if (refreshInterface != null) {
                    refreshInterface.refreshLayout();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            progressBar.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
            recyclerView.setVisibility(View.VISIBLE);

        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public class GetAllFoldersApi extends JsonHttpResponseHandler {
        Spinner spinner;
        List<FoldersModel> foldersModelList;
        SpinnerFolderAdapter spinnerFolderAdapter;
        Button confimBtn;
        String currentSource;
        DualProgressView progressBar;
        AlertDialog alertDialog;
        ShimmerFrameLayout shimmerFrameLayout;
        RecyclerView recyclerView;

        public GetAllFoldersApi(Spinner spinner, Button confirm, String sourceId, DualProgressView progressBar, AlertDialog alertDialog, ShimmerFrameLayout shimmerFrameLayout, View view) {
            this.spinner = spinner;
            foldersModelList = new ArrayList<>();
            this.confimBtn = confirm;
            currentSource = sourceId;
            this.progressBar = progressBar;
            this.alertDialog = alertDialog;
            this.shimmerFrameLayout = shimmerFrameLayout;
            this.recyclerView = (RecyclerView) view;
        }

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
                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("folders");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        FoldersModel foldersModel = new FoldersModel();
                        foldersModel.setId("" + jsonObject.getString("id"));
                        foldersModel.setName("" + jsonObject.getString("name"));
                        foldersModel.setDescription("" + jsonObject.getString("description"));
                        foldersModel.setIcon("" + jsonObject.getString("icon"));
                        foldersModel.setOwner("" + jsonObject.getString("owner"));
                        foldersModel.setCreated_at("" + jsonObject.getString("created_at"));
                        foldersModel.setIconName("" + jsonObject.getJSONObject("icondata").getString("name"));
                        foldersModel.setIconUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                        JSONArray menuData = jsonObject.getJSONArray("menudata");
                        List<LeftMenuModel> subList = new ArrayList<>();
                        for (int j = 0; j < menuData.length(); j++) {
                            JSONObject jsonObject1 = menuData.getJSONObject(j);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + jsonObject1.getString("id"));
                            leftMenuModel.setName("" + jsonObject1.getString("name"));
                            leftMenuModel.setIcon("" + jsonObject1.getString("icon"));
                            leftMenuModel.setApi("" + jsonObject1.getString("api"));
                            leftMenuModel.setUrl("" + jsonObject1.getJSONObject("icondata").getString("url"));
                            leftMenuModel.setNameIcon("" + jsonObject1.getJSONObject("icondata").getString("name"));
                            subList.add(leftMenuModel);
                        }
                        foldersModel.setMenudata(subList);
                        foldersModelList.add(foldersModel);
                    }
                    spinnerFolderAdapter = new SpinnerFolderAdapter(context, foldersModelList);
                    spinner.setAdapter(spinnerFolderAdapter);
                    confimBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("destination", foldersModelList.get(spinner.getSelectedItemPosition()).getId());
                            requestParams.put("sources[]", currentSource);
                            WebReq.post(context, "/api/user/move/file", requestParams, new CallingConfirmationApi(progressBar, recyclerView, shimmerFrameLayout), sharedPreferences.getString(preferenceToken, ""));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d("response_Failer", responseString);

        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public class CallingConfirmationApi extends JsonHttpResponseHandler {
        DualProgressView progressBar;
        ShimmerFrameLayout shimmerFrameLayout;
        RecyclerView recyclerView;

        public CallingConfirmationApi(DualProgressView progressBar, View recyclerView, ShimmerFrameLayout shimmerFrameLayout) {
            this.progressBar = progressBar;
            this.recyclerView = (RecyclerView) recyclerView;
            this.shimmerFrameLayout = shimmerFrameLayout;
        }

        @Override
        public void onStart() {
            super.onStart();
            recyclerView.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmerAnimation();


        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response_confirmation", response.toString());
            try {
                Toast.makeText(context, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmerAnimation();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            shimmerFrameLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}
