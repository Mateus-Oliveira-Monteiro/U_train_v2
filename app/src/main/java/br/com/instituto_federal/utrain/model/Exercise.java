package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Exercise {
    @SerializedName("id")
    private int id;
    @SerializedName("uuid")
    private String uuid;
    // Os campos 'name' e 'description' foram removidos daqui.
    @SerializedName("category")
    private int category;
    @SerializedName("muscles")
    private List<Integer> muscles;
    @SerializedName("muscles_secondary")
    private List<Integer> musclesSecondary;
    @SerializedName("equipment")
    private List<Integer> equipment;
    @SerializedName("license_author")
    private String licenseAuthor;

    // Getters e Setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }
    public List<Integer> getMuscles() { return muscles; }
    public void setMuscles(List<Integer> muscles) { this.muscles = muscles; }
    public List<Integer> getMusclesSecondary() { return musclesSecondary; }
    public void setMusclesSecondary(List<Integer> musclesSecondary) { this.musclesSecondary = musclesSecondary; }
    public List<Integer> getEquipment() { return equipment; }
    public void setEquipment(List<Integer> equipment) { this.equipment = equipment; }
    public String getLicenseAuthor() { return licenseAuthor; }
    public void setLicenseAuthor(String licenseAuthor) { this.licenseAuthor = licenseAuthor; }
}
