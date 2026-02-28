package utils;

import java.util.UUID;
import java.util.Random;

public class Utils {

    // Generate random title
    public static String generateRandomTitle() {
        // Ambil 8 karakter pertama dari UUID
        return "Title-" + UUID.randomUUID().toString().substring(0, 8);
    }

    // Generate random two-digit number
    public static int getRandomTwoDigit() {
        return new Random().nextInt(90) + 10;  // 10–99
    }

    // Generate dynamic category name
    public static String getCategoryName() {
        return "category" + getRandomTwoDigit();
    }

    // Generate random number untuk password
    public int randomNum = new Random().nextInt(99999);
    String randomPassword = "wrongPass" + randomNum;
}
