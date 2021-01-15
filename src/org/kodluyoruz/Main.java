package org.kodluyoruz;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static int NUMBER_OF_FOOD_TYPES;

    public static void main(String[] args) {

        NUMBER_OF_FOOD_TYPES = 2;
        int numDiners = 10;
        int numTables = 5;
        int numChef = 2;
        int numWaiter = 3;
        List<Integer> arrival = new ArrayList<Integer>();
        List<Integer> food = new ArrayList<Integer>();
        for (int i = 0; i < numDiners; ++i) {
            arrival.add(5);
            food.add(1);
         }
        Restaurant restaurant = new Restaurant(numTables, numWaiter, arrival);

        for (int i = 0; i < numDiners; ++i) {
            Order order = new Order(food.get(i));
            (new Thread(new Customer("Customer", arrival.get(i), order, restaurant))).start();
        }
      for (int i = 0; i < numWaiter; ++i){
            (new Thread(new Waiter("Waiter",i, restaurant))).start();
        }

     for (int i = 0; i < numChef; ++i) {
         (new Thread(new Chef("Chef", i, restaurant))).start();}

    }
}
