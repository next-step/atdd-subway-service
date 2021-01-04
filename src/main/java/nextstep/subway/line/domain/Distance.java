package nextstep.subway.line.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Distance {
    private static final int LIMIT = 1;
    private static final String ERR_TEXT_GIVEN_INVALID_DISTANCE = "거리가 올바르지 않습니다.";
    private static final String ERR_TEXT_NEED_TO_SHORT_DISTANCE_THAN_NOW = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";

    @lombok.Getter
    private int distance;

    private Distance(final int distance) {
        if (distance < LIMIT) {
            throw new IllegalArgumentException(ERR_TEXT_GIVEN_INVALID_DISTANCE);
        }
        this.distance = distance;
    }

    public static Distance of(final int distance) {
        return new Distance(distance);
    }

    public Distance minus(final int newDistance) {
        if (this.distance <= newDistance) {
            throw new IllegalArgumentException(ERR_TEXT_NEED_TO_SHORT_DISTANCE_THAN_NOW);
        }

        return new Distance(this.distance - newDistance);
    }
}
