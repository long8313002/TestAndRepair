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

//        AndRepair.getInstance().addPatch(new File(Environment.getExternalStorageDirectory(), "ppp.apatch").getAbsolutePath());
    }

}
