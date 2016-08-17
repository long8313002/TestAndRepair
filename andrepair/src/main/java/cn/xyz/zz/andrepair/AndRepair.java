package cn.xyz.zz.andrepair;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 张政 on 2016/8/13.
 */
public class AndRepair {

    private static AndRepair andRepair;

    private AndRepairClassLoader andRepairClassLoader;
    private Set<ReplaceClassInfo> replaceClassInfos = new LinkedHashSet<>();
    private Application application;
    private PatchManager manager;

    public static void init(Application application) {
        initLoadClass(Class.class,Set.class,LinkedHashSet.class,Iterator.class,List.class);
        ZZClassLoader.init(application);
        if (andRepair != null) {
            return;
        }
        andRepair = new AndRepair(application);
        andRepair.initPatch();
    }

    private static void initLoadClass(Class... clazz){}

    private void initPatch() {
        String appversion = AndRepairUtil.getAppVersion(application);
        manager = new PatchManager(application);
        manager.init(appversion);
        replaceClassInfos.addAll(manager.loadPatch());
    }

    public Context getContext() {
        return application;
    }

    public void addPatch(String filePath) {
        List<ReplaceClassInfo> infos = manager.addPatch(filePath);
        replaceClassInfos.addAll(infos);
    }

    public void addPatchFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String fileName = URLEncoder.encode(url);
        File patchFile = new File(getContext().getExternalFilesDir(null), fileName);
        new HttpDownLoader().downLoad(url, patchFile.getAbsolutePath(), true, new HttpDownLoader.HttpDownLoaderListener() {
            @Override
            public void compute(boolean isSucess, String savePath) {
                if (!isSucess) {
                    return;
                }
                addPatch(savePath);
            }
        });
    }

    public static AndRepair getInstance() {
        if (andRepair == null) {
            throw new RuntimeException("need to initialize");
        }
        return andRepair;
    }

    private AndRepair(Application application) {
        this.application = application;
        try {
            andRepairClassLoader = replaceClassLoader(application);
        } catch (Exception e) {
            AndRepairLog.error("andrepair error");
        }
    }

    /**
     * 替换掉系统默认的类加载器
     *
     * @param application
     * @throws Exception
     */
    private AndRepairClassLoader replaceClassLoader(Application application) throws Exception {
        ClassLoader classLoader = application.getClassLoader();
        ClassLoader parent = classLoader.getParent();

        Field field = null;
        Class clazz = classLoader.getClass();
        while (field == null) {
            try {
                field = clazz.getDeclaredField("parent");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }
        field.setAccessible(true);
        AndRepairClassLoader andRepairClassLoader = new AndRepairClassLoader(parent, classLoader, replaceClassInfos);
        field.set(classLoader, andRepairClassLoader);
        return andRepairClassLoader;
    }

    /**
     * 当本身的类中包含替换的类，需要调用本方法 否则会造成死循环
     *
     * @param infos
     */
    public void addReplaceClassSolveLoop(ReplaceClassInfo... infos) {
        if (infos == null || infos.length == 0) {
            return;
        }
        for (ReplaceClassInfo info : infos) {
            AndRepair.getInstance().loadCustomaryClass(info.getClassName());
            replaceClassInfos.add(info);
        }
    }

    /**
     * 替换类
     *
     * @param infos
     */
    public void addReplaceClass(ReplaceClassInfo... infos) {
        if (infos == null || infos.length == 0) {
            return;
        }
        for (ReplaceClassInfo info : infos) {
            replaceClassInfos.add(info);
        }
    }

    public void addReplaceClass(List<ReplaceClassInfo> infos) {
        if (infos == null || infos.size() == 0) {
            return;
        }
        replaceClassInfos.addAll(infos);
    }

    public Class loadCustomaryClass(String className) {
        return andRepairClassLoader.loadCustomaryClass(className);
    }

    public void removeAllPatch() {
        manager.removeAllPatch();
    }

    /**
     * 关闭热修复打印出的log
     */
    public void closeAndRepairLog() {
        AndRepairLog.closeLog();
    }

    public Set<ReplaceClassInfo> getReplaceClassInfos(){
        return replaceClassInfos;
    }
}
