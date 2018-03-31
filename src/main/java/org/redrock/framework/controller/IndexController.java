package org.redrock.framework.controller;

import org.redrock.framework.annotation.Autowired;
import org.redrock.framework.annotation.RequestMapping;
import org.redrock.framework.annotation.RequestMethod;
import org.redrock.framework.bean.World;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexController {

    @Autowired
    World world;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void test(HttpServletRequest request, HttpServletResponse response)
        throws IOException{
        response.getWriter().println(world.test());
    }
}
