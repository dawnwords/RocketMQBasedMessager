package cn.edu.fudan.se.messager;

import cn.edu.fudan.se.Parameter;
import cn.edu.fudan.se.bean.Lecture;
import cn.edu.fudan.se.bean.LectureRequest;
import cn.edu.fudan.se.bean.LectureResponse;
import com.alibaba.rocketmq.client.producer.SendResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dawnwords on 2015/5/23.
 */
public class Invoker extends Messager implements Runnable {


    private ConcurrentHashMap<String, ListResponseHandler<Lecture>> idHandlerMap;
    private int responsorCount;

    public Invoker(int responsorCount) {
        super(Parameter.TOPIC, Parameter.INVOKER_CONSUMER_GROUP, Parameter.INVOKER_PRODUCER_GROUP);
        this.responsorCount = responsorCount;
        this.idHandlerMap = new ConcurrentHashMap<>();
    }

    @Override
    protected boolean onReceiveMessage(String messageId, Object messageBody) {
        if (!(messageBody instanceof LectureResponse)) {
            log("response type error");
            return false;
        }
        LectureResponse response = (LectureResponse) messageBody;
        if (!idHandlerMap.containsKey(response.requestId)) {
            log("request id not exists");
        } else {
            idHandlerMap.get(response.requestId).addResponse(response.lectures);
        }
        return true;
    }

    @Override
    public void run() {
        start(Parameter.RESPONSE_TAG);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line;
            try {
                if ("stop".equals(line = reader.readLine())) {
                    break;
                } else {
                    LectureRequest body = new LectureRequest(line);
                    SendResult sendResult = sendMessage(Parameter.REQUEST_TAG, Parameter.INVOKER_KEY, body);
                    idHandlerMap.put(sendResult.getMsgId(), new Handler(responsorCount, sendResult.getMsgId()));
                    log(String.format("[%s]%s", sendResult.getMsgId(), body));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    private class Handler extends ListResponseHandler<Lecture> {
        Handler(int expectResponseCount, String messageId) {
            super(expectResponseCount, messageId);
        }

        @Override
        void enoughResponse(Vector<Lecture> result, String key) {
            String print = "";
            for (Lecture lecture : result) {
                print += lecture + "\n";
            }
            idHandlerMap.remove(key);
            log(String.format("enough Response:\n%s", print));
        }
    }

    public static void main(String[] args) {
        new Thread(new Invoker(3)).start();
    }
}
