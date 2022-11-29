package nextstep.subway.line.domain;

import nextstep.subway.exception.InvalidRequestException;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private int distance;

    protected Distance() {}

    public Distance(int distance) {
        this.distance = distance;
    }

    public int value() {
        return distance;
    }

    public void checkValidationSize(int changeDistance) {
        if (this.distance <= changeDistance) {
            throw new InvalidRequestException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    public void minusChangeDistance(int changeDistance) {
        this.distance -= changeDistance;
    }

}
