package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Fare {
    private static int MINIMUM_FARE = 0;

    @Column
    private int value;

    protected Fare() {
    }

    public Fare(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(int fare) {
        if (fare < MINIMUM_FARE) {
            throw new IllegalArgumentException("요금은 0원 이상이어야 합니다");
        }
    }

    public int getValue() {
        return value;
    }

    public Fare addExtraOf(Path path) {
        return addExtraOf(path.getDistance())
                .addExtraOf(path.getSectionEdges());
    }

    public Fare discountForAge(LoginMember member) {
        if (member.isGuest()) {
            return this;
        }

        try {
            AgeDiscount ageDiscount = AgeDiscount.of(member.getAge());
            return new Fare((int) (value - ageDiscount.calculateDiscount(value)));
        } catch (IllegalArgumentException e) {
            return this;
        }
    }

    public Fare addExtraOf(int distance) {
        Fare result = this;
        for (DistanceExtraFare distanceExtraFare : DistanceExtraFare.values()) {
            result = result.plus(getExtraFare(distance, distanceExtraFare));
        }

        return result;
    }

    public Fare addExtraOf(List<SectionEdge> edgeList) {
        if (edgeList.isEmpty()) {
            throw new IllegalStateException("경로가 잘못되어 노선별 추가 요금을 계산할 수 없습니다.");
        }

        int max = edgeList.get(0).getLineFare();
        for (SectionEdge sectionEdge : edgeList) {
            max = sectionEdge.max(max);
        }

        return new Fare(value + max);
    }

    private Fare plus(Fare extra) {
        return new Fare(this.value + extra.value);
    }

    private Fare getExtraFare(int distance, DistanceExtraFare distanceExtraFare) {
        if (distance < distanceExtraFare.getFrom()) {
            return new Fare(0);
        }
        int extraDistance = Math.min(distance, distanceExtraFare.getTo()) - distanceExtraFare.getFrom() + 1;
        return new Fare(calculateExtraFare(extraDistance, distanceExtraFare));
    }

    private int calculateExtraFare(int additionalDistance, DistanceExtraFare distanceExtraFare) {
        return (int) ((Math.ceil((additionalDistance - 1) / distanceExtraFare.getUnitDistance()) + 1) * distanceExtraFare.getUnitExtra());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
