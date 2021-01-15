package org.kodluyoruz;

public class Order {
    private final int orderNum;

    public Order(int orderNum) {
        this.orderNum = orderNum;
    }

    public String toString() {
        return "Meal " + orderNum;
    }


}
