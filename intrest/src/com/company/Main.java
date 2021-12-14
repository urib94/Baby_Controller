package com.company;

import java.util.Arrays;

public class Main {


    public static double yearlyYield(double precentage, double amount, int duration) {
        int i = 1;
        while (duration > 0) {
            amount += amount * ( precentage / 100);
            System.out.println("in day " + i + " the amount is " + amount);
            duration--;
            i++;
        }
        return amount;
    }

    public static double yearlyYieldPlusSaving(double precentage, double amount, int duration, int monthlySaving) {
        int i = 1;
        while (duration > 0) {
            if(duration % 30 == 0){
                amount += monthlySaving;
            }
            amount += amount * ( precentage / 100);
            System.out.println("in day " + i + " the amount is " + amount);
            duration--;
            i++;
        }
        return amount;
    }

    public static void main(String[] args) {
        System.out.println((String.valueOf(yearlyYieldPlusSaving(0.0241095890410959, 7000, 3650 * 5,300))));

    }

    // return if ther is a ellemnt in the array that is equal to 1 in a binary arrey of size n containing  no elemnts that are equal to 1 or n/2 elemnts that are equal to 1 minimum complexity

}
