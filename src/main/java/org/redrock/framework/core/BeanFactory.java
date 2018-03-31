package org.redrock.framework.core;

import org.redrock.framework.annotation.Autowired;
import org.redrock.framework.annotation.RequestMapping;
import org.redrock.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author ugly
 */
public class BeanFactory {

    private Map<Class<?>, Object> controllers;
    private Map<String, Method> handlers;
    private Map<Class<?>, Object> components;

    private ClassLoader classLoader;

    public BeanFactory(){
        initControllersAndHandlers();
        initComponent();
    }

    private void initComponent(){
        Set<Class<?>> componentSet = classLoader.getComponentSet();
        components = new HashMap<>();
        for (Class<?> clazz : componentSet){
            try {
                Object object = clazz.newInstance();
                components.put(clazz, object);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void initControllersAndHandlers(){
        Set<Class<?>> controllerSet = ClassLoader.getControllerSet();
        components = new HashMap<>();
        controllers = new HashMap<>();
        for (Class<?> controller : controllerSet){
            String baseUri = controller.getAnnotation(RequestMapping.class) != null
                    ? controller.getAnnotation
                    (RequestMapping.class).value() : "";
            Object object = ReflectionUtil.newInstance(controller);
            components.put(controller, object);

            Method[] methods = controller.getMethods();
            for (Method method : methods){
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null){
                    String requestUri = requestMapping.method().name() + ":"
                            + baseUri + requestMapping.value();
                    handlers.put(requestUri, method);
                }
            }
            controllers.put(controller, object);

            Field[] fields = controller.getDeclaredFields();
            for (Field field : fields){
                if (field.getAnnotation(Autowired.class)!= null){
                    Class<?> fieldClazz = field.getType();
                    Object fieldValue = components.get(fieldClazz);
                    if (!field.isAccessible()){
                        field.setAccessible(true);
                    }
                    try {
                        field.set(controller, fieldValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Map<Class<?>, Object> getConrollers() {
        return controllers;
    }


    public Map<Class<?>, Object> getComponents() {
        return components;
    }

    public void setConrollers(Map<Class<?>, Object> conrollers) {
        this.controllers = conrollers;
    }

    public Map<String, Method> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<String, Method> handlers) {
        this.handlers = handlers;
    }

    public void setComponents(Map<Class<?>, Object> components) {
        this.components = components;
    }
}
