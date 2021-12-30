package nextstep.subway.fare.domain;

import java.util.Objects;

public class SubwayFare {
    int subwayFare;

    public SubwayFare(int subwayFare) {
        if (subwayFare < 0) {
            throw new IllegalArgumentException("지하철 요금은 0보다 작을 수 없습니다.");
        }
        this.subwayFare = subwayFare;
    }

    public int getSubwayFare() {
        return subwayFare;
    }

    public SubwayFare plus(SubwayFare fare) {
        return new SubwayFare(subwayFare + fare.getSubwayFare());
    }

    public SubwayFare multiple(SubwayFare fare) {
        return new SubwayFare(subwayFare * fare.getSubwayFare());
    }

    public SubwayFare multiple(double chargeRate) {
        return new SubwayFare((int) (subwayFare * chargeRate));
    }

    public SubwayFare minus(SubwayFare discountFare) {
        return new SubwayFare(subwayFare - discountFare.getSubwayFare());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubwayFare that = (SubwayFare) o;
        return subwayFare == that.subwayFare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subwayFare);
    }
}
