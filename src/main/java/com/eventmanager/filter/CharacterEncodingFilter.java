package com.eventmanager.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filtro para establecer la codificación de caracteres en las peticiones.
 */
@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(CharacterEncodingFilter.class);
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
        logger.info("CharacterEncodingFilter inicializado con encoding: {}", encoding);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.debug("Aplicando filtro de codificación a: {}", httpRequest.getRequestURI());
        
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
            response.setCharacterEncoding(encoding);
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No hay recursos que liberar
        logger.info("CharacterEncodingFilter destruido");
    }
}

