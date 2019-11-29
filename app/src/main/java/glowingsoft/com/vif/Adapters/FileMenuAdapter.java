package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;

public class FileMenuAdapter extends BaseAdapter {
    List<LeftMenuModel> leftMenuModels;
    Context context;
    LayoutInflater layoutInflater;

    public FileMenuAdapter(List<LeftMenuModel> leftMenuModels, Context context) {
        this.leftMenuModels = leftMenuModels;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return leftMenuModels.size();
    }

    @Override
    public Object getItem(int i) {
        return leftMenuModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.files_menu_view, viewGroup, false);
        ImageView imageView = layout.findViewById(R.id.imageView);
        Picasso.get().load(leftMenuModels.get(i).getUrl()).resize(110, 110).centerInside().placeholder(R.mipmap.ic_launcher).into(imageView);
        return layout;
    }
}
