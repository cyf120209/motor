package test.JMS;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Server {

    public static void main(String[] args) throws JMSException {
        Session session = JMSFactory.session;
        Queue queue = session.createQueue("cyf_queue");
        MessageProducer producer = session.createProducer(queue);

//        Destination destination = session.createQueue("cyf_queue");
//        MessageProducer messageProducer = session.createProducer(destination);

        //创建消息
        TextMessage message = session.createTextMessage();
        message.setText("测试队列消息");
        //发送消息到目的地
        producer.send(message);
    }
}
