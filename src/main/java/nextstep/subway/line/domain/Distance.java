package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    protected Distance() {
    }

    public int getValue() {
        return distance;
    }

    public Distance minus(int newDistance) {
        validateGreaterThanNewDistance(newDistance);
        return new Distance(distance - newDistance);
    }

    private void validateGreaterThanNewDistance(int newDistance) {
        if (this.distance <= newDistance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }
}
