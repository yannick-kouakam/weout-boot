package weout.fun.auth.exceptions;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthenticationFailureException extends AuthenticationException {
  private final int statusCode ;

  public AuthenticationFailureException(String message) {
    super(message);
    statusCode = 401;
  }

  public AuthenticationFailureException(String message, int statusCode){
    super(message);
    this.statusCode = statusCode;
  }

}
