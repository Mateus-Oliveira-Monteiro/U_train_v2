package br.com.instituto_federal.utrain.model;

import com.google.gson.annotations.SerializedName;

public class Muscle {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("name_en")
    private String nameEn;
    @SerializedName("image_url_main")
    private String imageUrlMain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getImageUrlMain() {
        return imageUrlMain;
    }

    public void setImageUrlMain(String imageUrlMain) {
        this.imageUrlMain = imageUrlMain;
    }
}


