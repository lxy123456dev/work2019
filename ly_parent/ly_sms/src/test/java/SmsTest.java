import com.leyou.LySmsApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySmsApplication.class)
public class SmsTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSendMessage() throws InterruptedException {
        Map<String, String> map = new HashMap<>();
        map.put("phone", "18732509426");
        map.put("code", "5559");
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", map);
        Thread.sleep(5000);
    }
}