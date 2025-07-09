package StepApp;

import StepApp.servlet.*;
import StepApp.util.StartApp;
import StepApp.util.TemplateEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import static StepApp.util.HttpFilterImpl.getFilterHolder;

public class WebServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);

        StartApp startApp = new StartApp();

        ServletContextHandler handler = new ServletContextHandler();
        StaticContentServlet staticContent = new StaticContentServlet();
        TemplateEngine templateEngine = new TemplateEngine("templates");

        var options = EnumSet.of(DispatcherType.REQUEST);

        startApp.startApp();

        FilterHolder authorisedFilter = getFilterHolder();

        handler.setContextPath("/");

        handler.addServlet(new ServletHolder(staticContent), "/css/*");
        handler.addServlet(new ServletHolder(staticContent), "/img/*");

        handler.addServlet(new ServletHolder(new LoginServlet(templateEngine)), "/login");

        handler.addServlet(new ServletHolder(new LikePageServlet(templateEngine)), "/users");
        handler.addFilter(authorisedFilter, "/users", options);

        handler.addServlet(new ServletHolder(new LikedPageServlet(templateEngine)), "/liked");
        handler.addFilter(authorisedFilter, "/liked", options);

        handler.addServlet(new ServletHolder(new ChatServlet(templateEngine)), "/messages/*");
        handler.addFilter(authorisedFilter, "/messages/*", options);

        handler.addServlet(new ServletHolder(new TemplateServlet(templateEngine, "register.ftl")), "/register");


        server.setHandler(handler);
        server.start();

        server.join();
    }


}
