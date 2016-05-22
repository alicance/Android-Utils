package com.samego.alic.androidutils.utils.okhttp;

/**
 * 响应体进度回调接口，比如用于文件下载中
 * Created by alic on 16-5-20.
 */
public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
