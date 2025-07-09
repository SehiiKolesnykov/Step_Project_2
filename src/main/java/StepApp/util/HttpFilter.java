package StepApp.util;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Predicate;

public interface HttpFilter extends Filter {

  default void init(FilterConfig filterConfig) throws ServletException {}

  default boolean isHttp(ServletRequest rq0, ServletResponse rs0) {
    return rq0 instanceof HttpServletRequest &&
      rs0 instanceof HttpServletResponse;
  }

  boolean checkFn(HttpServletRequest rq);

  void onError(HttpServletResponse rs) throws IOException;

  default void doFilter(ServletRequest rq0,
                       ServletResponse rs0,
                       FilterChain chain) throws IOException, ServletException {
    if (isHttp(rq0, rs0)) {
      HttpServletRequest rq = (HttpServletRequest) rq0;
      HttpServletResponse rs = (HttpServletResponse) rs0;

      if (checkFn(rq)) { // token is present, go further
        chain.doFilter(rq0, rs);
      } else {
        onError(rs);
      }

    } else  { // not http, go further
      chain.doFilter(rq0, rs0);
    }
  }

  default void destroy() {}

  interface ConsumerThrowable<A> {
    void accept(A a) throws IOException;
  }

  static HttpFilter make(
    Predicate<HttpServletRequest> checkFn,
    ConsumerThrowable<HttpServletResponse> onError
  ) {
    return new HttpFilter() {
      @Override
      public boolean checkFn(HttpServletRequest rq) {
        return checkFn.test(rq);
      }

      @Override
      public void onError(HttpServletResponse rs) throws IOException {
        onError.accept(rs);
      }
    };
  }

}
