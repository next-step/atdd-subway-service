package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidDistanceException;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private static final int DISTANCE_MINIMUM = 1;
    private int distance;

    protected Distance(){}

    public Distance(int distance) {
        if ( distance < DISTANCE_MINIMUM ) {
            throw new InvalidDistanceException("역과 역 사이의 거리는 1 미만일 수 없습니다");
        }
        this.distance = distance;
    }

    public int value() {
        return distance;
    }

    public Distance plus(int distance) {
        return new Distance(this.distance + distance);
    }

    public Distance minus(int distance) {
        if (this.distance <= distance) {
            throw new InvalidDistanceException("기존의 역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return new Distance(this.distance - distance);
    }
}
