package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.GlobalClass;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.ContactModel;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.R;
import glowingsoft.com.vif.webRequest.Urls;
import glowingsoft.com.vif.webRequest.WebReq;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.FolderViewHolder> {
    Context context;
    List<ContactModel> foldersModels;
    LayoutInflater layoutInflater;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public ContactAdapter(Context context, List<ContactModel> foldersModels, DualProgressView progressBar,
                          RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.context = context;
        this.foldersModels = foldersModels;
        this.refreshInterface = refreshInterface;
        this.progressBar = progressBar;
        this.shimmerFrameLayout = shimmerFrameLayout;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.recyclerView = (RecyclerView) view;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.folder_view_contact, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FolderViewHolder holder, final int position) {
        holder.titleTv.setText("" + foldersModels.get(position).getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, FoldersActivtiy.class);
//                context.startActivity(intent);
//            }
//        });
        holder.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (list.get(i).getIcon().equals("BACKUP1")) {
                if (holder.menuIv.isChecked()) {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("is_active", "1");
//                    if (GlobalClass.getInstance().isOnline(context)) {
//                        WebReq.post(context, "/api/user/backup/folders/" + foldersModels.get(position).getId(), requestParams, new CallingConfirmationApi(progressBar, recyclerView, shimmerFrameLayout));
//
//                    } else {
//                        Toast.makeText(context, "" + Urls.internetString, Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("is_active", "0");
//                    if (GlobalClass.getInstance().isOnline(context)) {
//                        WebReq.post(context, "/api/user/backup/folders/" + foldersModels.get(position).getId(), requestParams, new CallingConfirmationApi(progressBar, recyclerView, shimmerFrameLayout));
//                    } else {
//                        Toast.makeText(context, "" + Urls.internetString, Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return foldersModels.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        Switch menuIv;

        public FolderViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            menuIv = itemView.findViewById(R.id.menuIv);
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
