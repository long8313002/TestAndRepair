package cn.xyz.zz.andrepair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

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
}
