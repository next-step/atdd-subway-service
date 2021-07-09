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

    public SectionDistance(int distance) {
        verifyGreaterThanOrEqualToSectionDistanceMin(distance);
        this.distance = distance;
    }

    public void updateDistance(int newDistance) {
        verifyGreaterThanOrEqualToSectionDistanceMin(newDistance);
        verifyGreaterThanOrEqualToCurrentDistance(newDistance);
        distance -= newDistance;
    }

    private void verifyGreaterThanOrEqualToSectionDistanceMin(int distance) {
        if (distance < SECTION_DISTANCE_MIN) {
            throw new IllegalArgumentException("구간 길이는 1 이상이어야 합니다.");
        }
    }

    private void verifyGreaterThanOrEqualToCurrentDistance(int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalArgumentException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionDistance distance = (SectionDistance) o;
        return getDistance() == distance.getDistance();
    }

    private int getDistance() {
        return distance;
    }

    public int value() {
        return distance;
    }
}
