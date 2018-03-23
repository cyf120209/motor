package test.JMS;

import javax.jms.*;

public class Client {

    public static void main(String[] args) throws JMSException {
        Session session = JMSFactory.session;
        Queue queue = session.createQueue("cyf_queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage msg = (TextMessage)message;
                try {
                    String text = msg.getText();
                    System.out.println("value: "+text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
