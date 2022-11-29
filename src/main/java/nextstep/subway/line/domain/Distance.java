package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {
    }

    public Distance(int distance) {
        this.distance = distance;
    }

    public void validateLength(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("구간 길이는 0보다 큰 값을 입력해주세요.");
        }

        if (this.distance <= distance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public int getDistance() {
        return distance;
    }

    public void setMinusDistance(int distance) {
        validateLength(distance);
        this.distance = Math.abs(this.distance - distance);
    }
}
