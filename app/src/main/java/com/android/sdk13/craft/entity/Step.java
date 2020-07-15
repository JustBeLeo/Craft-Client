package com.android.sdk13.craft.entity;

public class Step {
    int step;
    String operate;
    int comments;
    int learned;
    String videoUrl;

    public Step(int step, String operate, int comments, int learned, String videoUrl) {
        this.step = step;
        this.operate = operate;
        this.comments = comments;
        this.learned = learned;
        this.videoUrl = videoUrl;
    }

    public Step() {
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLearned() {
        return learned;
    }

    public void setLearned(int learned) {
        this.learned = learned;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
