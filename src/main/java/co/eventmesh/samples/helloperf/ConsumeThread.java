package co.eventmesh.samples.helloperf;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.ConsumerFlowProperties;
import com.solacesystems.jcsmp.Context;
import com.solacesystems.jcsmp.EndpointProperties;
import com.solacesystems.jcsmp.FlowReceiver;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageProducer;

public class ConsumeThread 
//implements Runnable 
{
	static private Logger logger = LoggerFactory.getLogger(ConsumeThread.class);
	
	private String queueName;
	private String publishTopicName;
	
	
    private String messageText;
    private int repeatCount = 0;
    private XMLMessageProducer prod = null;
	private JCSMPStreamingPublishEventHandler handler;
	private TextMessage msg;
	private Boolean semaphore = true;

	private Topic topic;

	private String topicName;

	private CountDownLatch startSignal;
	private CountDownLatch doneSignal;
    
    public ConsumeThread(CountDownLatch startSignal, CountDownLatch doneSignal,
    		String queueName, String publishTopicName){
    	this.startSignal = startSignal;
    	this.doneSignal = doneSignal;
    	this.queueName = queueName;
        this.publishTopicName= publishTopicName;
    }
    
    
    
//    @Override
//    public void run() {
//        logger.info(Thread.currentThread().getName()+" - Started.");
//        try {
//            logger.info(Thread.currentThread().getName()+" - No waiting to proceeed.");
//        	
//			startSignal.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//    	Stopwatch watch = new Stopwatch("Consume messages");
//        processCommand();
//    	logger.info("Processing Time:" + watch.toString());
//        logger.info(Thread.currentThread().getName()+" - Terminated.");
//
//    }
//
//    
//    
//    private void processCommand() {
//    	try {
//    		logger.info("Thread processing about to wait");
//    		synchronized (this.semaphore) {
//        		this.semaphore.wait();
//        		logger.info("Wait finished");
//
//    		}
//    	}catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    	return;
//    
//    
//    }


    
    
    @Override
    public String toString(){
        return this.messageText + " " + this.repeatCount;
    }

	public void setConsumerListener(ConsumeMessageListener handler) throws JCSMPException, InterruptedException {
		
    	ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
		Queue listenQueue = JCSMPFactory.onlyInstance().createQueue(this.queueName);

    	flow_prop.setEndpoint(listenQueue);
    	flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);
    	
    	EndpointProperties endpoint_props = new EndpointProperties();
    	endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_NONEXCLUSIVE);

    	Context defaultContext = JCSMPFactory.onlyInstance().getDefaultContext();
    	logger.info("DefaultContext: " + defaultContext );
    	Context context = JCSMPFactory.onlyInstance().createContext(null);
    	JCSMPSession session = JCSMPFactory.onlyInstance().createSession(SolaceConsumerFactory.getProperties(), context);
	   	System.out.println("Listener setup......");
    	FlowReceiver cons = session.createFlow(handler, flow_prop, endpoint_props);  
//    	logger.info(Thread.currentThread().getName()+" - No waiting to proceeed.");
//		this.startSignal.await();

    	cons.start();
				
	}
}