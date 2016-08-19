package cn.xyz.zz.andrepair;

import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * Created by zhangzheng on 2016/8/18.
 */
public class PrivateClassLoader extends DexClassLoader {
    private final ZZClassLoader zzClassLoader;
    private final List<String > classnames;

    public PrivateClassLoader(String dexPath, String optimizedDirectory,
                              String libraryPath, ClassLoader parent,
                              ZZClassLoader zzClassLoader,List<String> classnames) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        this.zzClassLoader = zzClassLoader;
        this.classnames = classnames;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if(classnames.contains(className)){
            return super.loadClass(className, resolve);
        }
        return zzClassLoader.loadClass(className,resolve);
    }
}
