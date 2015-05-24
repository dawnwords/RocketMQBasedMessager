package cn.edu.fudan.se.bean;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/24.
 */
public class LectureResponse implements Serializable {
    public final String requestId;
    public final List<Lecture> lectures;


    public LectureResponse(String requestId, List<Lecture> lectures) {
        this.requestId = requestId;
        this.lectures = lectures;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
