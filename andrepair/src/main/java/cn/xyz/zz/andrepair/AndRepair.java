package cn.xyz.zz.andrepair;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张政 on 2016/8/13.
 */
public class AndRepair {

    private static AndRepair andRepair;

    private AndRepairClassLoader andRepairClassLoader;
    private List<ReplaceClassInfo> replaceClassInfos = new ArrayList<>();
    private Application application;
    private PatchManager manager;

    public static void init(Application application) {
        if (andRepair != null) {
            return;
        }
        andRepair = new AndRepair(application);
        andRepair.initPatch();
    }

    private void initPatch(){
        try {
            PackageManager packageManager = application.getPackageManager();
            String packageName = application.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String appversion= packageInfo.versionName;
            manager=new PatchManager(application);
            manager.init(appversion);
            manager.loadPatch();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addPatch(String filePath){
         boolean isSucess=false;
        try {
            if(manager != null){
                manager.addPatch(filePath);
                isSucess = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!isSucess){
            AndRepairLog.error("加载补丁失败");
        }
    }

    public static AndRepair getInstance(){
        if(andRepair == null){
            throw new RuntimeException("请首先调用init方法");
        }
        return andRepair;
    }

    private AndRepair(Application application) {
        this.application = application;
        try {
            andRepairClassLoader = replaceClassLoader(application);
        } catch (Exception e) {
            AndRepairLog.error("热修复启动失败");
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
     * @param infos
     */
    public void addReplaceClassSolveLoop(ReplaceClassInfo... infos){
        if(infos == null||infos.length==0){
            return;
        }
        for (ReplaceClassInfo info:infos){
            AndRepair.getInstance().loadCustomaryClass(info.getClassName());
            replaceClassInfos.add(info);
        }
    }

    /**
     * 替换类
     * @param infos
     */
    public void addReplaceClass(ReplaceClassInfo... infos) {
        if(infos == null||infos.length==0){
            return;
        }
        for (ReplaceClassInfo info:infos){
            replaceClassInfos.add(info);
        }
    }

    public void addReplaceClass(List<ReplaceClassInfo> infos) {
        if(infos == null||infos.size()==0){
            return;
        }
        replaceClassInfos.addAll(infos);
    }

    public Class loadCustomaryClass(String className){
        return andRepairClassLoader.loadCustomaryClass(className);
    }

    /**
     * 关闭热修复打印出的log
     */
    public void closeAndRepairLog() {
        AndRepairLog.closeLog();
    }
}
