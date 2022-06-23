package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.DistanceFare;
import nextstep.subway.member.domain.Age;

public class Fare {
    private final DistanceFare distanceFare;
    private final Age age;
    private final int extraFare;

    public Fare(Distance distance, Age age, int extraFare) {
        this.distanceFare = new DistanceFare(distance.getDistance());
        this.age = age;
        this.extraFare = extraFare;
    }

    public int getFare() {
        int distanceFareValue = this.distanceFare.getFare();
        if (age != null) {
            return age.calculateAgeSale(distanceFareValue) + extraFare;
        }
        return distanceFareValue + extraFare;
    }
}
