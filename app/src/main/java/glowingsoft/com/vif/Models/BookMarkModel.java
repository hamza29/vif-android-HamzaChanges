package glowingsoft.com.vif.Models;

import android.util.Log;

public class BookMarkModel {
    String id, type, ceated_at;
    FoldersModel folderModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCeated_at() {
        return ceated_at;
    }

    public void setCeated_at(String ceated_at) {
        this.ceated_at = ceated_at;
    }

    public FoldersModel getFolderModel() {
        return folderModel;
    }

    public void setFolderModel(FoldersModel folderModel) {
        this.folderModel = folderModel;
    }

    @Override
    public String toString() {
        Log.d("response "," BookMarkModel getId() = "+this.getId().toString());
        return super.toString();
    }
}
