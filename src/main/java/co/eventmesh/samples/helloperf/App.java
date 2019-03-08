/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package co.eventmesh.samples.helloperf;


import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.XMLMessageProducer;


public class App {
	static private Logger logger = LoggerFactory.getLogger(App.class);

    public String getGreeting() {
        return "Hello world.";
    }

    
    public static void main(String[] args) throws Exception {
    	logger.info("App Starting");
    	Configuration.setupDefaults(args);

    	int publishCount = Configuration.PUBLISH_COUNT;
    	if (Configuration.getDefaults().get("publishcount") != null) {
    		publishCount = Integer.parseInt(Configuration.getDefaults().get("publishcount"));
    	}

    	
    	CountDownLatch startSignal = new CountDownLatch(1);
    	
    	
    	
        CountDownLatch doneSignal = new CountDownLatch(publishCount);
    	
    	
    	//    	logger.info(createString(1024));
    	
    	if (Configuration.getDefaults().get("consume") != null) {
    		startConsuming(startSignal, doneSignal);
    	}
    	if (Configuration.getDefaults().get("publish") != null) {
    		startPublishing(startSignal, doneSignal);    		
    	}
    	
    	
    	
    	
//    	new Thread(new PublishThread(5, "Hello World"), "publish-thread").start();
//    	System.out.println("0:" + args[0] + "1:" + args[1] + "2:" + args[2]);
//    	
//    	HashMap<String, String> parsedArgs = Configuration.getArguments(args);    		
//        System.out.println("ParsedArgs = " + parsedArgs);
//        SimpleThreadPool.main(args);
    	doneSignal.await();
    	Thread.sleep(5000);
    }
    
    
    public static void startConsuming(CountDownLatch startSignal, CountDownLatch doneSignal) throws JCSMPException, InterruptedException {
    	int consThreadCount = Configuration.CONSUMER_THREAD_COUNT;

    	if (Configuration.getDefaults().get("consthreadcount") != null) {
    		consThreadCount = Integer.parseInt(Configuration.getDefaults().get("consthreadcount"));    		
    	}

    	String consumequeuename = Configuration.getDefaults().get("consumequeuename");
    	String publishouttopic = Configuration.getDefaults().get("publishouttopic");

    	
    	for (int i = 0; i < consThreadCount; i++) {
    		startConsumingThread(startSignal, doneSignal, i, consumequeuename, publishouttopic  );
    	}
    	
    }
    
    
    public static String createString(int stringLength){
        
        char[] charArray = new char[stringLength];
        char ch = 'A';
        Arrays.fill(charArray, ch);        
        return new String(charArray);
    }
    
    public static void startPublishing(CountDownLatch startSignal, CountDownLatch doneSignal) throws JCSMPException {
    	int threadCount = Configuration.THREAD_COUNT;
    	int messagesize = Configuration.MESSAGE_SIZE;
    	
    	if (Configuration.getDefaults().get("threadcount") != null) {
    		threadCount = Integer.parseInt(Configuration.getDefaults().get("threadcount"));    		
    	}
    	

    	if (Configuration.getDefaults().get("messagesize") != null) {
    		messagesize = Integer.parseInt(Configuration.getDefaults().get("messagesize"));    		
    	}

    	for (int i = 0; i < threadCount; i++) {

    		startPublishingThread(startSignal, doneSignal,i, messagesize);    		
    	}

    	
    }
    
    
    public static void startPublishingThread(CountDownLatch startSignal, CountDownLatch doneSignal,
    		int threadNo, int messageSize) throws JCSMPException {
    	int repeatCount = Configuration.PUBLISH_COUNT;
    	if (Configuration.getDefaults().get("publishcount") != null) {
    		repeatCount = Integer.parseInt(Configuration.getDefaults().get("publishcount"));
    	}
    	String publishTopic = Configuration.getDefaults().get("publishtopic");
    	String messageStr = createString(messageSize);

    	PublishThread publishThread = new PublishThread(publishTopic, repeatCount, messageStr);
    	PublishEventHandler handler = new PublishEventHandler();
    	XMLMessageProducer prod = SolacePublisherFactory.getProducer(handler);
    	publishThread.setProducerListener(prod, handler);
    	
    	new Thread(publishThread, "publish-thread-" + threadNo).start();    	
    }

    
    public static void startConsumingThread(CountDownLatch startSignal, CountDownLatch doneSignal,
    		int threadNo, String queueName, String publishTopic) throws JCSMPException, InterruptedException {
    	ConsumeThread consumeThread = new ConsumeThread(startSignal, doneSignal,
    			queueName, publishTopic);
    	
    	ConsumeMessageListener handler = new ConsumeMessageListener(threadNo, doneSignal);    	
    	consumeThread.setConsumerListener(handler);
//    	new Thread(consumeThread, "consume-thread-" + threadNo).start();    

    }

    
    
    
}
