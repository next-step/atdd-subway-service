package nextstep.subway.fee;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.LineWeightedEdge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum AdditionalFeeByDistance {

    UNDER_10KM_DISTANCE(
            distance -> distance <= AdditionalFeeByDistance.DISTANCE_10KM,
            (additionalFee, distance) -> AdditionalFeeByDistance.DEFAULT_FEE + additionalFee),

    OVER_10KM_DISTANCE(
            distance -> distance <= AdditionalFeeByDistance.DISTANCE_50KM,
            (additionalFee, distance) -> AdditionalFeeByDistance.DEFAULT_FEE + calculateFeeOver10Km(distance) + additionalFee),

    OVER_50KM_DISTANCE(
            distance -> distance > AdditionalFeeByDistance.DISTANCE_50KM,
            (additionalFee, distance) -> getOver50kmDefaultFee() + calculateFeeOver50Km(distance) + additionalFee);

    private static final int KM_10 = 10;

    private static final int DISTANCE_10KM = 10;
    private static final int DISTANCE_50KM = 50;

    private static final int DEFAULT_FEE = 1250;
    private static final int PER_5KM = 5;
    private static final int PER_8KM = 8;
    private static final int WON_100 = 100;


    private final Predicate<Integer> distanceType;
    private final BiFunction<Integer, Integer, Integer> expression;

    AdditionalFeeByDistance(Predicate<Integer> distanceType, BiFunction<Integer, Integer, Integer> expression) {
        this.distanceType = distanceType;
        this.expression = expression;
    }

    public static int getFee(GraphPath<Station, LineWeightedEdge> shortestPath) {
        int distance = (int) shortestPath.getWeight();
        int additionalFee = getAdditionalFee(shortestPath);

        System.out.println("distance = " + distance);
        System.out.println("additionalFee = " + additionalFee);

        AdditionalFeeByDistance additionalFeeByDistance =  Arrays.stream(values())
                                                                .filter(value -> value.distanceType.test(distance))
                                                                .findFirst()
                                                                .orElseThrow(IllegalArgumentException::new);

        return additionalFeeByDistance.expression.apply(additionalFee, distance);
    }

    private static int getAdditionalFee(GraphPath<Station, LineWeightedEdge> shortestPath) {

        System.out.println("shortestPath = " + shortestPath.getEdgeList());
        return shortestPath.getEdgeList().stream()
                .map(LineWeightedEdge::getLine)
                .mapToInt(Line::getAdditionalFee)
                .max()
                .orElse(0);
    }

    private static int calculateFeeOver10Km(int distance) {
        distance -= AdditionalFeeByDistance.DISTANCE_10KM;
        return (int) ((Math.ceil((distance - KM_10 - 1) / PER_5KM) + 1) * WON_100);
    }

    private static int calculateFeeOver50Km(int distance) {
        distance -= AdditionalFeeByDistance.DISTANCE_50KM;
        return (int) ((Math.ceil((distance - KM_10 - 1) / PER_8KM) + 1) * WON_100);
    }

    private static int getOver50kmDefaultFee() {
        return DEFAULT_FEE + calculateFeeOver10Km(DISTANCE_50KM);
    }

}
