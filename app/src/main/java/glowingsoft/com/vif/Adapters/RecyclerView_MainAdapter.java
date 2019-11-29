package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.BookMarkModel;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.Models.SliderModel;
import glowingsoft.com.vif.R;

public class RecyclerView_MainAdapter extends RecyclerView.Adapter<RecyclerView_MainAdapter.RecycelerViewHolder> {
    Context context;
    JSONObject jsonObject;
    LayoutInflater layoutInflater;
    List<SliderModel> sliderModels;
    List<BookMarkModel> bookMarkModels;
    DualProgressView progressbar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public RecyclerView_MainAdapter(Context context, JSONObject jsonObject, DualProgressView progressbar, RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.refreshInterface = refreshInterface;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sliderModels = new ArrayList<>();
        this.progressbar = progressbar;
        bookMarkModels = new ArrayList<>();
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.recyclerView = (RecyclerView) view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecycelerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = layoutInflater.inflate(R.layout.view_pager_header, parent, false);
                return new RecycelerViewHolder(view, 0);
            case 1:
                View view1 = layoutInflater.inflate(R.layout.horizontal_bookmark, parent, false);
                return new RecycelerViewHolder(view1, 1);
            case 2:
                Log.e("response TGED", "running");
                View ViewFooter = layoutInflater.inflate(R.layout.mainactiivty_footer_layout, parent, false);
                return new RecycelerViewHolder(ViewFooter, 2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycelerViewHolder holder, int position) {
        switch (position) {
            case 0:
                try {
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("sliders");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SliderModel sliderModel = new SliderModel();
                        sliderModel.setId("" + jsonObject.getString("id"));
                        sliderModel.setName("" + jsonObject.getString("name"));
                        sliderModel.setDescription("" + jsonObject.getString("description"));
                        sliderModel.setImage("" + jsonObject.getString("image"));
                        sliderModel.setUrl("" + jsonObject.getString("url"));
                        sliderModel.setViews("" + jsonObject.getString("views"));
                        sliderModels.add(sliderModel);
                    }
                    holder.adapter = new BookMarkSliderAdapter(sliderModels, context);
                    holder.viewPager.setAdapter(holder.adapter);
                    holder.tabLayout.setupWithViewPager(holder.viewPager);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("bookmarks");
                    Log.d("response_book_size", jsonArray.length() + "");
                    Log.d("response_bookmark", jsonArray.toString());
                    if (jsonArray.length() == 0) {
                        holder.parentLayout.setVisibility(View.GONE);
                        break;
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BookMarkModel bookMarkModel = new BookMarkModel();
                        bookMarkModel.setId("" + jsonObject.getString("id"));
                        bookMarkModel.setType("" + jsonObject.getString("type"));
                        bookMarkModel.setCeated_at("" + jsonObject.getString("created_at"));
                        FoldersModel foldersModel = new FoldersModel();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("bookmarkdata");
                        foldersModel.setId("" + jsonObject1.getString("id"));
                        foldersModel.setName("" + jsonObject1.getString("name"));
                        foldersModel.setDescription("" + jsonObject1.getString("description"));
                        foldersModel.setIcon("" + jsonObject1.getString("icon"));
                        foldersModel.setOwner("" + jsonObject1.getString("owner"));
                        foldersModel.setCreated_at("" + jsonObject1.getString("created_at"));
                        foldersModel.setType("" + jsonObject1.getString("type"));
                        List<LeftMenuModel> leftMenuModels = new ArrayList<>();
                        JSONArray menuArray = jsonObject1.getJSONArray("menudata");
                        for (int j = 0; j < menuArray.length(); j++) {
                            JSONObject jsonObject2 = menuArray.getJSONObject(j);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + jsonObject2.getString("id"));
                            leftMenuModel.setName("" + jsonObject2.getString("name"));
                            leftMenuModel.setIcon("" + jsonObject2.getString("icon"));
                            leftMenuModel.setApi("" + jsonObject2.getString("api"));
                            leftMenuModel.setUrl("" + jsonObject2.getJSONObject("icondata").getString("url"));
                            leftMenuModel.setNameIcon("" + jsonObject2.getJSONObject("icondata").getString("name"));
                            leftMenuModels.add(leftMenuModel);
                        }
                        Log.d("response_menu_size", leftMenuModels.size() + "");
                        foldersModel.setMenudata(leftMenuModels);
                        bookMarkModel.setFolderModel(foldersModel);
                        bookMarkModel.toString();
                        bookMarkModels.add(bookMarkModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response_exception", e.getMessage());
                }
                Log.d("response_bookmark", bookMarkModels.size() + "");

                holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.bookmarkRecyclerViewAdapter = new BookmarkRecyclerViewAdapter(context, bookMarkModels, progressbar, refreshInterface, shimmerFrameLayout, recyclerView);
                holder.recyclerView.setAdapter(holder.bookmarkRecyclerViewAdapter);
                break;
            case 2:
                try {
                    JSONArray jsonArrayFiles = jsonObject.getJSONObject("data").getJSONArray("files");
                    JSONArray jsonArrayFolders = jsonObject.getJSONObject("data").getJSONArray("folders");
                    holder.footerrecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    holder.footerAdapter = new MainActivityFooterAdapter(context, jsonArrayFiles, jsonArrayFolders, progressbar, refreshInterface, shimmerFrameLayout,recyclerView);
                    holder.footerrecyclerView.setAdapter(holder.footerAdapter);
                    Log.e("response 163", "Folder size "+jsonArrayFolders.length() + "");
                    Log.e("response", jsonArrayFiles.length() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class RecycelerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        TabLayout tabLayout;
        ViewPager viewPager;
        BookMarkSliderAdapter adapter;
        BookmarkRecyclerViewAdapter bookmarkRecyclerViewAdapter;
        RecyclerView footerrecyclerView;
        MainActivityFooterAdapter footerAdapter;
        RelativeLayout parentLayout;

        public RecycelerViewHolder(View itemView, int pos) {
            super(itemView);
            switch (pos) {
                case 0:
                    viewPager = itemView.findViewById(R.id.viewPager);
                    tabLayout = itemView.findViewById(R.id.tabDots);
                    break;
                case 1:
                    parentLayout = itemView.findViewById(R.id.parentLayout);
                    recyclerView = itemView.findViewById(R.id.recylerview);
                    break;
                case 2:
                    footerrecyclerView = itemView.findViewById(R.id.recylerview);
                    break;
            }
        }
    }
}
