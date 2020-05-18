package weout.fun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weout.fun.config.filter.JwtTokenProvider;
import weout.fun.models.AuthenticationRequest;
import weout.fun.user.UserService;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  @Autowired
  public AuthController(
      JwtTokenProvider jwtTokenProvider,
      AuthenticationManager authenticationManager,
      UserService userService) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  @PostMapping("/signin")
  public ResponseEntity signin(@RequestBody AuthenticationRequest auth) {
    var userDetails = userService.loadUserByUsername(auth.getUsername());
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
    var result = new HashMap<>();
    result.put("username", userDetails.getUsername());
    String token = jwtTokenProvider.createToken(auth.getUsername());
    result.put("token", token);
    return ResponseEntity.ok(result);
  }
}
