package org.redrock.formToBean;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "login", urlPatterns = "/login")
public class LoginServlet extends HttpServlet{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){
        User bean = RequestUtil.getObject(User.class, request);
        System.out.println("welcome"+bean.getUsername());
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("welcome:"+bean.getUsername());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        doPost(request, response);
    }
}
