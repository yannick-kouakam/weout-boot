package weout.fun.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Repository
public class UserRepository {
  private MongoTemplate mongoTemplate;

  @Autowired
  public UserRepository(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public WUser findByUsername(String username) {
    var passwordEncoder = new BCryptPasswordEncoder();
    //    Criteria c = Criteria.where("username").is(username);
    //    Query query = new Query(c);
    //    return mongoTemplate.findOne(query, WUser.class);
    String secret = passwordEncoder.encode("secret");
    return new WUser("Yannick", secret, Collections.emptyList());
  }
}
