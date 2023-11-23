package com.example.socialapp.models;

public class profileFriendListModel {
    private String followdedBy;
    private long time;

    public profileFriendListModel() {
    }

    public String getFollowdedBy() {
        return followdedBy;
    }

    public void setFollowdedBy(String followdedBy) {
        this.followdedBy = followdedBy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
