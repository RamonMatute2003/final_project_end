package com.example.final_project.Settings;

public class Messages_groups{
    private String name_user_messg, time_messg, name_file, weight, extension, image_user, image_file, id, id_user;

    public Messages_groups(String name_user_messg, String time_messg, String name_file, String weight, String extension, String image_user, String image_file, String id, String id_user) {
        this.name_user_messg = name_user_messg;
        this.time_messg = time_messg;
        this.name_file = name_file;
        this.weight = weight;
        this.extension = extension;
        this.image_user = image_user;
        this.image_file = image_file;
        this.id = id;
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public Messages_groups(){

    }

    public String getName_user_messg() {
        return name_user_messg;
    }

    public void setName_user_messg(String name_user_messg) {
        this.name_user_messg = name_user_messg;
    }

    public String getTime_messg() {
        return time_messg;
    }

    public void setTime_messg(String time_messg) {
        this.time_messg = time_messg;
    }

    public String getName_file() {
        return name_file;
    }

    public void setName_file(String name_file) {
        this.name_file = name_file;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }
}