package fun.connor.lighter.servlet;

import fun.connor.lighter.LighterRequestResolver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO: rewrite service method to avoid duplicate logic where possible
public class LighterServlet extends HttpServlet {

    private LighterRequestResolver resolver;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        resolver.resolve(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        resolver.resolve(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        resolver.resolve(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        resolver.resolve(request, response);
    }
}
