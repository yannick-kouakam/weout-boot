package weout.fun.config.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import weout.fun.auth.exceptions.AuthenticationFailureException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

@Slf4j
@Service
public class JwtTokenFilter extends OncePerRequestFilter {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String token = getHeaderToken(request);
      UsernamePasswordAuthenticationToken authenticatedUser = jwtTokenProvider.resolveToken(token);
      authenticatedUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
      filterChain.doFilter(request, response);

    } catch (AuthenticationFailureException e) {
      processFailure(response, e);
    }
  }

  private String getHeaderToken(HttpServletRequest request) {
    var headerToken = request.getHeader("authorization");

    var token = headerToken.substring(7);

    if (!isWellForm(headerToken)) {
      throw new AuthenticationFailureException("Token is missing or has incorrect format");
    }
    return token;
  }

  private void processFailure(HttpServletResponse response, AuthenticationFailureException e)
      throws IOException {
    log.info("Authentication failure {}", e.getMessage());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    var message = Map.of("timestamp", Calendar.getInstance().getTime(), "message", e.getMessage());
    response.getOutputStream().println(objectMapper.writeValueAsString(message));
  }

  private boolean isWellForm(String headerToken) {
    return headerToken != null && headerToken.startsWith("Bearer");
  }
}
