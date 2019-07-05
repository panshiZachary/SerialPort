package com.adyun.serialport;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Zachary
 * on 2019/7/4.
 */
public abstract class SerialPortReadThread extends Thread {
    private FileInputStream mFileInputStream;
    private byte[] mReadBuffer;

    public boolean isInterrupted = false;

    public void setInterrupted(boolean interrupted) {
        isInterrupted = interrupted;
    }

    public SerialPortReadThread(FileInputStream mFileInputStream) {
        this.mFileInputStream = mFileInputStream;
        this.mReadBuffer = new byte[1024];
    }

    @Override
    public void run() {
        while (!isInterrupted){
            try {
                int size  = mFileInputStream.read(mReadBuffer);
                if (size==-1 || size <=0){
                    return;
                }
                byte[] readBytes = new byte[size];
                System.arraycopy(mReadBuffer,0,readBytes,0,size);
                onDataReceive(readBytes);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    public abstract void onDataReceive(byte[] readBytes) ;
}
