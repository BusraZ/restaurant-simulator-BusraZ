package org.kodluyoruz;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Restaurant {

    private PriorityQueue<Customer> arrivedCustomers;
    private AtomicInteger numberOfRemainCustomers;

    private PriorityQueue<Table> tables;
    private BlockingQueue<Table> unassignedTables;

    private PriorityQueue<Waiter> availableWaiters;
    private int numberOfWaiter;
    private PriorityQueue<Waiter> servedWaiter;
    private AtomicInteger numberOfPreparingOrders;

    private BlockingQueue<Waiter> unassignedWaiter;
    private PriorityQueue<Waiter> waiters;
    private PriorityQueue<Chef> availableChef;
    private int numberOfChef;
    private PriorityQueue<Chef> cookingChef;

    public Restaurant(int numTables, int numWaiter, List<Integer> arrival) {
        arrivedCustomers = new PriorityQueue<Customer>();
        numberOfRemainCustomers = new AtomicInteger(arrival.size());

        tables = new PriorityQueue<Table>(numTables);
        for (int i = 0; i < numTables; ++i)
            tables.add(new Table(i));
        unassignedTables = new ArrayBlockingQueue<Table>(numTables, true);

        availableWaiters = new PriorityQueue<Waiter>();
        numberOfWaiter = numWaiter;
        servedWaiter = new PriorityQueue<Waiter>();
        numberOfPreparingOrders = new AtomicInteger(1);

    }

    public Restaurant( int numWaiter, int numChef) {
        waiters = new PriorityQueue<Waiter>(numWaiter);
        for (int i = 0; i < numWaiter; ++i)
            waiters.add(new Waiter(i));
        unassignedWaiter = new ArrayBlockingQueue<Waiter>(numWaiter, true);
        availableChef = new PriorityQueue<Chef>();
        numberOfChef = numChef;
        cookingChef = new PriorityQueue<Chef>();
       numberOfPreparingOrders = new AtomicInteger(1);
    }
    public void enter(Customer diner) {
        synchronized (arrivedCustomers) {
            arrivedCustomers.offer(diner);
            arrivedCustomers.notifyAll();
            try {
                while (arrivedCustomers.size() < numberOfRemainCustomers.get()-numberOfPreparingOrders.get()+1 ||
                        diner != arrivedCustomers.peek())
                    arrivedCustomers.wait();
                arrivedCustomers.poll();
                Table table = tables.poll();
                diner.setTable(table);
                unassignedTables.put(table);
            } catch (InterruptedException e) {}
        }
    }
    public void leave(Customer customer) {
        Table table = customer.getTable();
        customer.setTable(null);
        table.setCurrentTime(customer.getFinishedTime());

        synchronized (tables) {
            tables.add(table);
        }
        synchronized (arrivedCustomers) {
            if (numberOfRemainCustomers.decrementAndGet() == 0) {
                System.out.println(customer.getFinishedTime());
                System.exit(0);
            }
            arrivedCustomers.notifyAll();
        }
    }
    public void assignWaiter(Waiter waiter) {
        try {
            synchronized (availableWaiters) {
                availableWaiters.add(waiter);
                availableWaiters.notifyAll();
                while (availableWaiters.size() < numberOfWaiter-numberOfPreparingOrders.get()+1 ||
                        availableWaiters.peek() != waiter)
                    availableWaiters.wait();
                availableWaiters.poll();
            }
            waiter.setTable(unassignedTables.take());
        } catch (InterruptedException e) {}
    }
    public void assignChef(Chef chef) {
        try {
            synchronized (availableChef) {
                availableChef.offer(chef);
                availableChef.notifyAll();
                while (availableChef.size() < numberOfChef-numberOfPreparingOrders.get()+1 ||
                        availableChef.peek() != chef)
                    availableChef.wait();
                availableChef.poll();
            }
            chef.setWaiter(unassignedWaiter.take());
        } catch (InterruptedException e) {}
    }


    private boolean isNextOrderAvailable(Waiter waiter) {
        Customer diner = arrivedCustomers.peek();
        Waiter nextWaiter = availableWaiters.peek();
        Table table = tables.peek();

        if (diner == null || nextWaiter == null || table == null) return false;

        if (nextWaiter.getCurrentTime() < waiter.getCurrentTime() &&
                diner.getArrivalTime() < waiter.getCurrentTime() &&
                table.getCurrentTime() < waiter.getCurrentTime()) {
            return true;
        }
        return false;
    }



}
