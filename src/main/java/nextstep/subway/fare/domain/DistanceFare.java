package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFare {

    BASIC_DISTANCE(0, 10, (distance) -> {
        return Fare.of(calculateAdditionalFare(distance, 0, 10));
    }, null),
    SHORT_DISTANCE(10, 50, (distance) -> {
        return Fare.of(calculateAdditionalFare(distance, 10,5));
    }, BASIC_DISTANCE),
    LONG_DISTANCE(50, Integer.MAX_VALUE, (distance) -> {
        return Fare.of(calculateAdditionalFare(distance, 50, 8));
    }, SHORT_DISTANCE);

    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private final int startDistance;
    private final int endDistance;
    private final Function<Integer, Fare> fareExpression;
    private final DistanceFare nextFare;

    DistanceFare(int startDistance, int endDistance, Function<Integer, Fare> fareExpression, DistanceFare nextFare) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.fareExpression = fareExpression;
        this.nextFare = nextFare;
    }

    private static int calculateAdditionalFare(int distance, int startDistance, int baseDistance) {
        if (startDistance == 0) {
            return 0;
        }
        int exceededDistance = distance - startDistance;
        return (int) ((Math.ceil((exceededDistance - 1) / baseDistance) + 1) * ADDITIONAL_FARE);
    }

    public Fare additionalFare(int distance) {
        Fare fare = Fare.of(BASE_FARE).plus(this.fareExpression.apply(distance));
        fare = plusNextFare(fare);
        return fare;
    }

    private Fare plusNextFare(Fare fare) {
        DistanceFare nextDistanceFare = this.nextFare;
        while(nextDistanceFare != null) {
            Fare next = nextDistanceFare.fareExpression.apply(nextDistanceFare.endDistance);
            fare = fare.plus(next);
            nextDistanceFare = nextDistanceFare.nextFare;
        }
        return fare;
    }

    public static DistanceFare findDistanceFare(int distance) {
        return Arrays.stream(values())
                .filter(distanceFare -> distanceFare.startDistance <= distance)
                .filter(distanceFare -> distanceFare.endDistance >= distance)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
