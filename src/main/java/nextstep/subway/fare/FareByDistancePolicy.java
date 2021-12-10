package nextstep.subway.fare;

import java.util.*;
import java.util.function.*;

import org.jgrapht.*;

import nextstep.subway.path.domain.*;
import nextstep.subway.station.domain.*;

public enum FareByDistancePolicy {
    LESS_THAN_TEN(
        distance -> distance <= FareByDistancePolicy.TEN_KILOMETER,
        FareByDistancePolicy::calculateBasicFare),

    BETWEEN_TEN_AND_FIFTY(
        distance -> distance <= FareByDistancePolicy.FIFTY_KILOMETER,
        FareByDistancePolicy::calculateFareBetweenTenAndFifty),

    GREATER_THAN_FIFTY(
        distance -> distance > FareByDistancePolicy.FIFTY_KILOMETER,
        FareByDistancePolicy::calculateFareGreaterThanFifty);

    private static final int TEN_KILOMETER = 10;
    private static final int FIFTY_KILOMETER = 50;

    private static final int BASIC_FARE = 1250;

    private final Predicate<Integer> distancePolicy;
    private final Function<Integer, Integer> function;

    FareByDistancePolicy(Predicate<Integer> distancePolicy, Function<Integer, Integer> function) {
        this.distancePolicy = distancePolicy;
        this.function = function;
    }

    public static int calculateFare(GraphPath<Station, WeightedEdgeWithLine> shortestPath) {
        int distance = (int)shortestPath.getWeight();

        FareByDistancePolicy fareByDistance = Arrays.stream(values())
            .filter(value -> value.distancePolicy.test(distance))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        return fareByDistance.function.apply(distance);
    }

    private static int calculateBasicFare(int distance) {
        return FareByDistancePolicy.BASIC_FARE;
    }

    private static int calculateFareBetweenTenAndFifty(int distance) {
        return calculateBasicFare(distance) + (int)((Math.ceil((distance - TEN_KILOMETER) / 5) + 1) * 100);
    }

    private static int calculateFareGreaterThanFifty(int distance) {
        return calculateFareBetweenTenAndFifty(distance) + (int)((Math.ceil((distance - FIFTY_KILOMETER) / 8) + 1)
            * 100);
    }
}
