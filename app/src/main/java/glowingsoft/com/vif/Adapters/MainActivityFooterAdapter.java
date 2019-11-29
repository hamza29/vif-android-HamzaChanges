package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import glowingsoft.com.vif.DualProgressView;
import glowingsoft.com.vif.Interfaces.RefreshInterface;
import glowingsoft.com.vif.Models.FilesModels;
import glowingsoft.com.vif.Models.FoldersModel;
import glowingsoft.com.vif.Models.LeftMenuModel;
import glowingsoft.com.vif.R;


public class MainActivityFooterAdapter extends RecyclerView.Adapter<MainActivityFooterAdapter.FooterView> {
    Context context;
    LayoutInflater layoutInflater;
    JSONArray files, folders;
    List<FoldersModel> foldersModels;
    List<FilesModels> filesModelsList;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public MainActivityFooterAdapter(Context context, JSONArray files, JSONArray folder, DualProgressView progressBar, RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.files = files;
        this.folders = folder;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        foldersModels = new ArrayList<>();
        filesModelsList = new ArrayList<>();
        this.progressBar = progressBar;
        this.refreshInterface = refreshInterface;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.recyclerView = (RecyclerView) view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public FooterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = layoutInflater.inflate(R.layout.folder_layout_recy, parent, false);
                return new FooterView(view, 0);
            case 1:
                View view1 = layoutInflater.inflate(R.layout.file_layout_recy, parent, false);
                return new FooterView(view1, 1);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull FooterView holder, int position) {
        switch (position) {
            case 0:
                try {
                    if (folders.length() == 0) {
                        holder.folderParent.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < folders.length(); i++) {
                        JSONObject jsonObject = folders.getJSONObject(i);
                        FoldersModel foldersModel = new FoldersModel();
                        foldersModel.setId("" + jsonObject.getString("id"));
                        foldersModel.setName("" + jsonObject.getString("name"));
                        foldersModel.setDescription("" + jsonObject.getString("description"));
                        foldersModel.setIcon("" + jsonObject.getString("icon"));
                        foldersModel.setOwner("" + jsonObject.getString("owner"));
                        foldersModel.setCreated_at("" + jsonObject.getString("created_at"));
                        foldersModel.setIconName("" + jsonObject.getJSONObject("icondata").getString("name"));
                        foldersModel.setIconUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                        JSONArray menuData = jsonObject.getJSONArray("menudata");
                        List<LeftMenuModel> subList = new ArrayList<>();
                        for (int j = 0; j < menuData.length(); j++) {
                            JSONObject jsonObject1 = menuData.getJSONObject(j);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + jsonObject1.getString("id"));
                            leftMenuModel.setName("" + jsonObject1.getString("name"));
                            leftMenuModel.setIcon("" + jsonObject1.getString("icon"));
                            leftMenuModel.setApi("" + jsonObject1.getString("api"));
                            try {
                                leftMenuModel.setUrl("" + jsonObject1.getJSONObject("icondata").getString("url"));
                                leftMenuModel.setNameIcon("" + jsonObject1.getJSONObject("icondata").getString("name"));
                            } catch (Exception e) {
                                Log.e("response",  "This one running");
                            }
                            subList.add(leftMenuModel);
                        }
                        foldersModel.setMenudata(subList);
                        foldersModels.add(foldersModel);
                    }
                    Log.e("response 115", "Folder size "+foldersModels + "");
                    Log.e("response", "Folder size "+foldersModels.size() + "");
                    holder.folderRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    holder.folderRecyclerView.setHasFixedSize(true);
                    holder.folderRecyclerViewAdapter = new FolderRecyclerViewAdapter(context, foldersModels, progressBar, refreshInterface, shimmerFrameLayout, recyclerView);
                    holder.folderRecyclerView.setAdapter(holder.folderRecyclerViewAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    if (files.length() == 0) {
                        holder.fileParent.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < files.length(); i++) {
                        JSONObject fileJsonobject = files.getJSONObject(i);
                        FilesModels filesModels = new FilesModels();
                        filesModels.setId("" + fileJsonobject.getString("id"));
                        filesModels.setName("" + fileJsonobject.getString("name"));
                        filesModels.setDescription("" + fileJsonobject.getString("description"));
                        filesModels.setPath("" + fileJsonobject.getString("path"));
                        filesModels.setOwner("" + fileJsonobject.getString("owner"));
                        filesModels.setShares("" + fileJsonobject.getString("shares"));
                        filesModels.setViews("" + fileJsonobject.getString("views"));
                        filesModels.setDownloads("" + fileJsonobject.getString("downloads"));
                        filesModels.setIcon("" + fileJsonobject.getString("icon"));
                        filesModels.setCreated_at("" + fileJsonobject.getString("created_at"));
                        List<LeftMenuModel> menuModels = new ArrayList<>();
                        JSONArray menuArray = fileJsonobject.getJSONArray("menudata");
                        Log.e("response", menuArray.toString());
                        for (int j = 0; j < menuArray.length(); j++) {
                            JSONObject jsonObject = menuArray.getJSONObject(j);
                            LeftMenuModel leftMenuModel = new LeftMenuModel();
                            leftMenuModel.setId("" + jsonObject.getString("id"));
                            leftMenuModel.setName("" + jsonObject.getString("name"));
                            leftMenuModel.setIcon("" + jsonObject.getString("icon"));
                            leftMenuModel.setApi("" + jsonObject.getString("api"));
                            try {
                                leftMenuModel.setNameIcon("" + jsonObject.getJSONObject("icondata").getString("name"));
                                leftMenuModel.setUrl("" + jsonObject.getJSONObject("icondata").getString("url"));
                            } catch (Exception e) {
                                Log.e("response", "This one is running");
                            }
                            menuModels.add(leftMenuModel);
                        }
                        Log.e("response_size", menuModels.size() + "");
                        filesModels.setMenudata(menuModels);
                        filesModels.setIconname("" + fileJsonobject.getJSONObject("icondata").getString("name"));
                        filesModels.setIconUrl("" + fileJsonobject.getJSONObject("icondata").getString("url"));
                        filesModelsList.add(filesModels);
                    }
                    Log.e("response ","LAST size "+ filesModelsList + "");
                    Log.e("response ", filesModelsList.size() + "");
                    holder.filesecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    holder.filesecyclerView.setHasFixedSize(true);
                    holder.filesRecyclerViewAdapter = new FilesRecyclerViewAdapter(context, filesModelsList, progressBar, refreshInterface, shimmerFrameLayout,recyclerView);
                    holder.filesecyclerView.setAdapter(holder.filesRecyclerViewAdapter);

                } catch (Exception e) {

                }
                break;
        }

    }


    @Override
    public int getItemCount() {
        return 2;
    }

    public class FooterView extends RecyclerView.ViewHolder {
        RecyclerView folderRecyclerView;
        RecyclerView filesecyclerView;
        FolderRecyclerViewAdapter folderRecyclerViewAdapter;
        FilesRecyclerViewAdapter filesRecyclerViewAdapter;
        RelativeLayout fileParent;
        RelativeLayout folderParent;

        public FooterView(View itemView, int pos) {
            super(itemView);
            switch (pos) {
                case 0:
                    folderRecyclerView = itemView.findViewById(R.id.folderRecycler);
                    folderParent = itemView.findViewById(R.id.folderParent);
                    break;
                case 1:
                    filesecyclerView = itemView.findViewById(R.id.fileRecycler);
                    fileParent = itemView.findViewById(R.id.fileParent);


            }
        }
    }
}
