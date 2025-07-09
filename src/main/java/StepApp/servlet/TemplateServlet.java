package StepApp.servlet;

import StepApp.util.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class TemplateServlet extends HttpServlet {

    private final TemplateEngine templateEngine;
    private Map<String, Object> data;
    private final String template;

    public TemplateServlet(TemplateEngine templateEngine, String template) {
        this.templateEngine = templateEngine;
        this.template = template;
    }

    public TemplateServlet(TemplateEngine templateEngine, String template, Map<String, Object> data) {
        this.templateEngine = templateEngine;
        this.data = data;
        this.template = template;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        templateEngine.render(template, data, response);
    }
}
