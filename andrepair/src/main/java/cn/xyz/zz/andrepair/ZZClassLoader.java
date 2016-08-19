package cn.xyz.zz.andrepair;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by 张政 on 2016/8/14.
 */
 public class ZZClassLoader extends DexClassLoader {

    private static final HashMap<String, ZZClassLoader> mPatchinClassLoaders = new HashMap<>();
    private static Context context;
    public static File dexOutputDir;
    private PrivateClassLoader privayeClassLoader ;

    public static void init(Context context) {
        if (ZZClassLoader.context != null) {
            return;
        }
        ZZClassLoader.context = context;
        dexOutputDir = context.getDir("patch", Context.MODE_PRIVATE);
    }

    protected ZZClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        privayeClassLoader = new PrivateClassLoader(dexPath,optimizedDirectory,libraryPath,getBootClassLoader(),this);
    }

    public static void clearPluginClassLoaders() {
        mPatchinClassLoaders.clear();
        deleteFileList(dexOutputDir);
    }

    private static void deleteFileList(File file) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFileList(f);
            }
        } else {
            file.delete();
        }
    }


    public static ZZClassLoader getClassLoader(String dexPath) {
//        ZZClassLoader dLClassLoader = mPatchinClassLoaders.get(dexPath);
//        if (dLClassLoader != null)
//            return dLClassLoader;

        ClassLoader parentLoader = context.getClassLoader();

        if (!new File(dexPath).exists()) {
            return null;
        }

        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        if (!dexOutputDir.exists()) {
            dexOutputDir.mkdir();
        }
        ZZClassLoader dLClassLoader = new ZZClassLoader(dexPath, dexOutputPath, null, parentLoader);
        mPatchinClassLoaders.put(dexPath, dLClassLoader);

        return dLClassLoader;
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        if(className.equals("cn.xyz.zz.testAndrepair.MainActivity")){
            return privayeClassLoader.loadClass(className,resolve);
        }
        if(className.equals("cn.xyz.zz.testAndrepair.MainActivity$1")){
            return privayeClassLoader.loadClass(className,resolve);
        }
        if(className.equals("cn.jiajixin.nuwa.Hack")){
            return Class.class;
        }
        return super.loadClass(className,resolve);
    }

    private void setParent(ClassLoader loader,ClassLoader parent){
        if(loader.getParent()==parent){
            return;
        }

        Field field = null;
        Class clazz = loader.getClass();
        while (field == null) {
            try {
                field = clazz.getDeclaredField("parent");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }
        field.setAccessible(true);
        try {
            field.set(loader,parent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static ClassLoader getBootClassLoader(){
        ClassLoader classLoader = context.getClassLoader();
        while(true){
            ClassLoader parent = classLoader.getParent();
            if(parent==null){
                return classLoader;
            }
            classLoader = parent;
        }
    }
}
