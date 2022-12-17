package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {

    }

    public Distance(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야 합니다.");
        }
    }


    public void minus(int distance) {
        if (this.distance <= distance) {
            throw new IllegalArgumentException("기존 노선의 거리보다 작거나 같을 수 없습니다.");
        }
        this.distance -= distance;
    }

    public void plus(int distance) {
        this.distance += distance;
    }

    public int value() {
        return distance;
    }

}
