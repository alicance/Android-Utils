package com.samego.alic.androidutils.utils.okhttp;

/**
 *
 * Created by alic on 16-5-20.
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
