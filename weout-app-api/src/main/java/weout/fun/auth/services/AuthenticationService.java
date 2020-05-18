package weout.fun.auth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import weout.fun.auth.requests.AuthenticationRequest;

@Service
public class AuthenticationService {
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationService(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  public void authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
  }
}
