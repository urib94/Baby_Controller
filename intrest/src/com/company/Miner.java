package com.company;

public class Miner {
    private double price;
    private double dailyElectricityCost;
    private double totalElectricityCost;
    private double income;
    private double profit;
    private int daysInUse = 0;

    public Miner(double price, double electricityCost, double income) {
        this.price = price;
        this.dailyElectricityCost = electricityCost;
        this.income = income;
    }

    public double calculateDailyProfit() {
      return income - dailyElectricityCost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDailyElectricityCost() {
        return dailyElectricityCost;
    }

    public void setDailyElectricityCost(int dailyElectricityCost) {
        this.dailyElectricityCost = dailyElectricityCost;
    }

    public double getTotalElectricityCost() {
        return totalElectricityCost;
    }

    public void setTotalElectricityCost(int totalElectricityCost) {
        this.totalElectricityCost = totalElectricityCost;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getDaysInUse() {
        return daysInUse;
    }
    public void newDay(){
        daysInUse++;
    }
}
