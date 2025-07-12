package StepApp.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        MIME_TYPES.put("map", "application/json");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String path = "css" + pathInfo;

        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            System.out.println("Resource not found: " + path);
        }

        String mimePipe = getMimeType(path);
        if (mimePipe != null) {
            response.setContentType(mimePipe);
        }

        try (java.io.InputStream is = resource.openStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Error reading resource: " + path);
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
