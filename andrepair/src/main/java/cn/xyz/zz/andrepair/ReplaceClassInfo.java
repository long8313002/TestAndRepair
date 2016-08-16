package cn.xyz.zz.andrepair;

/**
 * 替换类的信息
 * Created by 张政 on 2016/8/13.
 */
public class ReplaceClassInfo {

    private final String needReplaceClassName;
    private final Class replaceClass;

    public ReplaceClassInfo(String needReplaceClassName,Class replaceClass){

        this.needReplaceClassName = needReplaceClassName;
        this.replaceClass = replaceClass;
    }

    @Override
    public int hashCode() {
        return replaceClass.hashCode()+needReplaceClassName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ReplaceClassInfo)){
            return false;
        }
        return needReplaceClassName.equals(((ReplaceClassInfo) o).needReplaceClassName)
                &&replaceClass==((ReplaceClassInfo) o).replaceClass;
    }

    public String getClassName(){
        return needReplaceClassName;
    }

    public Class getReplaceClass(){
        return replaceClass;
    }
}
