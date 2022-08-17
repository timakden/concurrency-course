package course.concurrency.m3_shared.collections;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RestaurantService {
    private final ConcurrentHashMap<String, Integer> stat = new ConcurrentHashMap<>();
    private final Restaurant mockRestaurant = new Restaurant("A");

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return mockRestaurant;
    }

    public void addToStat(String restaurantName) {
        stat.compute(restaurantName, (key, value) -> value == null ? 1 : value + 1);
    }

    public Set<String> printStat() {
        return stat.reduce(Runtime.getRuntime().availableProcessors(),
            (key, value) -> Set.of(key + " - " + value.toString()),
            (set1, set2) -> Stream.concat(set1.stream(), set2.stream()).collect(Collectors.toSet()));
    }
}
