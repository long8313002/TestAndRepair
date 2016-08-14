package cn.xyz.zz.testAndrepair;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.alipay.euler.andfix.patch.PatchManager;

import java.io.File;

import cn.xyz.zz.andrepair.AndRepair;
import cn.xyz.zz.andrepair.ReplaceClassInfo;
import cn.xyz.zz.andrepair.ZZClassLoader;

/**
 * Created by Administrator on 2016/8/12.
 */
public class BaseApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        AndRepair.init(this);
//        AndRepair.getInstance().addReplaceClassSolveLoop(new ReplaceClassInfo("android.widget.RadioButton", NewRadioButton.class));
//        AndRepair.getInstance().addReplaceClassSolveLoop(new ReplaceClassInfo("android.widget.Button",NewButton.class));
//        AndRepair.getInstance().addReplaceClassSolveLoop(new ReplaceClassInfo("android.view.View",NewView.class));


        AndRepair.getInstance().addPatch(new File(Environment.getExternalStorageDirectory(), "ppp.apatch").getAbsolutePath());
        AndRepair.getInstance().addDexOrJarPatch(new File(Environment.getExternalStorageDirectory(),"ppp.dex").getAbsolutePath());

        ZZClassLoader classLoader = ZZClassLoader.getClassLoader(new File(Environment.getExternalStorageDirectory(), "ppp.dex").getAbsolutePath());
        try {
            Class<?> aClass = classLoader.loadClass("cn.xyz.zz.testAndrepair.MainActivity");
            Log.i("111","222");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Class<?> aClass2 = classLoader.loadClass("cn.xyz.zz.testAndrepair.MainActivity_CF");
            Class<?>[] classes = aClass2.getClasses();
            Log.i("111","222");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Class<?> aClass3 = classLoader.loadClass("cn.xyz.zz.testAndrepair.MainActivity$1");
            Log.i("111","222");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            Class<?> aClass4 = classLoader.loadClass("cn.xyz.zz.testAndrepair.MainActivity_CF$1");
            Log.i("111","222");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
