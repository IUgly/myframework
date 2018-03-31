package org.redrock.formToBean;

import org.redrock.formToBean.User;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;

/**
 * @author ugly
 */
public class RequestUtil {
    public static <T>T getObject(Class<User> clazz, HttpServletRequest request){
        T entity = null;
        try {
            entity = (T) clazz.newInstance();
        } catch (IllegalAccessException|InstantiationException e) {
            e.printStackTrace();
        }

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String name = paramNames.nextElement();
            String value = request.getParameter(name);
            try{
                PropertyDescriptor pd = new PropertyDescriptor(name, clazz);
                Method method = pd.getWriteMethod();
                Class<?> type = pd.getPropertyType();
                if (type.equals(int.class)){
                    method.invoke(entity,Integer.valueOf(value));
                }else {
                    method.invoke(entity, value);
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}
