package com.macdev.admingroupspanel;

public class ModelGroups {

    String groupImage, groupName, groupCategory, groupLink;



    public ModelGroups(String groupImage, String groupName, String groupCategory, String groupLink) {
        this.groupImage = groupImage;
        this.groupName = groupName;
        this.groupCategory = groupCategory;
        this.groupLink = groupLink;
    }


    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(String groupCategory) {
        this.groupCategory = groupCategory;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String groupLink) {
        this.groupLink = groupLink;
    }
}
