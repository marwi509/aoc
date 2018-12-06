package aoc;

import aoc.util.FileReader;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Sixth {

    private static final Pattern pattern = Pattern.compile("(\\d+),\\W(\\d+)");

    public static void main(String[] args) {
        List<String> lines = new FileReader().readFile("input.sixth");
        AtomicInteger index = new AtomicInteger(1);
        List<Coordinate> coordinates = lines.stream().map(s -> {
            Matcher matcher = pattern.matcher(s);
            matcher.matches();
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            return new Coordinate(index.getAndIncrement(), x, y);
        }).collect(Collectors.toList());

        int minX = coordinates.stream().mapToInt(c -> c.x).min().orElseThrow(RuntimeException::new);
        int maxX = coordinates.stream().mapToInt(c -> c.x).max().orElseThrow(RuntimeException::new);
        int minY = coordinates.stream().mapToInt(c -> c.y).min().orElseThrow(RuntimeException::new);
        int maxY = coordinates.stream().mapToInt(c -> c.y).max().orElseThrow(RuntimeException::new);

        int[][] closestPoints = new int[maxX+1][maxY+1];
        for(int i = minX; i <= maxX; i ++) {
            for(int j = minY; j <= maxY; j ++) {
                int minDist = Integer.MAX_VALUE;
                Coordinate closestCoord = null;
                boolean clashes = false;
                for(Coordinate coordinate : coordinates) {
                    int dist = coordinate.distanceTo(i, j);
                    if (dist == minDist) {
                        clashes = true;
                    } else if (dist < minDist) {
                        clashes = false;
                        minDist = dist;
                        closestCoord = coordinate;
                    }
                }
                if (closestCoord != null && !clashes) {
                    closestPoints[i][j] = closestCoord.id;
                }
            }
        }

        StringBuilder view = new StringBuilder();
        for(int i = minX; i <= maxX; i ++) {
            for(int j = minY; j <= maxY; j ++) {
                view.append(closestPoints[i][j]);
            }
            view.append("\n");
        }

        System.out.println(view);

        Set<Integer> disqualifiedIds = new HashSet<>();
        for(int i = minX; i <= maxX; i ++) {
            int idAtTop = closestPoints[i][0];
            int idAtBottom = closestPoints[i][maxY];
            disqualifiedIds.add(idAtTop);
            disqualifiedIds.add(idAtBottom);
        }

        List<Map.Entry<Integer, Long>> result = Arrays.stream(closestPoints).flatMapToInt(Arrays::stream)
                .filter(i -> !disqualifiedIds.contains(i))
                .mapToObj(i -> i)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().sorted(Comparator.comparing((Map.Entry<Integer, Long> e) -> e.getValue()).reversed())
                .collect(Collectors.toList());

        System.out.println(result);

        int[][] totalDistances = new int[maxX-minX+1][maxY-minY+1];
        for(int i = minX; i <= maxX; i ++) {
            for(int j = minY; j <= maxY; j ++) {
                int sumDist = 0;
                for(Coordinate coordinate : coordinates) {
                    int dist = coordinate.distanceTo(i, j);
                    sumDist += dist;
                }
                totalDistances[i-minX][j-minY] = sumDist;
            }
        }

        long lessThanOneThousand = Arrays.stream(totalDistances).flatMapToInt(Arrays::stream).filter(i -> i < 10_000).count();
        System.out.println(lessThanOneThousand);

    }

    private static final class Coordinate {
        private final int id;
        private final int x;
        private final int y;

        private Coordinate(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        private int distanceTo(int x, int y) {
            return Math.abs(this.x - x) + Math.abs(this.y - y);
        }
    }

}
