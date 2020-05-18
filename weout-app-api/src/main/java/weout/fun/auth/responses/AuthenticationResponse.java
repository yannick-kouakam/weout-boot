package weout.fun.auth.responses;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class AuthenticationResponse {
  private final String accessToken;
  private final String refreshToken;
}
