package io.andy.shorten_url.session;

import io.andy.shorten_url.exception.client.UnauthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import static io.andy.shorten_url.session.SessionPolicy.SESSION_KEY_LOGIN;
import static io.andy.shorten_url.session.SessionPolicy.SESSION_ACTIVE_TIME;
import static io.andy.shorten_url.util.mail.MailPolicy.EMAIL_AUTH_SESSION_ACTIVE_TIME;
import static io.andy.shorten_url.util.mail.MailPolicy.EMAIL_AUTH_SESSION_KEY;

@Slf4j
@Service
public class SessionService {
    // TODO refactor functions

    public Object getAuthSession(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();

            return session.getAttribute(SESSION_KEY_LOGIN);
        } catch (Exception e) {
            log.error("failed to get session, request={}, error message={}",request, e.getMessage());
            throw new IllegalStateException("FAILED TO GET SESSION");
        }
    }

    public Object setAuthSession(HttpServletRequest request, Long userId) {
        try {
            HttpSession session = request.getSession();

            session.setAttribute(SESSION_KEY_LOGIN, userId);
            session.setMaxInactiveInterval(SESSION_ACTIVE_TIME);
            return this.getAuthSession(request);
        } catch (Exception e) {
            log.error("failed to set session, id={}, error message={}", userId, e.getMessage());
            throw new IllegalStateException("FAILED TO SET SESSION");
        }
    }

    public Object getMailAuthSession(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();

            return session.getAttribute(EMAIL_AUTH_SESSION_KEY);
        } catch (Exception e) {
            log.error("failed to get mail auth session, request={}, error message={}",request, e.getMessage());
            throw new IllegalStateException("FAILED TO GET MAIL AUTH SESSION");
        }
    }

    public void setMailAuthSession(HttpServletRequest request, String secretCode) {
        try {
            HttpSession session = request.getSession();

            session.setAttribute(EMAIL_AUTH_SESSION_KEY, secretCode);
            session.setMaxInactiveInterval(EMAIL_AUTH_SESSION_ACTIVE_TIME);
        } catch (Exception e) {
            log.error("failed to set session for email auth, code={}, error message={}", secretCode, e.getMessage());
            throw e;
        }
    }

    public Object getPasswordAuthSession(HttpServletRequest request, String key) {
        try {
            HttpSession session = request.getSession();

            return session.getAttribute(key);
        } catch (Exception e) {
            log.error("failed to get session for find password, request={}, error message={}",request, e.getMessage());
            throw new IllegalStateException("FAILED TO GET PASSWORD AUTH SESSION");
        }
    }

    public void setPasswordAuthSession(HttpServletRequest request, String secretCode, String key, int ttl) {
        try {
            HttpSession session = request.getSession();

            session.setAttribute(key, secretCode);
            session.setMaxInactiveInterval(ttl);
        } catch (Exception e) {
            log.error("failed to set session for find password auth, code={}, error message={}", secretCode, e.getMessage());
            throw e;
        }
    }

    public void invalidateSession(HttpServletRequest request, Long userId) {
        try {
            Object session = getAuthSession(request);
            if (!userId.equals(session)) {
                log.debug("user={} failed to invalidate session={}", userId, session);
                throw new UnauthorizedException("UNAUTHORIZED SESSION");
            }

            request.getSession().invalidate();
        } catch (Exception e) {
            log.error("failed to remove session, error message={}", e.getMessage());
            throw new IllegalStateException("FAILED TO REMOVE SESSION");
        }
    }
}
