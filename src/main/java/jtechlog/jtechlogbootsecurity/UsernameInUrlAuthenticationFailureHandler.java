package jtechlog.jtechlogbootsecurity;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernameInUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String LAST_USERNAME_KEY = "lastUsername";

    public UsernameInUrlAuthenticationFailureHandler() {
        super("/login?error");
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        var usernameParameter =
                UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;
        var lastUserName = request.getParameter(usernameParameter);

        var session = request.getSession(false);
        if (session != null || isAllowSessionCreation()) {
            request.getSession().setAttribute(LAST_USERNAME_KEY, lastUserName);
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
