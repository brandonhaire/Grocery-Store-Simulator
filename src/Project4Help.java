/*
Brandon Haire
CS1181 Project04
Michelle Cheatham
 */
package project4help;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Project4Help {

/**
 * This program simulates a grocery store and the effect of opening or closing different
 * numbers of lanes and express lanes on wait time
 * @param args 
 */
    public static void main(String[] args) {

        PriorityQueue<Event> q = new PriorityQueue<>();

        // create all of the checkout lanes
        ArrayList<CheckoutLane> lanes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            lanes.add(new RegularCheckoutLane());
        }
        for (int i = 0; i < 2; i++) {
            lanes.add(new ExpressLane());
        }

        // read in the data file
        // create a new customer object
        ArrayList<Customer> customers = new ArrayList<>();
        File file = new File("arrival medium.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                customers.add(new Customer(scanner.nextDouble(), scanner.nextInt(), scanner.nextDouble()));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        // from each line, create a new ArrivalEvent and put it in the queue q
        for (int i = 0; i < customers.size(); i++) {
            q.offer(new ArrivalEvent(customers.get(i).getArrivalTime(), customers.get(i)));
        }

        while (!q.isEmpty()) {
            //print out the time and what event is next
            System.out.printf("%.2f",(double)Math.round((q.peek().getTime())*100)/100);
            if(q.peek() instanceof ArrivalEvent){
                System.out.println(" Arrival Customer " + customers.indexOf(q.peek().getTheCustomer()));
            }else if(q.peek() instanceof FinishedShoppingEvent){
                System.out.println(" Finished Shopping Customer " + customers.indexOf(q.peek().getTheCustomer()));
            } else{
                System.out.println(" Checkout Event Customer " + customers.indexOf(q.peek().getTheCustomer()));
            }
            
            //pick next event from q
            Event nextEvent = q.poll();
            double simClock = nextEvent.getTime();
            Customer cust = nextEvent.getTheCustomer();
            if (nextEvent instanceof ArrivalEvent) {
                // create a FinishedShopping event for this customer and add 
                // it to the queue q
                double fst = (double) Math.round((nextEvent.getTime() + (cust.getNumItems() * cust.getTimePerItem())) * 100) / 100;
                q.offer(new FinishedShoppingEvent(fst, cust));
                cust.setFinishedShoppingTime(fst);

            } else if (nextEvent instanceof FinishedShoppingEvent) {
                int temp = 0;
                // pick a checkout lane for this customer
                // add them to that lane (probably a queue or arraylist)
                if (cust.getNumItems() <= 12) {
                    //check all lanes
                    temp = 0;
                    for (int i = 0; i < lanes.size(); i++) {
                        if (lanes.get(i).size() < lanes.get(temp).size()) {
                            temp = i;
                        }
                    }
                } else {
                    //check all regular checkout lanes
                    temp = 0;
                    for (int i = 0; i < lanes.size(); i++) {
                        if (lanes.get(i) instanceof RegularCheckoutLane) {
                            if (lanes.get(i).size() < lanes.get(temp).size()) {
                                temp = i;
                            }
                        }
                    }
                }
                cust.setLane(lanes.get(temp));
                System.out.printf("%.2f",(double)Math.round((nextEvent.getTime())*100)/100);
                System.out.println(" chose lane " + lanes.indexOf(cust.getLane()) + "(" + cust.getLane().size() + ")");
                lanes.get(temp).add(cust);
                

                // schedule a CheckoutEvent based on how many people are ahead
                // of them in that line, add that event to the q
                if (cust.getLane().size() == 1) {       //if customer is only one in line
                    cust.setFrontOfLine(simClock);
                    q.offer(new CheckoutEvent(cust.getFinishedShoppingTime() + cust.getLengthOfCheckout(), cust));
                    cust.setCheckoutTime(cust.getFinishedShoppingTime() + cust.getLengthOfCheckout());
                }

            } else { // checkout event
                cust.getLane().remove(cust);        //remove customer from lane
                //schedule next person in line to checkout
                if (!(cust.getLane().isEmpty())) {
                    cust.getLane().get(0).setFrontOfLine(simClock);
                    Customer nextPerson = cust.getLane().get(0);
                    q.offer(new CheckoutEvent(nextPerson.getLengthOfCheckout() + simClock, nextPerson));
                    nextPerson.setCheckoutTime(nextPerson.getLengthOfCheckout()+simClock);

                }
            }
        }
//prints out all customers' timestamps
        System.out.printf("%s\t%s\t%s\t%s\t%s\t%s\t%s\n", "#Itms", "TPI", "Arrive", "FST", "FOL", "COT", "LOC");
        double average = 0;
        for (int i = 0; i < customers.size(); i++) {
            System.out.println(customers.get(i));
            average += (customers.get(i).getFrontOfLine()- customers.get(i).getFinishedShoppingTime());
        }
        average = (double) Math.round((average / customers.size()) * 100) / 100;
        System.out.println("Average wait time: " + average);
    }
}