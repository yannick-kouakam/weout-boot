package weout.fun.config.filter;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import weout.fun.user.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
  private final byte[] key = "tres_compliacte_key".getBytes();
  private final long ACCESS_TOKEN_EXPIRY_TIME = 7 * 24 * 60 * 60;
  private final long REFRESH_TOKEN_EXPIRY_TIME = 30 * 24 * 60 * 60;
  private Logger logger = LoggerFactory.getLogger(getClass());
  private UserService userService;

  @Autowired
  public JwtTokenProvider(UserService userService) {
    this.userService = userService;
  }

  public String createAccessToken(String username) {

    return creatToken(username, ACCESS_TOKEN_EXPIRY_TIME);
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

  public String resolveToken(HttpServletRequest request) {
    logger.info("Class name {} Path {}", request.getClass().getName(), request.getRequestURI());
    var headerToken = request.getHeader("authorization");
    if (headerToken == null || !headerToken.startsWith("Bearer")) {
      logger.error("Missing user token");
      return null;
    }
    return headerToken.substring(7);
  }

  public boolean isValid(String token) {
    try {
      Jws<Claims> claim = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
      var expiryDAte = claim.getBody().getExpiration();
      logger.info("token is expire {}", expiryDAte.before(new Date()));
      return expiryDAte.before(new Date());

    } catch (SignatureException e) {
      logger.error("Invalid token");
    } catch (ExpiredJwtException e) {
      logger.error("Token has expired");
    }
    return false;
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    // get username from token
    String userNameFromToken = getUserNameFromToken(token);
    logger.info("Username: {}", userNameFromToken);
    var userDetails = userService.loadUserByUsername(userNameFromToken);
    if (userDetails == null) {
      logger.error("invalid token {}", token);
      return null;
    }

    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  private String getUserNameFromToken(String token) {
    try {

      return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    } catch (Exception e) {
      logger.info("failed to parse token {}", e.getMessage());
      return "";
    }
  }

  public String createRefreshToken(String username) {
    return creatToken(username, REFRESH_TOKEN_EXPIRY_TIME);
  }
}
