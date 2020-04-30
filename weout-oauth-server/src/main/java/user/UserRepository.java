package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class UserRepository {
  @Autowired private MongoTemplate mongoTemplate;

  public WUser findByUsername(String username) {
    Criteria c = Criteria.where("username").is(username);
    Query query = new Query(c);
    return mongoTemplate.findOne(query, WUser.class);
  }
}
