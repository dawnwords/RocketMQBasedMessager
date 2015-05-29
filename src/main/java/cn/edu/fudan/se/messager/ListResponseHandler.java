package cn.edu.fudan.se.messager;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Dawnwords on 2015/5/24.
 */
public abstract class ListResponseHandler<T> {
    private final int expectResponseCount;
    private AtomicInteger responseCount;
    private Vector<T> result;
    private String key;

    ListResponseHandler(int expectResponseCount, String key) {
        this.key = key;
        this.expectResponseCount = expectResponseCount;
        this.responseCount = new AtomicInteger(0);
        this.result = new Vector<>();
    }

    void addResponse(List<T> lecture) {
        result.addAll(lecture);
        if (responseCount.incrementAndGet() == expectResponseCount) {
            enoughResponse(result, key);
        }
    }

    abstract void enoughResponse(Vector<T> result, String key);
}
