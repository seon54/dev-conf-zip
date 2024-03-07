package com.dev.conf.global.security.filter;

import com.dev.conf.global.common.dto.ApiResponse;
import com.dev.conf.global.common.enums.StatusCode;
import com.dev.conf.global.error.exception.GeneralException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            setResponse(response, StatusCode.TOKEN_EXPIRED);
        } catch (GeneralException e) {
            setResponse(response, e.getStatusCode());
        } catch (MalformedJwtException e) {
            setResponse(response, StatusCode.TOKEN_INVALID);
        }

    }

    private void setResponse(HttpServletResponse response, StatusCode statusCode) throws IOException {
        response.setStatus(statusCode.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write(mapper.writeValueAsString(ApiResponse.of(statusCode)));
    }
}
