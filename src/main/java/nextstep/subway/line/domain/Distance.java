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
            throw new IllegalArgumentException("역의 거리는 양수를 입력해야합니다.");
        }
    }


    public void minus(Distance distance) {
        if (this.distance <= distance.getDistance()) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        this.distance -= distance.getDistance();
    }

    public void plus(Distance distance) {
        this.distance += distance.getDistance();
    }

    public int getDistance() {
        return distance;
    }
}
