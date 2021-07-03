package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;

import java.util.function.BiFunction;

public enum AdditionalFeeStrategy {
    ABOVE_10KM_DISTANCE((additionalFee, distance) -> StandardFee.DEFAULT + additionalFee),
    OVER_10KM_DISTANCE((additionalFee, distance) -> StandardFee.DEFAULT + calculateOver10KmFare(distance - 100) + additionalFee),
    OVER_50KM_DISTANCE((additionalFee, distance) -> StandardFee.OVER_50KM + calculateOver50KmFare(distance - 500) + additionalFee);

    private final BiFunction<Integer, Integer, Integer> expression;

    AdditionalFeeStrategy(BiFunction<Integer, Integer, Integer> expression) {
        this.expression = expression;
    }

    public static int getFee(GraphPath<Station, SubwayWeightedEdge> shortestPath) {
        int distance = (int) shortestPath.getWeight();
        int additionalFee = getAdditionalCharge(shortestPath);
        if (distance <= Distance.LIMIT_10KM) {
            return ABOVE_10KM_DISTANCE.calculate(additionalFee, distance);
        }

        if (distance <= Distance.LIMIT_50KM) {
            return OVER_10KM_DISTANCE.calculate(additionalFee, distance);
        }

        return OVER_50KM_DISTANCE.calculate(additionalFee, distance);
    }

    private static int getAdditionalCharge(GraphPath<Station, SubwayWeightedEdge> shortestPath) {
        return shortestPath.getEdgeList().stream().map(SubwayWeightedEdge::getLine)
                .map(Line::getAdditionalCharge)
                .max(Integer::compareTo)
                .orElse(0);
    }


    private static int calculateOver10KmFare(int distance) {
        return (int) ((Math.ceil((distance - 10) / 50) + 1) * 100);
    }
//    1250 + (25-10)/5*100 = 1550
    private static int calculateOver50KmFare(int distance) {
        return (int) ((Math.ceil((distance - 10) / 80) + 1) * 100);
    }

    private int calculate(int additionalFee, int distance) {
        return expression.apply(additionalFee, distance);
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
