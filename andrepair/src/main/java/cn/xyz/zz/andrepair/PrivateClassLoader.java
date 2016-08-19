package cn.xyz.zz.andrepair;

import android.annotation.TargetApi;
import android.os.Build;

import dalvik.system.DexClassLoader;

/**
 * Created by zhangzheng on 2016/8/18.
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class PrivateClassLoader extends DexClassLoader {
    private final String dexPath;
    private final String optimizedDirectory;
    private final String libraryPath;
    private final ClassLoader parent;
    private final ZZClassLoader zzClassLoader;

    public PrivateClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent, ZZClassLoader zzClassLoader) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        this.dexPath = dexPath;
        this.optimizedDirectory = optimizedDirectory;
        this.libraryPath = libraryPath;
        this.parent = parent;
        this.zzClassLoader = zzClassLoader;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if(className.equals("cn.xyz.zz.testAndrepair.MainActivity")
                ||className.equals("cn.xyz.zz.testAndrepair.MainActivity$1")){
            return super.loadClass(className, resolve);
        }
        if(className.equals("cn.jiajixin.nuwa.Hack")){
            return Class.class;
        }
        return zzClassLoader.loadClass(className,resolve);
    }
}
