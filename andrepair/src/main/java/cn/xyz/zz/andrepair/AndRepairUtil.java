package cn.xyz.zz.andrepair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by Administrator on 2016/8/14.
 */
public class AndRepairUtil {
    /**
     * 获取应用版本号
     *
     * @param context
     * @return 版本号
     */
    public static String getAppVersion(Context context) {
        String versionName = "1.0.0";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获得dex中的类名
     * @param file
     * @return
     */
    public static List<String> getDexClassNames(File file){
        List<String> classNames = new ArrayList<>();
        try {
            DexFile dexFile = new DexFile(file);
            Enumeration<String> entries = dexFile.entries();
            while(entries.hasMoreElements()){
                String name=  entries.nextElement();
                classNames.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }
}
