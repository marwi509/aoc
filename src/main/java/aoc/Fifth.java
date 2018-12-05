package aoc;

import aoc.util.FileReader;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Fifth {

    private static final String alphabet = "abcdefghijklmnopqrstuvqxyz";

    public static void main(String[] args) {
        Set<Integer> lengths = new HashSet<>();
        String line = new FileReader().readFile("input.fifth").get(0);

        String original = line;
        for(char c : alphabet.toCharArray()) {
            String upperCase = (""+c).toUpperCase();
            String lowerCase = (""+c).toLowerCase();
            line = original.replace(lowerCase, "").replace(upperCase, "");
            lengths.add(getResult(line, original));
        }

        System.out.println(lengths.stream().mapToInt(i -> i).min().orElseThrow());
    }

    private static int getResult(String line, String original) {
        AtomicInteger index = new AtomicInteger(0);
        while(index.get() < line.length() - 1) {
            line = checkForMatch(line, index);
            index.incrementAndGet();
        }

        System.out.println(line.length() + " " + original.length());
        return line.length();
    }

    private static String checkForMatch(String line, AtomicInteger currentIndex) {
        if (currentIndex.get() < 0 || currentIndex.get() >= line.length() - 1) {
            return line;
        }
        if (Objects.equals(("" + line.charAt(currentIndex.get())).toLowerCase(),  (""+line.charAt(currentIndex.get() + 1)).toLowerCase()) &&
                !Objects.equals(("" + line.charAt(currentIndex.get())),  (""+line.charAt(currentIndex.get() + 1)))) {
            if (currentIndex.get() <= line.length() - 2) {
                line = line.substring(0, currentIndex.get()) + line.substring(currentIndex.get() + 2);
            } else {
                line = line.substring(0, currentIndex.get());
            }
            currentIndex.decrementAndGet();
            return checkForMatch(line, currentIndex);
        }
        return line;
    }

}
