package BrowserFactory;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String input = "programming";
        Map<Character, Integer> map = new HashMap<>();

        for (char c : input.toCharArray()) {
            if (map.containsKey(c)) {
                // If character already exists, increment the count
                map.put(c, map.get(c) + 1);
            } else {
                // If character doesn't exist, put count as 1
                map.put(c, 1);
            }
        }

        // Print only the duplicates
        System.out.println("Duplicate characters and their counts:");
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }
    }
}

