package com.lichao.java360;

import com.lichao.java360.service.MyFloatService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    //弹出浮窗小球
    public void startFloat(View view){
    	//开启服务
    	Intent intent = new Intent(this, MyFloatService.class);
    	startService(intent);
    	finish();
    }
}
