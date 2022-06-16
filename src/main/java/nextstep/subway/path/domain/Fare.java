package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int BASE_LINE01 = 10;
    private static final int BASE_LINE02 = 50;

    private int value;

    public Fare(Surcharge surcharge, Distance distance) {
        this.value = DEFAULT_FARE;
        this.value += surcharge.getValue();
        int remainingDistance = addFarePer8km(distance.getDistance());
        addFarePer5km(remainingDistance);
    }

    private int addFarePer8km(int distance) {
        if (distance > BASE_LINE02) {
            distance -= BASE_LINE02;
            this.value += (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
            return BASE_LINE02;
        }
        return distance;
    }

    private void addFarePer5km(int distance) {
        if (distance > BASE_LINE01) {
            distance -= BASE_LINE01;
            this.value += (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }
    }

    public int getValue() {
        return value;
    }
}
