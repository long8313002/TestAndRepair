package cn.xyz.zz.andrepair;

import android.util.Log;

/**
 * 负责log打印
 * Created by 张政 on 2016/8/13.
 */
class AndRepairLog {

    private static final String LOGTAG = "AndRepair";
    private static boolean closeLog;

    public static void closeLog() {
        closeLog = true;
    }

    public static void error(String errMessage) {
        if (closeLog) {
            return;
        }
        Log.e(LOGTAG, errMessage);
    }

    public static void info(String infoMessage) {
        if (closeLog) {
            return;
        }
        Log.i(LOGTAG, infoMessage);
    }
}
