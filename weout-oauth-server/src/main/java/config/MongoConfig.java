package config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.internal.MongoClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("user")
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Bean
    public MongoCredential mongoCredential(){
        return  MongoCredential.createCredential("yannick", "weout-db", "pass".toCharArray());
    }

    @Bean
    public MongoClientSettings settings(){
        return MongoClientSettings.builder().credential(mongoCredential()).build();
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClientImpl(settings(), null);
    }

    @Override
    protected String getDatabaseName() {
        return "weout-db";
    }
}
