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
import glowingsoft.com.vif.Models.BookMarkModel;
import glowingsoft.com.vif.R;

public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.BookmarkViews> {
    Context context;
    List<BookMarkModel> bookMarkModels;
    LayoutInflater layoutInflater;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public BookmarkRecyclerViewAdapter(Context context, List<BookMarkModel> bookMarkModels, DualProgressView progressBar, RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.context = context;
        this.bookMarkModels = bookMarkModels;
        this.progressBar = progressBar;
        this.refreshInterface = refreshInterface;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("response_bookmark_Size", bookMarkModels.size() + "");
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.recyclerView = (RecyclerView) view;
    }

    @NonNull
    @Override
    public BookmarkViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.bookmark_view, parent, false);

        return new BookmarkViews(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViews holder, final int position) {
        BookMarkModel bookMarkModel = bookMarkModels.get(position);
        holder.textView.setText("" + bookMarkModel.getFolderModel().getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoldersActivtiy.class);
                intent.putExtra("ID", "" + bookMarkModels.get(position).getFolderModel().getId());
                context.startActivity(intent);
                //Toast.makeText(context, "RIGHT", Toast.LENGTH_SHORT).show();
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
                Log.d("response_size_file", bookMarkModels.get(position).getFolderModel().getMenudata().size() + "");
                AlertDailogAdpter alertDailogAdpter = new AlertDailogAdpter(bookMarkModels.get(position).getFolderModel().getMenudata(), context, progressBar, refreshInterface, bookMarkModels.get(position).getId(), shimmerFrameLayout,recyclerView);
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
        return bookMarkModels.size();
    }

    public class BookmarkViews extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView menuIv;

        public BookmarkViews(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.titleTv);
            menuIv = itemView.findViewById(R.id.menuIv);
        }
    }
}
