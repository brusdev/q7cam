package com.brusdev.q7cam;

import java.io.Serializable;

public class DevInfo implements Serializable {

    private String devid;
    private int hkid;
    private String videoType;
    private String audioType;
    private int channal;
    private int stats;
    private String alias;
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public int getHkid() {
        return hkid;
    }

    public void setHkid(int hkid) {
        this.hkid = hkid;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }

    public int getChannal() {
        return channal;
    }

    public void setChannal(int channal) {
        this.channal = channal;
    }

    public int getStats() {
        return stats;
    }

    public void setStats(int stats) {
        this.stats = stats;
    }


}