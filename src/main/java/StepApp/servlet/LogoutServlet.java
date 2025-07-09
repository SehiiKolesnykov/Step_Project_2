//package StepApp.servlet;
//
//import StepApp.auth.*;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Optional;
//import java.util.UUID;
//
//public class LogoutServlet extends HttpServlet {
//
//  private final Tokens tokens;
//
//  public LogoutServlet(Passwords passwords, Tokens tokens) {
//    this.tokens = tokens;
//  }
//
//  @Override
//  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//    Optional<UUID> maybeToken = Token.get(req);
//    if (maybeToken.isEmpty()) {
//      try (PrintWriter w = resp.getWriter()) {
//        w.write("Token should be set");
//        return;
//      }
//    }
//
//    UUID token = maybeToken.get();
//    Optional<String> maybeUser = tokens.check(token);
//    if (maybeUser.isEmpty()) {
//      try (PrintWriter w = resp.getWriter()) {
//        w.write("Nobody logged in");
//        return;
//      }
//    }
//
//    tokens.logout(token);
//    Token.logout(resp);
//
//    try(PrintWriter w = resp.getWriter()) {
//      w.printf("User %s logged out%n", maybeUser.get());
//    }
//  }
//
//}
