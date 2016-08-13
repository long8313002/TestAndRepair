package cn.xyz.zz.testAndrepair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.textView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void onClick1(View view){
        Log.i("point","onClick1");
        startActivity(new Intent(this,MainActivity.class));
    }

    public void onClick2(View view){
        Log.i("point","onClick2");
    }

    public void onClick3(View view){
        Log.i("point","onClick3");
    }

    public void onClick4(View view){
        Log.i("point","onClick4");
    }

    public void onClick5(View view){
        Log.i("point","onClick5");
    }

    public void onClick6(View view){
        Log.i("point", "onClick6");
    }

    public void onClick7(View view){
        Log.i("point", "onClick7");
    }

    public void onClick8(View view){
        List<String> aaa = new ArrayList<>();
        Toast.makeText(this,"hahha"+aaa.size(),1).show();
        Log.i("point","onClick8");
    }

}
