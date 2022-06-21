package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.member.domain.Age;

public class Fare {
    private final Distance distance;
    private final Age age;
    private final int extraFare;

    public Fare(Distance distance, Age age, int extraFare) {
        this.distance = distance;
        this.age = age;
        this.extraFare = extraFare;
    }

    public int getFare() {
        int distanceFare = distance.getDistanceFare();
        if (age != null) {
            return age.calculateAgeSale(distanceFare) + extraFare;
        }
        return distanceFare + extraFare;
    }
}
