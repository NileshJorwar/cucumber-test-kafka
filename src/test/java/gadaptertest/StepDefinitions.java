
package gadaptertest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
        , classes = GptmAdapterApplication.class)
@ActiveProfiles(value = "test")
public class StepDefinitions {
    private String concat;
    private String producerTopic = "TOPIC1";
    private String consumerTopic = "TOPIC2";
    private CucumberKafkaProducer cucumberKafkaProducer = new CucumberKafkaProducer();
    private final CucumberKafkaConsumer cucumberKafkaConsumer = new CucumberKafkaConsumer(consumerTopic);

    @Given("^I am giving Message (.*) and (.*)$")
    public void setPrerequisites(String message, String resources) {
        concat = message;
        //System.out.println(concat);
        cucumberKafkaProducer.sendMessage(producerTopic,message);
    }

    @Then("I should be able to see the message on the queue.")
    public void verifyMessage() {
        Map<String, Integer> actual = getConsumerRecords();
        System.out.println("Verifying length: " + concat.length());
    }

    private Map<String, Integer> getConsumerRecords()
    {
        return cucumberKafkaConsumer.consumeMessage();
    }

}

