package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private Integer value;

    protected Distance() {

    }

    public Distance(Integer value) {
        validateDistanceValue(value);
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    private void validateDistanceValue(Integer value) {
        if(value <= 0) {
            throw new IllegalArgumentException("거리가 0보다 작을 수 없습니다.");
        }
    }

    private boolean isGraterOrEqual(Distance otherDistance) {
        return this.value > otherDistance.getValue();
    }

    public Distance plus(Distance otherDistance) {
        return new Distance(this.value + otherDistance.getValue());
    }

    public Distance minus(Distance otherDistance) {
        if(otherDistance.isGraterOrEqual(this)) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.value - otherDistance.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return Objects.equals(value, distance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
