package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.R;

public class FolderAdapter extends BaseAdapter {
    List<FoldersModel> foldersModels;
    Context context;
    LayoutInflater layoutInflater;

    public FolderAdapter(List<FoldersModel> foldersModels, Context context) {
        this.foldersModels = foldersModels;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("response_book_size", foldersModels.size() + "");
    }

    @Override
    public int getCount() {
        return foldersModels.size();
    }

    @Override
    public Object getItem(int i) {
        return foldersModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View view1 = layoutInflater.inflate(R.layout.folder_view, viewGroup, false);
        TextView titleTv = view1.findViewById(R.id.titleTv);
        titleTv.setText("" + foldersModels.get(i).getName());
        ImageView menuIv = view1.findViewById(R.id.menuIv);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialog = layoutInflater.inflate(R.layout.alertdialog_folder_view, null);
                GridView gridView = dialog.findViewById(R.id.gridView);
                ImageView closeIv = dialog.findViewById(R.id.closeIv);
                Log.d("response_size_file", foldersModels.get(i).getMenudata().size() + "");
                AlertDailogAdpter alertDailogAdpter = new AlertDailogAdpter(foldersModels.get(i).getMenudata(), context, null, null, foldersModels.get(i).getId(), null, null);
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
        menuIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//                bottomSheetFragment.setdata(foldersModels.get(i).getMenudata());
//                bottomSheetFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), bottomSheetFragment.getTag());
                LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view2 = layout.inflate(R.layout.folder_long_click_popup, null);
                BottomSheetDialog dialog = new BottomSheetDialog(context);
                dialog.setContentView(view2);
                dialog.show();
            }
        });
        return view1;
    }


}
