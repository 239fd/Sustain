package by.sustain.zoneservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    public static final String REQUEST_ID_MDC_KEY = "requestId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isExcludedPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestId = request.getHeader(REQUEST_ID_HEADER);

        if (requestId == null || requestId.isBlank()) {
            log.warn("Missing required header: {}", REQUEST_ID_HEADER);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "error": "MISSING_REQUEST_ID",
                    "message": "X-Request-ID header is required"
                }
                """);
            return;
        }

        try {
            MDC.put(REQUEST_ID_MDC_KEY, requestId);
            response.setHeader(REQUEST_ID_HEADER, requestId);
            log.debug("Processing request with X-Request-ID: {}", requestId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_MDC_KEY);
        }
    }

    private boolean isExcludedPath(String path) {
        return path.contains("/swagger") ||
               path.contains("/api-docs") ||
               path.contains("/actuator") ||
               path.contains("/favicon") ||
               path.equals("/error");
    }
}
