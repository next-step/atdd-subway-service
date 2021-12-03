package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import nextstep.subway.common.exception.InvalidParameterException;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {
    }

    protected Distance(Integer distance) {
        this.distance = distance;
    }

    public static Distance of(Integer distance) {
        return new Distance(distance);
    }

    public Integer getDistance() {
        return distance;
    }

    public void minus(Distance distance) {
        if (this.distance <= distance.distance) {
            throw new InvalidParameterException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }

        this.distance -= distance.distance;
    }

    public void plus(Distance distance) {
        this.distance += distance.distance;
    }

    public void zero() {
        this.distance = 0;
    }
}
