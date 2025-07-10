package StepApp.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TemplateEngine {

    private final Configuration config;
    private final Map<String, Object> empty = new HashMap<>();

    public TemplateEngine(String prefix) throws IOException {
        this.config = new Configuration(Configuration.VERSION_2_3_33);
        // Завантажуємо шаблони з ClassLoader (з src/main/resources/templates)
        config.setClassForTemplateLoading(TemplateEngine.class, "/" + prefix);
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public void render(String templateName, Map<String, Object> data, HttpServletResponse rs) {
        try (PrintWriter w = rs.getWriter()) {
            Template template = config.getTemplate(templateName);
            template.process(data, w); // void
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(String template, HttpServletResponse rs) {
        render(template, empty, rs);
    }
}