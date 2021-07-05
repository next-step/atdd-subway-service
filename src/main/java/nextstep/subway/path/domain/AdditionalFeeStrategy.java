package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public enum AdditionalFeeStrategy {
    ABOVE_10KM_DISTANCE(distance -> distance <= Distance.LIMIT_10KM, (additionalFee, distance) -> StandardFee.DEFAULT + additionalFee),
    OVER_10KM_DISTANCE(distance -> distance <= Distance.LIMIT_50KM, (additionalFee, distance) -> StandardFee.DEFAULT + calculateOver10KmFare(distance) + additionalFee),
    OVER_50KM_DISTANCE(distance -> distance > Distance.LIMIT_50KM, (additionalFee, distance) -> StandardFee.OVER_50KM + calculateOver50KmFare(distance) + additionalFee);

    private static final int KM_1 = 10;
    private static final int PER_5KM = 50;
    private static final int PER_8KM = 80;
    private static final int WON_100 = 100;

    private final Predicate<Integer> distanceType;
    private final BiFunction<Integer, Integer, Integer> expression;

    AdditionalFeeStrategy(Predicate<Integer> distanceType, BiFunction<Integer, Integer, Integer> expression) {
        this.distanceType = distanceType;
        this.expression = expression;
    }

    public static int getFee(GraphPath<Station, SubwayWeightedEdge> shortestPath) {
        int distance = (int) shortestPath.getWeight();
        int additionalFee = getAdditionalCharge(shortestPath);

        for (AdditionalFeeStrategy strategy : AdditionalFeeStrategy.values()) {
            if (strategy.distanceType.test(distance)) {
                return strategy.expression.apply(additionalFee, distance);
            }
        }
        throw new IllegalArgumentException();
    }

    private static int getAdditionalCharge(GraphPath<Station, SubwayWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().map(SubwayWeightedEdge::getLine)
                .map(Line::getAdditionalCharge)
                .max(Integer::compareTo)
                .orElse(0);
    }


    private static int calculateOver10KmFare(int distance) {
        distance = distance - Distance.LIMIT_10KM;
        return (int) ((Math.ceil((distance - KM_1) / PER_5KM) + 1) * WON_100);
    }

    private static int calculateOver50KmFare(int distance) {
        distance = distance - Distance.LIMIT_50KM;
        return (int) ((Math.ceil((distance - KM_1) / PER_8KM) + 1) * WON_100);
    }

    private static class StandardFee {
        public static final int DEFAULT = 1250;
        public static final int OVER_50KM = 2050;
    }

    private static class Distance {
        public static final int LIMIT_10KM = 100;
        public static final int LIMIT_50KM = 500;
    }
}
