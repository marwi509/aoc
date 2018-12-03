package aoc;

import aoc.util.FileReader;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class Third {

    private static final Pattern pattern = Pattern.compile("#(\\d+)\\W@\\W(\\d+),(\\d+):\\W(\\d+)x(\\d+)");

    public static void main(String[] args) {
        List<String> lines = new FileReader().readFile("input.third");
        Coordinate[][] coordinates = new Coordinate[1000][1000];
        IntStream.range(0, 1000).forEach(x -> IntStream.range(0, 1000).forEach(y -> coordinates[x][y] = new Coordinate()));
        Set<Integer> allClaimIds = new HashSet<>();

        lines.forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            int index = find(matcher, 1);
            allClaimIds.add(index);
            int xIndex = find(matcher, 2);
            int yIndex = find(matcher, 3);
            int width = find(matcher, 4);
            int height = find(matcher, 5);

            Cut cut = new Cut(index, xIndex, yIndex, width, height);
            cut.apply(coordinates);
        });

        long count = getCoordinateStream(coordinates).filter(i -> i.nbrClaims() >= 2).count();
        System.out.println("Count overlap: " + count);

        Set<Integer> allIdsWithOverlap = getCoordinateStream(coordinates).flatMap(coordinate -> coordinate.getClaimIdsWithOverlap().stream()).collect(Collectors.toSet());
        allClaimIds.removeAll(allIdsWithOverlap);
        System.out.println("Claim without overlap: " + allClaimIds);
    }

    private static Stream<Coordinate> getCoordinateStream(Coordinate[][] coordinates) {
        return Arrays.stream(coordinates).flatMap(Arrays::stream);
    }

    private static int find(Matcher matcher, int index) {
        return Integer.parseInt(matcher.group(index));
    }

    private static class Coordinate {
        private final List<Integer> claimIds = new ArrayList<>();

        private Coordinate() {
        }

        void increment(int claimId) {
            claimIds.add(claimId);
        }

        int nbrClaims() {
            return claimIds.size();
        }

        List<Integer> getClaimIdsWithOverlap() {
            if (claimIds.size() >= 2) {
                return claimIds;
            } else {
                return emptyList();
            }
        }

    }

    private static class Cut {
        private final int index;
        private final int xIndex;
        private final int yIndex;
        private final int width;
        private final int height;

        private Cut(int index, int xIndex, int yIndex, int width, int height) {
            this.index = index;
            this.xIndex = xIndex;
            this.yIndex = yIndex;
            this.width = width;
            this.height = height;
        }

        public void apply(Coordinate[][] coordinates) {
            IntStream.range(xIndex, xIndex + width)
                    .forEach(x -> IntStream.range(yIndex, yIndex + height).forEach(y -> coordinates[x][y].increment(index)));
        }
    }

}
