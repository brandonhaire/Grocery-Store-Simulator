/*
Brandon Haire
CS1181 Project04
Michelle Cheatham
 */
package project4help;


public class Customer {

    private double arrivalTime;
    private int numItems;
    private double timePerItem;
    private double finishedShoppingTime;
    private double checkoutTime;
    private double lengthOfCheckout;
    private double frontOfLine;
    private CheckoutLane lane;

    public Customer(double arrivalTime, int numItems, double timePerItem) {
        this.arrivalTime = arrivalTime;
        this.numItems = numItems;
        this.timePerItem = timePerItem;
    }

    public String toString() {
        return String.format("%d\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f", numItems, timePerItem, arrivalTime, finishedShoppingTime, frontOfLine, checkoutTime, lengthOfCheckout);
    }

    public double getArrivalTime() {
        return arrivalTime;
    }

    public int getNumItems() {
        return numItems;
    }

    public double getTimePerItem() {
        return timePerItem;
    }

    public double getFinishedShoppingTime() {
        return finishedShoppingTime;
    }

    public void setFinishedShoppingTime(double finishedShoppingTime) {
        this.finishedShoppingTime = finishedShoppingTime;
    }

    public double getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(double checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public double getLengthOfCheckout() {
        return lengthOfCheckout;
    }

    public CheckoutLane getLane() {
        return lane;
    }

    public double getFrontOfLine() {
        return frontOfLine;
    }

    public void setFrontOfLine(double frontOfLine) {
        this.frontOfLine = frontOfLine;
    }
    

    public void setLane(CheckoutLane lane) {
        this.lane = lane;
        if(this.lane instanceof RegularCheckoutLane){
           this.lengthOfCheckout = (double)Math.round(((this.numItems * 0.05) + 2.0)*100)/100;
        } else if(this.lane instanceof ExpressLane){
            this.lengthOfCheckout = (double)Math.round(((this.numItems * 0.10) + 1.0)*100)/100;
        }
    }

}