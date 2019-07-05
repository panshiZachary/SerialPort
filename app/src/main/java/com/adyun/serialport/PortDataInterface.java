package com.adyun.serialport;

/**
 * Created by Zachary
 * on 2019/7/4.
 */
public interface PortDataInterface {
    /**
     * 数据接收
     * @param bytes 接收到的数据
     */
    void onDataReveiced(byte[] bytes);

}
