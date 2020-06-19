/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demography;

/**
 *
 * @author olao
 */
public class Samples_at_time implements Comparable<Samples_at_time>{
    public Samples_at_time(int samples, int time_backward_in_time)
    {
        this.samples = samples;
        this.time_backward_in_time = time_backward_in_time;
    }
    
    private final int samples, time_backward_in_time;
           

    /**
     * Get the samples associated to this time
     * @return 
     */
    public int getSamples() {
        return samples;
    }

    /**
     * 
     * @return 
     */
    public int getTime_backward_in_time() {
        return time_backward_in_time;
    }

    @Override
    public int compareTo(Samples_at_time t) {
        return Integer.compare(this.getTime_backward_in_time(), t.getTime_backward_in_time());
    }
}
