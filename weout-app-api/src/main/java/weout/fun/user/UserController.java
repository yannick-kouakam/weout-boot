package weout.fun.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("/user/me")
  public ResponseEntity user(@AuthenticationPrincipal WUser user) {

    return ResponseEntity.ok(user);
  }
}
