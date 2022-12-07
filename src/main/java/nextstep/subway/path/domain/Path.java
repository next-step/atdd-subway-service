package nextstep.subway.path.domain;

import java.util.Objects;

import nextstep.subway.amount.domain.Amount;
import nextstep.subway.amount.domain.AmountPolicy;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Stations;

public class Path {
    private final Stations stations;
    private final Distance distance;

    private Path(Stations stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path of(Stations stations, Distance distance) {
        return new Path(stations, distance);
    }

    public Amount getAmount(Lines lines, LoginMember loginMember) {
        Amount amount = AmountPolicy.valueOf(distance).calculateAmount(distance);
        Amount additionalAmount = lines.findPathLines(stations).maxAmount();
        amount = amount.sum(additionalAmount);
        amount = DiscountPolicy.valueOf(loginMember.getAge()).calculateDiscount(amount);
        return amount;
    }

    public Stations getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path)o;
        return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}
