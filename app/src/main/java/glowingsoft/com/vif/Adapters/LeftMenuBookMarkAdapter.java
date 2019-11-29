package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import glowingsoft.com.vif.Models.BookMarkModel;
import glowingsoft.com.vif.R;

public class LeftMenuBookMarkAdapter extends RecyclerView.Adapter<LeftMenuBookMarkAdapter.BookMarkViewHolder> {
    Context context;
    List<BookMarkModel> bookMarkModelList;
    LayoutInflater layoutInflater;

    public LeftMenuBookMarkAdapter(Context context, List<BookMarkModel> bookMarkModels) {
        this.bookMarkModelList = bookMarkModels;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public BookMarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.files_view, parent, false);
        return new BookMarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookMarkViewHolder holder, int position) {
        holder.fileTv.setText("" + bookMarkModelList.get(position).getFolderModel().getName());
        Picasso.get().load(bookMarkModelList.get(position).getFolderModel().getPath()).fit().centerCrop().placeholder(R.drawable.filesplaceholder).into(holder.fileIv);


    }

    @Override
    public int getItemCount() {
        return bookMarkModelList.size();
    }

    public class BookMarkViewHolder extends RecyclerView.ViewHolder {
        ImageView fileIv;
        TextView fileTv;
        ImageView menuIvfile;

        public BookMarkViewHolder(View itemView) {
            super(itemView);
            fileIv = itemView.findViewById(R.id.fileIv);
            fileTv = itemView.findViewById(R.id.fileTv);
            menuIvfile = itemView.findViewById(R.id.menuIv);
        }
    }
}
