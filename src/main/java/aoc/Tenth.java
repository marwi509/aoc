package aoc;

import aoc.util.FileReader;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tenth {

    private static final Pattern pattern = Pattern.compile(".*?([+-]?\\d+).*?([+-]?\\d+).*?([+-]?\\d+).*?([+-]?\\d+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public static void main(String[] args) {
        List<String> strings = new FileReader().readFile("input.tenth");

        PointList pointList = new PointList();
        strings.stream().map(line -> {
            Matcher m = pattern.matcher(line);

            if (m.find()) {
                String int1 = m.group(1);
                String int2 = m.group(2);
                String int3 = m.group(3);
                String int4 = m.group(4);

                return new Point(
                        Integer.parseInt(int1),
                        Integer.parseInt(int2),
                        Integer.parseInt(int3),
                        Integer.parseInt(int4));

            } else {
                throw new RuntimeException();
            }
        }).forEach(pointList::addPoint);

        List<Long> areas = new ArrayList<>();

        for (int i = 1; i < 1_000_000; i++) {
            pointList.iterate();
            long area = pointList.boundingBox().area();
            areas.add(area);
            if (area < 600) {
                System.out.println(i);
                pointList.print();
                return;
            }
        }

        System.out.println(areas.stream().mapToLong(i -> i).min().getAsLong());
    }

    private static class Rectangle {
        private final long minX;
        private final long maxX;
        private final long minY;
        private final long maxY;

        private Rectangle(long minX, long maxX, long minY, long maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }

        long area() {
            return (maxX - minX) * (maxY - minY);
        }
    }

    private static class PointList {
        private List<Point> points = new ArrayList<>();
        private Set<Point> pointsSet = new HashSet<>();

        public void addPoint(Point point) {
            points.add(point);
            pointsSet.add(point);
        }

        public Rectangle boundingBox() {
            long minX = points.stream().mapToLong(Point::getX).min().orElseThrow(RuntimeException::new);
            long maxX = points.stream().mapToLong(Point::getX).max().orElseThrow(RuntimeException::new);
            long minY = points.stream().mapToLong(Point::getY).min().orElseThrow(RuntimeException::new);
            long maxY = points.stream().mapToLong(Point::getY).max().orElseThrow(RuntimeException::new);
            return new Rectangle(
                    minX,
                    maxX,
                    minY,
                    maxY);
        }

        public void print() {
            Rectangle rectangle = boundingBox();
            StringBuilder buffer = new StringBuilder();
            for (long j = rectangle.minY - 1; j <= rectangle.maxY + 1; j++) {
                for (long i = rectangle.minX - 1; i <= rectangle.maxX + 1; i++) {
                    if (pointsSet.contains(new Point(i, j))) {
                        buffer.append('#');
                    } else {
                        buffer.append('.');
                    }
                }
                buffer.append('\n');
            }
            System.out.println(buffer.toString());
        }

        void iterate() {
            points.forEach(Point::iterate);
            pointsSet = new HashSet<>(points);
        }

    }

    private static class Point {
        private long x;
        private long y;
        private final long xSpeed;
        private final long ySpeed;

        private Point(long x, long y) {
            this(x, y, 0, 0);
        }

        private Point(long x, long y, long xSpeed, long ySpeed) {
            this.x = x;
            this.y = y;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
        }

        public long getX() {
            return x;
        }

        public long getY() {
            return y;
        }

        void iterate() {
            this.x = this.x + xSpeed;
            this.y = this.y + ySpeed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
