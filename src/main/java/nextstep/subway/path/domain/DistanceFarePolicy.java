package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;
import java.util.function.Function;

import static nextstep.subway.path.domain.Fare.BASIC_FARE;
import static nextstep.subway.path.domain.Fare.FIRST_MAX_FARE;

/**
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리초과 시 추가운임 부과
 * 10km초과∼50km까지(5km마다 100원)
 * 50km초과 시 (8km마다 100원)
 */
public class DistanceFarePolicy implements FarePolicy {
    public static final int FIRST_OVER_FARE_DISTANCE = 5;
    public static final int SECOND_OVER_FARE_DISTANCE = 8;

    private final FarePolicy farePolicy;
    private final Distance distance;

    public DistanceFarePolicy(FarePolicy farePolicy, Distance distance) {
        this.farePolicy = farePolicy;
        this.distance = distance;
    }

    public Fare fare() {
        DistanceFare distanceFare = DistanceFare.from(distance);
        Fare fare = distanceFare.calculate(distance);

        return fare.add(farePolicy.fare());
    }

    private enum DistanceFare {
        BASIC(1, 10, distance -> BASIC_FARE),
        FIRST_OVER(10, 50, distance -> BASIC_FARE.add(calculateFirstOverFare(distance))),
        SECOND_OVER(50, Integer.MAX_VALUE, distance -> FIRST_MAX_FARE.add(calculateSecondOverFare(distance)))
        ;

        private final int start;
        private final int end;
        private final Function<Distance, Fare> calculable;

        DistanceFare(int start, int end, Function<Distance, Fare> calculable) {
            this.start = start;
            this.end = end;
            this.calculable = calculable;
        }

        public static DistanceFare from(Distance distance) {
            return Arrays.stream(values())
                    .filter(fare -> fare.between(distance))
                    .findFirst()
                    .get();
        }

        public boolean between(Distance distance) {
            return start <= distance.value() && distance.value() <= end;
        }

        private static Fare calculateFirstOverFare(Distance distance) {
            int fare = (int) ((Math.ceil((distance.value() - FIRST_OVER.start - 1) / FIRST_OVER_FARE_DISTANCE) + 1) * 100);
            return new Fare(fare);
        }

        private static Fare calculateSecondOverFare(Distance distance) {
            int fare = (int) ((Math.ceil((distance.value() - SECOND_OVER.start - 1) / SECOND_OVER_FARE_DISTANCE) + 1) * 100);
            return new Fare(fare);
        }

        public Fare calculate(Distance distance) {
            return calculable.apply(distance);
        }
    }
}
