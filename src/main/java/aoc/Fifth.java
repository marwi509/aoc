package aoc;

import aoc.util.FileReader;
import org.apache.commons.lang.time.StopWatch;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Fifth {

    private static final String alphabet = "abcdefghijklmnopqrstuvqxyz";
    private static final StopWatch sw = new StopWatch();

    public static void main(String[] args) {
        sw.start();
        Set<Integer> lengths = new HashSet<>();
        String line = new FileReader().readFile("input.fifth").get(0);

        String original = line;
        for (char c : alphabet.toCharArray()) {
            StringBuilder sb = new StringBuilder();
            String upperCase = ("" + c).toUpperCase();
            String lowerCase = ("" + c).toLowerCase();
            line = original.replace(lowerCase, "").replace(upperCase, "");
            sb.append(line);
            lengths.add(getResult(sb));
        }

        System.out.println(lengths.stream().mapToInt(i -> i).min().orElseThrow());
        System.out.println(sw.getTime());
    }

    private static int getResult(StringBuilder line) {
        AtomicInteger index = new AtomicInteger(0);
        while (index.get() < line.length() - 1) {
            line = checkForMatch(line, index);
            index.incrementAndGet();
        }

        return line.length();
    }

    private static StringBuilder checkForMatch(StringBuilder line, AtomicInteger currentIndex) {
        if (currentIndex.get() < 0 || currentIndex.get() >= line.length() - 1) {
            return line;
        }
        if (Objects.equals(("" + line.charAt(currentIndex.get())).toLowerCase(), ("" + line.charAt(currentIndex.get() + 1)).toLowerCase()) &&
                !Objects.equals(("" + line.charAt(currentIndex.get())), ("" + line.charAt(currentIndex.get() + 1)))) {
            line = line.replace(currentIndex.get(), currentIndex.get() + 2, "");//line.substring(0, currentIndex.get()) + line.substring(currentIndex.get() + 2);
            currentIndex.decrementAndGet();
            return checkForMatch(line, currentIndex);
        }
        return line;
    }

}
