package nextstep.subway.line.domain;

public class FareGuest implements FarePolicy {

    @Override
    public Fare calculateFare(Distance distance) {
        Fare distanceFare = FareRuleDistance.calculate(distance);
        return Fare.sum(Fare.defaultFare, distanceFare);
    }

}
