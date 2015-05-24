package cn.edu.fudan.se.messager;


import cn.edu.fudan.se.Parameter;
import cn.edu.fudan.se.bean.Lecture;
import cn.edu.fudan.se.bean.LectureResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by Dawnwords on 2015/5/23.
 */
public class Responsor extends Messager implements Runnable{
    private int responsorId;

    public Responsor(int responsorId) {
        super(Parameter.TOPIC, Parameter.RESPONSOR_CONSUMER_GROUP, Parameter.RESPONSOR_PRODUCER_GROUP);
        this.responsorId = responsorId;
    }

    @Override
    protected boolean onReceiveMessage(String messageId, Object messageBody) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        LectureResponse response = new LectureResponse(messageId, Arrays.asList(
                new Lecture("软件工程-" + responsorId, "PX", 60),
                new Lecture("离散数学-" + responsorId, "ZYM", 60),
                new Lecture("数据结构-" + responsorId, "HWL", 60)
        ));
        sendMessage(Parameter.RESPONSE_TAG, Parameter.RESPONSOR_KEY, response);
        return true;
    }

    @Override
    public void run() {
        start(Parameter.REQUEST_TAG);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                if ("stop".equals(reader.readLine())) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    public static void main(String[] args) {
        new Thread(new Responsor((int) (System.currentTimeMillis()%1000))).start();
    }
}
