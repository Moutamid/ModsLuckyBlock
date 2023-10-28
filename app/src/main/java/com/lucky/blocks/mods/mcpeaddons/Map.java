package com.lucky.blocks.mods.mcpeaddons;

public class Map {
    private String archive_name;
    private String description;
    private String downloads;
    private int id;
    private String name;
    private float rating;
    private String ratingText;
    private int sort;
    private String thumbnail;
    private String type;
    private int unSort;
    private String url;
    private String version;
    private String views;

    public Map(int i, String str, String str2, String str3, String str4, String str5, String str6, float f, String str7, String str8, String str9, String str10, int i2, int i3) {
        this.id = i;
        this.name = str;
        this.views = str6;
        this.description = str2;
        this.thumbnail = str4;
        this.url = str3;
        this.archive_name = str5;
        this.rating = f;
        this.version = str7;
        this.downloads = str8;
        this.ratingText = str9;
        this.type = str10;
        this.sort = i2;
        this.unSort = i3;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getViews() {
        return this.views;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public String getArchiveName() {
        return this.archive_name;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String str) {
        this.thumbnail = str;
    }

    public String getDownloads() {
        return this.downloads;
    }

    public void setDownloads(String str) {
        this.downloads = str;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float f) {
        this.rating = f;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String str) {
        this.version = str;
    }

    public String getRatingText() {
        return this.ratingText;
    }

    public void setRatingText(String str) {
        this.ratingText = str;
    }

    public String getType() {
        return this.type;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int i) {
        this.sort = i;
    }

    public int getUnSort() {
        return this.unSort;
    }

    public void setUnSort(int i) {
        this.unSort = i;
    }
}
