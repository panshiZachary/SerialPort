package com.adyun.serialport;

import java.io.File;
import java.io.IOException;

/**
 * Created by Zachary
 * on 2019/7/4.
 */
class Utils {
    public static boolean chmod777(File file) {
        if (file == null || !file.exists()){
            // 文件不存在
            return false;
        }

        try {
            // 获取 root 权限
            Process su = Runtime.getRuntime().exec("system/bin/su");
            // 修改文件属性 （ 可读 可写 可执行）
            String chmod = "chmod 777" + file.getAbsolutePath() + "\n" + "exit\n";
            su.getOutputStream().write(chmod.getBytes());
            if (0 == su.waitFor() && file.canRead() && file.canWrite() && file.canExecute()){
                return true;
            }
         } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;


    }
}
