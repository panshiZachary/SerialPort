package com.adyun.serialport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity  implements PortDataInterface{


    private static final String TAG = "MainActivity";
    EditText edit_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_content = findViewById(R.id.edit_content);


    }



    /**
     * 打开串口
     * @param view
     */
    public void open(View view) {
        SerialPortManager.getInstance().openSerialPort("/dev/ttySAC2", 115200);
        SerialPortManager.getInstance().regist(this);
    }

    /**
     * 发送数据
     * @param view
     */
    public void send(View view) {
        String command = edit_content.getText().toString().trim();

        if (TextUtils.isEmpty(command)) {
            return;
        }
        byte[] sendContentBytes = command.getBytes();
        SerialPortManager.getInstance().putCommand(sendContentBytes);

    }

    @Override
    public void onDataReveiced(byte[] bytes) {
        Log.i(TAG, "onDataReveiced: "+new String(bytes));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SerialPortManager.getInstance().unregist(this);
    }
}
