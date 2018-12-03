package aoc;

import aoc.util.FileReader;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Third {

    private static final Pattern pattern = Pattern.compile("#\\d+\\W@\\W(\\d+),(\\d+):\\W(\\d+)x(\\d+)");
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;

    public static void main(String[] args) {
        List<String> lines = new FileReader().readFile("input.third");
        int[][] cloth = new int[1000][1000];

        Map<Integer, Long> collect = lines.stream().map(line -> {
            System.out.println(line);
            Matcher matcher = pattern.matcher(line);
            System.out.println(matcher.matches());
            int xIndex = find(matcher, 1);
            int yIndex = find(matcher, 2);
            int width = find(matcher, 3);
            int height = find(matcher, 4);

            System.out.println(xIndex + " " + yIndex + " " + width + " " + height);

            Cut cut = new Cut(xIndex, yIndex, width, height);
            System.out.println(cut.allCoveredIndexes().boxed().collect(toList()).size());
            return cut;

        }).flatMapToInt(Cut::allCoveredIndexes).boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long count = collect.entrySet().stream().filter(i -> i.getKey() >= 2).count();
        System.out.println(count);

    }

    private static int find(Matcher matcher, int index) {
        return Integer.parseInt(matcher.group(index));
    }

    private static int flattenIndex(int xIndex, int yIndex) {
        if (xIndex >= 1000 || yIndex >= 1000) {
            throw new IllegalStateException();
        }
        return yIndex * WIDTH + xIndex;
    }

    private static class Cut {
        private final int xIndex;
        private final int yIndex;
        private final int width;
        private final int height;

        private Cut(int xIndex, int yIndex, int width, int height) {
            this.xIndex = xIndex;
            this.yIndex = yIndex;
            this.width = width;
            this.height = height;
        }

        public IntStream allCoveredIndexes() {
            return IntStream.range(xIndex, xIndex + width)
                    .flatMap(x -> IntStream.range(yIndex, yIndex + height).map(y -> flattenIndex(x, y)));
        }
    }

}
