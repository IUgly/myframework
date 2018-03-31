package org.redrock.framework.core;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.redrock.framework.annotation.Component;
import org.redrock.framework.annotation.Controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClassLoader {

    private Set<Class<?>> classSet;
    private Set<Class<?>> controllerSet;
    private Set<Class<?>> componentSet;

    public Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources
                    (packageName.replaceAll("\\.","/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if (url != null){
                    if (protocol.equals("file")){
                        String packagePath = url.getPath();
                        addClass(classSet, packagePath, packageName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadComponent();
        loadControllerSet();
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath,
                                 String packageName){
        File[] files = new File(packagePath).listFiles(pathname -> pathname.isDirectory() ||
                (pathname.isFile() && pathname.getName().endsWith(".class")));
        for (File file : files){
            String fileName = file.getName();
            if (file.isFile()){
                if (packageName !=null && !packageName.equals("")){
                    String className = packageName + "." +fileName.substring
                            (0,fileName.lastIndexOf("."));
                }
                Class<?> clazz = getClass(fileName);
                classSet.add(clazz);
            }
            else {
                String subPackagePath = fileName;
                if (packagePath != null &&!packagePath.equals("")){
                    subPackagePath = packagePath + "/" +subPackagePath;
                }
                String subPackageName = fileName;
                if (packageName != null&&!packageName.equals("")){
                    subPackageName = packageName + "." +subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }
    private static Class<?> getClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

public static Class<?> loadClass(String className, boolean isInitialized){
        Class<?> cls = null;
        try {
            cls = Class.forName(className, isInitialized,
                    Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }
    private void loadComponent(){
    componentSet = new HashSet<>();
    if (classSet != null){
        for (Class<?> clazz : classSet){
            if (clazz.getAnnotation(Component.class) != null){
                componentSet.add(clazz);
            }
        }
    }
    }

    private void loadControllerSet(){
        controllerSet = new HashSet<>();
        if (classSet != null){
            for (Class<?> clazz : classSet){
                if (clazz.getAnnotation(Controller.class) != null){
                    controllerSet.add(clazz);
                }
            }
        }
    }

    public void setClassSet(Set<Class<?>> classSet) {
        this.classSet = classSet;
    }

    public void setControllerSet(Set<Class<?>> controllerSet) {
        this.controllerSet = controllerSet;
    }

    public void setComponentSet(Set<Class<?>> componentSet) {
        this.componentSet = componentSet;
    }

    public Set<Class<?>> getClassSet() {
        return classSet;
    }

    public static Set<Class<?>> getControllerSet() {
        return controllerSet;
    }

    public Set<Class<?>> getComponentSet() {
        return componentSet;
    }
}
