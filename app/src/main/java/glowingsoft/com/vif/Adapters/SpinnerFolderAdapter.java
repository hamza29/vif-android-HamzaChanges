package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.R;

public class SpinnerFolderAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private List<FoldersModel> foldersModelList;

    public SpinnerFolderAdapter(Context context, List<FoldersModel> foldersModelList) {
        this.context = context;
        this.foldersModelList = foldersModelList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return foldersModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return foldersModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.spinner_view, viewGroup, false);
        TextView textView = layout.findViewById(R.id.titleTv);
        textView.setText("" + foldersModelList.get(i).getCreated_at());

        return layout;
    }
}
