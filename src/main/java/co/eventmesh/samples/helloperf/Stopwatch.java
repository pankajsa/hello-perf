package co.eventmesh.samples.helloperf;

public class Stopwatch { 

    private final long start;
	private String name;

    /**
     * Initializes a new stopwatch.
     */
    public Stopwatch(String name) {
    	this.name = name;
        start = System.currentTimeMillis();
    } 


    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        return (now - start);
    }
    
   
    public String toString() {
    	return "Stopwatch:" + this.name + ":" + this.elapsedTime() + "ms";
    }
} 

