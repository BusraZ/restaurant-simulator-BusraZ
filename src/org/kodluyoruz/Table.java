package org.kodluyoruz;

import java.util.concurrent.SynchronousQueue;

public class Table implements Comparable<Table> {
    private final int number;

    private int currentTime;
    private Order order;
    private Waiter waiter;
    private Chef chef;
    private SynchronousQueue<Integer> servedTime;
    public Table(int number) {
        this.number = number;
        servedTime = new SynchronousQueue<Integer>();
        currentTime = 0;
     }

        public int getNumber() {
            return number;
        }
        public synchronized int getCurrentTime() {
            return currentTime;
        }
        public synchronized void setCurrentTime(int currentTime) {
            this.currentTime = currentTime;
        }
        public Order getOrder() {
            return order;
        }
        public void setOrder(Order order) {
            this.order = order;
        }
        public Chef getChef() {return chef;}
        public Waiter getWaiter(){
        return waiter;
       }
        public void setWaiter(Waiter waiter) {
            this.waiter = waiter;
        }
        public int getServedTime() {
            int time = -1;
            try {
                time = servedTime.take();
            } catch (InterruptedException e) {}
            return time;
        }
        public void setServedTime(int time) {
            try {
                servedTime.put(time);
            } catch (InterruptedException e) {}
         }
        @Override
        public int compareTo(Table arg0) {
            if (currentTime == arg0.currentTime)
                return 0;
            else if (currentTime < arg0.currentTime)
                return -1;
            else
                return 1;
        }
}
