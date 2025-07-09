package StepApp.util;

import StepApp.servlet.TemplateServlet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class TemplateEngine {

    private final Configuration config;
    private final Map<String, Object> empty = new HashMap<>();

    private String base(String prefix) {
        try {
            return TemplateServlet.class
                    .getClassLoader()
                    .getResource(prefix)
                    .toURI()
                    .getPath();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public TemplateEngine(String prefix) throws IOException {
        this.config = new Configuration(Configuration.VERSION_2_3_33);
        config.setDirectoryForTemplateLoading(new File(base(prefix)));
    }

    public void render(String templateName, Map<String, Object> data, HttpServletResponse rs) {
        try (PrintWriter w = rs.getWriter()) {
            Template template = config.getTemplate(templateName);
            template.process(data, w); // void
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(String template, HttpServletResponse rs)  {
        render(template, empty, rs);
    }
}
