package weout.fun.auth.exceptions;

import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

public class AuthenticationException extends AbstractAuthenticationFailureEvent {
  private final int statusCode = 403;
  private String message;

  public AuthenticationException(String message) {
    this.message = message;
  }
}
