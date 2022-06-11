package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column
    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    protected Distance() {
    }

    public Distance minus(int distance) {
        validateDistanceRange(distance);
        this.distance -= distance;
        return this;
    }

    public Distance minus(Distance distance) {
        return minus(distance.getDistance());
    }

    private void validateDistanceRange(int distance) {
        if (this.distance <= distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public Distance plus(int distance){
        this.distance += distance;
        return this;
    }

    public Distance plus(Distance distance){
        return plus(distance.getDistance());
    }

    public int getDistance() {
        return distance;
    }
}
