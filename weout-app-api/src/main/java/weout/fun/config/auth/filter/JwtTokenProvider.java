package weout.fun.config.auth.filter;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import weout.fun.auth.exceptions.AuthenticationFailureException;
import weout.fun.user.UserService;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
  private final byte[] key = "tres_compliacte_key".getBytes();

  private final long ACCESS_TOKEN_EXPIRY_TIME = 7 * 24 * 3600L;

  private final long REFRESH_TOKEN_EXPIRY_TIME = 30 * 24 * 3600L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private UserService userService;

  @Autowired
  public JwtTokenProvider(UserService userService) {
    this.userService = userService;
  }

  public String createAccessToken(String username) {

    return creatToken(username, ACCESS_TOKEN_EXPIRY_TIME);
  }

  public UsernamePasswordAuthenticationToken resolveToken(String token)
      throws AuthenticationFailureException {
    // get token claims
    Claims claim = getClaims(token);

    // check token validity
    isValid(claim.getExpiration());
    // get user from token subject

    // validate the user
    return getAuthentication(claim.getSubject());
  }

  private Claims getClaims(String token) throws AuthenticationFailureException {
    try {
      return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    } catch (SignatureException
        | MalformedJwtException
        | UnsupportedJwtException
        | IllegalArgumentException ex) {
      throw new AuthenticationFailureException("Invalid Token");
    } catch (ExpiredJwtException ex) {
      throw new AuthenticationFailureException("Token has expired");
    }
  }

  private void isValid(Date expiryDate) throws AuthenticationFailureException {

    if (expiryDate.before(new Date())) {
      throw new AuthenticationFailureException("Token as expired");
    }
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    // get username from token
    String userNameFromToken = getUserNameFromToken(token);
    var userDetails = userService.loadUserByUsername(userNameFromToken);
    if (userDetails == null) {
      logger.error("invalid token {}", token);
      return null;
    }

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  public String createRefreshToken(String username) {
    return creatToken(username, REFRESH_TOKEN_EXPIRY_TIME);
  }

  private String getUserNameFromToken(String token) {
    try {

      return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    } catch (Exception e) {
      logger.info("failed to parse token {}", e.getMessage());
      return "";
    }
  }

  private String creatToken(String username, long expirenIn) {
    return Jwts.builder()
        .signWith(SignatureAlgorithm.HS256, key)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plusSeconds(expirenIn)))
        .setIssuer("weout-api")
        .setAudience("weout-app")
        .setHeaderParam("type", "JWT")
        .setSubject(username)
        .compact();
  }
}
