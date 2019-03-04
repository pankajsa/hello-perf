package co.eventmesh.samples.helloperf;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerThread implements Runnable {
	static private Logger logger = LoggerFactory.getLogger(App.class);
	  
    private String command;
    
    public WorkerThread(String s){
        this.command=s;
    }

    @Override
    public void run() {
    	logger.info(Thread.currentThread().getName()+" Start. Command = "+command);

//        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
        	int processTime = 5000 + new Random().nextInt(4000);
//            System.out.println("ProcessTime:" + processTime);

            Thread.sleep(processTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return this.command;
    }
}