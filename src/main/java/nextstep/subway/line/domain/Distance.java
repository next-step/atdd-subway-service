package nextstep.subway.line.domain;

import nextstep.subway.common.constant.Constants;
import nextstep.subway.common.exception.RegisterDistanceException;
import nextstep.subway.common.message.Message;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {
    @Column
    private int distance;

    protected Distance() {
    }

    private Distance(int distance) {
        this.distance = distance;
    }

    public static Distance of(int distance) {
        validate(distance);
        return new Distance(distance);
    }

    public Distance minus(Distance targetDistance) {
        if (isLessThanOrEqualTo(targetDistance)) {
            throw new RegisterDistanceException(Message.NOT_REGISTER_SECTION_DISTANCE.getMessage());
        }
        return Distance.of(this.distance - targetDistance.distance);
    }

    public Distance plus(Distance targetDistance) {
        return Distance.of(this.distance + targetDistance.distance);
    }

    private static void validate(int distance) {
        if( distance <= Constants.INT_ZERO ) {
            throw new RegisterDistanceException(Message.NOT_REGISTER_SECTION_DISTANCE.getMessage());
        }
    }

    private boolean isLessThanOrEqualTo(Distance targetDistance) {
        return this.distance <= targetDistance.distance;
    }

    public int getDistance() {
        return distance;
    }
}
