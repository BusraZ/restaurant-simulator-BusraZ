package org.kodluyoruz;

   class Customer implements Runnable,Comparable<Customer> {
    private String threadName;

    private static final int EATING_TIME = 30;
    private final int arrivalTime;
    private Order order;
    private Restaurant restaurant;

    private Table table;
    private int seatedTime;
    private int servedTime;

    public Customer(String name,int arrival, Order order, Restaurant restaurant) {
        this.threadName=name;
        arrivalTime = arrival;
        this.order = order;
        this.restaurant = restaurant;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public Table getTable() {
        return table;
    }
    public void setTable(Table table) {
        this.table = table;
        if (table == null) return;
        int time = this.table.getCurrentTime();
        if (time < arrivalTime) {
            seatedTime = arrivalTime;
            this.table.setCurrentTime(seatedTime);
        } else
            seatedTime = time;
        this.table.setOrder(order);
    }
    public int getFinishedTime() {
        return servedTime + EATING_TIME;
    }
    public void result() {
       System.out.printf("arrival: %d, seated: %d, table: %d, waiter: %d",
                arrivalTime, seatedTime, table.getNumber(), table.getWaiter().getNumber());

        System.out.println();

    }
    public void run() {
             try {
                 restaurant.enter(this);
                 System.out.println(threadName + " sipariş verdi");
                 servedTime = table.getServedTime();
                 Thread.sleep(100);
                 result();
                 restaurant.leave(this);

             } catch (InterruptedException e) {
                 System.out.println(threadName + " is interrupted");
             }
    }
      @Override
    public int compareTo(Customer arg0) {
        if (arrivalTime == arg0.arrivalTime)
            return 0;
        else if (arrivalTime < arg0.arrivalTime)
            return -1;
        else
            return 1;
    }



}
