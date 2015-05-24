package cn.edu.fudan.se.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dawnwords on 2015/5/24.
 */
public class LectureRequest implements Serializable {
    public final String id;

    public LectureRequest(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
