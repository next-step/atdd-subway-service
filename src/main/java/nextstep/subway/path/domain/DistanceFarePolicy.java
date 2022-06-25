package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;
import java.util.function.Function;

/**
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리초과 시 추가운임 부과
 * 10km초과∼50km까지(5km마다 100원)
 * 50km초과 시 (8km마다 100원)
 */
public class DistanceFarePolicy {
    public static final int BASIC_FARE = 1_250;
    public static final int FIRST_MAX_OVER_FARE = 800;
    public static final int FIRST_OVER_FARE_DISTANCE = 5;
    public static final int SECOND_OVER_FARE_DISTANCE = 8;

    public int fare(Distance distance) {
        Fare fare = Fare.from(distance);
        return fare.calculate(distance.value());
    }

    private enum Fare {
        BASIC(1, 10, distance -> BASIC_FARE),
        FIRST_OVER(10, 50, distance -> BASIC_FARE + calculateFirstOverFare(distance)),
        SECOND_OVER(50, Integer.MAX_VALUE, distance -> BASIC_FARE + FIRST_MAX_OVER_FARE + calculateSecondOverFare(distance))
        ;

        private final int start;
        private final int end;
        private final Function<Integer, Integer> calculable;

        Fare(int start, int end, Function<Integer, Integer> calculable) {
            this.start = start;
            this.end = end;
            this.calculable = calculable;
        }

        public static Fare from(Distance distance) {
            return Arrays.stream(values())
                    .filter(fare -> fare.between(distance.value()))
                    .findFirst()
                    .get();
        }

        public boolean between(int distance) {
            return start <= distance && distance <= end;
        }

        private static int calculateFirstOverFare(int distance) {
            return (int) ((Math.ceil((distance - 1) / FIRST_OVER_FARE_DISTANCE) + 1) * 100);
        }

        private static int calculateSecondOverFare(int distance) {
            return (int) ((Math.ceil((distance - SECOND_OVER.start - 1) / SECOND_OVER_FARE_DISTANCE) + 1) * 100);
        }

        public int calculate(int distance) {
            return calculable.apply(distance);
        }
    }
}
