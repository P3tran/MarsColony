package com.pefthymiou.mars.security.ratelimit;

import org.apache.catalina.connector.Request;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class IpRateLimitFilter implements Filter {

    private static final int TOO_MANY_REQUESTS = 429;
    private static final String LIMIT_REACHED = "Request rate limit reached for IP ";
    private static final String NOT_HTTP_SERVLET_REQUEST = "Request should be a HttpServletRequest";
    private static final String REQUEST_ATTRIBUTES = "org.springframework.web.context.request.RequestContextListener.REQUEST_ATTRIBUTES";
    private static String[] LIMITED_PATHS = new String[]{
            "/api/v1/unit",
            "/api/v1/booking",
            "/api/v1/units/browse",
            "/api/v1/units"
    };

    private RequestsWindow requestsWindow;

    public IpRateLimitFilter(RequestsWindow requestsWindow) {
        this.requestsWindow = requestsWindow;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = getHttpServletRequest(servletRequest);

        if(isRestPublicUserServicePostCall(request)) {
            String ip = request.getRemoteAddr();
            requestsWindow.addRequestFrom(ip);

            if (requestsWindow.ipAddressReachedLimit(ip))
                ((HttpServletResponse) servletResponse).sendError(TOO_MANY_REQUESTS, LIMIT_REACHED + ip);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private HttpServletRequest getHttpServletRequest(ServletRequest servletRequest) throws ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            return (HttpServletRequest) servletRequest;
        } else {
            Request springRequest = (Request) servletRequest.getAttribute(REQUEST_ATTRIBUTES);
            if (springRequest instanceof HttpServletRequest) {
                return springRequest;
            } else {
                throw new ServletException(NOT_HTTP_SERVLET_REQUEST);
            }
        }
    }

    private boolean isRestPublicUserServicePostCall(HttpServletRequest request) {
        String requestedUri = request.getRequestURI();
        return Arrays.stream(LIMITED_PATHS).anyMatch(requestedUri::startsWith);
    }

    @Override
    public void destroy() {
    }
}