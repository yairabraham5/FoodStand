package com.example.foodstand;

public class Order {

    private String customerName;
    private int pickles;
    private boolean hummus;
    private boolean tahini;
    private String comment;
    private String status;
    public Order(){}

        public Order(String comment, String customerName,  boolean hummus, int pickles,  String status,
                     boolean tahini) {
        this.customerName = customerName;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
        this.comment = comment;
        this.status = status;
        }


    public int getPickles() {
        return pickles;
    }

    public String getComment() {
        return comment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStatus() {
        return status;
    }

    public boolean isHummus() {
        return hummus;
    }

    public boolean isTahini() {
        return tahini;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setPickles(int pickles) {
        this.pickles = pickles;
    }

    public void setHummus(boolean hummus) {
        this.hummus = hummus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTahini(boolean tahini) {
        this.tahini = tahini;
    }
}
