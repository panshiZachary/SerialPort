package com.adyun.serialport;

import androidx.core.app.NavUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Zachary
 * on 2019/7/4.
 */
public class SerialPortManager {
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private List<PortDataInterface> observable;
    private LinkedBlockingDeque<byte[]> queue = new LinkedBlockingDeque<>();
    Executor executor = Executors.newSingleThreadExecutor();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final SerialPortManager ourInstance = new SerialPortManager();

    public static SerialPortManager getInstance() {
        return ourInstance;
    }

    private SerialPortManager() {
        observable = new ArrayList<>();
    }
    // 观察者 模式
    public void regist(PortDataInterface portDataInterface){
        if (portDataInterface != null){
            observable.add(portDataInterface);
        }
    }
    public void unregist(PortDataInterface portDataInterface){
        if (portDataInterface != null){
            observable.remove(portDataInterface);
        }
    }
    public void update(byte[] content){
        for (PortDataInterface portDataInterface : observable) {
            portDataInterface.onDataReveiced(content);
        }


    }




    public void openSerialPort(String path, int baudRate) {
        File file = new File(path);
        // 获取 root 权限
        Utils.chmod777(file);

        FileDescriptor mFd = open(path,baudRate);
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);

        if (mFd!=null){
            startReadThread();
            executor.execute(taskCenterRunnable);
        }
    }

    private void startReadThread() {
        SerialPortReadThread serialPortReadThread = new SerialPortReadThread(mFileInputStream){
            @Override
            public void onDataReceive(byte[] readBytes) {
                switch (ActivityStackState.getActivityState()){
                    case 1:
                        break;
                }
                update(readBytes);

            }
        };
        serialPortReadThread.start();

    }



    public native FileDescriptor open(String path, int baudRate) ;


    public void putCommand(byte[] commond) {
        try {queue.put(commond);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private Runnable taskCenterRunnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                byte[] content = null;
                try {
                    content = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (content !=null){
                    try {
                        mFileOutputStream.write(content);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


        }
    };

}
