package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.policy.fare.discount.DiscountByAgeStrategyFacade;
import nextstep.subway.path.domain.policy.fare.distance.OverFareByDistanceStrategyFacade;
import nextstep.subway.path.domain.policy.fare.line.OverFareByLineStrategyFacade;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fare {
    public static final int BASIC_FARE = 1_250;

    private int fare = BASIC_FARE;

    public Fare() {
    }

    public Fare(int fare) {
        verifyAvailable(fare);
        this.fare = fare;
    }

    public void calculate(Path path, LoginMember loginMember) {
        applyDistanceOverFarePolicy(path.getDistance());
        applyLineOverFarePolicy(path.getStations());
        applyDiscountPolicy(loginMember);
    }

    private void applyDiscountPolicy(LoginMember loginMember) {
        DiscountByAgeStrategyFacade facade = new DiscountByAgeStrategyFacade();
        this.fare -= facade.discountBy(loginMember, this);
    }

    private void applyLineOverFarePolicy(List<Station> stations) {
        List<Line> lines = findLines(stations);
        OverFareByLineStrategyFacade facade = new OverFareByLineStrategyFacade();
        this.fare += facade.calculateOverFare(lines);
    }

    private List<Line> findLines(List<Station> stations) {
        List<Line> lines = new ArrayList<>();

        for (int i = 1; i < stations.size(); i++) {
            Station upStation = stations.get(i - 1);
            Station downStation = stations.get(i);
            addLine(lines, upStation, downStation);
        }
        return lines;
    }

    private void addLine(List<Line> lines, Station upStation, Station downStation) {
        upStation.getDownSections()
                .forEach(downSection -> downStation.getUpSections().stream()
                        .filter(upSection -> upSection.equals(downSection))
                        .findFirst()
                        .ifPresent(findSection -> lines.add(findSection.getLine()))
                );
    }

    private void applyDistanceOverFarePolicy(ShortestDistance distance) {
        OverFareByDistanceStrategyFacade facade = new OverFareByDistanceStrategyFacade();
        this.fare += facade.calculateOverFare(distance);
    }

    private void verifyAvailable(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 0 이상이어야 합니다.");
        }
    }

    public int value() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
