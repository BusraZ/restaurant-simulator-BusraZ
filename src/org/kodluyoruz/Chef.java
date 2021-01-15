package org.kodluyoruz;

public class Chef implements Runnable, Comparable<Chef> {
    private String threadName;
    private final int number;
    private final Restaurant restaurant;
    private Chef chef;
    private Waiter waiter;
    private int currentTime;
    private Table table;
    private Order order;
    public Chef(String name,int number, Restaurant restaurant) {
        this.threadName=name;
        this.number = number;
        this.restaurant = restaurant;
        currentTime = 0;
        table = null;
        order = null;
    }
    public int getNumber() {
        return number;
    }
    public Order getOrder() {
        return order;
    }
    public int getCurrentTime() {
        return currentTime;
    }
    public Table getTable() {
        return table;
    }
   

    public void setWaiter(Waiter waiter){
        this.waiter = waiter;
        if (waiter == null) return;
        chef.setWaiter(waiter);
        int time = this.waiter.getCurrentTime();
        if (time < currentTime) {
            this.table.setCurrentTime(currentTime);
        } else
            currentTime = time;
        order = waiter.getOrder();
    }

    @Override
    public void run() {
        try {
            while (true) {
                synchronized (this) {
                    wait();
                    restaurant.assignChef(this);
                    System.out.println(threadName + " sipariş aldı");

                    System.out.println(threadName + " siparişi hazırladı");
                    Thread.sleep(100);
                    order = null;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " is interrupted");
        }
    }

   @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Chef) {
            if (currentTime == ((Chef) obj).currentTime)
                return true;
        }
        return false;
    }
    @Override
    public int compareTo(Chef o) {
        if (currentTime < o.currentTime)
            return -1;
        else if (currentTime > o.currentTime)
            return 1;
        return 0;
    }
}
