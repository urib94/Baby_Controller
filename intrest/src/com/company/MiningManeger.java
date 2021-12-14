package com.company;

import java.util.LinkedList;

public class MiningManeger {
    private double income;
    private double profit;

    public LinkedList<Miner> minersList = new LinkedList<>();

    public void addMiner(Miner miner){
        minersList.add(miner);
    }

    //calculate daily electricity cost
    public double calculateDailyElectricityCost(){
        double tmpCost = 0;
        for(Miner miner : minersList){
            tmpCost += miner.getDailyElectricityCost();
        }
        return tmpCost;
    }

    //calculate daily electricity cost
    public void calculateIncome(){
        int tmpIncome = 0;
        for(Miner miner : minersList){
            tmpIncome += miner.getIncome();
        }
        income = tmpIncome;
    }
    //calculate income
    public double calculateDailyIncome(){
        double tmpIncome = 0;
        for(Miner miner : minersList){
            tmpIncome += miner.getIncome();
        }
        return tmpIncome;
    }

    //calculate profit per duration
    public void updateProfitPerDuration(int duration){
        while (duration > 0){
            profit += income;
            duration--;
        }
    }

    //calculate daily profit
    public double calculateDailyProfit(){
        double income = 0;
        double electricityCost = 0;

        for(Miner miner : minersList){
            income += miner.getIncome();
            electricityCost += miner.getDailyElectricityCost();
        }
        return income - electricityCost;
    }

    //new day for all miners
    public void newDay(){
        for(Miner miner : minersList){
            miner.newDay();
        }
    }

    //remove all miners that older tha n days
    public void removeOldMiners(int days){
        for (int i = 0; i < minersList.size(); i++){
            if (minersList.get(i).getDaysInUse() > days){
                minersList.remove(0);
                i--;
            }
            else break;
        }
    }

    //calculate profit per duration and buy new miner when profit is enough
    public long updateProfitPerDurationAndBuyNewMiner(int duration, double monthlyDividendPercentage, int dividendAfterDays){
        double monthlyIncome = 0, monthlyElectricityCost = 0, monthlyProfit = 0, months = 0, totalIncome = 0, totalProfit = 0;
        double tmpProfit = 0;
        int i = 0;
        while (duration > 0) {
            newDay();
            tmpProfit += calculateDailyProfit();
            monthlyProfit += calculateDailyProfit();
            monthlyIncome += calculateDailyIncome();
            totalIncome += calculateDailyIncome();
            monthlyElectricityCost += calculateDailyElectricityCost();
            duration--;
            if (i != 0 && i % 30 == 0) {
                if(months >= 66)
                    i = i;
                if(i > 880)
                    removeOldMiners(890);
                System.out.println("at the " + ((int) months + 1) + "st month -" + " Income: " + (int)monthlyIncome + " Electricity Cost: " + (int)monthlyElectricityCost + " Profit: "
                        +"total profit so far = " + ((int)totalProfit ) + " monthly profit " +  (long) monthlyProfit + " amount of miners: " + minersList.size());
                if(i >= dividendAfterDays) {
                    totalProfit += tmpProfit * (monthlyDividendPercentage / 100);
                    tmpProfit -= tmpProfit * (monthlyDividendPercentage / 100);
                }
                monthlyProfit = 0;
                monthlyIncome = 0;
                monthlyElectricityCost = 0;
                months++;

            }
            tmpProfit = timeToBuyNewMiner(tmpProfit);
            i++;
        }
            return (long) tmpProfit + (long) totalProfit;
    }

    private double timeToBuyNewMiner(double tmpProfit) {
        if (tmpProfit >= minersList.get(0).getPrice()) {
            tmpProfit -= minersList.get(0).getPrice();
            buyNewMiners(minersList.get(1).getPrice(), minersList.get(1).getDailyElectricityCost(), minersList.get(1).getIncome(), 1);
        }
        return tmpProfit;
    }

    //buy new miner
    public void buyNewMiners(double price, double electricityCost, double income, double amount){
        while (amount > 0){
            minersList.add(new Miner(price, electricityCost, income));
            amount--;
        }
    }

    public static void main(String[] args) {
        MiningManeger miningManeger = new MiningManeger();
        miningManeger.buyNewMiners(3000,0.84,10.87,11);
        System.out.println("total profit is : " + miningManeger.updateProfitPerDurationAndBuyNewMiner(3650,60,365));
    }
}
