package af.gov.anar.connect.core.init;

import af.gov.nsia.core.kafka.Topics;
import af.gov.nsia.core.kafka.connect.KafkaConnectIntegrationService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaConnectorsInitiator {

    @Autowired
    KafkaConnectIntegrationService kafkaConnectIntegrationService;


    @PostConstruct
    public  void init(){
        createDefaultTopics();
        createAsimsPostgresConnetor();
        createElasticsearchSinkConnector();
    }

    public void createAsimsPostgresConnetor()
    {
        Map<String, String> config = new HashMap<>();
        config.put("connector.class", "io.confluent.connect.jdbc.JdbcSourceConnector");
//        config.put("errors.log.include.messages", "false");
        config.put("tasks.max", "5");
        config.put("table.types", "TABLE");
        config.put("table.whitelist","instance");
        config.put("mode","bulk");
        config.put("topic.prefix","asims-test-");
//        config.put("poll.interval.ms","5000");
//        config.put("db.timezone","UTC");
        config.put("value.converter","org.apache.kafka.connect.json.JsonConverter");
//        config.put("config.action.reload","restart");
        config.put("errors.log.enable","false");
        config.put("key.converter","org.apache.kafka.connect.json.JsonConverter");
//        config.put("errors.retry.timeout","0");
//        config.put("validate.non.null","true");
//        config.put("connection.attempts","3");
//        config.put("errors.retry.delay.max.ms","60000");
//        config.put("batch.max.rows","100");
//        config.put("connection.backoff.ms","10000");
//        config.put("timestamp.delay.interval.ms","0");
//        config.put("table.poll.interval.ms","60000");
        config.put("value.converter.schemas.enable","false");
//        config.put("errors.tolerance","none");
        config.put("connection.url","jdbc:postgresql://asims.gov.af:5432/asims?user=asims_user&password=secret");
        config.put("numeric.precision.mapping","false");
        config.put("quote.sql.identifiers","ALWAYS");


        kafkaConnectIntegrationService.deployConnector("Asims-postgresql-1", config);

    }



    public void  createElasticsearchSinkConnector(){


        Map<String, String> config = new HashMap<>();
        config.put("connector.class", "io.confluent.connect.elasticsearch.ElasticsearchSinkConnector");
        config.put("connection.url", "http://localhost:9200");
        config.put("tasks.max", "5");
        config.put("topics", Topics.ODKX_ELASTICSEARCH_INSTANCE_TOPIC);
//        config.put("topic.index.map", "asims:instance");
        config.put("type.name","kafka-connect");
//        config.put("key.ignore","false");
//        config.put("schema.ignore","false");
//        config.put("drop.invalid.message", "true");
//        config.put("behavior.on.null.values", "ignore");
//        config.put("behavior.on.malformed.documents", "ignore");


        kafkaConnectIntegrationService.deployConnector("Asims-elasticsearch-1", config);


    }


    private void createDefaultTopics(){
        new NewTopic("asims-test-instance", 3, (short) 1);
    }

}
