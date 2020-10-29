
package gadaptertest;

import com.broadridge.datafabric.transform.generated.bps.TransactionTobrfsTransactionTransformer;
import com.broadridge.dft.conversion.TactConverter;
import com.broadridge.gptm.services.gptmadapter.AdapterChannels;
import com.broadridge.gptm.services.gptmadapter.GptmAdapterApplication;
import com.broadridge.gptm.services.gptmadapter.controller.StreamController;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
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
    @LocalServerPort
    private int port;
    private RestTemplate restTemplate = new RestTemplate();
    private String concat;
    private String producerTopic = "BR.DATAFABRIC.PESOURCEXML.GPTM.DEMO.INT.TOPIC";
    private String consumerTopic = "BR.DATAFABRIC.LATESTVERSION.OUTPUT.GPTM.DEMO.INT.TOPIC";
    private CucumberKafkaProducer cucumberKafkaProducer = new CucumberKafkaProducer();
    private final CucumberKafkaConsumer cucumberKafkaConsumer = new CucumberKafkaConsumer(consumerTopic);

    @InjectMocks
    StreamController streamController;

    @Value("${tact.xsdPath}")
    Resource resource;

    @Value("${tact.copybooks}")
    Resource[] resources;

    @Mock
    AdapterChannels adapterChannels;


    @Before
    public void setup()  {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void contextLoads() throws Exception{
        String inputString = "DATA6577702420200921004450T096D00000000027862          Y                     V03                                                                      D1BPU29   0700  0133101000317000C0537330191996  PTRD XS202009242020092111D0005200921EL00000000027862001CXLI                                TCC    P        I          TS  TU       0           00000000010000000             DC00000888000000000000/0000  000C0000000000T     000000000000T0000/0000TAC053733        007    I      I                 D978    SZAEAVKBMSC    11  1 L01256C            CC               A       20200921 00000    09P1 0 0C                      00000000000000000T          014QB0211161T                  3456              123 5678                                                                                                  202009210158082    23               01            N #@R1BNDTRTA 0400  0700040005000700036002700220050200010100               03450                                    0101 01I015                    0   Y                                2020092120200921        10 N  1623000             200921EL00000000027862000200921EL00000000027862000                                                                                                                  Y   #@G1P3MSD   0500  18383M17500010030003 00000000       00000000000000000000000000000000000000000000000000000000000             000000000000000000*N  0000001                  Nm0000000000000000   YY      J 00000000          0  0000000000000000000     DUS1NN             US18383M1751000000000000000000000000 0000000000000000000000000000000000000000000000000000000000 00000000001US00000000                     0000000000000000         BZ77Y62     NN       0000000000000000000NEQIY    NY    NN     {  N   #@H1BNDTRTA 0700  NET00000000008871927001PPRI00000000008880000001PW/H00000000000000000001N P000000000P                                    BAS                 001 INT00000000000000000001+I ICOM0000000007073001-PGRC0000000000000001NI I I0000000000000000SEC0000000000600001-P IPOS0000000000000001NP ISTX0000000000500001-T IGPF0000000000400001NPGP20000000000000001NPGP30000000000000001NPGP40000000000000001NPGP50000000000000001NP000001000000100000000 000000000000000000000 000000000000000000000 000000000000000000000 000000000000000000000                                      W/I00000000000000000001N P00000000008871927D                                                    N                               #@K1BPU30   0360                                     GOBTUN         00000000000000000000000000000 00000000000000000 00000000000000000 000000000                                                                                                                                                                                    001                                  #@L1ZP3TANA 0270  3100000  0000000003100 E00500500P  123 5            000 0000000000000000N                       0     20171003             MS20171003        1     000 0NN000 000000000                             00000000     F                                          #@N1BKP20PS 0220  05M 001CLAYMORE EXCHANGE TRADED FUND T  1 M 002TRUST GUGGENHEIM DOW JONES    T  2 M 003INDUSTRIAL AVERAGE DIV ETF    T  3   004AUTO LIQUIDATION FRAC         C  1 C 005TO CXL PREVIOUS BUY                #@S1BPU30   0200    00969T          01          0000000001000000000000000008880000001T00000000000000000001+T                                                                                            #@U1SEGMSDD 0100                               001100  N   N  00000                                 #@";
        when(TactConverter.convertTactToXML(inputString,resources,resource)).thenReturn(any());
        when(streamController.convertToGenericRecord(any())).thenReturn(any());
        when(new TransactionTobrfsTransactionTransformer().transform(any()));
        streamController.processIpeInboundTxntoBrcm(any());
        System.out.println(streamController);
    }
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

