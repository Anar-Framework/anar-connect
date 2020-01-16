package af.gov.anar.connect.core.init;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InitBeans {


    // @PostConstruct
    // public void init()
    // {
    //     new NewTopic(topicName, 3, (short) 1);
    // }

    @Bean
    public KafkaProperties kafkaProperties()
    {
        return new KafkaProperties();
    }


}
