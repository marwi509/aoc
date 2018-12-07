package aoc;

import aoc.util.FileReader;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Seventh {

    private static final Pattern pattern = Pattern.compile("Step (.) must be finished before step (.) can begin.");

    public static void main(String[] args) {
        List<String> strings = new FileReader().readFile("input.seventh");
        System.out.println(strings);

        Map<String, Set<String>> stepsToDo = new LinkedHashMap<>();

        strings.stream().sorted().forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            matcher.matches();
            String requirement = matcher.group(1);
            String thisLetter = matcher.group(2);
            Set<String> requirements = stepsToDo.computeIfAbsent(thisLetter, s -> new HashSet<>());
            requirements.add(requirement);

            stepsToDo.computeIfAbsent(requirement, s -> new HashSet<>());
        });
        Set<String> completedSteps = new LinkedHashSet<>();

        List<Step> stepsList = stepsToDo.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(e -> new Step(e.getKey(), e.getValue())).collect(Collectors.toList());

        int index = 0;

        while (index < stepsList.size()) {
            Step currentStep = stepsList.get(index);
            Set<String> requirements = currentStep.getRequirements();
            if (completedSteps.containsAll(requirements)) {
                completedSteps.add(currentStep.getLetter());
                stepsList.remove(index);
                index = 0;
            } else {
                index++;
            }
        }

        StringBuilder sb = new StringBuilder();
        completedSteps.forEach(sb::append);
        System.out.println("Step one: " + sb.toString());

        completedSteps.clear();
        List<Step> stepsListMulti = stepsToDo.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey)).map(e -> new Step(e.getKey(), e.getValue())).collect(Collectors.toList());

        List<Worker> workers = IntStream.range(0, 5).mapToObj(i -> new Worker()).collect(Collectors.toList());

        int secondsSpent = 0;
        while (!stepsListMulti.isEmpty()) {
            if (workers.stream().allMatch(w -> w.getCurrentTask().isPresent())) {
                int secondsToWork = workers.stream().mapToInt(w -> w.getSecondsLeft()).min().orElseThrow(RuntimeException::new);
                workers.forEach(w -> {
                    Optional<String> result = w.work(secondsToWork);
                    result.ifPresent(completedSteps::add);
                });
                secondsSpent += secondsToWork;
            }

            List<String> candidates = new ArrayList<>();
            for (Step currentStep : stepsListMulti) {
                Set<String> requirements = currentStep.getRequirements();
                if (completedSteps.containsAll(requirements)) {
                    candidates.add(currentStep.getLetter());
                }
            }

            for (String task : candidates) {
                if (workers.stream().allMatch(w -> w.getCurrentTask().isPresent())) break;
                for (Worker worker : workers) {
                    if (!worker.getCurrentTask().isPresent()) {
                        worker.startTask(task);
                        stepsListMulti.removeIf(s -> s.getLetter().equals(task));
                        break;
                    }
                }
            }

            List<Worker> workToDo = workers.stream().filter(w -> w.getCurrentTask().isPresent()).collect(Collectors.toList());
            int secondsToWork = workToDo.stream().mapToInt(Worker::getSecondsLeft).min().orElseThrow(RuntimeException::new);
            workToDo.forEach(w -> {
                Optional<String> result = w.work(secondsToWork);
                result.ifPresent(completedSteps::add);
            });
            secondsSpent += secondsToWork;
        }

        sb = new StringBuilder();
        completedSteps.forEach(sb::append);
        System.out.println("Step one: " + sb.toString());
        System.out.println(secondsSpent);

        System.out.println(completedSteps.stream().map(s -> getSeconds(s)).mapToInt(i -> i).sum());
    }

    private static class Worker {

        private String currentTask;
        private int secondsLeft;

        public Optional<String> getCurrentTask() {
            return Optional.ofNullable(currentTask);
        }

        public void startTask(String currentTask) {
            this.currentTask = currentTask;
            secondsLeft = getSeconds(currentTask);
            //System.out.println("Worker starting " + currentTask + " with " + secondsLeft);
        }

        public Optional<String> work(int secondsToWork) {
            secondsLeft -= secondsToWork;
            //System.out.println(secondsLeft);
            if (secondsLeft == 0) {
                String task = currentTask;
                currentTask = null;
                return Optional.of(task);
            } else {
                return Optional.empty();
            }
        }

        public String finishWork() {
            return work(secondsLeft).get();
        }

        public int getSecondsLeft() {
            return secondsLeft;
        }

    }

    private static int getSeconds(String currentTask) {
        return currentTask.charAt(0) - 'A' + 1 + 60;
    }

    private static class Step {
        private final String letter;
        private final Set<String> requirements;

        private Step(String letter, Set<String> requirements) {
            this.letter = letter;
            this.requirements = requirements;
        }

        public String getLetter() {
            return letter;
        }

        public Set<String> getRequirements() {
            return requirements;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Step step = (Step) o;
            return letter.equals(step.letter) &&
                    requirements.equals(step.requirements);
        }

        @Override
        public int hashCode() {
            return Objects.hash(letter, requirements);
        }
    }

}
