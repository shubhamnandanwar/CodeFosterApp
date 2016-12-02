package com.clabs.codefosterapp;

/**
 * Created by shubh on 02-12-2016.
 */
public class EventPost {

    private String downloadUrl;
    private String postHeading;
    private String postDesc;

    public EventPost(String downloadUrl, String postHeading, String postDesc) {
        this.downloadUrl = downloadUrl;
        this.postHeading = postHeading;
        this.postDesc = postDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPostHeading() {
        return postHeading;
    }

    public void setPostHeading(String postHeading) {
        this.postHeading = postHeading;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }
}
