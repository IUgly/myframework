package org.redrock.framework.core;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet(name = "*", urlPatterns = "/*")
public class DispatcherServlet extends GenericServlet{

    private Map<Class<?>, Object> controllers;
    private Map<String, Method> handlers;

    @Override
    public void init() throws ServletException{
        ClassLoader classLoader = new ClassLoader();
        BeanFactory beanFactory = new BeanFactory();
        controllers = beanFactory.getConrollers();
        handlers = beanFactory.getHandlers();
    }
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        String handlerKey = req.getMethod() + ":" +req.getRequestURI();
        Method handler = handlers.get(handlerKey);
        Object controller = controllers.get(handler.getDeclaringClass());
        if (controller != null){
            try {
                handler.invoke(controller, req, resp);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
