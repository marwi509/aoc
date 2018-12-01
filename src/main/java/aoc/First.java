package aoc;

import aoc.util.FileReader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class First {

    public static void main(String[] args) {
        Set<Integer> frequenciesReached = new HashSet<>();
        List<String> strings = new FileReader().readFile("input.first");
        System.out.println(strings);

        int sum = 0;
        frequenciesReached.add(sum);
        for(int i = 0;; i++) {
            String s = strings.get(i % strings.size());
            int f = Integer.parseInt(s);
            sum+=f;
            if(!frequenciesReached.add(sum)) {
                System.out.println(sum);
                break;
            }
        }
    }

}
