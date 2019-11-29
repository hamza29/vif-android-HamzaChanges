package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import glowingsoft.com.vif.Models.UserListModel;
import glowingsoft.com.vif.R;

public class UserListAdapter extends BaseAdapter {
    List<UserListModel> userListModels;
    Context context;
    LayoutInflater layoutInflater;

    public UserListAdapter(List<UserListModel> userListModels, Context context) {
        this.userListModels = userListModels;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return userListModels.size();
    }

    @Override
    public Object getItem(int i) {
        return userListModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.spinner_view, viewGroup, false);
        TextView textView = layout.findViewById(R.id.titleTv);
        textView.setText("" + userListModels.get(i).getName());
        return layout;
    }
}
