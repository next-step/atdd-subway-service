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

    public Fare addExtraOf(int distance) {
        DistanceExtraFare distanceExtraFare = DistanceExtraFare.of(distance);
        return new Fare(value + distanceExtraFare.addExtraOf(distance));
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

    public Fare discountForAge(LoginMember member) {
        if (member.isGuest()) {
            return this;
        }

        AgeDiscount ageDiscount = AgeDiscount.of(member.getAge());
        return new Fare((int) (value - ageDiscount.calculateDiscount(value)));
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
