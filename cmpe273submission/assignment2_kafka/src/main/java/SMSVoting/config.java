package SMSVoting;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;


@ComponentScan
@Configuration
@EnableAutoConfiguration
public class config {
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        //MongoClient mongoClient = new MongoClient("",27017);//localhost
        MongoClient mongoClient = new MongoClient("ds031661.mongolab.com",31661);//mongolab credentials
        UserCredentials userCredentials = new UserCredentials("nk","nk25");
        return new SimpleMongoDbFactory(mongoClient, "sms",userCredentials) ;
    }
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
}
