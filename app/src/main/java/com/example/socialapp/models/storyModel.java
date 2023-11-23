package com.example.socialapp.models;

import java.util.ArrayList;

public class storyModel {
    public String getStoryBy() {
        return storyBy;
    }

    public void setStoryBy(String storyBy) {
        this.storyBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStoryModel> getList() {
        return list;
    }

    public void setList(ArrayList<UserStoryModel> list) {
        this.list = list;
    }

    private String storyBy;
private long storyAt;

ArrayList<UserStoryModel> list;


    public storyModel(String storyBy, long storyAt, ArrayList<UserStoryModel> list) {
        this.storyBy = storyBy;
        this.storyAt = storyAt;
        this.list = list;
    }

    public storyModel() {
    }
}