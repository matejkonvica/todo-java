package cz.vse.todo.todo_java.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${todo.auth.api-key}")
    private String apiKey;

    @Value("${todo.auth.enabled:true}")
    private boolean enabled;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.equals("/") || path.startsWith("/index.html") || path.startsWith("/static/")
                || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/favicon")) {
            return true;
        }

        if (path.startsWith("/h2")) return true;

        if (path.startsWith("/actuator/health")) return true;

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!enabled) {
            filterChain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            unauthorized(response, "missing Authorization: Bearer <token>");
            return;
        }

        String token = auth.substring("Bearer ".length()).trim();
        if (!apiKey.equals(token)) {
            unauthorized(response, "invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write("{\"error\":\"unauthorized\",\"message\":\"" + message + "\"}\n");
    }
}
