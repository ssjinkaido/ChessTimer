package com.mp.chesstimer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class GameMode implements Serializable {
    private String name;
    private String delay;
    private String duration;

    public GameMode(String name, String delay, String duration) {
        this.name = name;
        this.delay = delay;
        this.duration = duration;
    }

    public GameMode() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
