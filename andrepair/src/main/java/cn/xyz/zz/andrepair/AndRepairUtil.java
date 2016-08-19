package cn.xyz.zz.andrepair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

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
     * 从dex中获得类名 有些手机禁用了。。。。。
     *
     * @param file
     * @return
     */
    public static List<String> getDexClassNames(File file) {
        List<String> classNames = new ArrayList<>();
        try {
            DexFile dexFile = new DexFile(file);
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement();
                classNames.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNames;
    }

    /**
     * 从配置文件中获得类名
     * @param file
     * @return
     */
    public static List<String> getDexClassNamesFromFile(File file) {
        try {
            JarFile jarFile = new JarFile(file);
            ZipEntry entry = jarFile.getEntry("fixClassNames.txt");
            InputStream in = jarFile.getInputStream(entry);
            return getClassNames(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private static List<String> getClassNames(InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> classNames = new ArrayList<>();
        String className;
        while ((className = bufferedReader.readLine()) != null) {
            classNames.add(className);
        }
        return classNames;
    }


}
