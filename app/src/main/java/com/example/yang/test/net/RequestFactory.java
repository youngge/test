package com.example.yang.test.net;

/**
 * Created by czy on 2016/12/11.
 */

public class RequestFactory {

    public static IRequestManager getRequestManager(){
        return OkHttpRequestManager.getInstance();
    }

}
