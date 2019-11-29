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

public class BottomSheetAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    List<LeftMenuModel> leftMenuModels;

    public BottomSheetAdapter(Context context, List<LeftMenuModel> leftMenuModels) {
        this.context = context;
        this.leftMenuModels = leftMenuModels;
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
        View view1 = layoutInflater.inflate(R.layout.bottom_sheet_view, viewGroup, false);
        TextView descTv = view1.findViewById(R.id.descTv);
        ImageView image = view1.findViewById(R.id.image);
        descTv.setText(leftMenuModels.get(i).getName());
        Picasso.get().load(leftMenuModels.get(i).getUrl()).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(image);

        return view1;
    }
}
