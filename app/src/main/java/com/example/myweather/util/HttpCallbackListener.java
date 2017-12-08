package com.example.myweather.util;

/**
 * 网络请求的回调接口
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
