package co.eventmesh.samples.helloperf;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;

public class ConsumeMessageListener implements XMLMessageListener {
	static private Logger logger = LoggerFactory.getLogger(ConsumeMessageListener.class);
	static int  totalCount = 0;
	int messageCount = 0;
	private int threadNo;
	private CountDownLatch doneSignal;
	private PublishThread2 pt2;
	
	ConsumeMessageListener(int threadNo, CountDownLatch doneSignal, String publishTopic, String respondmode){
		super();
		this.threadNo = threadNo;
		this.doneSignal = doneSignal;
		pt2 = new PublishThread2(publishTopic, respondmode);
    	new Thread(pt2, "publish2-thread-").start();    	
		
	}
	
	
    @Override
    public void onReceive(BytesXMLMessage msg) {
    	messageCount++;
        totalCount ++;			
//        logger.info(messageCount + " messages received. Listener: " + threadNo + " Total Count:" + totalCount);
        

//        if (msg instanceof TextMessage) {
//            System.out.printf("TextMessage received: '%s'%n", ((TextMessage) msg).getText());
//        } else {
//            System.out.println("Message received.");
//        }
//        System.out.printf("Message Dump:%n%s%n", msg.dump());
//        logger.info(msg.dump());
//        System.out.printf("Message Dump: %s %s",((TextMessage) msg).getText(), msg.getDeliveryMode());

        
        // When the ack mode is set to SUPPORTED_MESSAGE_ACK_CLIENT,
        // guaranteed delivery messages are acknowledged after
        // processing
        pt2.addMessage(((TextMessage) msg).getText()  + ":" + totalCount);
        msg.ackMessage();
        doneSignal.countDown();

    }

    @Override
    public void onException(JCSMPException e) {
        System.out.printf("Consumer received exception: %s%n", e);
    }
	
	
}

