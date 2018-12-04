package aoc;

import aoc.util.FileReader;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Fourth {

    private static final Pattern pattern = Pattern.compile("\\[(.*)]\\W(.*)");
    private static final Pattern timePattern = Pattern.compile("\\d\\d\\d\\d-\\d\\d-\\d\\d\\W\\d\\d:(\\d\\d)");
    private static final Pattern guardPattern = Pattern.compile("Guard #(\\d+) begins shift");
    private static final Pattern fallAsleepPattern = Pattern.compile("falls asleep");
    private static final Pattern wakesUpPattern = Pattern.compile("wakes up");

    public static void main(String[] args) {
        List<String> lines = new FileReader().readFile("input.fourth");


        List<Event> events = lines.stream().map(line -> {
            System.out.println(line);
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            String date = matcher.group(1);
            String event = matcher.group(2);

            Matcher timeMatcher = timePattern.matcher(date);
            timeMatcher.matches();
            int minute = Integer.parseInt(timeMatcher.group(1));

            return new Event(date, event, minute);
        }).sorted(Comparator.comparing(Event::getDate)).collect(Collectors.toList());

        Map<Integer, Guard> guards = new HashMap<>();
        Guard currentGuard = null;
        int lastSleepMinute = -1;
        for (Event currentEvent : events) {
            Matcher guardMatcher = guardPattern.matcher(currentEvent.event);
            Matcher fallAsleepMatcher = fallAsleepPattern.matcher(currentEvent.getEvent());
            Matcher wakeUpMatcher = wakesUpPattern.matcher(currentEvent.getEvent());
            if (guardMatcher.matches()) {
                int guardId = Integer.parseInt(guardMatcher.group(1));
                System.out.println("Guard id: " + guardId);
                currentGuard = guards.computeIfAbsent(guardId, Guard::new);
            } else if (fallAsleepMatcher.matches()) {
                lastSleepMinute = currentEvent.getMinute();
                System.out.println("Fall asleep at: " + lastSleepMinute);
            } else if (wakeUpMatcher.matches()) {
                System.out.println("Sleeping from " + lastSleepMinute + " to " + currentEvent.getMinute());
                for (int j = lastSleepMinute; j < currentEvent.getMinute(); j++) {
                    currentGuard.logSleep(j);
                }
            }

        }

        Guard guard = guards.values().stream().max(Comparator.comparing(Guard::totalSleep)).orElseThrow();

        System.out.println("Guard " + guard.getId() + " with " + guard.totalSleep() + " for minute " + guard.minuteWithMostSleep());
        System.out.println("Part one:" + guard.getId() * guard.minuteWithMostSleep());


        Guard bestGuard = guards.values().stream().max(Comparator.comparing(Guard::maxSleepOneMinute)).orElseThrow();
        System.out.println("Part two: " + bestGuard.getId() * bestGuard.minuteWithMostSleep());
    }

    private static class Guard {
        private final int[] sleep = new int[60];
        private final int id;

        private Guard(int id) {
            this.id = id;
        }

        public void logSleep(int minute) {
            sleep[minute]++;
        }

        public int totalSleep() {
            return Arrays.stream(sleep).sum();
        }

        public int minuteWithMostSleep() {
            int maxId = -1;
            int currentMax = Integer.MIN_VALUE;
            for (int i = 0; i < sleep.length; i ++) {
                if(sleep[i] > currentMax) {
                    maxId = i;
                    currentMax = sleep[i];
                }
            }
            return maxId;
        }

        public int maxSleepOneMinute() {
            return Arrays.stream(sleep).max().orElseThrow();
        }

        public int[] getSleep() {
            return sleep;
        }

        public int getId() {
            return id;
        }
    }

    private static class Event {
        private final String date;
        private final String event;
        private final int minute;

        private Event(String date, String event, int minute) {
            this.date = date;
            this.event = event;
            this.minute = minute;
        }

        public String getDate() {
            return date;
        }

        public String getEvent() {
            return event;
        }

        public int getMinute() {
            return minute;
        }
    }

}
