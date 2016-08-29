package com.android.oz.myleakactiviy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_image;
    /**
     * 声明自定义的handler
     **/
    private MyHandler myHandler = new MyHandler(this);

    /**
     * 定义的标识位,判断MyThread中的循环是否执行
     **/
    private static boolean mIsRunning = false;

    /**
     * 将标识位设置成false跳出循环
     **/
    public void setmIsRunning() {
        mIsRunning = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_image = (ImageView) findViewById(R.id.iv_image);

        MyThread myThread = new MyThread(this);
        myThread.start();

    }


    private static class MyHandler extends Handler {

        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (weakReference != null && weakReference.get() != null) {
                        weakReference.get().iv_image.setImageResource(R.mipmap.ic_launcher);
                    }
                    break;
            }
        }
    }

    private static class MyThread extends Thread {

        private WeakReference<MainActivity> weakReference;

        public MyThread(MainActivity mainActivity) {
            weakReference = new WeakReference<MainActivity>(mainActivity);
        }

        @Override
        public void run() {

            mIsRunning = true;

            for (int i = 0; i < 20; i++) {
                if (!mIsRunning) {
                    break;
                }
                Log.v("Oz", "运行-->" + i + "次");
                SystemClock.sleep(2000);
                if (weakReference != null && weakReference.get() != null) {
                    //Message message = mHandler.obtainMessage();
                    //message.what = 0;
                    //mHandler.sendMessageDelayed(message, 5000);

                    Message message = weakReference.get().myHandler.obtainMessage();
                    message.what = 0;
                    weakReference.get().myHandler.sendMessageDelayed(message, 5000);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将标识位设置成false
        setmIsRunning();
        // 清空message中的消息
        myHandler.removeCallbacksAndMessages(null);
    }
}


