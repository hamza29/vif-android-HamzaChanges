package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.felipecsl.gifimageview.library.GifImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import glowingsoft.com.vif.Models.SliderModel;
import glowingsoft.com.vif.R;

public class BookMarkSliderAdapter extends PagerAdapter {
    List<SliderModel> sliderModels;
    Context context;
    LayoutInflater layoutInflater;


    public BookMarkSliderAdapter(List<SliderModel> sliderModels, Context context) {
        this.sliderModels = sliderModels;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderModels.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.bookmarksliderview, container, false);
        ImageView imageview = view.findViewById(R.id.gifImageView);
        TextView titleTv = view.findViewById(R.id.titleTv);
        Log.d("response_gif", sliderModels.get(position).getImage());
        Glide
                .with(context)
                .load(sliderModels.get(position).getImage()).fitCenter().placeholder(R.drawable.filesplaceholder).apply(new RequestOptions().override(ViewGroup.LayoutParams.MATCH_PARENT, 120))
                .into(imageview);
//        Picasso.get().load(sliderModels.get(position).getImage()).fit().centerCrop().placeholder(R.mipmap.ic_launcher).into(imageview);
//        imageview.startAnimation();
//        imageview.animate();

        titleTv.setText("" + sliderModels.get(position).getName());
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
