package com.bingbingpa.ch05;

import static com.bingbingpa.ch04.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.bingbingpa.ch04.Dish;

public class Mapping {

    public static void main(String... args) {
        // map
        List<String> dishNames = menu.stream()
            .map(Dish::getName)
            .collect(toList());
        System.out.println(dishNames);
        // [pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon]
        // map
        List<String> words = Arrays.asList("Hello", "World");
        List<Integer> wordLengths = words.stream()
            .map(String::length)
            .collect(toList());
        System.out.println(wordLengths);
        // [5, 5]
        // flatMap
        words.stream()
            .flatMap((String line) -> Arrays.stream(line.split("")))
            .distinct()
            .forEach(System.out::println);
    /*
        H
        e
        l
        o
        W
        r
        d
    */


        // flatMap
        List<Integer> numbers1 = Arrays.asList(1,2,3,4,5);
        List<Integer> numbers2 = Arrays.asList(6,7,8);
        List<int[]> pairs = numbers1.stream()
            .flatMap((Integer i) -> numbers2.stream()
                .map((Integer j) -> new int[]{i, j})
            )
            .filter(pair -> (pair[0] + pair[1]) % 3 == 0)
            .collect(toList());
        pairs.forEach(pair -> System.out.printf("(%d, %d)", pair[0], pair[1]));

        // (1, 8)(2, 7)(3, 6)(4, 8)(5, 7)
    }
}