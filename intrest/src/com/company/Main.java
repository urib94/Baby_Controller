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

    public static int[] q4(int[] arr) {
        int n = arr.length;
        int a = 9;
        while (n > 0 ){
            System.out.println(" before swich the pivot " + Arrays.toString(arr));
            partitionWithPivot(arr, 0, n, n / 2);


            n /=  2;
            a--;
        }
        return arr;
    }

    public static int partitionWithPivot(int[] arr, int start, int end, int pivot) {
        int tmp = arr[pivot];
        arr[pivot] = arr[end - 1];
        arr[end - 1] = tmp;
        System.out.println(" after swich the pivot " + Arrays.toString(arr));
        return partition(arr, start, end );
    }

    public static int partition(int[] arr, int start, int end) {
        int x = arr[end -1];
        System.out.println("the pivot element is " + x);
        int i = start - 1;
        for (int j = start; j < end ; j++) {

            if (arr[j] <= x) {
                i++;
                int tmpI = arr[i];
                int tmpJ = arr[j];
                arr[i] = arr[j];
                arr[j] = tmpI;
                System.out.println(" in loop : after switch between a[i] (" + tmpI + ") & and arr[j](" + tmpJ + ")" + "arr is: " + Arrays.toString(arr) + " i is " + i + " j is " + j);

            }
        }
        int tmp = arr[i + 1];
        int tmp2 = arr[end - 1];
        arr[i + 1] = arr[end -1];
        arr[end -1] = tmp;
        System.out.println(" after switch between a[i + 1] (" + tmp + ") & and arr[end](" + tmp2 + ")" + "arr is: " + Arrays.toString(arr));
        return i + 1;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(q4(new int[]{-17, 5, 34, 2, 10, -21, 15})));
    //        System.out.println((String.valueOf(yearlyYield(0.0137, 1000, 365))));

    }

    // return if ther is a ellemnt in the array that is equal to 1 in a binary arrey of size n containing  no elemnts that are equal to 1 or n/2 elemnts that are equal to 1 minimum complexity

}
