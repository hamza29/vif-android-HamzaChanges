package glowingsoft.com.vif.Adapters;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.FilesModels;
import glowingsoft.com.vif.R;

public class FilesRecyclerViewAdapter extends RecyclerView.Adapter<FilesRecyclerViewAdapter.FilesViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<FilesModels> filesModels;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public FilesRecyclerViewAdapter(Context context, List<FilesModels> filesModels
            , DualProgressView progressBar, RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout,
                                    View view) {
        this.context = context;
        this.filesModels = filesModels;
        this.progressBar = progressBar;
        this.refreshInterface = refreshInterface;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.e("response TGED", "FINAL "+filesModels.size() + "");
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.recyclerView = (RecyclerView) view;

    }

    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.files_view, parent, false);
       // Toast.makeText(context, "FINALLY", Toast.LENGTH_SHORT).show();
        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, final int position) {
        final FilesModels filesModel = filesModels.get(position);
        Picasso.get().load(filesModel.getPath()).fit().centerCrop().placeholder(R.drawable.filesplaceholder).into(holder.fileIv);
        holder.fileTv.setText("" + filesModel.getName());
        //Toast.makeText(context, ""+filesModel.getCreated_at(), Toast.LENGTH_SHORT).show();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.menuIvfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialog = layoutInflater.inflate(R.layout.alertdialog_folder_view, null);
                GridView gridView = dialog.findViewById(R.id.gridView);
                ImageView closeIv = dialog.findViewById(R.id.closeIv);
                Log.d("response_size_file", filesModel.getMenudata().size() + "");
                AlertDailogAdpter alertDailogAdpter = new AlertDailogAdpter(filesModel.getMenudata(), context, progressBar, refreshInterface, filesModels.get(position).getId(), shimmerFrameLayout, recyclerView);
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
        return filesModels.size();
    }


    public class FilesViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIv;
        TextView fileTv;
        ImageView menuIvfile;

        public FilesViewHolder(View itemView) {
            super(itemView);
            fileIv = itemView.findViewById(R.id.fileIv);
            fileTv = itemView.findViewById(R.id.fileTv);
            menuIvfile = itemView.findViewById(R.id.menuIv);
        }
    }
}
