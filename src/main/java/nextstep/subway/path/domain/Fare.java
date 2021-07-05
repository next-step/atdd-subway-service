package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.policy.fare.FarePolicies;
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

    public void calculate(Path path, LoginMember loginMember, FarePolicies farePolicies) {
        applyDistanceOverFarePolicy(path.getDistance(), farePolicies);
        applyLineOverFarePolicy(path.getStations(), farePolicies);
        applyDiscountPolicy(loginMember, farePolicies);
    }

    private void applyDiscountPolicy(LoginMember loginMember, FarePolicies farePolicies) {
        farePolicies.getDiscountByAgeStrategy()
                .ifPresent(discountByAgeStrategy -> this.fare -= discountByAgeStrategy.discountBy(loginMember, this));
    }

    private void applyLineOverFarePolicy(List<Station> stations, FarePolicies farePolicies) {
        List<Line> lines = new ArrayList<>();

        for (int i = 1; i < stations.size(); i++) {
            Station upStation = stations.get(i - 1);
            Station downStation = stations.get(i);
            addLine(lines, upStation, downStation);
        }

        this.fare += farePolicies.getLineStrategy()
                .orElseThrow(() -> new IllegalStateException("노선 추가운임 정책을 찾을 수 없습니다."))
                .calculateOverFare(lines);
    }

    private void addLine(List<Line> lines, Station upStation, Station downStation) {
        upStation.getDownSections()
                .forEach(downSection -> downStation.getUpSections().stream()
                        .filter(upSection -> upSection.equals(downSection))
                        .findFirst()
                        .ifPresent(findSection -> lines.add(findSection.getLine()))
                );
    }

    private void applyDistanceOverFarePolicy(ShortestDistance distance, FarePolicies farePolicies) {
        this.fare += farePolicies.getDistanceStrategy()
                .orElseThrow(() -> new IllegalStateException("거리에 맞는 추가운임 정책을 찾을 수 없습니다."))
                .calculateOverFare(distance);
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
