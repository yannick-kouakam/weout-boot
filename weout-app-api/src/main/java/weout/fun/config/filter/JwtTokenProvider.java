package weout.fun.config.filter;

import org.springframework.security.core.Authentication;

import javax.servlet.ServletRequest;

public class JwtFilterProvider {
    public String resolveToken(ServletRequest servletRequest) {
    return null;
    }

    public boolean isValid(String token) {
        return false;
    }

    public Authentication getAuthentication(String token) {
        return null;
    }
}
