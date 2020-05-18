package weout.fun.config.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

@Slf4j
public class AuthFailureHandler implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
      throws IOException, ServletException {
    log.info("Authentication failure {}", ex.getMessage());

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    var message = Map.of("timestamp", Calendar.getInstance().getTime(), "message", ex.getMessage());
    response.getOutputStream().println(objectMapper.writeValueAsString(message));
  }
}
