package aoc;

import aoc.util.FileReader;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Third {

    private static final Pattern pattern = Pattern.compile("#\\d+\\W@\\W(\\d+),(\\d+):\\W(\\d+)x(\\d+)");

    public static void main(String[] args) {
        List<String> lines = new FileReader().readFile("input.third");
        int[][] cloth = new int[1000][1000];

        lines.forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            int xIndex = find(matcher, 1);
            int yIndex = find(matcher, 2);
            int width = find(matcher, 3);
            int height = find(matcher, 4);

            Cut cut = new Cut(xIndex, yIndex, width, height);
            cut.apply(cloth);
        });

        long count = Arrays.stream(cloth).flatMap(s -> Arrays.stream(s).boxed()).filter(i -> i >= 2).count();
        System.out.println(count);
    }

    private static int find(Matcher matcher, int index) {
        return Integer.parseInt(matcher.group(index));
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

        public void apply(int[][] array) {
            IntStream.range(xIndex, xIndex + width)
                    .forEach(x -> IntStream.range(yIndex, yIndex + height).forEach(y -> array[x][y]++));
        }
    }

}
