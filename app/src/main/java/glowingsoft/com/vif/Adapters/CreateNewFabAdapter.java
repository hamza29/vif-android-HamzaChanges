package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;

public class CreateNewFabAdapter extends BaseAdapter {
    List<LeftMenuModel> leftMenuModels;
    Context context;
    LayoutInflater layoutInflater;


    public CreateNewFabAdapter(List<LeftMenuModel> leftMenuModels, Context context) {
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
        View layout = layoutInflater.inflate(R.layout.create_new_menu_view, viewGroup, false);
        TextView textView = layout.findViewById(R.id.titleTv);
        ImageView iconIv = layout.findViewById(R.id.iconIv);
        textView.setText("" + leftMenuModels.get(i).getName());
        Picasso.get().load(leftMenuModels.get(i).getUrl()).resize(40, 40).centerInside().placeholder(R.mipmap.ic_launcher).into(iconIv);
        return layout;
    }
}
