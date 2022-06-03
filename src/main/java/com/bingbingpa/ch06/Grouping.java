package com.bingbingpa.ch06;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static com.bingbingpa.ch06.Dish.dishTags;
import static com.bingbingpa.ch06.Dish.menu;
import static java.util.stream.Collectors.*;

public class Grouping {

    public static void main(String... args) {
        System.out.println("Dishes grouped by type: " + groupDishesByType());
        System.out.println("Dish names grouped by type: " + groupDishNamesByType());
        System.out.println("Dish tags grouped by type: " + groupDishTagsByType());
        System.out.println("Caloric dishes grouped by type: " + groupCaloricDishesByType());
        System.out.println("Dishes grouped by caloric level: " + groupDishesByCaloricLevel());
        System.out.println("Dishes grouped by type and caloric level: " + groupDishedByTypeAndCaloricLevel());
        System.out.println("Count dishes in groups: " + countDishesInGroups());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByType());
        System.out.println("Most caloric dishes by type: " + mostCaloricDishesByTypeWithoutOptionals());
        System.out.println("Sum calories by type: " + sumCaloriesByType());
        System.out.println("Caloric levels by type: " + caloricLevelsByType());
    }

    /*
        Dishes grouped by type: {OTHER=[french fries, rice, season fruit, pizza], FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}
        Dish names grouped by type: {OTHER=[french fries, rice, season fruit, pizza], FISH=[prawns, salmon], MEAT=[pork, beef, chicken]}
        Dish tags grouped by type: {OTHER=[salty, greasy, natural, light, tasty, fresh, fried], FISH=[roasted, tasty, fresh, delicious], MEAT=[salty, greasy, roasted, fried, crisp]}
        Caloric dishes grouped by type: {OTHER=[french fries, pizza], FISH=[], MEAT=[pork, beef]}
        Dishes grouped by caloric level: {DIET=[chicken, rice, season fruit, prawns], NORMAL=[beef, french fries, pizza, salmon], FAT=[pork]}
        Dishes grouped by type and caloric level: {OTHER={DIET=[rice, season fruit], NORMAL=[french fries, pizza]}, FISH={DIET=[prawns], NORMAL=[salmon]}, MEAT={DIET=[chicken], NORMAL=[beef], FAT=[pork]}}
        Count dishes in groups: {OTHER=4, FISH=2, MEAT=3}
        Most caloric dishes by type: {OTHER=Optional[pizza], FISH=Optional[salmon], MEAT=Optional[pork]}
        Most caloric dishes by type: {OTHER=pizza, FISH=salmon, MEAT=pork}
        Sum calories by type: {OTHER=1550, FISH=850, MEAT=1900}
        Caloric levels by type: {OTHER=[DIET, NORMAL], FISH=[DIET, NORMAL], MEAT=[DIET, NORMAL, FAT]}
    */

    private static Map<Dish.Type, List<Dish>> groupDishesByType() {
        return menu.stream().collect(groupingBy(Dish::getType));
    }

    private static Map<Dish.Type, List<String>> groupDishNamesByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType, mapping(Dish::getName, toList()))
        );
    }

    private static Map<Dish.Type, Set<String>> groupDishTagsByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet()))
        );
    }

    private static Map<Dish.Type, List<Dish>> groupCaloricDishesByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        filtering(dish -> dish.getCalories() > 500, toList()))
        );
    }

    private static Map<CaloricLevel, List<Dish>> groupDishesByCaloricLevel() {
        return menu.stream().collect(
                groupingBy((Dish dish) -> {
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                })
        );
    }

    private static Map<Dish.Type, Map<CaloricLevel, List<Dish>>> groupDishedByTypeAndCaloricLevel() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        groupingBy((Dish dish) -> {
                            if (dish.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (dish.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        }))
        );
    }

    private static Map<Dish.Type, Long> countDishesInGroups() {
        return menu.stream().collect(groupingBy(Dish::getType, counting()));
    }

    private static Map<Dish.Type, Optional<Dish>> mostCaloricDishesByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType,
                        reducing((Dish d1, Dish d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)
                )
        );
    }

    private static Map<Dish.Type, Dish> mostCaloricDishesByTypeWithoutOptionals() {
        return menu.stream().collect(
                toMap(Dish::getType, Function.identity(), (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
    }

    private static Map<Dish.Type, Integer> sumCaloriesByType() {
        return menu.stream().collect(groupingBy(Dish::getType,
                summingInt(Dish::getCalories)));
    }

    private static Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType() {
        return menu.stream().collect(
                groupingBy(Dish::getType, mapping(
                        dish -> {
                            if (dish.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (dish.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        },
                        toSet()
                ))
        );
    }

    private enum CaloricLevel {DIET, NORMAL, FAT}
}
