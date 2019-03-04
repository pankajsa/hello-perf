package co.eventmesh.samples.helloperf;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.DeliveryMode;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;
import com.solacesystems.jcsmp.XMLMessageProducer;
import com.solacesystems.jcsmp.JCSMPSendMultipleEntry;

public class SolacePublisherFactory{
	static private Logger logger = LoggerFactory.getLogger(SolacePublisherFactory.class);
    private JCSMPSession session = null;
    private XMLMessageProducer prod = null;
    private TextMessage msg = null;
    private Topic topic = null;
    private Queue listenQueue = null;
//    private XMLMessageConsumer cons = null;


	private static SolacePublisherFactory instance = null;
	
	
	
	public static XMLMessageProducer getProducer(
			JCSMPStreamingPublishEventHandler handler) throws JCSMPException{
		
		return(createProducer(handler));
		
	}
	
	private static XMLMessageProducer createProducer(JCSMPStreamingPublishEventHandler handler) throws JCSMPException {
		XMLMessageProducer prod = null;
		
		JCSMPSession session = JCSMPFactory.onlyInstance().createSession(getProperties());
		prod = session.getMessageProducer(handler);
		
		return prod;
		
	}
	
    
    
    
//    public void sendMany(String topicName, String text) throws JCSMPException {
//    	MultipleSendEntry entries[] = new MultipleSendEntry[1];
//
//    	MultipleSendEntry entry = new MultipleSendEntry();
//    	if (msg == null) {
//        	this.msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);    		
//    	}
//    	msg.reset();
//        msg.setText(text);
//        msg.setDeliveryMode(DeliveryMode.PERSISTENT);
//    	entry.setMessage(msg);
//    	if (topic == null){
//            topic = JCSMPFactory.onlyInstance().createTopic(topicName);
//    	}
//    	entry.setDestination(topic);
//    	entries[0] = entry;
//    	
//        prod.sendMultiple(entries, 0, 1, 0);
//
//    }
    
    private static JCSMPProperties getProperties() {
    	HashMap<String, String> hmap = Configuration.getDefaults();
//    	logger.info(hmap.toString());

    	JCSMPProperties properties = new JCSMPProperties();
//    	properties.setProperty(JCSMPProperties.HOST, "localhost");
//    	properties.setProperty(JCSMPProperties.USERNAME, "admin");
//    	properties.setProperty(JCSMPProperties.PASSWORD, "admin");
//    	properties.setProperty(JCSMPProperties.VPN_NAME, "default");

    	properties.setProperty(JCSMPProperties.HOST, hmap.get("hostname"));
    	properties.setProperty(JCSMPProperties.USERNAME, hmap.get("username"));
    	properties.setProperty(JCSMPProperties.PASSWORD, hmap.get("password"));
    	properties.setProperty(JCSMPProperties.VPN_NAME, hmap.get("vpn"));

    	
    	properties.setProperty(JCSMPProperties.PUB_ACK_WINDOW_SIZE, 255);
    	
//    	logger.info(properties.toString());


    	return(properties);
    }

    
    
}