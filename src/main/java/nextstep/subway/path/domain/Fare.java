package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.DistanceFare;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.member.domain.Age;

public class Fare {
    private final DistanceFare distanceFare;
    private final Age age;
    private final ExtraFare extraFare;

    public Fare(Distance distance, Age age, ExtraFare extraFare) {
        this.distanceFare = new DistanceFare(distance.getDistance());
        this.age = age;
        this.extraFare = extraFare;
    }

    public int getFare() {
        int distanceFareValue = distanceFare.getFare();
        if (age != null) {
            return age.calculateAgeSale(distanceFareValue) + extraFare.getExtraFare();
        }
        return distanceFareValue + extraFare.getExtraFare();
    }
}
