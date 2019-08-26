package cn.itcast.listener;

import cn.itcast.utils.MailUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;


@Component
public class EmailMessageListener implements MessageListener {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onMessage(Message message) {
        try {
            JsonNode jsonNode = MAPPER.readTree(message.getBody());
            String email = jsonNode.get("email").asText();
            String subject = jsonNode.get("subject").asText();
            String context = jsonNode.get("context").asText();
            System.out.println("消费消息成功：" + email);
            // send email....
            MailUtil.sendMsg(email,subject,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}