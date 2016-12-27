package com.example.yang.test.net;

import java.util.List;

/**
 * Created by czy on 2016/12/11.
 */

public interface IRequestManager {

    void get(String url, IRequestCallback requestCallback);

//    void post(String url, String requestBodyJson, IRequestCallback requestCallback);
    void post(String url, List<Param> params, IRequestCallback requestCallback);

    void put(String url, String requestBodyJson, IRequestCallback requestCallback);

    void delete(String url, String requestBodyJson, IRequestCallback requestCallback);

    /**
     * post请求参数类
     */
    public static class Param {

        String key;//请求的参数
        String value;//参数的值

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }
}
