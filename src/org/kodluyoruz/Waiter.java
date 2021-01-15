package org.kodluyoruz;

import java.util.concurrent.SynchronousQueue;

class Waiter implements Runnable, Comparable<Waiter> {
    private String threadName;
    private final int number;
    private  Restaurant restaurant;


    private int currentTime;
    private Table table;
    private Order order;
    private SynchronousQueue<Integer> servedTime;
    public Waiter(int number) {
        this.number = number;
        servedTime = new SynchronousQueue<Integer>();
        currentTime = 0;
    }
    public Waiter(String name,int number, Restaurant restaurant) {
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
    public void setTable(Table table){
        this.table = table;
        if (table == null) return;
        this.table.setWaiter(this);
        int time = this.table.getCurrentTime();
        if (time < currentTime) {
            this.table.setCurrentTime(currentTime);
        } else
            currentTime = time;
        order = table.getOrder();
    }


    @Override
    public void run() {
        try {
             while (true) {
                    synchronized (this) {
                        restaurant.assignWaiter(this);
                        System.out.println(threadName + " sipariş aldı");
                        table.setServedTime(currentTime);
                        System.out.println("Chef" + " siparişi hazırladı");
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
        if (obj instanceof Waiter) {
            if (currentTime == ((Waiter) obj).currentTime)
                return true;
        }
        return false;
    }
    @Override
    public int compareTo(Waiter o) {
        if (currentTime < o.currentTime)
            return -1;
        else if (currentTime > o.currentTime)
            return 1;
        return 0;
    }
}
