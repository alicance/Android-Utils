package com.samego.alic.androidutils.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.samego.alic.androidutils.R;
import com.samego.alic.androidutils.common.AppConfig;
import com.samego.alic.androidutils.utils.OkHttpManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private static final int REQUEST_CODE = 0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initVIew();
//        System.out.println("alic-----------"+StorageUtils.getCacheDirectory(this).toString());
        initTest();
    }

    public void initVIew() {
        imageView = (ImageView) findViewById(R.id.imageVIew);
        //drawable
        ImageLoader.getInstance().displayImage("drawable://" + R.drawable.app_icon, imageView, AppConfig.imageOptions());
        //url
//        ImageLoader.getInstance().displayImage("http://home.sise.cn/img/LOGO.png", imageView, AppConfig.imageOptions());
    }

    public void initTest() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //同步 返回Response
                Response response = OkHttpManager.executeSync("http://www.baidu.com");
                if (response != null) {
                    Log.d("Test", "this is okay!");
                    try {
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    Log.e("Test", "this is error!");

                //同步处理 get String
                String string = OkHttpManager.executeSyncString("http://home.sise.cn");
                if (string != null)
                    System.out.println(string);

                //异步处理 结果是我的装备
                OkHttpManager.enqueueAsync("http://home.sise.cn", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });

                //异步处理 结果我才不鸟它呢
                OkHttpManager.enqueueAsync("http://home.sise.cn");


                //post异步处理 结果是我的装备
                FormBody body = new FormBody.Builder()
                        .add("username", "alic")
                        .add("password", "alic")
                        .build();
                OkHttpManager.postEnqueueAsync("http://172.16.168.35:1010/login.php", body, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("Test", response.body().string());
                    }
                });

                HashMap<String, String> mapData = new HashMap<>();
                mapData.put("username", "alic");
                mapData.put("password", "alic");

                File file1 = new File("/storage/sdcard1/1.png");
                File file2 = new File("/storage/sdcard1/scs.jpg");
                HashMap<String, File> mapFile = new HashMap<>();
                mapFile.put("screen1", file1);
                mapFile.put("screen2", file2);
                OkHttpManager.postFormAsync("http://172.16.168.35:1010/upload.php", mapData, mapFile, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("form-" + response.body().string());
                    }
                });

                //上传一个文件
                OkHttpManager.uploadFile("http://172.16.168.35:1010/upload.php", file1, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
                
                //原生的OkHttp方法 参数
                Request request = new Request.Builder().build();
                //原生的OkHttp方法 同步请求
                OkHttpManager.execute(request);
                //原生的OkHttp方法 异步请求 没回调
                OkHttpManager.enqueue(request);
                //原生的OkHttp方法 异步请求 有回调
                OkHttpManager.enqueue(request, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }



        }).start();

    }

}
