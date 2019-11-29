package glowingsoft.com.vif.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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


public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.FooterView> {
    Context context;
    LayoutInflater layoutInflater;
    JSONArray files, folders;
    List<FoldersModel> foldersModels;
    DualProgressView progressBar;
    RefreshInterface refreshInterface;
    ShimmerFrameLayout shimmerFrameLayout;
    RecyclerView recyclerView;

    public BackupAdapter(Context context, JSONArray folder, DualProgressView progressBar, RefreshInterface refreshInterface, ShimmerFrameLayout shimmerFrameLayout, View view) {
        this.files = files;
        this.folders = folder;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        foldersModels = new ArrayList<>();
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
                        foldersModel.setSize("" + jsonObject.getString("size"));
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
                                Log.d("response", "This one running");
                            }
                            subList.add(leftMenuModel);
                        }
                        foldersModel.setMenudata(subList);
                        foldersModels.add(foldersModel);
                    }
                    Log.d("response_folder_data", foldersModels + "");
                    Log.d("response_folder", foldersModels.size() + "");
                    holder.folderRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.folderRecyclerView.setHasFixedSize(true);
                    holder.folderRecyclerViewAdapter = new FolderBackupRecyclerViewAdapter(context, foldersModels, progressBar, refreshInterface, shimmerFrameLayout, recyclerView);
                    holder.folderRecyclerView.setAdapter(holder.folderRecyclerViewAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class FooterView extends RecyclerView.ViewHolder {
        RecyclerView folderRecyclerView;
        RecyclerView filesecyclerView;
        FolderBackupRecyclerViewAdapter folderRecyclerViewAdapter;
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


            }
        }
    }
}
