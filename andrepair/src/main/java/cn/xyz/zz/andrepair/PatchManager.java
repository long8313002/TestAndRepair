package cn.xyz.zz.andrepair;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class PatchManager {
    private static final String TAG = "PatchManager";
    private static final String SUFFIX = ".apatch";
    private static final String DIR = "apatch";
    private static final String SP_NAME = "_andrepair_";
    private static final String SP_VERSION = "version";

    private final Context mContext;
    private AndFixManager mAndFixManager;
    private final File mPatchDir;
    private final List<Patch> mPatchs;
    private final Map<String, ClassLoader> mLoaders;

    public PatchManager(Context context) {
        mContext = context;
        mAndFixManager = new AndFixManager(mContext);
        mPatchDir = new File(mContext.getFilesDir(), DIR);
        mPatchs = new ArrayList<>();
        mLoaders = new ConcurrentHashMap<>();
    }

    public void init(String appVersion) {
        if (!mPatchDir.exists() && !mPatchDir.mkdirs()) {// make directory fail
            Log.e(TAG, "patch dir create error.");
            return;
        } else if (!mPatchDir.isDirectory()) {// not directory
            mPatchDir.delete();
            return;
        }
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        String ver = sp.getString(SP_VERSION, null);
        if (ver == null || !ver.equalsIgnoreCase(appVersion)) {
            cleanPatch();
            sp.edit().putString(SP_VERSION, appVersion).commit();
        } else {
            initPatchs();
        }
    }

    private void initPatchs() {
        File[] files = mPatchDir.listFiles();
        for (File file : files) {
            addPatch(file);
        }
    }

    private Patch addPatch(File file) {
        Patch patch = null;
        try {
            patch = new Patch(file);
            mPatchs.add(patch);
        } catch (IOException e) {
            Log.e(TAG, "addPatch", e);
        }
        return patch;
    }

    private void cleanPatch() {
        File[] files = mPatchDir.listFiles();
        for (File file : files) {
            mAndFixManager.removeOptFile(file);
            if (!FileUtil.deleteFile(file)) {
                Log.e(TAG, file.getName() + " delete error.");
            }
        }
    }

    public List<ReplaceClassInfo> addPatch(String path) {
        List<ReplaceClassInfo> infos = new ArrayList<>();
        File src = new File(path);
        File dest = new File(mPatchDir, src.getName());
        if (!src.exists()) {
            return infos;
        }
        if (dest.exists()) {
            Log.d(TAG, "patch [" + path + "] has be loaded.");
            return infos;
        }
        try {
            FileUtil.copyFile(src, dest);// copy to patch's directory
        } catch (IOException e) {
            e.printStackTrace();
        }
        Patch patch = addPatch(dest);
        if (patch != null) {
            infos.addAll(loadPatch(patch));
        }
        return infos;
    }

    public void removeAllPatch() {
        cleanPatch();
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME,
                Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    public List<ReplaceClassInfo> loadPatch() {
        List<ReplaceClassInfo> infos = new ArrayList<>();
        mLoaders.put("*", mContext.getClassLoader());// wildcard
        Set<String> patchNames;
        List<String> classes;
        for (Patch patch : mPatchs) {
            patchNames = patch.getPatchNames();
            for (String patchName : patchNames) {
                classes = patch.getClasses(patchName);
                infos.addAll(mAndFixManager.fix(patch.getFile(), classes));
            }
        }
        return infos;
    }

    private List<ReplaceClassInfo> loadPatch(Patch patch) {
        List<ReplaceClassInfo> infos = new ArrayList<>();
        Set<String> patchNames = patch.getPatchNames();
        ClassLoader cl;
        List<String> classes;
        for (String patchName : patchNames) {
            if (mLoaders.containsKey("*")) {
                cl = mContext.getClassLoader();
            } else {
                cl = mLoaders.get(patchName);
            }
            if (cl != null) {
                classes = patch.getClasses(patchName);
                infos.addAll(mAndFixManager.fix(patch.getFile(), classes));
            }
        }
        return infos;
    }

}
