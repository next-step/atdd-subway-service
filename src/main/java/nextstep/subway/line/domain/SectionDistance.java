package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SectionDistance {
    private static final int SECTION_DISTANCE_MIN = 1;

    @Column
    private int distance;

    protected SectionDistance() {
    }

    private SectionDistance(int distance) {
        this.distance = distance;
    }

    public static SectionDistance create(int distance) {
        verifyAvailable(distance);
        return new SectionDistance(distance);
    }

    private static void verifyAvailable(int distance) {
        if (distance < SECTION_DISTANCE_MIN) {
            throw new IllegalArgumentException("구간 길이는 1 이상이어야 합니다.");
        }
    }

    public void updateDistance(int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
        distance -= newDistance;
    }

    public int getDistance() {
        return distance;
    }
}
