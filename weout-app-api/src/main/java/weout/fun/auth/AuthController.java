package weout.fun.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weout.fun.auth.requests.AuthenticationRequest;
import weout.fun.auth.responses.AuthenticationResponse;
import weout.fun.auth.services.AuthenticationService;
import weout.fun.config.auth.filter.JwtTokenProvider;
import weout.fun.user.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final JwtTokenProvider jwtTokenProvider;
  private final UserService userService;
  private AuthenticationService authenticationService;

  @Autowired
  public AuthController(
      JwtTokenProvider jwtTokenProvider,
      AuthenticationManager authenticationManager,
      UserService userService,
      AuthenticationService authenticationService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userService = userService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/signin")
  public AuthenticationResponse signin(@RequestBody AuthenticationRequest auth) {
//    var userDetails = userService.loadUserByUsername(auth.getUsername());
//    if (userDetails == null) {
//      throw new UsernameNotFoundException(
//          String.format("User with username %s not found", auth.getUsername()));
//    }

    authenticationService.authenticate(auth);

    String token = jwtTokenProvider.createAccessToken(auth.getUsername());
    String refresToken = jwtTokenProvider.createRefreshToken(auth.getUsername());
    return new AuthenticationResponse(token, refresToken);
  }
}
