package com.fdjloto.api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

class AdminAuditInterceptorIT {

    private final AdminAuditInterceptor interceptor = new AdminAuditInterceptor();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Test 1
     * Path non admin -> doit retourner immédiatement
     */
    @Test
    void shouldIgnoreNonAdminPath() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        when(req.getRequestURI()).thenReturn("/api/users");
        when(req.getMethod()).thenReturn("GET");

        interceptor.afterCompletion(req, res, new Object(), null);

        verify(req).getRequestURI();
        verifyNoMoreInteractions(res);
    }

    /**
     * Test 2
     * Path admin avec utilisateur authentifié
     */
    @Test
    void shouldLogAdminActionWhenAuthenticated() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        when(req.getRequestURI()).thenReturn("/api/admin/users");
        when(req.getMethod()).thenReturn("POST");
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
        when(res.getStatus()).thenReturn(200);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin");

        SecurityContextHolder.getContext().setAuthentication(auth);

        interceptor.afterCompletion(req, res, new Object(), null);

        verify(req).getMethod();
        verify(req).getRemoteAddr();
        verify(res).getStatus();
    }

    /**
     * Test 3
     * Path admin sans authentification
     */
    @Test
    void shouldHandleAnonymousAdminWhenNoAuthentication() throws Exception {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse res = mock(HttpServletResponse.class);

        when(req.getRequestURI()).thenReturn("/api/admin/tickets");
        when(req.getMethod()).thenReturn("DELETE");
        when(req.getRemoteAddr()).thenReturn("192.168.1.10");
        when(res.getStatus()).thenReturn(403);

        SecurityContextHolder.clearContext();

        interceptor.afterCompletion(req, res, new Object(), null);

        verify(req).getMethod();
        verify(req).getRemoteAddr();
        verify(res).getStatus();
    }
}
