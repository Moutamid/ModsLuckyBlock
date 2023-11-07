package com.lucky.blocks.mods.mcpeaddons;

public class Map {
    public int app_id, id;
    public String archive, description, downloads, image, name;
    public double rating;
    public int sort, unSort;
    public String type, version;
    public int views;

    public Map() {
    }

    public Map(int app_id, int id, String archive, String description, String downloads, String image, String name, double rating, int sort, int unSort, String type, String version, int views) {
        this.app_id = app_id;
        this.id = id;
        this.archive = archive;
        this.description = description;
        this.downloads = downloads;
        this.image = image;
        this.name = name;
        this.rating = rating;
        this.sort = sort;
        this.unSort = unSort;
        this.type = type;
        this.version = version;
        this.views = views;
    }

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArchive() {
        return archive;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getUnSort() {
        return unSort;
    }

    public void setUnSort(int unSort) {
        this.unSort = unSort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
