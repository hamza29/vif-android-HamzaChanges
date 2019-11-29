package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import glowingsoft.com.vif.Activities.FoldersActivtiy;
import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.R;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.FolderViewHolder> {
    Context context;
    List<FoldersModel> foldersModels;
    LayoutInflater layoutInflater;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public FolderRecyclerViewAdapter(Context context, List<FoldersModel> foldersModels, DualProgressView progressBar, RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.context = context;
        this.foldersModels = foldersModels;
        this.refreshInterface = refreshInterface;
        this.progressBar = progressBar;
        this.shimmerFrameLayout = shimmerFrameLayout;
        Log.e("response TGED", "FOLDER IS " + foldersModels.size());
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.recyclerView = (RecyclerView) view;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.folder_view, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, final int position) {
        holder.titleTv.setText("" + foldersModels.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoldersActivtiy.class);
                intent.putExtra("ID", "" + foldersModels.get(position).getId());
                context.startActivity(intent);
                Log.d("response 66","FolderRecyclerViewAdapter");
//                Toast.makeText(context, "RIGHT", Toast.LENGTH_SHORT).show();
            }
        });
        holder.menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialog = layoutInflater.inflate(R.layout.alertdialog_folder_view, null);
                GridView gridView = dialog.findViewById(R.id.gridView);
                ImageView closeIv = dialog.findViewById(R.id.closeIv);
                Log.e("response 77", "HELO   " + foldersModels.get(position).getMenudata().get(position).getId() + "");
                AlertDailogAdpter alertDailogAdpter = new AlertDailogAdpter(foldersModels.get(position).getMenudata(), context, progressBar, refreshInterface, foldersModels.get(position).getId(), shimmerFrameLayout, recyclerView);
                gridView.setAdapter(alertDailogAdpter);
                alert.setView(dialog);
                final AlertDialog alertDialog = alert.create();
                alertDailogAdpter.setReference(alertDialog);
                closeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alertDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                alertDialog.getWindow().setAttributes(lp);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foldersModels.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv;
        ImageView menuIv;

        public FolderViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            menuIv = itemView.findViewById(R.id.menuIv);
        }
    }
}
