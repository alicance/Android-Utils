package com.samego.alic.androidutils.utils.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 包装的响体，处理进度
 * Created by alic on 16-5-20.
 */
public class ProgressResponseBody extends RequestBody {
    //实际的待包装响应体
    private final ResponseBody responseBody;
    //进度回调接口
    private final ProgressResponseListener progressResponseListener;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressResponseListener progressResponseListener) {
        this.responseBody = responseBody;
        this.progressResponseListener = progressResponseListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }
    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return responseBody.contentLength();
    }
    /**
     * 重写进行包装source
     * @return BufferedSource
     * @throws IOException 异常
     */
    public BufferedSource source() throws IOException {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }


    /**
     * 读取，回调进度接口
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {

        return new ForwardingSource(source) {
            //当前读取字节数
            long totalBytesRead = 0L;
            @Override public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1
                progressResponseListener.onResponseProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                return bytesRead;
            }
        };
    }
    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {

    }
}
