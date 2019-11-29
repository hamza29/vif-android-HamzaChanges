package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import glowingsoft.com.vif.R;

public class FileTypeAdapter extends BaseAdapter {
    Context context;
    List<FileTypeModel> fileTypeModelList;
    LayoutInflater layoutInflater;

    public FileTypeAdapter(Context context, List<FileTypeModel> fileTypeModelList) {
        this.context = context;
        this.fileTypeModelList = fileTypeModelList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return fileTypeModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return fileTypeModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.spinner_view, viewGroup, false);
        TextView textView = layout.findViewById(R.id.titleTv);
        textView.setText("" + fileTypeModelList.get(i).getName());
        return layout;
    }
}
