package it.italiancoders.mybudgetrest.utils;

import java.util.Random;

public class CommonUtils {
    public static int generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m);
    }
}
