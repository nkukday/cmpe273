package SMSVoting.kafka.scheduler;

import SMSVoting.Model.Moderator;
import SMSVoting.Model.Poll;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

@Component
public class KafkaProducer {

    public static final String COLLECTION_NAME_POLLS = "poll";
    public static final String COLLECTION_NAME_MODERATORS = "moderator";

    @Autowired
    private MongoTemplate mongoTemplate;

    //Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private static Producer<Integer, String> producer;
    private final Properties properties = new Properties();

    //logger.info("kafka");
    String topic = "cmpe273-topic";
    //String topic = "polls";

    public KafkaProducer(){
        properties.put("metadata.broker.list", "54.149.84.25:9092");//54.149.84.25 -- localhost
        properties.put("serializer.class", "kafka.serializer.StringEncoder");
        properties.put("request.required.acks", "1");
        producer = new Producer<>(new ProducerConfig(properties));


    }



    @Scheduled(fixedDelay =30000,initialDelay=1000)
    public void sendMessage() throws Exception{


        new KafkaProducer();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date currentDate = new Date();

        BasicQuery query_get_expired_polls = new BasicQuery("{resultProduced:false},{id:1,expiredAt:1,results:1}");
        ArrayList<Poll> polls = (ArrayList<Poll>) mongoTemplate.find(query_get_expired_polls, Poll.class,COLLECTION_NAME_POLLS);
        for(Poll p:polls)
        {
            Date expiry_date = formatter.parse(p.getExpiredAt());
            if(expiry_date.before(currentDate))
            {
                StringBuilder tmp = new StringBuilder();
                BasicQuery query_get_moderator_with_pollid = new BasicQuery("{polls:\""+p.getId()+"\"},{email:1}");
                tmp.append(mongoTemplate.findOne(query_get_moderator_with_pollid, Moderator.class, COLLECTION_NAME_MODERATORS).getEmail());
                tmp.append(":009993880:"); //Enter Your ID Here
                tmp.append("Poll Result [" + p.getchoiceResultPairs()+"]");
                KeyedMessage<Integer, String> data = new KeyedMessage<>(topic,tmp.toString());
                producer.send(data);
                p.setResultProduced(true);
                mongoTemplate.save(p, COLLECTION_NAME_POLLS);
            }
        }



        producer.close();

}
}
