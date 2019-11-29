package glowingsoft.com.vif.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import glowingsoft.com.vif.Adapters.FileTypeModel;

public class FieldsModel {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("needs_validation")
    @Expose
    private String needsValidation;
    @SerializedName("values")
    @Expose
    private List<ValuesModel> values = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNeedsValidation() {
        return needsValidation;
    }

    public void setNeedsValidation(String needsValidation) {
        this.needsValidation = needsValidation;
    }

    public List<ValuesModel> getValues() {
        return values;
    }

    public void setValues(List<ValuesModel> values) {
        this.values = values;
    }

}