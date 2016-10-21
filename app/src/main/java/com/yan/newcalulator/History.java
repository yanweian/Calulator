package com.yan.newcalulator;

/**
 * Created by Yan on 2016/10/18.
 */
public class History {
    private String ques;
    private String result;

    public History(String ques, String result) {
        this.ques = ques;
        this.result = result;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
