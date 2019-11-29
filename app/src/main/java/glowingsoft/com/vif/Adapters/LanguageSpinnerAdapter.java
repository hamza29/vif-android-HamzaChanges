package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import glowingsoft.com.vif.Models.LanguageModel;
import glowingsoft.com.vif.R;

public class LanguageSpinnerAdapter extends BaseAdapter {
    Context context;
    List<LanguageModel> languageModels;
    LayoutInflater layoutInflater;

    public LanguageSpinnerAdapter(Context context, List<LanguageModel> languageModels) {
        this.context = context;
        this.languageModels = languageModels;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return languageModels.size();
    }

    @Override
    public Object getItem(int i) {
        return languageModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = layoutInflater.inflate(R.layout.spinner_view, viewGroup, false);
        TextView textView = layout.findViewById(R.id.titleTv);
        textView.setText("" + languageModels.get(i).getLanguage());
        return layout;
    }
}
