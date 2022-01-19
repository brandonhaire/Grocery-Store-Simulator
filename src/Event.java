/*
Brandon Haire
CS1181 Project04
Michelle Cheatham
 */
package project4help;


public class Event implements Comparable<Event> {

    private double time;
    private Customer theCustomer;

    public Event(double time, Customer theCustomer){
        this.time = time;
        this.theCustomer = theCustomer;
    }
    public Customer getTheCustomer() {
        return theCustomer;
    }

    public double getTime() {
        return time;
    }
    
    @Override
    public int compareTo(Event o) {
        if (time > o.time) {
            return 1;
        } else if (time < o.time) {
            return -1;
        } else {
            return 0;
        }
    }

}