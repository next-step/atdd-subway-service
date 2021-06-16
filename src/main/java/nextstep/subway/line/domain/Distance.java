package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column(name = "distance", nullable = false)
    private final int value;

    protected Distance() {
        value = 0;
    }

    public Distance(int value) {
        verifyPositive(value);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private void verifyPositive(int value) {
        if (value <= 0) {
            throw new ModifySectionException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public Distance plus(Distance distance) {
        return new Distance(this.value + distance.getValue());
    }

    public Distance plus(int distance) {
        return plus(new Distance(distance));
    }

    public Distance minus(Distance distance) {
        return new Distance(this.value - distance.getValue());
    }

    public Distance minus(int distance) {
        return minus(new Distance(distance));
    }
}
