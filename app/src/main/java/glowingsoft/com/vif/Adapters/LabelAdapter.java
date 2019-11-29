package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;

public class LabelAdapter extends BaseAdapter {
    Context context;
    List<LeftMenuModel> alertModels;
    LayoutInflater layoutInflater;

    public LabelAdapter(Context context, List<LeftMenuModel> alertModels) {
        this.context = context;
        this.alertModels = alertModels;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return alertModels.size();
    }

    @Override
    public Object getItem(int i) {
        return alertModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.alert_view, viewGroup, false);
        TextView titleTv = layout.findViewById(R.id.nameTv);
        TextView descTv = layout.findViewById(R.id.descTv);
        titleTv.setText("" + alertModels.get(i).getName());
        descTv.setText("" + alertModels.get(i).getDescription());
        return layout;
    }
}
