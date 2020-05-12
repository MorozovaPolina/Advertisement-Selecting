package rest;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class Calculations {
    public static int get_advertisement(Map<String, Double> interest_evaluations) {
        int result =  Integer.parseInt(interest_evaluations.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), Collections.max(interest_evaluations.values())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).get(0));

        return result;
    }
}
