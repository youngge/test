package com.example.yang.test.bean;

/**
 * Created by Administrator on 2017/1/18.
 */

public class ConversationBean {

    int type;
    String content;

    public ConversationBean() {
    }
    public ConversationBean(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
