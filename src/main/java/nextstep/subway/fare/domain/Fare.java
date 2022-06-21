package nextstep.subway.fare.domain;

import nextstep.subway.path.domain.SectionEdge;

import java.util.List;
import java.util.Objects;

public class Fare {
    private int value;

    public Fare(int value) {
        this.value = value;
    }

    public static Fare of(int baseFare) {
        return new Fare(baseFare);
    }

    public int getValue() {
        return value;
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

        return Fare.of(value + max);
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
