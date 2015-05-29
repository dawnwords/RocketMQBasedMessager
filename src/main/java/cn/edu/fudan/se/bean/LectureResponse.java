package cn.edu.fudan.se.bean;

import com.alibaba.fastjson.JSON;

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
        return JSON.toJSONString(this);
    }
}
