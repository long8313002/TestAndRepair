package cn.xyz.zz.andrepair;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2016/8/14.
 */
public class ZZClassLoader extends DexClassLoader {

    private static final HashMap<String, ZZClassLoader> mPatchinClassLoaders = new HashMap<>();
    private static  Context context ;
    public static File dexOutputDir ;

    public static void init(Context context){
        ZZClassLoader.context = context;
        dexOutputDir = context.getDir("patch", Context.MODE_PRIVATE);
    }

    protected ZZClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }

    public static void clearPluginClassLoaders(){
        mPatchinClassLoaders.clear();
        deleteFileList(dexOutputDir);
    }

    private static void deleteFileList(File file) {
        if(file==null){
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f:files){
                deleteFileList(f);
            }
        } else {
            file.delete();
        }
    }


    public static ZZClassLoader getClassLoader(String dexPath) {
        ZZClassLoader dLClassLoader = mPatchinClassLoaders.get(dexPath);
        if (dLClassLoader != null)
            return dLClassLoader;

        ClassLoader parentLoader = context.getClassLoader();
        while (parentLoader!=null){
            ClassLoader parent = parentLoader.getParent();
            if(parent==null){
                break;
            }
            parentLoader = parent;
        }

        if(!new File(dexPath).exists()){
            return null;
        }

        final String dexOutputPath = dexOutputDir.getAbsolutePath();
        if(!dexOutputDir.exists()){
            dexOutputDir.mkdir();
        }
        dLClassLoader = new ZZClassLoader(dexPath, dexOutputPath, null, parentLoader);
        mPatchinClassLoaders.put(dexPath, dLClassLoader);

        return dLClassLoader;
    }
}
