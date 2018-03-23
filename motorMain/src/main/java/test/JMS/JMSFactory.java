package test.JMS;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSFactory {

    public static Session session;
    private static ActiveMQConnectionFactory connectionFactory=null;
    private static Connection connection=null;
    final static public String CONNECT_URL_PROPNAME = "org.fusesource.rmiviajms.CONNECT_URL";

    static  {
        connectionFactory = new ActiveMQConnectionFactory(System.getProperty(CONNECT_URL_PROPNAME,"tcp://localhost:61616"));
        try {
            connection=connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

//    ConnectionFactory getConnectionFactory() throws JMSException {
//        if (connectionFactory == null) {
//            connectionFactory = remoteSystem.createConnectionFactory();
//        }
//        return connectionFactory;
//    }
}
