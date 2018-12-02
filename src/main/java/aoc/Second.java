package aoc;

import aoc.util.FileReader;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Second {

    public static void main(String[] args) {
        List<String> strings = new FileReader().readFile("input.second");
        System.out.println(strings);

        AtomicInteger sumNbrTwice = new AtomicInteger();
        AtomicInteger sumNbrThrice = new AtomicInteger();

        strings.forEach(s -> {
            if (matchesNbrExactly(s, 2)) {
                sumNbrTwice.getAndIncrement();
            }
            if (matchesNbrExactly(s, 3)) {
                sumNbrThrice.getAndIncrement();
            }
        });

        System.out.println(sumNbrThrice.get() * sumNbrTwice.get());


        System.out.println(new BoxId("evsialjqydnrohxypwbcngtjmf").mutations());

        Map<String, Long> countMap = strings.stream().map(BoxId::new).map(BoxId::mutations).flatMap(Collection::stream).collect(groupingBy(Function.identity(), counting()));

        List<String> result = countMap.entrySet().stream().filter(e -> e.getValue() >= 2).map(e -> e.getKey().replace("$", "")).collect(Collectors.toList());

        System.out.println(result);
    }

    private static class BoxId {
        private final String id;

        private BoxId(String id) {
            this.id = id;
        }

        public List<String> mutations() {
            return IntStream.range(0, id.length())
                    .mapToObj(i -> id.substring(0, i) + '$' + id.substring(i + 1))
                    .collect(Collectors.toList());
        }

    }

    private static boolean matchesNbrExactly(String str, int n) {
        int[] array = new int[Character.MAX_VALUE];
        for(char c : str.toCharArray()) {
            array[c]++;
        }
        return Arrays.stream(array).anyMatch(i -> i == n);
    }
}
