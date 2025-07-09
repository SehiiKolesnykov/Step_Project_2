package StepApp.util;

import StepApp.controller.TokenController;
import org.eclipse.jetty.servlet.FilterHolder;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.UUID;

public class HttpFilterImpl {

    public static FilterHolder getFilterHolder() {

        TokenController tokenController = new TokenController();

        HttpFilter myFilter = HttpFilter.make(
                rq -> {
                    try {
                        // Отримуємо токен із запиту
//                        var tokenOpt = Token.get(rq);
                        Optional<UUID> tokenOpt = tokenController.getToken(rq);
                        if (tokenOpt.isEmpty()) {
                            return false; // Токен відсутній
                        }
                        // Конвертуємо рядок токена в UUID
                        UUID token = UUID.fromString(String.valueOf(tokenOpt.get()));
                        // Перевіряємо токен у базі даних
                        return tokenController.checkToken(token).isPresent();
                    } catch (IllegalArgumentException e) {
                        // Помилка конвертації токена в UUID
                        System.err.println("Невалідний формат токена: " + e.getMessage());
                        return false;
                    } catch (Exception e) {
                        // Інші помилки (наприклад, проблеми з базою даних)
                        System.err.println("Помилка перевірки токена: " + e.getMessage());
                        return false;
                    }
                },
                rs -> {
                    try (PrintWriter w = rs.getWriter()) {
                        w.write("you must have a valid cookie set");
                        rs.sendRedirect(rs.encodeRedirectURL("/login"));
                    }
                }
        );

        return new FilterHolder(myFilter);
    }
}
