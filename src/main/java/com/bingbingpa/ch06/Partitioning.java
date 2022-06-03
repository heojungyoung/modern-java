package com.bingbingpa.ch06;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.bingbingpa.ch06.Dish.menu;
import static java.util.stream.Collectors.*;

public class Partitioning {

    public static void main(String... args) {
        System.out.println("Dishes partitioned by vegetarian: " + partitionByVegetarian());
        System.out.println("Vegetarian Dishes by type: " + vegetarianDishesByType());
        System.out.println("Most caloric dishes by vegetarian: " + mostCaloricPartitionedByVegetarian());
    }

    /*
        Dishes partitioned by vegetarian: {false=[pork, beef, chicken, prawns, salmon], true=[french fries, rice, season fruit, pizza]}
        Vegetarian Dishes by type: {false={MEAT=[pork, beef, chicken], FISH=[prawns, salmon]}, true={OTHER=[french fries, rice, season fruit, pizza]}}
        Most caloric dishes by vegetarian: {false=pork, true=pizza}
    */

    private static Map<Boolean, List<Dish>> partitionByVegetarian() {
        return menu.stream().collect(partitioningBy(Dish::isVegetarian));
    }

    private static Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType() {
        return menu.stream().collect(partitioningBy(Dish::isVegetarian, groupingBy(Dish::getType)));
    }

    private static Object  mostCaloricPartitionedByVegetarian() {
        return menu.stream().collect(
                partitioningBy(Dish::isVegetarian,
                        collectingAndThen(
                                maxBy(Comparator.comparingInt(Dish::getCalories)),
                                Optional::get
                        ))
        );
    }
}
