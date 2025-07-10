package StepApp.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class StaticContentServlet extends HttpServlet {

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "application/javascript");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("map", "application/json"); // Для .map файлів, як bootstrap.min.css.map
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Видаляємо початковий слеш і додаємо префікс для коректного пошуку
        String path = pathInfo.substring(1); // Наприклад, "css/style.css" -> "css/style.css"
        System.out.println("Requested path: " + path);

        // Отримуємо ресурс відносно src/main/resources
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("Resource not found: " + path);
            return;
        }

        // Встановлюємо тип вмісту на основі розширення файлу
        String mimeType = getMimeType(path);
        if (mimeType != null) {
            response.setContentType(mimeType);
        }

        // Копіюємо вміст ресурсу в відповідь
        try (InputStream is = resource.openStream();
             ServletOutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                os.write(buffer, 0, bytesRead);
            }
        }
    }

    private String getMimeType(String path) {
        int lastDot = path.lastIndexOf('.');
        if (lastDot > 0 && lastDot < path.length() - 1) {
            String extension = path.substring(lastDot + 1).toLowerCase();
            return MIME_TYPES.getOrDefault(extension, "application/octet-stream");
        }
        return null;
    }
}