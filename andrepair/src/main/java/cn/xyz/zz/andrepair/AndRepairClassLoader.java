package cn.xyz.zz.andrepair;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 自定义的类加载器
 * Created by 张政 on 2016/8/13.
 */
public class AndRepairClassLoader extends ClassLoader {

    private final ClassLoader userClassLoader;
    private final Set<ReplaceClassInfo> replaceClassInfos;
    private boolean needLoadRepairClass = true;
    private List<ZZClassLoader> zzClassLoaders = new ArrayList<>();

    public void setZZClassLoader(ZZClassLoader zzClassLoader) {
        this.zzClassLoaders.add(zzClassLoader);
    }

    public AndRepairClassLoader(ClassLoader parentLoader, ClassLoader userClassLoader, Set<ReplaceClassInfo> replaceClassInfos) {
        super(parentLoader);
        this.userClassLoader = userClassLoader;
        this.replaceClassInfos = replaceClassInfos;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        Class<?> repairClass = null;
        boolean repairPager = className.contains("cn.xyz.zz.andrepair");
        if (needLoadRepairClass&&!repairPager) {
            repairClass = loadRepairClass(className, resolve);
        }
        return repairClass == null ? super.loadClass(className, resolve) : repairClass;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        return super.findClass(className);
    }

    public Class<?> loadRepairClass(String className, boolean resolve) {
        Class clazz = null;
        try {
            clazz = getClassFromReplaceClassInfo(className);
            className = clazz == null ? className : clazz.getName();
            Class<?> classFromDexClassLoader = getClassFromDexClassLoader(className);
            if (classFromDexClassLoader != null) {
                clazz = classFromDexClassLoader;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return clazz;
    }

    private Class<?> getClassFromReplaceClassInfo(String className) {
        for (ReplaceClassInfo info : replaceClassInfos) {
            if (info.getClassName().equals(className)) {
                return info.getReplaceClass();
            }
        }
        return null;
    }

    private Class<?> getClassFromDexClassLoader(String className) throws ClassNotFoundException {
        if (zzClassLoaders == null||zzClassLoaders.size()==0) {
            return null;
        }
        String finalClassName = className.replaceAll("_CF","");
        for (ZZClassLoader zzClassLoader:zzClassLoaders) {
            try {
                return zzClassLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(className.equals(finalClassName)){
            return null;
        }

        for (ZZClassLoader zzClassLoader:zzClassLoaders) {
            try {
                return zzClassLoader.loadClass(finalClassName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 加载原来的类 非补丁类
     *
     * @param className
     * @return
     */
    public Class<?> loadCustomaryClass(String className) {
        try {
            needLoadRepairClass = false;
            return userClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            needLoadRepairClass = true;
        }
        return null;
    }

}
