
package cn.xyz.zz.andrepair;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.os.Looper;
import android.util.Log;


import dalvik.system.DexFile;

class AndFixManager {
    private static final String TAG = "AndFixManager";
    private static final String DIR = "apatch_opt";
    private final Context mContext;
    private boolean mSupport;

    private SecurityChecker mSecurityChecker;
    private File mOptDir;
    private boolean needCheckerSecurity = false;

    public AndFixManager(Context context) {
        mContext = context;
        mSupport = Compat.isSupport();
        if (mSupport) {
            mSecurityChecker = new SecurityChecker(mContext);
            mOptDir = new File(mContext.getFilesDir(), DIR);
            if (!mOptDir.exists() && !mOptDir.mkdirs()) {// make directory fail
                mSupport = false;
                Log.e(TAG, "opt dir create error.");
            } else if (!mOptDir.isDirectory()) {// not directory
                mOptDir.delete();
                mSupport = false;
            }
        }
    }

    public synchronized void removeOptFile(File file) {
        File optfile = new File(mOptDir, file.getName());
        if (optfile.exists() && !optfile.delete()) {
            Log.e(TAG, optfile.getName() + " delete error.");
        }
    }

    public List<ReplaceClassInfo> fix(final File file, final List<String> classes) {

        List<ReplaceClassInfo> infos = new ArrayList<>();
        if (!mSupport) {
            return infos;
        }

        if (needCheckerSecurity && !mSecurityChecker.verifyApk(file)) {
            return infos;
        }

        ZZClassLoader classLoader = ZZClassLoader.getClassLoader(file.getAbsolutePath());
        if(classLoader==null){
            return infos;
        }

        for (String className:classes){
            try {
                Class<?> aClass = classLoader.loadClass(className);
                if(aClass==null){
                    continue;
                }
                infos.add(fixClass(aClass));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return infos;
    }

    private ReplaceClassInfo fixClass(Class<?> clazz) {
        String className = clazz.getName().replace("_CF", "");
        return new ReplaceClassInfo(className, clazz);
    }

}
