package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance valueOf(int distance) {
        return new Distance(distance);
    }

    public int getDistance() {
        return distance;
    }

    public Distance add(Distance newDistance) {
        return Distance.valueOf(distance + newDistance.distance);
    }

    public Distance subtract(Distance newDistance) {
        if (distance <= newDistance.distance) {
            throw new RuntimeException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        return Distance.valueOf(distance - newDistance.distance);
    }
}
