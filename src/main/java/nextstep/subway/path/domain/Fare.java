package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.SurCharge;

public class Fare {

    private int fare;

    public Fare(Distance distance, SurCharge surCharge, int age) {
        calculateFare(distance, surCharge, age);
    }

    private void calculateFare(Distance distance, SurCharge surCharge, int age) {

    }

    public int value() {
        return fare;
    }

}
