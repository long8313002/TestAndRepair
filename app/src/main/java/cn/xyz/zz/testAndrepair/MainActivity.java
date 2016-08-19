package cn.xyz.zz.testAndrepair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import cn.xyz.zz.andrepair.ZZClassLoader;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        initViews();
    }

    private void initViews() {
        Button bt1 = (Button) findViewById(R.id.bt1);
        Button bt2 = (Button) findViewById(R.id.bt2);
        Button bt3 = (Button) findViewById(R.id.bt3);
        Button bt4 = (Button) findViewById(R.id.bt4);
        Button bt5 = (Button) findViewById(R.id.bt5);

        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("onClick5");
            }
        });

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        bt4.setOnClickListener(this);
//        bt5.setOnClickListener(this);

    }

    public void onClick1(View view){
        Log.i("point","onClick1");
//        toast("onClick1");
//        new HaHa().say(this);
    }

    public void onClick2(View view){
        Log.i("point", "onClick2");
//        toast("onClick2");
    }

    public void onClick3(View view){
        Log.i("point", "onClick3");
        toast("onClick3");
    }

    public void onClick4(View view){
        Log.i("point","onClick4");
        toast("onClick4");
    }

    public void onClick5(View view){
        Log.i("point","onClick5");
        toast("onClick5");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                onClick1(v);
                break;
            case R.id.bt2:
                onClick2(v);
                break;
            case R.id.bt3:
                onClick3(v);
                break;
            case R.id.bt4:
                onClick4(v);
                break;
            case R.id.bt5:
                onClick5(v);
                break;
        }
    }

    private void toast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
